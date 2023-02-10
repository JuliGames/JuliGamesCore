package net.juligames.core.paper.plugin;

import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.Warning;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 * @author Ture Bentzin
 * 30.12.2022
 */
@ApiStatus.Experimental
public class CorePluginLoader implements PluginLoader {
    private static final boolean DISABLE_CLASS_PRIORITIZATION = Boolean.getBoolean("Paper.DisableClassPrioritization");
    final Server server;
    private final Pattern fileFilter = Pattern.compile("\\.core$"); //check that bnick does not try to load 7zip files here
    private final Map<String, ReentrantReadWriteLock> classLoadLock = new java.util.HashMap<String, java.util.concurrent.locks.ReentrantReadWriteLock>();
    private final Map<String, Integer> classLoadLockCount = new java.util.HashMap<>();
    private final List<CorePluginClassLoader> loaders = new CopyOnWriteArrayList<>();

    /**
     * This class was not meant to be constructed explicitly
     *
     * @param instance the server instance
     */
    @Deprecated
    public CorePluginLoader(@NotNull Server instance) {
        Validate.notNull(instance, "Server cannot be null");
        server = instance;
    }

    @Override
    @NotNull
    public Plugin loadPlugin(@NotNull final File file) throws InvalidPluginException {
        Validate.notNull(file, "File cannot be null");

        if (!file.exists()) {
            throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exist"));
        }

        final PluginDescriptionFile description;
        try {
            description = getPluginDescription(file);
        } catch (InvalidDescriptionException ex) {
            throw new InvalidPluginException(ex);
        }

        final File parentFile = this.server.getPluginsFolder();
        final File dataFolder = new File(parentFile, description.getName());
        @SuppressWarnings("deprecation") final File legacyDataFolder = new File(parentFile, description.getRawName()); //Support it?

        // Found old data folder
        if (dataFolder.equals(legacyDataFolder)) {
            // They are equal -- nothing needs to be done!
        } else if (dataFolder.isDirectory() && legacyDataFolder.isDirectory()) {
            server.getLogger().warning(String.format(
                    "While loading %s (%s) found old-data folder: `%s' next to the new one `%s'",
                    description.getFullName(),
                    file,
                    legacyDataFolder,
                    dataFolder
            ));
        } else if (legacyDataFolder.isDirectory() && !dataFolder.exists()) {
            if (!legacyDataFolder.renameTo(dataFolder)) {
                throw new InvalidPluginException("Unable to rename old data folder: `" + legacyDataFolder + "' to: `" + dataFolder + "'");
            }
            server.getLogger().log(Level.INFO, String.format(
                    "While loading %s (%s) renamed data folder: `%s' to `%s'",
                    description.getFullName(),
                    file,
                    legacyDataFolder,
                    dataFolder
            ));
        }

        if (dataFolder.exists() && !dataFolder.isDirectory()) {
            throw new InvalidPluginException(String.format(
                    "Projected datafolder: `%s' for %s (%s) exists and is not a directory", //ok wtf?
                    dataFolder,
                    description.getFullName(),
                    file
            ));
        }

        Set<String> missingHardDependencies = new HashSet<>(description.getDepend().size()); // HardDependency = "depend"
        for (final String pluginName : description.getDepend()) {
            Plugin current = server.getPluginManager().getPlugin(pluginName);

            if (current == null) {
                missingHardDependencies.add(pluginName); // Paper - list all missing hard depends
            }
        }
        // Paper start - list all missing hard depends
        if (!missingHardDependencies.isEmpty()) {
            throw new UnknownDependencyException(missingHardDependencies, description.getFullName());
        }
        // Paper end

        server.getUnsafe().checkSupported(description); //TODO Figure out what this does... - figured it out maybe?

        final CorePluginClassLoader loader;
        try {
            loader = new CorePluginClassLoader(this, getClass().getClassLoader(), description, dataFolder, file); //can be present but cant
        } catch (InvalidPluginException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new InvalidPluginException(ex);
        }

        loaders.add(loader);

        return loader.plugin;
    }

