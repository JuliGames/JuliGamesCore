package net.juligames.core.api.config;

import java.util.Comparator;

/**
 * @author Ture Bentzin
 * 26.11.2022
 */
public interface ConfigurationAPI {

    Configuration search(String name);
    Configuration master();

    Comparator<Configuration> comparator();
}
