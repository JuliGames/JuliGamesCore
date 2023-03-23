package net.juligames.core.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.bentzin.tools.pair.Pair;
import net.juligames.core.api.cacheing.CacheApi;
import net.juligames.core.api.config.Interpreter;
import net.juligames.core.api.jdbi.DBMessage;
import net.juligames.core.api.misc.EntryInterpretationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

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

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Map<String, String> reverseCache(Interpreter<K> kInterpreter, Interpreter<V> vInterpreter, @NotNull Cache<K, V> cache) {
        return Map.ofEntries(EntryInterpretationUtil.reverseEntries(cache.asMap().entrySet(), kInterpreter, vInterpreter).toArray(Map.Entry[]::new));
    }


    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Map<K, V> interpretCache(Interpreter<K> kInterpreter, Interpreter<V> vInterpreter, @NotNull Cache<String, String> cache) {
        return Map.ofEntries(EntryInterpretationUtil.interpretEntries(cache.asMap().entrySet(), kInterpreter, vInterpreter).toArray(Map.Entry[]::new));
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
