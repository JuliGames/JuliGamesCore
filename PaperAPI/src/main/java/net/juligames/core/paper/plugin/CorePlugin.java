package net.juligames.core.paper.plugin;

import com.google.common.base.Charsets;
import net.juligames.core.api.TODO;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ture Bentzin
 * 30.12.2022
 */
@TODO(doNotcall = true)
public abstract class CorePlugin extends PluginBase {
    Logger logger = null; // Paper - PluginLogger -> Logger, package-private
    private boolean isEnabled = false;
    private CorePluginLoader loader = null;
    private Server server = null;
    private File file = null;
    private PluginDescriptionFile description = null;
    private File dataFolder = null;
    private ClassLoader classLoader = null;
    private boolean naggable = true;
    private FileConfiguration newConfig = null;
    private File configFile = null;

    public CorePlugin() {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        if (!(classLoader instanceof CorePluginClassLoader)) {
            throw new IllegalStateException("CorePlugin requires " + CorePluginClassLoader.class.getName());
        }
        ((CorePluginClassLoader) classLoader).initialize(this);
    }

    protected CorePlugin(@NotNull final CorePluginLoader loader, @NotNull final PluginDescriptionFile description, @NotNull final File dataFolder, @NotNull final File file) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        if (classLoader instanceof CorePluginClassLoader) {
            throw new IllegalStateException("Cannot use initialization constructor at runtime");
        }
        init(loader, loader.server, description, dataFolder, file, classLoader);
    }

    /**
     * This method provides fast access to the plugin that has {@link
     * #getProvidingPlugin(Class) provided} the given plugin class, which is
     * usually the plugin that implemented it.
     * <p>
     * An exception to this would be if plugin's jar that contained the class
     * does not extend the class, where the intended plugin would have
     * resided in a different jar / classloader.
     *
     * @param <T>   a class that extends CorePlugin
     * @param clazz the class desired
     * @return the plugin that provides and implements said class
     * @throws IllegalArgumentException if clazz is null
     * @throws IllegalArgumentException if clazz does not extend {@link
     *                                  CorePlugin}
     * @throws IllegalStateException    if clazz was not provided by a plugin,
     *                                  for example, if called with
     *                                  <code>CorePlugin.getPlugin(CorePlugin.class)</code>
     * @throws IllegalStateException    if called from the static initializer for
     *                                  given CorePlugin
     * @throws ClassCastException       if plugin that provided the class does not
     *                                  extend the class
     */
    @NotNull
    public static <T extends CorePlugin> T getPlugin(@NotNull Class<T> clazz) {
        Validate.notNull(clazz, "Null class cannot have a plugin");
        if (!CorePlugin.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(clazz + " does not extend " + CorePlugin.class);
        }
        final ClassLoader cl = clazz.getClassLoader();
        if (!(cl instanceof CorePluginClassLoader)) {
            throw new IllegalArgumentException(clazz + " is not initialized by " + CorePluginClassLoader.class);
        }
        CorePlugin plugin = ((CorePluginClassLoader) cl).getPlugin();
        if (plugin == null) {
            throw new IllegalStateException("Cannot get plugin for " + clazz + " from a static initializer");
        }
        return clazz.cast(plugin);
    }

    /**
     * This method provides fast access to the plugin that has provided the
     * given class.
     *
     * @param clazz a class belonging to a plugin
     * @return the plugin that provided the class
     * @throws IllegalArgumentException if the class is not provided by a
     *                                  CorePlugin
     * @throws IllegalArgumentException if class is null
     * @throws IllegalStateException    if called from the static initializer for
     *                                  given CorePlugin
     */
    @NotNull
    public static CorePlugin getProvidingPlugin(@NotNull Class<?> clazz) {
        Validate.notNull(clazz, "Null class cannot have a plugin");
        final ClassLoader cl = clazz.getClassLoader();
        if (!(cl instanceof CorePluginClassLoader)) {
            throw new IllegalArgumentException(clazz + " is not provided by " + CorePluginClassLoader.class);
        }
        CorePlugin plugin = ((CorePluginClassLoader) cl).plugin;
        if (plugin == null) {
            throw new IllegalStateException("Cannot get plugin for " + clazz + " from a static initializer");
        }
        return plugin;
    }

    /**
     * Returns the folder that the plugin data's files are located in. The
     * folder may not yet exist.
     *
     * @return The folder.
     */
    @NotNull
    @Override
    public final File getDataFolder() {
        return dataFolder;
    }

    /**
     * Gets the associated PluginLoader responsible for this plugin
     *
     * @return PluginLoader that controls this plugin
     */
    @NotNull
    @Override
    public final CorePluginLoader getPluginLoader() {
        return loader;
    }

    /**
     * Returns the Server instance currently running this plugin
     *
     * @return Server running this plugin
     */
    @NotNull
    @Override
    public final Server getServer() {
        return server;
    }

    /**
     * Returns a value indicating whether or not this plugin is currently
     * enabled
     *
     * @return true if this plugin is enabled, otherwise false
     */
    @Override
    public final boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Sets the enabled state of this plugin
     *
     * @param enabled true if enabled, otherwise false
     */
    protected final void setEnabled(final boolean enabled) {
        if (isEnabled != enabled) {
            isEnabled = enabled;

            if (isEnabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    /**
     * Returns the file which contains this plugin
     *
     * @return File containing this plugin
     */
    @NotNull
    protected File getFile() {
        return file;
    }

    /**
     * Returns the plugin.yaml file containing the details for this plugin
     *
     * @return Contents of the plugin.yaml file
     */
    @NotNull
    @Override
    public final PluginDescriptionFile getDescription() {
        return description;
    }

    @NotNull
    @Override
    public FileConfiguration getConfig() {
        if (newConfig == null) {
            reloadConfig();
        }
        return newConfig;
    }

    /**
     * Provides a reader for a text file located inside the jar.
     * <p>
     * The returned reader will read text with the UTF-8 charset.
     *
     * @param file the filename of the resource to load
     * @return null if {@link #getResource(String)} returns null
     * @throws IllegalArgumentException if file is null
     * @see ClassLoader#getResourceAsStream(String)
     */
    @Nullable
    protected final Reader getTextResource(@NotNull String file) {
        final InputStream in = getResource(file);

        return in == null ? null : new InputStreamReader(in, Charsets.UTF_8);
    }

    @Override
    public void reloadConfig() {
        newConfig = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = getResource("configuration.yml");
        if (defConfigStream == null) {
            return;
        }

        newConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    @Override
    public void saveConfig() {
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    @Override
    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            saveResource("configuration.yml", false);
        }
    }

    @Override
    public void saveResource(@NotNull String resourcePath, boolean replace) {
        if (resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + file);
        }

        File outFile = new File(dataFolder, resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(dataFolder, resourcePath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                logger.log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }

    @Nullable
    @Override
    public InputStream getResource(@NotNull String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null"); //may not be even thrown due to NotNull in parameter
        }

        try {
            URL url = getClassLoader().getResource(filename);

            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Returns the ClassLoader which holds this plugin
     *
     * @return ClassLoader holding this plugin
     */
    @NotNull
    protected final ClassLoader getClassLoader() {
        return classLoader;
    }

    final void init(@NotNull CorePluginLoader loader, @NotNull Server server, @NotNull PluginDescriptionFile description, @NotNull File dataFolder, @NotNull File file, @NotNull ClassLoader classLoader) {
        this.loader = loader;
        this.server = server;
        this.file = file;
        this.description = description;
        this.dataFolder = dataFolder;
        this.classLoader = classLoader;
        this.configFile = new File(dataFolder, "configuration.yml");
        // Paper start
        if (this.logger == null) {
            this.logger = com.destroystokyo.paper.utils.PaperPluginLogger.getLogger(this.description);
        }
        // Paper end
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }

    /**
     * Gets the command with the given name, specific to this plugin. Commands
     * need to be registered in the {@link PluginDescriptionFile#getCommands()
     * PluginDescriptionFile} to exist at runtime.
     *
     * @param name name or alias of the command
     * @return the plugin command if found, otherwise null
     */
    @Nullable
    public PluginCommand getCommand(@NotNull String name) {
        String alias = name.toLowerCase(java.util.Locale.ENGLISH);
        PluginCommand command = getServer().getPluginCommand(alias);

        if (command == null || command.getPlugin() != this) {
            command = getServer().getPluginCommand(description.getName().toLowerCase(java.util.Locale.ENGLISH) + ":" + alias);
        }

        if (command != null && command.getPlugin() == this) {
            return command;
        } else {
            return null;
        }
    }

    @Override
    public void onLoad() {
    }

    @Override
    public abstract void onEnable();

    @Override
    public abstract void onDisable();

    @Nullable
    @Override
    public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return null;
    }

    @Nullable
    @Override
    public BiomeProvider getDefaultBiomeProvider(@NotNull String worldName, @Nullable String id) {
        return null;
    }

    @Override
    public final boolean isNaggable() {
        return naggable;
    }

    @Override
    public final void setNaggable(boolean canNag) {
        this.naggable = canNag;
    }

    @NotNull
    @Override
    public Logger getLogger() {
        return logger;
    }

    @NotNull
    @Override
    public String toString() {
        return description.getFullName();
    }
}
