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
 * Provides an api for accessing and managing messages and message
 * replacements.
 *
 * @author Ture Bentzin
 * @apiNote Please consider that the core your using may not support all the
 * operations below. If a core should not support one of the methods
 * there are two options what could happen. First the core could
 * automatically execute a similar Method and print a warning. If this
 * should not be possible then the Core will throw an
 * {@link UnsupportedOperationException} with further information on
 * how to approach this issue.
 * @since 18.11.2022
 */
@SuppressWarnings("unused")
public interface MessageApi {

    /**
     * Returns an array from a varargs input.
     *
     * @param ts  the input arguments
     * @param <T> the type of the array elements
     * @return the array
     */
    @SafeVarargs
    @ApiStatus.AvailableSince("1.5")
    static <T> T[] arrFromVargs(T... ts) {
        return ts;
    }

    /**
     * Alias for {@link #arrFromVargs(Object[])}.
     *
     * @param ts  the input arguments
     * @param <T> the type of the array elements
     * @return the array
     * @apiNote You should import this method static!
     */
    @SafeVarargs
    @ApiStatus.AvailableSince("1.5")
    static <T> T[] repl(T... ts) {
        return arrFromVargs(ts);
    }

    /**
     * Calls a message DAO extension.
     *
     * @param extensionCallback the callback to call
     * @param <R>               the return type of the callback
     * @return the callback result
     * @apiNote Internal use only.
     */
    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R> @Nullable R callMessageExtension(@NotNull ExtensionCallback<R, MessageDAO, RuntimeException> extensionCallback);

    /**
     * Calls a locale DAO extension.
     *
     * @param extensionCallback the callback to call
     * @param <R>               the return type of the callback
     * @return the callback result
     * @apiNote Internal use only.
     * @since experimental
     */
    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R> @Nullable R callLocaleExtension(@NotNull ExtensionCallback<R, LocaleDAO, RuntimeException> extensionCallback);

    /**
     * Calls an extension callback with a ReplacementDAO instance as the second argument.
     *
     * @param extensionCallback the extension callback to call
     * @param <R>               the type of the return value
     * @return the result of the extension callback, or null if the callback returns null
     * @throws RuntimeException if the extension callback throws an exception
     */
    @ApiStatus.Internal
    @ApiStatus.Experimental
    @Nullable <R> R callReplacementExtension(@NotNull ExtensionCallback<R, ReplacementDAO, RuntimeException> extensionCallback);

    /**
     * Calls an extension callback with a ReplacementTypeDAO instance as the second argument.
     *
     * @param extensionCallback the extension callback to call
     * @param <R>               the type of the return value
     * @return the result of the extension callback, or null if the callback returns null
     * @throws RuntimeException if the extension callback throws an exception
     */
    @ApiStatus.Internal
    @ApiStatus.Experimental
    @Nullable <R> R callReplacementTypeExtension(@NotNull ExtensionCallback<R, ReplacementTypeDAO, RuntimeException> extensionCallback);


    //get

    /**
     * Returns the {@link Message} with the specified message key and locale. If there is no {@link Message} with the
     * specified key and locale, a FallbackMessage is returned instead.
     *
     * @param messageKey the key of the message to retrieve.
     * @param locale     the locale of the message to retrieve.
     * @return the {@link Message} with the specified key and locale, or a FallbackMessage if no such message exists.
     */
    @NotNull Message getMessage(@NotNull String messageKey, @NotNull Locale locale);

    /**
     * Returns the {@link Message} with the specified message key and locale, with placeholders replaced by the specified
     * replacement strings. If there is no {@link Message} with the specified key and locale, a FallbackMessage is returned
     * instead.
     *
     * @param messageKey   the key of the message to retrieve.
     * @param locale       the locale of the message to retrieve.
     * @param replacements the replacement strings to substitute into the message.
     * @return the {@link Message} with the specified key and locale, or a FallbackMessage if no such message exists.
     */
    @NotNull Message getMessage(@NotNull String messageKey, @NotNull Locale locale, String... replacements);

