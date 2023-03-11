package net.juligames.core.api.message;

import net.juligames.core.api.jdbi.*;
import org.jdbi.v3.core.extension.ExtensionCallback;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * @author Ture Bentzin
 * 18.11.2022
 * @apiNote Please consider that the core your using may not support all the operations below. If a core should not
 * support one of the methods there are two options what could happen. First the core could automatically execute a similar
 * Method and print a warning. If this should not be possible then the Core will throw an {@link UnsupportedOperationException} with further
 * information on how to approach this issue.
 */
@SuppressWarnings("unused")
public interface MessageApi {

    @SafeVarargs
    @ApiStatus.AvailableSince("1.5")
    static <T> T[] arrFromVargs(T... ts) {
        return ts;
    }


    /**
     * This is an alias for {@link #arrFromVargs(Object[])}
     *
     * @apiNote You should import this method static!
     */
    @SafeVarargs
    @ApiStatus.AvailableSince("1.5")
    static <T> T[] repl(T... ts) {
        return arrFromVargs(ts);
    }

    //DAO
    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R>
    @Nullable R callMessageExtension(@NotNull ExtensionCallback<R, MessageDAO, RuntimeException> extensionCallback);

    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R>
    @Nullable R callLocaleExtension(@NotNull ExtensionCallback<R, LocaleDAO, RuntimeException> extensionCallback);

    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R>
    @Nullable R callReplacementExtension(@NotNull ExtensionCallback<R, ReplacementDAO, RuntimeException> extensionCallback);

    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R>
    @Nullable R callReplacementTypeExtension(@NotNull ExtensionCallback<R, ReplacementTypeDAO, RuntimeException> extensionCallback);

    //get
    @NotNull Message getMessage(@NotNull String messageKey, @NotNull Locale locale);

    @NotNull Message getMessage(@NotNull String messageKey, @NotNull Locale locale, String... replacements);

    @NotNull Message getMessage(@NotNull String messageKey, @NotNull String locale);

    @NotNull Message getMessage(@NotNull String messageKey, @NotNull String locale, String... replacements);

    @NotNull Message getMessage(@NotNull String messageKey, @NotNull DBLocale dbLocale);

    @NotNull Message getMessage(@NotNull String messageKey, @NotNull DBLocale dbLocale, String... replacements);

    @NotNull Collection<? extends Message> getMessage(@NotNull String messageKey);

    @NotNull Collection<? extends Message> getMessage(@NotNull String messageKey, String... replacements);

    @NotNull Message getMessageSmart(@NotNull String messageKey, @Nullable Locale locale);

    @NotNull Message getMessageSmart(@NotNull String messageKey, @Nullable Locale locale, String... replacements);

    @NotNull Message getMessageSmart(@NotNull String messageKey, @NotNull String locale);

    @NotNull Message getMessageSmart(@NotNull String messageKey, @NotNull String locale, String... replacements);

    @NotNull Message getMessageSmart(@NotNull String messageKey, @NotNull DBLocale dbLocale);

    @NotNull Message getMessageSmart(@NotNull String messageKey, @NotNull DBLocale dbLocale, String... replacements);

    @NotNull Collection<? extends Message> getAllFromLocale(@NotNull Locale locale);

    @NotNull Collection<? extends Message> getAllFromLocale(@NotNull Locale locale, String... replacements);

    @NotNull Collection<? extends Message> getAllFromLocale(@NotNull String locale);

    @NotNull Collection<? extends Message> getAllFromLocale(@NotNull String locale, String... replacements);

    @NotNull Collection<? extends Message> getAllFromLocale(@NotNull DBLocale dbLocale);

    @NotNull Collection<? extends Message> getAllFromLocale(@NotNull DBLocale dbLocale, String... replacements);

    @ApiStatus.Experimental
    @NotNull
    Collection<? extends Message> getAll();

    @ApiStatus.Experimental
    @NotNull
    Collection<? extends Message> getAll(String... replacements);

    @ApiStatus.Internal
    @NotNull
    Stream<? extends DBMessage> streamData();

    @NotNull Collection<? extends DBReplacement> getReplacers();

    //register

    @Deprecated
    void registerMessage(@NotNull String messageKey);

    void registerMessage(@NotNull String messageKey, @NotNull String defaultMiniMessage);

