package net.juligames.core.api.cacheing;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.bentzin.tools.pair.Pair;
import net.juligames.core.api.config.Interpreter;
import net.juligames.core.api.jdbi.DBMessage;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

/**
 * @author Ture Bentzin
 * 21.01.2023
 */
public interface CacheApi {

    /**
     * Creates a cache with the default settings suggested by the Core
     *
     * @param <K> Key
     * @param <V> value
     * @return new {@link Cache}
     */
    <K, V> Cache<K, V> newCache();

    @ApiStatus.AvailableSince("1.5")
    <K,V> Map<String, String> reverseCache(Interpreter<K> kInterpreter, Interpreter<V> vInterpreter, Cache<K,V> cache);

    @ApiStatus.AvailableSince("1.5")
    <K,V> Map<K,V> interpretCache(Interpreter<K> kInterpreter, Interpreter<V> vInterpreter, Cache<String,String> cache);

    /**
     * Creates a builder to create your custom caches by default this will enter the suggested settings
     *
     * @param <K> key
     * @param <V> value
     */
    <K, V> Caffeine<K, V> newBuilder();

    /**
     * Cache used for message caching.
     * The key consists out of the following two things:
     * - MessageKey
     * - Locale (as String)
     * The Value is the MiniMessage without replacements applied
     *
     * @return the messageCache
     */
    Cache<Pair<String>, DBMessage> messageCache();
}
