package net.juligames.core.config;

import net.juligames.core.api.config.Configuration;
import net.juligames.core.api.config.ConfigurationAPI;

import java.util.Comparator;

/**
 * @author Ture Bentzin
 * 26.11.2022
 */
public class CoreConfigurationApi implements ConfigurationAPI {

    public static final String MASTER_CONFIG_NAME = "master-config";

    @Override
    public Configuration getOrCreate(String name) {
        return new CoreConfiguration(name);
    }

    @Override
    public Configuration master() {
        return getOrCreate(MASTER_CONFIG_NAME);
    }

    @Override
    public Comparator<? extends Configuration> comparator() {
        return (o1, o2) -> Comparator.<Configuration>naturalOrder().compare(o1, o2);
    }
}
