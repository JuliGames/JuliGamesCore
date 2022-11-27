package net.juligames.core.api.config;

import java.util.Comparator;

/**
 * @author Ture Bentzin
 * 26.11.2022

 */
public interface ConfigurationAPI {

    /**
     * This will create a new configuration (if none with the same name is already present)
     * @param name the name
     * @return a new Configuration or the old if already one existed
     */
    Configuration getOrCreate(String name);

    Configuration master();

    Comparator<? extends Configuration> comparator();

    //buildInInterpreters
}
