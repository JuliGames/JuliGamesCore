package net.juligames.core.adventure.api;

import net.juligames.core.adventure.AdventureTagManager;
import net.juligames.core.api.API;
import net.juligames.core.api.config.BuildInInterpreters;
import net.juligames.core.api.jdbi.DBLocale;
import net.juligames.core.api.message.Message;
import net.juligames.core.api.misc.EntryInterpretationUtil;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Ture Bentzin
 * 25.03.2023
 */
@ApiStatus.AvailableSince("1.6")
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
                pointered.get(Identity.LOCALE).orElse(defaultLocale())));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull MessageRepresentation represent(String messageKey, Pointered pointered, String... replacements) {
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

    private void demo() {

        Map<File, URL> fileURLMap = new HashMap<>();
        Map<String, String> stringStringMap = EntryInterpretationUtil.reverseMap(fileURLMap, BuildInInterpreters.fileInterpreter(), BuildInInterpreters.urlInterpreter());
        Map<File, URL> fileURLMap1 = EntryInterpretationUtil.interpretMap(stringStringMap, BuildInInterpreters.fileInterpreter(), BuildInInterpreters.urlInterpreter());
        assert fileURLMap1.equals(fileURLMap);

        Stream<Map.Entry<String, String>> entryStream = fileURLMap.entrySet().stream().map(fileURLEntry -> EntryInterpretationUtil.reverseEntry(fileURLEntry, BuildInInterpreters.fileInterpreter(), BuildInInterpreters.urlInterpreter()));

    }
}
