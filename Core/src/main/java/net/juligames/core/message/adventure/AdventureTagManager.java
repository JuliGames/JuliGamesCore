package net.juligames.core.message.adventure;

import de.bentzin.tools.register.Registerator;
import net.juligames.core.Core;
import net.juligames.core.api.jdbi.DBReplacement;
import net.juligames.core.api.message.TagManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public final class AdventureTagManager extends Registerator<Tag> implements TagManager {


    @Override
    public boolean register(DBReplacement dbReplacement) {
        try {
            register(JDBITagAdapter.fromJDBI(dbReplacement));
            return true;
        } catch (DuplicateEntryException e) {
            Core.getInstance().getCoreLogger().warning("someone tried to register a tag that was already registered...");
            e.printStackTrace();
            return false;
        }
    }

    @Contract(pure = true)
    public @NotNull MiniMessage getMiniMessage(){
        return MiniMessage.miniMessage();
    }

    public @NotNull TagResolver getResolver(){
        return TagResolver.builder().build(); //TODO
    }

    public @NotNull Component resolve(String miniMessage) {
        return getMiniMessage().deserialize(miniMessage,getResolver());
    }

    @Contract(pure = true)
    public @NotNull TagResolver getFallbackResolver() {
        return TagResolver.standard();
    }

    @Contract(pure = true)
    public @NotNull Component fallbackResolve(String miniMessage) {
        return getMiniMessage().deserialize(miniMessage,getFallbackResolver());
    }
}
