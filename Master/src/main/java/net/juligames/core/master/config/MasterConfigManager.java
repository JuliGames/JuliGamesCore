package net.juligames.core.master.config;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import de.bentzin.tools.Hardcode;
import net.juligames.core.Core;
import net.juligames.core.config.CoreConfiguration;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Properties;

/**
 * @author Ture Bentzin
 * 27.11.2022
 */
public class MasterConfigManager {

    public static final File CONFIG_FOLDER;
    public static final String FILE_ENDING = ".properties";

    static {
        CONFIG_FOLDER = new File("config");
        CONFIG_FOLDER.mkdirs();
    }

    private final Collection<String> configurations;

    public MasterConfigManager(Collection<String> configurations) {
        this.configurations = configurations;
    }

    public MasterConfigManager() {
        this.configurations = new ArrayList<>();
    }

    public Collection<String> coreConfigurations() {
        return configurations;
    }

    @Hardcode
    public void storeAll() {
        for (DistributedObject distributedObject : Core.getInstance().getOrThrow().getDistributedObjects()) {
            if (distributedObject instanceof IMap<?, ?> map) {
                if (map.getName().startsWith("config:")) {
                    store(map.getName().replace("config:", ""));
                }
            }
        }

        // for (String configuration : configurations) {
        //     store(configuration);
        // }
    }

    public void store(String hazel) {
        CoreConfiguration configuration = new CoreConfiguration(hazel);
        //generate File
        File file = fileFromName(configuration.getName());
        try {
            file.createNewFile();
            Properties properties = configuration.cloneToProperties();
            try (FileWriter writer = new FileWriter(file)) {
                Core.getInstance().getCoreLogger().info("trying to store: \"" + configuration + "\" to \""
                        + file.getAbsolutePath() + "\"!");
                properties.store(writer, configuration.header_comment() + " written at: " + currentDateString());
                Core.getInstance().getCoreLogger().info("file was saved: \"" + file.getName() + "\"");
            }

        } catch (IOException e) {
            Core.getInstance().getCoreLogger().error("failed to save: " + configuration);
        }
    }

    @Hardcode
    public void load() {
        String[] prop = CONFIG_FOLDER.list((dir, name) -> name.endsWith(FILE_ENDING));
        Collection<Properties> properties = new ArrayList<>();
        if (prop == null) {
            Core.getInstance().getCoreLogger().warning("no configs found... skipping step!");
            return;
        }

        //s contains fileEnding
        for (String s : prop) {
            File file = fileFromName(s, true);
            Properties properties1 = new Properties();
            try {
                FileReader reader = new FileReader(file);
                properties1.load(reader);
                properties1.setProperty("configuration_name", s.replace(FILE_ENDING, ""));
                properties.add(properties1);

            } catch (Exception e) {
                Core.getInstance().getCoreLogger().error("failed to load config: " + file + " :: " + e.getMessage());
            }
        }

        for (Properties property : properties) {
            Core.getInstance().getCoreLogger().info("loaded: " + property);
        }

        //flush old data
        configurations.clear();
        for (DistributedObject distributedObject : Core.getInstance().getOrThrow().getDistributedObjects()) {
            if (distributedObject instanceof IMap<?, ?> map) {
                Core.getInstance().getCoreLogger().debug("found: " + map.getName() + "<" + map.size() + ">");
                if (map.getName().startsWith("config:")) {
                    Core.getInstance().getCoreLogger().debug("removing: " + map.getName());
                    map.destroy();
                }
            }
        }

        //create new hazelObjects
        for (Properties property : properties) {
            String name = property.getProperty("configuration_name");
            CoreConfiguration.fromProperties(property, true); //override --force
            configurations.add(name);
        }
    }

    @Contract("_ -> new")
    private @NotNull File fileFromName(String name) {
        return fileFromName(name, false);
    }

    @Contract(value = "_,_ -> new")
    private @NotNull File fileFromName(String name, boolean containsFileEnding) {
        if (containsFileEnding)
            return new File(CONFIG_FOLDER, name);
        else return new File(CONFIG_FOLDER, name + FILE_ENDING);
    }

    @ApiStatus.Internal
    public void deleteConfigFromSystem(String hazel) {
        CoreConfiguration configuration = new CoreConfiguration(hazel);
        configuration.accessHazel().get().destroy();
        fileFromName(hazel, true);
    }

    @ApiStatus.Internal
    public void destroy() {
        for (String configuration : configurations) {
            deleteConfigFromSystem(configuration);
        }
    }


    private @NotNull String currentDateString() {
        java.util.Date time = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd | hh:mm:ss");
        return dateFormatter.format(time);
    }

}
