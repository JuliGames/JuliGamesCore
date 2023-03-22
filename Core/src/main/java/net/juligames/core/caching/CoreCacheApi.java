package net.juligames.core.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.bentzin.tools.pair.Pair;
import net.juligames.core.api.cacheing.CacheApi;
import net.juligames.core.api.config.Configuration;
import net.juligames.core.api.err.dev.TODOException;
import net.juligames.core.api.jdbi.DBMessage;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author Ture Bentzin
 * 21.01.2023
 */
public class CoreCacheApi implements CacheApi {

    //This will be extended to read from configuration later, if good defaults are collected

    @Override
    public <K, V> Cache<K, V> newCache() {
        return Caffeine.newBuilder().build();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Caffeine newBuilder() {
        return Caffeine.newBuilder();
    }

    @Override
    public Cache<Pair<String>, DBMessage> messageCache() {
        return MessageCaching.messageCache();
    }
}
