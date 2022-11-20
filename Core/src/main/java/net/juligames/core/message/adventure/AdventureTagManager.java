package net.juligames.core.message.adventure;

import de.bentzin.tools.register.Registerator;
import net.juligames.core.Core;
import net.juligames.core.api.jdbi.DBReplacement;
import net.juligames.core.api.jdbi.ReplacementDAO;
import net.juligames.core.api.jdbi.mapper.bean.ReplacementBean;
import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.TagManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.ComponentSerializer;
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
public final class AdventureTagManager implements TagManager {

    private TagResolver internalResolver = TagResolver.standard();

    @Override
    public void register(@NotNull DBReplacement dbReplacement) {
        register(dbReplacement.getTag(), JDBITagAdapter.fromJDBI(dbReplacement));
    }

    @Override
    public void reload() {
        clearResolver();
        load();
    }

    public void register(String name, Tag tag) {
        Core.getInstance().getCoreLogger().info("added: " + name + " as " + tag);
        internalResolver = TagResolver.builder().resolver(internalResolver).tag(name,tag).build();
    }

    @ApiStatus.Internal
    public void clearResolver() {
        internalResolver = TagResolver.standard();
    }

    @Contract(pure = true)
    public @NotNull MiniMessage getMiniMessage(){
        return MiniMessage.miniMessage();
    }

    public @NotNull TagResolver getResolver(){
        return internalResolver;
    }

    public @NotNull Component resolve(String miniMessage) {
        return getMiniMessage().deserialize(miniMessage,getResolver());
    }

    public @NotNull Component resolve(@NotNull Message message) {
        return getMiniMessage().deserialize(message.getMiniMessage(),getResolver());
    }

    public @NotNull String resolvePlain(@NotNull Message message) {
        Component deserialize = getMiniMessage().deserialize(message.getMiniMessage(), getResolver());
        return PlainTextComponentSerializer.plainText().serialize(deserialize);
    }

    @Deprecated
    public @NotNull String resolveLegacy(@NotNull Message message) {
        Component deserialize = getMiniMessage().deserialize(message.getMiniMessage(), getResolver());
        return LegacyComponentSerializer.legacyAmpersand().serialize(deserialize);
    }

    @Contract(pure = true)
    public @NotNull TagResolver getFallbackResolver() {
        return TagResolver.standard();
    }

    @Contract(pure = true)
    public @NotNull Component fallbackResolve(String miniMessage) {
        return getMiniMessage().deserialize(miniMessage,getFallbackResolver());
    }


    public void load() {
        List<DBReplacement> dbReplacements =
                Core.getInstance().getMessageApi().callReplacementExtension(ReplacementDAO::listAll);
        for (DBReplacement replacement : dbReplacements) {
            register(replacement);
        }
    }
}