    /**
     * If you want to register a legacyMessage please use the provided method in the AdventureAPI
     *
     * @param messageKey        the ley
     * @param thirdPartyMessage the input
     * @param dealer            the dealer
     */
    @ApiStatus.AvailableSince("1.4")
    void registerThirdPartyMessage(@NotNull String messageKey, @NotNull String thirdPartyMessage, @NotNull CustomMessageDealer dealer);

    boolean hasMessage(@NotNull String messageKey);

    boolean hasMessage(@NotNull String messageKey, @NotNull String locale);

    boolean hasMessage(@NotNull String messageKey, @NotNull Locale locale);

    boolean hasMessage(@NotNull String messageKey, @NotNull DBLocale locale);

    //send

    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient);

    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient, @NotNull Locale overrideLocale);

    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient, @NotNull String overrideLocale);

    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient, @NotNull DBLocale overrideLocale);

    @NotNull MultiMessagePostScript broadcastMessage(@NotNull String messageKey, @NotNull Locale defaultLocale);

    @NotNull MultiMessagePostScript broadcastMessage(@NotNull String messageKey, @NotNull String defaultLocale);

    @NotNull MultiMessagePostScript broadcastMessage(@NotNull String messageKey, @NotNull DBLocale defaultLocale);

    @NotNull Collection<? extends MessagePostScript> broadcastMessage(@NotNull String messageKey);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient, @NotNull String overrideLocale);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient, @NotNull Locale overrideLocale);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient, @NotNull DBLocale overrideLocale);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull String overrideLocale);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull Locale overrideLocale);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull DBLocale overrideLocale);

    @NotNull Collection<MultiMessagePostScript> sendMessageSmart(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, String... replacement);

    @NotNull Collection<MessagePostScript> sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients);

    @NotNull MultiMessagePostScript sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull String overrideLocale);

    @NotNull MultiMessagePostScript sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull Locale overrideLocale);

    @NotNull MultiMessagePostScript sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull DBLocale overrideLocale);

    @NotNull Collection<MessagePostScript> sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients, String... replacements);

    @NotNull MultiMessagePostScript sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull String overrideLocale, String... replacements);

    @NotNull MultiMessagePostScript sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull Locale overrideLocale, String... replacements);

    @NotNull MultiMessagePostScript sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull DBLocale overrideLocale, String... replacements);


    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys, @NotNull Locale defaultLocale);

    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys, @NotNull String defaultLocale);

    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys, @NotNull DBLocale defaultLocale);

    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys);

    //sending with replacements
    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient, String... replacement);

    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient, @NotNull Locale overrideLocale, String... replacement);

    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient, @NotNull String overrideLocale, String... replacement);

    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient, @NotNull DBLocale overrideLocale, String... replacement);

    @NotNull MultiMessagePostScript broadcastMessage(@NotNull String messageKey, @NotNull Locale defaultLocale, String... replacement);

    @NotNull MultiMessagePostScript broadcastMessage(@NotNull String messageKey, @NotNull String defaultLocale, String... replacement);

    @NotNull MultiMessagePostScript broadcastMessage(@NotNull String messageKey, @NotNull DBLocale defaultLocale, String... replacement);

    @NotNull Collection<? extends MessagePostScript> broadcastMessage(@NotNull String messageKey, String... replacement);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient, String... replacement);

    /**
     * @deprecated use {@link MessageApi#sendMessageSmart(Collection, Collection, String...)} instead
     */
    @Deprecated
    @NotNull
    MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, String... replacement);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient, @NotNull String overrideLocale, String... replacement);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient, @NotNull Locale overrideLocale, String... replacement);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient, @NotNull DBLocale overrideLocale, String... replacement);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull String overrideLocale, String... replacement);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull Locale overrideLocale, String... replacement);

    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull DBLocale overrideLocale, String... replacement);

    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys, @NotNull Locale defaultLocale, String... replacement);

    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys, @NotNull String defaultLocale, String... replacement);

    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys, @NotNull DBLocale defaultLocale, String... replacement);

    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys, String... replacement);

    @NotNull String defaultLocale();

    @NotNull Locale defaultUtilLocale();

    //TagManager getTagManager(); removed in favor of AdventureCore / AdventureAPI
}
