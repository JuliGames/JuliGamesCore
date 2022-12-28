package net.juligames.core.adventure;

import com.google.errorprone.annotations.DoNotCall;
import net.juligames.core.api.jdbi.DBReplacement;
import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.MiniMessageSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 21.11.2022
 */
public interface AdventureTagManager extends MiniMessageSerializer {
    @Contract(pure = true)
    @NotNull MiniMessage getMiniMessage();

    @Contract(pure = true)
    @NotNull TagResolver getFallbackResolver();

    @Contract(pure = true)
    @NotNull Component fallbackResolve(String miniMessage);

    void register(@NotNull DBReplacement dbReplacement);

    void reload();

    void register(String name, Tag tag);

    @ApiStatus.Internal
    void clearResolver();

    @NotNull TagResolver getResolver();

    @NotNull Component resolve(String miniMessage);

    @NotNull Component resolve(@NotNull Message message);

    @NotNull String resolvePlain(@NotNull Message message);

    @Deprecated
    @NotNull String resolveLegacy(@NotNull Message message);

    @ApiStatus.Internal
    @DoNotCall
    void load();
}
