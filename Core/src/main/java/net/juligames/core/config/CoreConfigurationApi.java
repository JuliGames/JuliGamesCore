package net.juligames.core.config;

import net.juligames.core.api.config.Configuration;
import net.juligames.core.api.config.ConfigurationAPI;
import net.juligames.core.api.err.dev.TODOException;

import java.util.Comparator;

/**
 * @author Ture Bentzin
 * 26.11.2022
 */
public class CoreConfigurationApi implements ConfigurationAPI {

    public static final String MASTER_CONFIG_NAME = "master-config";

    @Override
    public Configuration search(String name) {
        //TODO
        return null;
    }

    @Override
    public Configuration master() {
        return search(MASTER_CONFIG_NAME);
    }

    @Override
    public Comparator<Configuration> comparator() {
        throw new TODOException();
    }
}