    @Override
    @NotNull
    public PluginDescriptionFile getPluginDescription(@NotNull File file) throws InvalidDescriptionException {
        Validate.notNull(file, "File cannot be null");

        JarFile jar = null;
        InputStream stream = null;

        try {
            jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry("plugin.yml");

            if (entry == null) {
                throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain plugin.yml"));
            }

            stream = jar.getInputStream(entry);

            return new PluginDescriptionFile(stream);

        } catch (IOException | YAMLException ex) {
            throw new InvalidDescriptionException(ex);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException ignored) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    @Override
    @NotNull
    public Pattern[] getPluginFileFilters() {
        return new Pattern[]{fileFilter};
    }

    @SuppressWarnings("unused")
    @NotNull
    protected Pattern getPluginFileFilter() {
        return fileFilter;
    }

    /**
     * @deprecated unused
     */
    @Nullable
    @Deprecated
    Class<?> getClassByName(final String name, boolean resolve, PluginDescriptionFile description) {
        // Paper start - prioritize self
        return getClassByName(name, resolve, description, null);
    }

    Class<?> getClassByName(final String name, boolean resolve, PluginDescriptionFile description, CorePluginClassLoader requester) {
        // Paper end
        // Paper start - make MT safe
        java.util.concurrent.locks.ReentrantReadWriteLock lock;
        synchronized (classLoadLock) {
            lock = classLoadLock.computeIfAbsent(name, (x) -> new java.util.concurrent.locks.ReentrantReadWriteLock());
            classLoadLockCount.compute(name, (x, prev) -> prev != null ? prev + 1 : 1);
        }
        lock.writeLock().lock();
        try {
            //prioritize self
            if (!DISABLE_CLASS_PRIORITIZATION && requester != null) {
                try {
                    return requester.loadClass0(name, false, false, ((SimplePluginManager) server.getPluginManager()).isTransitiveDepend(description, requester.getDescription())); //Maybe unsafe
                } catch (ClassNotFoundException ignored) {
                }
            }
            for (CorePluginClassLoader loader : loaders) {
                try {
                    return loader.loadClass0(name, resolve, false, ((SimplePluginManager) server.getPluginManager()).isTransitiveDepend(description, loader.plugin.getDescription()));
                } catch (ClassNotFoundException ignored) {
                }
            }
            // Paper start - make MT safe
        } finally {
            synchronized (classLoadLock) {
                lock.writeLock().unlock();
                if (classLoadLockCount.get(name) == 1) {
                    classLoadLock.remove(name);
                    classLoadLockCount.remove(name);
                } else {
                    classLoadLockCount.compute(name, (x, prev) -> prev - 1); //may cause null
                }
            }
        }
        // Paper end
        return null;
    }

    void setClass(@NotNull final String name, @NotNull final Class<?> clazz) {
        if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
            Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
            ConfigurationSerialization.registerClass(serializable);
        }
    }

