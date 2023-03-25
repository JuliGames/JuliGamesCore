package net.juligames.core.adventure.api;

import net.juligames.core.adventure.AdventureTagManager;
import net.juligames.core.api.API;
import net.juligames.core.api.jdbi.DBLocale;
import net.juligames.core.api.jdbi.DBMessage;
import net.juligames.core.api.message.Message;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointer;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
public class MessageRepresentation implements ComponentLike {

    private final @NotNull AdventureTagManager adventureTagManager;
    private final @NotNull Message message;
    public MessageRepresentation(@NotNull AdventureTagManager adventureTagManager, @NotNull Message message) {
        this.adventureTagManager = adventureTagManager;
        this.message = message;
    }

    public MessageRepresentation(@NotNull Message message) {
        this.adventureTagManager = AdventureAPI.get().getAdventureTagManager();
        this.message = message;
    }

    @Contract("_, _ -> new")
    public static @NotNull MessageRepresentation represent(String messageKey, String locale) {
        return new MessageRepresentation(API.get().getMessageApi().getMessageSmart(messageKey, locale));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull MessageRepresentation represent(String messageKey, String locale, String... replacements) {
        return new MessageRepresentation(API.get().getMessageApi().getMessageSmart(messageKey, locale, replacements));
    }

    @Contract("_, _ -> new")
    public static @NotNull MessageRepresentation represent(String messageKey, Locale locale) {
        return new MessageRepresentation(API.get().getMessageApi().getMessageSmart(messageKey, locale));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull MessageRepresentation represent(String messageKey, Locale locale, String... replacements) {
        return new MessageRepresentation(API.get().getMessageApi().getMessageSmart(messageKey, locale, replacements));
    }

    @Contract("_, _ -> new")
    public static @NotNull MessageRepresentation represent(String messageKey, DBLocale locale) {
        return new MessageRepresentation(API.get().getMessageApi().getMessageSmart(messageKey, locale));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull MessageRepresentation represent(String messageKey, DBLocale locale, String... replacements) {
        return new MessageRepresentation(API.get().getMessageApi().getMessageSmart(messageKey, locale, replacements));
    }

    //Personal

    @Contract("_, _ -> new")
    public static @NotNull MessageRepresentation represent(String messageKey, Pointered pointered) {
        return new MessageRepresentation(API.get().getMessageApi().getMessageSmart(messageKey,
                pointered.get(Identity.LOCALE).orElseThrow()));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull MessageRepresentation represent(String messageKey, Pointered pointered, String... replacements) {
        return new MessageRepresentation(API.get().getMessageApi().getMessageSmart(messageKey, pointered.get(Identity.LOCALE).orElseThrow(), replacements));
    }



    @Override
    public @NotNull Component asComponent() {
        return adventureTagManager.resolve(message);
    }

    public Message getMessage() {
        return message;
    }
}
