package net.juligames.core.paper.misc;

import de.bentzin.tools.pair.EntryPairAdapter;
import de.bentzin.tools.pair.Pair;
import net.juligames.core.api.API;
import net.juligames.core.api.config.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

/**
 * @author Ture Bentzin
 * 17.02.2023
 */
@SuppressWarnings("unused")
@ApiStatus.AvailableSince("1.4")
public class ConfigurationYAMLConverter {

    private ConfigurationYAMLConverter() {
    }

    //Core -> Bukkit
    public static @NotNull YamlConfiguration readAndInsert(Configuration configuration, File file) {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        convert(configuration, yamlConfiguration);
        return yamlConfiguration;
    }

    public static @NotNull YamlConfiguration convert(Configuration configuration) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        convert(configuration, yamlConfiguration);
        return yamlConfiguration;
    }

    //Bukkit -> Core

    public static void convert(@NotNull YamlConfiguration yamlConfiguration, @NotNull Configuration configuration) {
        Set<String> keys = Objects.requireNonNull(yamlConfiguration).getKeys(true);
        for (String key : keys) {
            final String value = yamlConfiguration.getString(key);
            configuration.setString(key, value);
        }
    }

    public static void convert(@NotNull YamlConfiguration yamlConfiguration, @NotNull String hazel) {
        Configuration configuration = API.get().getConfigurationApi().getOrCreate(hazel);
        convert(yamlConfiguration, configuration);
    }

    /**
     * yamlConfiguration needs to contain configuration_name
     *
     * @return a new or existing configuration
     */
    public static @NotNull Configuration convert(YamlConfiguration yamlConfiguration) {
        List<Pair<String>> map = map(yamlConfiguration);
        String configurationName = yamlConfiguration.getString("configuration_name");
        Configuration configuration = API.get().getConfigurationApi().getOrCreate(configurationName);
        for (Pair<String> stringPair : map) {
            configuration.set(stringPair);
        }
        return configuration;
    }

    public static void convert(@NotNull Configuration configuration, @NotNull YamlConfiguration yamlConfiguration) {
        Map<String, String> export = configuration.export();
        export.forEach(yamlConfiguration::set);
    }

    public static @NotNull List<Pair<String>> map(@NotNull ConfigurationSection section) {
        ArrayList<Pair<String>> pairs = new ArrayList<>();
        final Set<String> keys = section.getKeys(true);
        for (String key : keys) {
            pairs.add(read(section, key));
        }
        return pairs;
    }

    public static List<Pair<String>> map(@NotNull Configuration configuration) {
        return configuration.entrySet().stream().map(EntryPairAdapter::pairFromEntry).toList();
    }

    @Contract("_, _ -> new")
    public static @NotNull Pair<String> read(@NotNull ConfigurationSection section, String key) {
        return new Pair<>(key, section.getString(key));
    }

    @Contract("_, _ -> new")
    public static @NotNull Pair<String> read(@NotNull Configuration configuration, String key) {
        return new Pair<>(key, configuration.getStringOrNull(key));
    }
}
