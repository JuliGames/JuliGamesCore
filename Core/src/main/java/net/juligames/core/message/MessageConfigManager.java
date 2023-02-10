package net.juligames.core.message;

import net.juligames.core.api.API;
import net.juligames.core.api.config.Configuration;

import java.util.Properties;

/**
 * @author Ture Bentzin
 * 02.02.2023
 */
public final class MessageConfigManager {

    private static final String name = "messageSystem";

    private MessageConfigManager() {
    }

    public static void init() {
        Properties properties = new Properties();
        properties.setProperty("configuration_name", name);
        properties.setProperty("max_locale_length", "3");
        properties.setProperty("throw_on_invalid_locale", "false");
        properties.setProperty("warn_on_invalid_locale", "true");
        Configuration orCreate = API.get().getConfigurationApi().getOrCreate(properties);
    }

    public static Configuration configuration() {
        return API.get().getConfigurationApi().getOrCreate(name);
    }

    public static String getName() {
        return name;
    }

    public static int getMaxLocaleLength() {
        return configuration().getInteger("max_locale_length").orElse(3);
    }

    public static boolean getThrowOnInvalidLocale() {
        return configuration().getBoolean("throw_on_invalid_locale").orElse(false);
    }

    public static boolean getWarnOnInvalidLocale() {
        return configuration().getBoolean("warn_on_invalid_locale").orElse(true);
    }
}