    /**
     * Returns the {@link Message} with the specified message key and locale string. If there is no {@link Message} with the
     * specified key and locale, a FallbackMessage is returned instead.
     *
     * @param messageKey the key of the message to retrieve.
     * @param locale     the locale string of the message to retrieve.
     * @return the {@link Message} with the specified key and locale, or a FallbackMessage if no such message exists.
     */
    @NotNull Message getMessage(@NotNull String messageKey, @NotNull String locale);

    /**
     * Returns the {@link Message} with the specified message key and locale string, with placeholders replaced by the specified
     * replacement strings. If there is no {@link Message} with the specified key and locale, a FallbackMessage is returned
     * instead.
     *
     * @param messageKey   the key of the message to retrieve.
     * @param locale       the locale string of the message to retrieve.
     * @param replacements the replacement strings to substitute into the message.
     * @return the {@link Message} with the specified key and locale, or a FallbackMessage if no such message exists.
     */
    @NotNull Message getMessage(@NotNull String messageKey, @NotNull String locale, String... replacements);


    /**
     * Returns the {@link Message} with the specified message key and locale string. If there is no {@link Message} with the
     * specified key and locale, a FallbackMessage is returned instead.
     *
     * @param messageKey the key of the message to retrieve.
     * @param dbLocale   the locale string of the message to retrieve.
     * @return the {@link Message} with the specified key and locale, or a FallbackMessage if no such message exists.
     */
    @NotNull Message getMessage(@NotNull String messageKey, @NotNull DBLocale dbLocale);

    /**
     * Returns the {@link Message} with the specified message key and locale string, with placeholders replaced by the specified
     * replacement strings. If there is no {@link Message} with the specified key and locale, a FallbackMessage is returned
     * instead.
     *
     * @param messageKey   the key of the message to retrieve.
     * @param dbLocale     the locale string of the message to retrieve.
     * @param replacements the replacement strings to substitute into the message.
     * @return the {@link Message} with the specified key and locale, or a FallbackMessage if no such message exists.
     */
    @NotNull Message getMessage(@NotNull String messageKey, @NotNull DBLocale dbLocale, String... replacements);

    /**
     * Returns a collection of Messages from the MessageSystem that match the specified message key.
     *
     * @param messageKey the message key used to retrieve the messages
     * @return a collection of messages that match the specified message key
     */
    @NotNull Collection<? extends Message> getMessage(@NotNull String messageKey);

    /**
     * Returns a collection of Messages from the MessageSystem that match the specified message key with the
     * specified replacements applied to each message.
     *
     * @param messageKey   the message key used to retrieve the messages
     * @param replacements the array of replacements to apply to the messages
     * @return a collection of messages that match the specified message key with the replacements applied
     */
    @NotNull Collection<? extends Message> getMessage(@NotNull String messageKey, String... replacements);


    /**
     * Retrieves the message from the MessageSystem using the specified key and locale. The locale is automatically selected
     * based on the provided locale parameter.
     *
     * @param messageKey The key to retrieve the message.
     * @param locale     The locale to use for the message. Can be null.
     * @return The message.
     */
    @NotNull Message getMessageSmart(@NotNull String messageKey, @Nullable Locale locale);

    /**
     * Retrieves the message from the MessageSystem using the specified key, locale, and replacements. The locale is
     * automatically selected based on the provided locale parameter.
     *
     * @param messageKey   The key to retrieve the message.
     * @param locale       The locale to use for the message. Can be null.
     * @param replacements The values to replace the placeholders in the message.
     * @return The message.
     */
    @NotNull Message getMessageSmart(@NotNull String messageKey, @Nullable Locale locale, String... replacements);

    /**
     * Retrieves the message from the MessageSystem using the specified key and locale. The locale is automatically selected
     * based on the provided locale parameter.
     *
     * @param messageKey The key to retrieve the message.
     * @param locale     The locale to use for the message.
     * @return The message.
     */
    @NotNull Message getMessageSmart(@NotNull String messageKey, @NotNull String locale);

