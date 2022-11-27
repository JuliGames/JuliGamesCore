package net.juligames.core.master.config;

import de.bentzin.tools.misc.WorkingLocator;
import net.juligames.core.config.CoreConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @author Ture Bentzin
 * 27.11.2022
 */
public class MasterConfigManager {

    public static final File CONFIG_FOLDER;

    static {
            CONFIG_FOLDER = new File("config");
            CONFIG_FOLDER.mkdirs();
    }

    private final Collection<CoreConfiguration> configurations;

    public MasterConfigManager(Collection<CoreConfiguration> configurations) {
        this.configurations = configurations;
    }

    public Collection<CoreConfiguration> coreConfigurations() {
        return configurations;
    }

    public void storeAll() {
        for (CoreConfiguration configuration : configurations) {
            //generate File
            File file = new File(CONFIG_FOLDER, configuration.getName());
        }
    }

}
