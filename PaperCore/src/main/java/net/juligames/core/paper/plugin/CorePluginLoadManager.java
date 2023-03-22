package net.juligames.core.paper.plugin;

import de.bentzin.tools.logging.Logger;
import net.juligames.core.Core;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ture Bentzin
 * 08.01.2023
 */
@SuppressWarnings("DuplicatedCode")
public class CorePluginLoadManager {

    private final File directory;
    private final Server server;
    private final Logger logger;

    public CorePluginLoadManager(@NotNull File directory, @NotNull Server server) {
        this.directory = directory;
        this.server = server;
        Validate.isTrue(directory.isDirectory(), "directory needs to be directory");
        logger = Core.getInstance().getCoreLogger().adopt("CorePluginManager");

        //pluginManager
        server.getPluginManager().registerInterface(CorePluginLoader.class);
    }

    private @NotNull PluginManager pluginManager() {
        return server.getPluginManager();
    }

    public File getDirectory() {
        return directory;
    }

    @Deprecated
    public List<Plugin> loadManuell(CorePluginLoader corePluginLoader) {
        final ArrayList<Plugin> plugins = new ArrayList<>();
        final File[] files = directory.listFiles();
        final List<File> fileList = Arrays.stream(files).toList();
        logger.info("Attempting to load: " + fileList.stream().map(File::getName).toList());
        for (File file : fileList) {
            try {
                Plugin plugin = corePluginLoader.loadPlugin(file);
                logger.info(plugin.getName() + " was loaded by the Core!");
                //plugin.getLogger().log(Level.SEVERE, "YES BABY!");
                plugins.add(plugin);
            } catch (InvalidPluginException e) {
                logger.error("Cant identify corePlugin :" + file.getAbsolutePath() + " -> " + e.getMessage());
            }
        }

        return plugins;
    }

    @SuppressWarnings("UnusedReturnValue")
    public List<Plugin> load() throws RuntimeException {
        final ArrayList<Plugin> plugins = new ArrayList<>();
        final File[] files = directory.listFiles();
        final List<File> fileList = Arrays.stream(files).toList();
        logger.info("Attempting to load: " + fileList.stream().map(File::getName).toList());
        for (File file : fileList) {
            try {
                Plugin plugin = pluginManager().loadPlugin(file);
                assert plugin != null;
                pluginManager().enablePlugin(plugin);
                plugins.add(plugin);
            } catch (InvalidDescriptionException | InvalidPluginException e) {
                logger.error("Cant identify corePlugin :" + file.getAbsolutePath() + " -> " + e.getMessage());
            }
        }

        return plugins;
    }
}