    /**
     * Retrieves the message from the MessageSystem using the specified key, locale, and replacements. The locale is
     * automatically selected based on the provided locale parameter.
     *
     * @param messageKey   The key to retrieve the message.
     * @param locale       The locale to use for the message.
     * @param replacements The values to replace the placeholders in the message.
     * @return The message.
     */
    @NotNull Message getMessageSmart(@NotNull String messageKey, @NotNull String locale, String... replacements);

    /**
     * Retrieves the message from the MessageSystem using the specified key and locale. The locale is automatically selected
     * based on the provided locale parameter.
     *
     * @param messageKey The key to retrieve the message.
     * @param dbLocale   The DBLocale to use for the message.
     * @return The message.
     */
    @NotNull Message getMessageSmart(@NotNull String messageKey, @NotNull DBLocale dbLocale);

    /**
     * Retrieves the message from the MessageSystem using the specified key, locale, and replacements. The locale is
     * automatically selected based on the provided locale parameter.
     *
     * @param messageKey   The key to retrieve the message.
     * @param dbLocale     The DBLocale to use for the message.
     * @param replacements The values to replace the placeholders in the message.
     * @return The message.
     */
    @NotNull Message getMessageSmart(@NotNull String messageKey, @NotNull DBLocale dbLocale, String... replacements);


    /**
     * Returns all messages for the specified {@link Locale}.
     *
     * @param locale the {@code Locale} used to filter the messages
     * @return a collection of all messages for the specified {@code Locale}, or an empty collection if none were found
     */
    @NotNull Collection<? extends Message> getAllFromLocale(@NotNull Locale locale);

    /**
     * Returns all messages for the specified {@link Locale} with replacements applied.
     *
     * @param locale       the {@code Locale} used to filter the messages
     * @param replacements an optional list of replacements to apply to the messages
     * @return a collection of all messages for the specified {@code Locale}, with replacements applied, or an empty collection if none were found
     */
    @NotNull Collection<? extends Message> getAllFromLocale(@NotNull Locale locale, String... replacements);

    /**
     * Returns all messages for the specified locale code.
     *
     * @param locale the locale code used to filter the messages
     * @return a collection of all messages for the specified locale code, or an empty collection if none were found
     */
    @NotNull Collection<? extends Message> getAllFromLocale(@NotNull String locale);

    /**
     * Returns all messages for the specified locale code with replacements applied.
     *
     * @param locale       the locale code used to filter the messages
     * @param replacements an optional list of replacements to apply to the messages
     * @return a collection of all messages for the specified locale code, with replacements applied, or an empty collection if none were found
     */
    @NotNull Collection<? extends Message> getAllFromLocale(@NotNull String locale, String... replacements);

    /**
     * Returns all messages for the specified database locale.
     *
     * @param dbLocale the database locale used to filter the messages
     * @return a collection of all messages for the specified database locale, or an empty collection if none were found
     */
    @NotNull Collection<? extends Message> getAllFromLocale(@NotNull DBLocale dbLocale);

    /**
     * Returns all messages for the specified database locale with replacements applied.
     *
     * @param dbLocale     the database locale used to filter the messages
     * @param replacements an optional list of replacements to apply to the messages
     * @return a collection of all messages for the specified database locale, with replacements applied, or an empty collection if none were found
     */
    @NotNull Collection<? extends Message> getAllFromLocale(@NotNull DBLocale dbLocale, String... replacements);


    /**
     * Returns all messages in the message system.
     * This method is experimental and may be removed or changed in a future release.
     *
     * @return A collection of all messages in the message system.
     */
    @ApiStatus.Experimental
    @NotNull Collection<? extends Message> getAll();

    /**
     * Returns all messages in the message system with the given replacements.
     * This method is experimental and may be removed or changed in a future release.
     *
     * @param replacements The replacements for the messages.
     * @return A collection of all messages in the message system with the given replacements.
     */
    @ApiStatus.Experimental
    @NotNull Collection<? extends Message> getAll(String... replacements);

    /**
     * Returns a stream of all the database messages in the message system.
     * This method is internal and should not be used outside of the message system implementation.
     *
     * @return A stream of all the database messages in the message system.
     */
    @ApiStatus.Internal
    @NotNull Stream<? extends DBMessage> streamData();


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
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, String... replacement);

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
