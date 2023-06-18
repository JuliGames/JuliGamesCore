package net.juligames.core.adventure.api;

import net.juligames.core.adventure.AdventureTagManager;
import net.juligames.core.api.API;
import net.juligames.core.api.config.representations.Representation;
import net.juligames.core.api.jdbi.DBLocale;
import net.juligames.core.api.message.Message;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
@ApiStatus.AvailableSince("1.6")
public class MessageRepresentation implements ComponentLike, Representation<Component> {

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
    public static @NotNull MessageRepresentation represent(String messageKey, @NotNull Pointered pointered) {
        return new MessageRepresentation(API.get().getMessageApi().getMessageSmart(messageKey,
                pointered.get(Identity.LOCALE).orElse(defaultLocale())));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull MessageRepresentation represent(String messageKey, @NotNull Pointered pointered, String... replacements) {
        return new MessageRepresentation(API.get().getMessageApi().getMessageSmart(messageKey, pointered.get(Identity.LOCALE).orElse(defaultLocale()), replacements));
    }

    private static @NotNull Locale defaultLocale() {
        return API.get().getMessageApi().defaultUtilLocale();
    }

    @Override
    public @NotNull Component asComponent() {
        return adventureTagManager.resolve(message);
    }

    public Message getMessage() {
        return message;
    }

    @Override
    @ApiStatus.AvailableSince("1.6")
    public Component represent() {
        return asComponent();
    }
}