    private void removeClass(@NotNull Class<?> clazz) {
        if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
            Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
            ConfigurationSerialization.unregisterClass(serializable);
        }
    }

    @Override
    @NotNull
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(@NotNull Listener listener, @NotNull final Plugin plugin) {
        Validate.notNull(plugin, "Plugin can not be null");
        Validate.notNull(listener, "Listener can not be null");

        boolean useTimings = server.getPluginManager().useTimings();
        Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<>();
        Set<Method> methods;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();
            methods = new HashSet<>(publicMethods.length + privateMethods.length, 1.0f);
            Collections.addAll(methods, publicMethods);
            Collections.addAll(methods, privateMethods);
        } catch (NoClassDefFoundError e) {
            plugin.getLogger().severe("Plugin " + plugin.getDescription().getFullName() + " has failed to register events for " + listener.getClass() + " because " + e.getMessage() + " does not exist.");
            return ret;
        }

        for (final Method method : methods) {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) continue;
            // Do not register bridge or synthetic methods to avoid event duplication
            // Fixes SPIGOT-893
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }
            final Class<?> checkClass;
            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                plugin.getLogger().severe(plugin.getDescription().getFullName() + " attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
                continue;
            }
            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);
            Set<RegisteredListener> eventSet = ret.computeIfAbsent(eventClass, k -> new HashSet<>());

            for (Class<?> clazz = eventClass; Event.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
                // This loop checks for extending deprecated events
                if (clazz.getAnnotation(Deprecated.class) != null) {
                    Warning warning = clazz.getAnnotation(Warning.class);
                    Warning.WarningState warningState = server.getWarningState();
                    if (!warningState.printFor(warning)) {
                        break;
                    }
                    plugin.getLogger().log(
                            Level.WARNING,
                            String.format(
                                    "\"%s\" has registered a listener for %s on method \"%s\", but the event is Deprecated. \"%s\"; please notify the authors %s.",
                                    plugin.getDescription().getFullName(),
                                    clazz.getName(),
                                    method.toGenericString(),
                                    (warning != null && warning.reason().length() != 0) ? warning.reason() : "Server performance will be affected",
                                    Arrays.toString(plugin.getDescription().getAuthors().toArray())),
                            warningState == Warning.WarningState.ON ? new AuthorNagException(null) : null);
                    break;
                }
            }

            EventExecutor executor = new co.aikar.timings.TimedEventExecutor(EventExecutor.create(method, eventClass), plugin, method, eventClass); // Paper // Paper (Yes.) - Use factory method `EventExecutor.create()`
            eventSet.add(new RegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled())); //removed if-statement
        }
        return ret;
    }

    @Override
    public void enablePlugin(@NotNull final Plugin plugin) {
        Validate.isTrue(plugin instanceof CorePlugin, "Plugin is not associated with this PluginLoader");

        if (!plugin.isEnabled()) {
            // Paper start - Add an asterisk to legacy plugins (so admins are aware) - copied from paper
            String enableMsg = "CorePluginEnable -> Enabling " + plugin.getDescription().getFullName();
            if (org.bukkit.UnsafeValues.isLegacyPlugin(plugin)) {
                enableMsg += "*";
            }

            plugin.getLogger().info(enableMsg);
            // Paper end

            CorePlugin jPlugin = (CorePlugin) plugin;

            CorePluginClassLoader pluginLoader = (CorePluginClassLoader) jPlugin.getClassLoader();

            if (!loaders.contains(pluginLoader)) {
                loaders.add(pluginLoader);
                server.getLogger().log(Level.WARNING, "Enabled plugin with unregistered PluginClassLoader " + plugin.getDescription().getFullName());
            }

            try {
                jPlugin.setEnabled(true);
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE, "Error occurred while enabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex); // Is it?
                // Paper start - Disable plugins that fail to load
                this.server.getPluginManager().disablePlugin(jPlugin);
                return;
                // Paper end
            }

            server.getPluginManager().callEvent(new PluginEnableEvent(plugin));
        }
    }

    @Override
    public void disablePlugin(@NotNull Plugin plugin) {
        Validate.isTrue(plugin instanceof CorePlugin, "Plugin is not associated with this PluginLoader");

        if (plugin.isEnabled()) {
            String message = String.format("Disabling %s", plugin.getDescription().getFullName());
            plugin.getLogger().info(message);

            server.getPluginManager().callEvent(new PluginDisableEvent(plugin));

            CorePlugin cPlugin = (CorePlugin) plugin;
            ClassLoader cLoader = cPlugin.getClassLoader();

            try {
                cPlugin.setEnabled(false);
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE, "Error occurred while disabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex); //is it?
            }

            if (cLoader instanceof CorePluginClassLoader loader) {
                loaders.remove(loader);

                Collection<Class<?>> classes = loader.getClasses();

                for (Class<?> clazz : classes) {
                    removeClass(clazz);
                }

                try {
                    loader.close();
                } catch (IOException ex) {
                    //
                    this.server.getLogger().log(Level.WARNING, "Error closing the PluginClassLoader for '" + plugin.getDescription().getFullName() + "'", ex); // Paper - log exception
                }
            }
        }
    }
}
