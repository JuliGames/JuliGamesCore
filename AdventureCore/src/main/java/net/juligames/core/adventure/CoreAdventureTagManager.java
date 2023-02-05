package net.juligames.core.adventure;

import com.google.errorprone.annotations.DoNotCall;
import net.juligames.core.api.API;
import net.juligames.core.api.jdbi.DBReplacement;
import net.juligames.core.api.jdbi.ReplacementDAO;
import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.TagManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public final class CoreAdventureTagManager implements TagManager, AdventureTagManager {

    private TagResolver internalResolver = TagResolver.standard();

    @Contract(pure = true)
    public @NotNull MiniMessage getMiniMessage() {
        return MiniMessage.miniMessage();
    }

    @Contract(pure = true)
    public @NotNull TagResolver getFallbackResolver() {
        return TagResolver.standard();
    }

    @Contract(pure = true)
    public @NotNull Component fallbackResolve(String miniMessage) {
        return getMiniMessage().deserialize(miniMessage, getFallbackResolver());
    }

    @Override
    public void register(@NotNull DBReplacement dbReplacement) {
        register(dbReplacement.getTag(), JDBITagAdapter.fromJDBI(dbReplacement));
    }

    @Override
    public void reload() {
        clearResolver();
        load();
    }

    @Override
    public void register(String name, Tag tag) {
        internalResolver = TagResolver.builder().resolver(internalResolver).tag(name, tag).build();
    }

    @Override
    @ApiStatus.Internal
    public void clearResolver() {
        internalResolver = TagResolver.standard();
    }

    @Override
    public @NotNull TagResolver getResolver() {
        return internalResolver;
    }

    @Override
    public @NotNull Component resolve(String miniMessage) {
        return getMiniMessage().deserialize(miniMessage, getResolver());
    }

    @Override
    public @NotNull Component resolve(@NotNull Message message) {
        return getMiniMessage().deserialize(message.getMiniMessage(), getResolver()); // https://docs.adventure.kyori.net/minimessage/dynamic-replacements.html#insert-some-unparsed-text
    }

    @Override
    public @NotNull String resolvePlain(@NotNull Message message) {
        return resolvePlain(message.getMiniMessage());
    }

    @Override
    @Deprecated
    public @NotNull String resolveLegacy(@NotNull Message message) {
        return resolveLegacy(message.getMiniMessage());
    }

    @Override
    public @NotNull String resolvePlain(@NotNull String miniMessage) {
        Component deserialize = getMiniMessage().deserialize(miniMessage, getResolver());
        return PlainTextComponentSerializer.plainText().serialize(deserialize);
    }

    @Override
    public @NotNull String resolveLegacy(@NotNull String miniMessage) {
        Component deserialize = getMiniMessage().deserialize(miniMessage, getResolver());
        return LegacyComponentSerializer.legacyAmpersand().serialize(deserialize);
    }


    @Override
    @ApiStatus.Internal
    @DoNotCall
    public void load() {
        List<DBReplacement> dbReplacements =
                API.get().getMessageApi().callReplacementExtension(ReplacementDAO::listAll);
        for (DBReplacement replacement : dbReplacements) {
            register(replacement);
        }
    }
}
