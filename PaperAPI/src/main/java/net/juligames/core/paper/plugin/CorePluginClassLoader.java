package net.juligames.core.paper.plugin;

import com.google.common.io.ByteStreams;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
@ApiStatus.Experimental
public final class CorePluginClassLoader extends URLClassLoader { // Spigot
    static {
        ClassLoader.registerAsParallelCapable();
    }

    final CorePlugin plugin;
    private final CorePluginLoader loader;
    private final Map<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>();
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final File file;
    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;
    private final Set<String> seenIllegalAccess = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final java.util.logging.Logger logger; // Paper - add field
    private CorePlugin pluginInit;
    private IllegalStateException pluginState;

    CorePluginClassLoader(@NotNull final CorePluginLoader loader, @Nullable final ClassLoader parent, @NotNull final PluginDescriptionFile description, @NotNull final File dataFolder, @NotNull final File file) throws IOException, InvalidPluginException {
        super(file.getName(), new URL[]{file.toURI().toURL()}, parent); // Paper - rewrite LogEvents to contain source jar info
        Validate.notNull(loader, "Loader cannot be null");

        this.loader = loader;
        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;
        this.jar = new JarFile(file, true, java.util.zip.ZipFile.OPEN_READ, JarFile.runtimeVersion()); // Paper - enable multi-release jars for Java 9+
        this.manifest = jar.getManifest();
        this.url = file.toURI().toURL();

        this.logger = com.destroystokyo.paper.utils.PaperPluginLogger.getLogger(description); // Paper - Register logger early

        try {
            Class<?> jarClass;
            try {
                jarClass = Class.forName(description.getMain(), true, this);
            } catch (ClassNotFoundException ex) {
                throw new InvalidPluginException("Cannot find main class `" + description.getMain() + "'", ex);
            }

            Class<? extends CorePlugin> pluginClass;
            try {
                pluginClass = jarClass.asSubclass(CorePlugin.class);
            } catch (ClassCastException ex) {
                throw new InvalidPluginException("main class `" + description.getMain() + "' does not extend CorePlugin", ex);
            }

            plugin = pluginClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("No public constructor", ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("Abnormal plugin type. Is it a Legacy-non-Core Plugin?", ex);
        }
    }

    public CorePlugin getPlugin() {
        return plugin;
    } // Spigot

    PluginDescriptionFile getDescription() {
        return description;
    } // Paper

    @Override
    public URL getResource(String name) {
        return findResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        return findResources(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return loadClass0(name, resolve, true, true);
    }

    Class<?> loadClass0(@NotNull String name, boolean resolve, boolean checkGlobal, boolean checkLibraries) throws ClassNotFoundException {
        try {
            Class<?> result = super.loadClass(name, resolve);

            // SPIGOT-6749: Library classes will appear in the above, but we don't want to return them to other plugins
            if (checkGlobal || result.getClassLoader() == this) {
                return result;
            }
        } catch (ClassNotFoundException ex) {
        }

        /*


        if (checkLibraries && libraryLoader != null) {
            try {
                return libraryLoader.loadClass(name);
            } catch (ClassNotFoundException ex) {
            }
        }

         */

        if (checkGlobal) {
            // This ignores the libraries of other plugins, unless they are transitive dependencies.
            Class<?> result = loader.getClassByName(name, resolve, description, this);  // Paper - prioritize self

            if (result != null) {
                // If the class was loaded from a library instead of a PluginClassLoader, we can assume that its associated plugin is a transitive dependency and can therefore skip this check.
                if (result.getClassLoader() instanceof CorePluginClassLoader) {
                    PluginDescriptionFile provider = ((CorePluginClassLoader) result.getClassLoader()).description;

                    if (provider != description
                            && !seenIllegalAccess.contains(provider.getName())
                            && !((SimplePluginManager) loader.server.getPluginManager()).isTransitiveDepend(description, provider)) {

                        seenIllegalAccess.add(provider.getName());
                        if (plugin != null) {
                            plugin.getLogger().log(Level.WARNING, "Loaded class {0} from {1} which is not a depend or softdepend of this plugin.", new Object[]{name, provider.getFullName()});
                        } else {
                            // In case the bad access occurs on construction
                            loader.server.getLogger().log(Level.WARNING, "[{0}] Loaded class {1} from {2} which is not a depend or softdepend of this plugin.", new Object[]{description.getName(), name, provider.getFullName()});
                        }
                    }
                }

                return result;
            }
        }

        throw new ClassNotFoundException(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.startsWith("org.bukkit.") || name.startsWith("net.minecraft.")) { //sus
            throw new ClassNotFoundException(name + " is not allowed");
        }
        Class<?> result = classes.get(name);

        if (result == null) {
            String path = name.replace('.', '/').concat(".class");
            JarEntry entry = jar.getJarEntry(path);

            if (entry != null) {
                byte[] classBytes;

                try (InputStream is = jar.getInputStream(entry)) {
                    classBytes = ByteStreams.toByteArray(is);
                } catch (IOException ex) {
                    throw new ClassNotFoundException(name, ex);
                }

                classBytes = loader.server.getUnsafe().processClass(description, path, classBytes);

                int dot = name.lastIndexOf('.');
                if (dot != -1) {
                    String pkgName = name.substring(0, dot);
                    if (getPackage(pkgName) == null) {
                        try {
                            if (manifest != null) {
                                definePackage(pkgName, manifest, url);
                            } else {
                                definePackage(pkgName, null, null, null, null, null, null, null);
                            }
                        } catch (IllegalArgumentException ex) {
                            if (getPackage(pkgName) == null) {
                                throw new IllegalStateException("Cannot find package " + pkgName);
                            }
                        }
                    }
                }

                CodeSigner[] signers = entry.getCodeSigners();
                CodeSource source = new CodeSource(url, signers);

                result = defineClass(name, classBytes, 0, classBytes.length, source);
            }

            if (result == null) {
                result = super.findClass(name);
            }

            loader.setClass(name, result);
            classes.put(name, result);
        }

        return result;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            jar.close();
        }
    }

    @NotNull
    Collection<Class<?>> getClasses() {
        return classes.values();
    }

    synchronized void initialize(@NotNull CorePlugin corePlugin) {
        Validate.notNull(corePlugin, "Initializing plugin cannot be null");
        Validate.isTrue(corePlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
        if (this.plugin != null || this.pluginInit != null) {
            throw new IllegalArgumentException("Plugin already initialized!", pluginState);
        }

        pluginState = new IllegalStateException("Initial initialization");
        this.pluginInit = corePlugin;

        corePlugin.logger = this.logger; // Paper - set logger
        corePlugin.init(loader, loader.server, description, dataFolder, file, this);
    }

    // Paper start
    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        CorePlugin currPlugin = plugin != null ? plugin : pluginInit;
        return "PluginClassLoader{" +
                "plugin=" + currPlugin +
                ", pluginEnabled=" + (currPlugin == null ? "uninitialized" : currPlugin.isEnabled()) +
                ", url=" + file +
                '}';
    }
    // Paper end
}
