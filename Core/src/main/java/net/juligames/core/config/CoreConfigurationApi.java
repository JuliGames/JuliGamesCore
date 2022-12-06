package net.juligames.core.config;

import net.juligames.core.api.config.Configuration;
import net.juligames.core.api.config.ConfigurationAPI;
import org.jetbrains.annotations.ApiStatus;

import java.util.Comparator;
import java.util.Properties;

/**
 * @author Ture Bentzin
 * 26.11.2022
 */
public class CoreConfigurationApi implements ConfigurationAPI {

    public static final String MASTER_CONFIG_NAME = "master-config";
    public static final String DATABASE_CONFIG_NAME = "database";

    @Override
    public Configuration getOrCreate(String name) {
        return new CoreConfiguration(name);
    }

    /**
     * This will create a new configuration (if none with the same name is already present)
     *
     * @param defaults the defaults
     * @return a new Configuration or the old if already one existed
     * @apiNote configuration_name is the reserved key for the name!!
     */
    @Override
    public Configuration getOrCreate(Properties defaults) {
        return CoreConfiguration.fromProperties(defaults);
    }

    @Override
    public Configuration master() {
        return getOrCreate(MASTER_CONFIG_NAME);
    }

    @ApiStatus.Internal
    public Configuration database() {
        Properties properties = new Properties();
        properties.setProperty("configuration_name", "database");
        properties.setProperty("jdbc", "jdbc:mysql://root@localhost:3306/minecraft");
        return getOrCreate(properties);
    }

    @Override
    public Comparator<? extends Configuration> comparator() {
        return (o1, o2) -> Comparator.<Configuration>naturalOrder().compare(o1, o2);
    }
}
