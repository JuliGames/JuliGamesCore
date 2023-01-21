package net.juligames.core.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.bentzin.tools.pair.Pair;
import net.juligames.core.api.API;
import net.juligames.core.api.config.Configuration;
import net.juligames.core.api.jdbi.DBMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author Ture Bentzin
 * 21.01.2023
 */
public class MessageCaching {
    public static boolean enabled = false;
    private static Cache<Pair<String>, DBMessage> messageCache;

    private MessageCaching() {}

    public static void init() {
        loadFromConfiguration(produceMessageCachingConfiguration());
    }

    public static void loadFromConfiguration(@NotNull Configuration configuration) {
        enabled = configuration.getBoolean("enabled").orElse(false);
        if (!enabled) return;

        MessageCacheLoader cacheLoader = new MessageCacheLoader();
        Long refreshAfterWriteMillis = configuration.getLongOrNull("refresh_after_write_millis");
        Long invalidateAfterReadMillis = configuration.getLongOrNull("invalidate_after_read_millis");
        Long maximumSize = configuration.getLongOrNull("maximum_size");
        Caffeine<Object, Object> builder = Caffeine.newBuilder();
        if (refreshAfterWriteMillis != null) {
            builder.refreshAfterWrite(refreshAfterWriteMillis, TimeUnit.MICROSECONDS);
        }
        if (invalidateAfterReadMillis != null) {
            builder.expireAfterAccess(invalidateAfterReadMillis, TimeUnit.MILLISECONDS);
        }
        if (maximumSize != null) {
            builder.maximumSize(maximumSize);
        }

        messageCache = builder.build(cacheLoader);
    }

    public static Configuration produceMessageCachingConfiguration() {
        Properties defaults = new Properties();
        defaults.setProperty("configuration_name", "messageCaching");
        defaults.setProperty("configuration_header", "This is used to configure the message caching");
        defaults.setProperty("enabled", "false");
        defaults.setProperty("refresh_after_write_millis", "1200000");
        defaults.setProperty("invalidate_after_read_millis", "600000");
        defaults.setProperty("maximum_size", "100000");
        return API.get().getConfigurationApi().getOrCreate(defaults);
    }

    public static Cache<Pair<String>, DBMessage> messageCache() {
        return messageCache;
    }
}
