package net.juligames.core.caching;

import com.github.benmanes.caffeine.cache.CacheLoader;
import de.bentzin.tools.pair.Pair;
import net.juligames.core.api.API;
import net.juligames.core.api.jdbi.DBMessage;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 21.01.2023
 */
public class MessageCacheLoader implements CacheLoader<Pair<String>, DBMessage> {

    @Override
    public @Nullable DBMessage load(@NotNull Pair<String> key) {
        final String messageKey = key.getFirst(), locale = key.getSecond();
        return API.get().getMessageApi().callMessageExtension(extension -> extension.select(messageKey, locale));
    }

}
