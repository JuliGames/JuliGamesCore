package net.juligames.core.api.message;

import net.juligames.core.api.jdbi.*;
import org.jdbi.v3.core.extension.ExtensionCallback;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Stream;

/*
The MessageApi interface is a high-level API for sending messages to various recipients.
It defines methods for sending single or multiple messages, with or without message key replacement, to either a single recipient or a collection of recipients.

The interface defines two types of message sending methods: sendMessage and broadcastMessage.
The sendMessage method is used to send a message to a single recipient, while the broadcastMessage method is used to send a message to multiple recipients.
Both methods have several overloaded versions to accommodate different scenarios, such as sending messages with or without message key replacements, specifying the default or override locales, and so on.

The MessageApi interface also provides two methods to retrieve the default locale, defaultLocale and defaultUtilLocale.
These methods return the default locale as a string and a java.util.Locale object, respectively.

Overall, the MessageApi interface is a powerful and flexible API for sending messages, suitable for a wide range of use cases.
 */

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


    /**
     * Returns a collection of all the replacers in the message system.
     *
     * @return a collection of all the replacers
     */
    @NotNull Collection<? extends DBReplacement> getReplacers();


    //register

    /**
     * Registers a message with the specified message key.
     *
     * @param messageKey the message key to register
     * @deprecated This method is deprecated and should be avoided. Instead, use the {@link #registerMessage(String, String)}
     * method to register a message with a defaultMiniMessage.
     */
    @Deprecated
    void registerMessage(@NotNull String messageKey);

    /**
     * Registers a message with the specified message key and default mini message.
     *
     * @param messageKey         the message key to register
     * @param defaultMiniMessage the default mini message for the message (EN_US)
     */
    void registerMessage(@NotNull String messageKey, @NotNull String defaultMiniMessage);

    /**
     * Registers a third-party message with the specified message key, third-party message, and custom message dealer.
     * If you want to register a legacy message, please use the provided method in the AdventureAPI.
     *
     * @param messageKey        the message key to register
     * @param thirdPartyMessage the third-party message to register (EN_US)
     * @param dealer            the custom message dealer to convert the third-party message to a message that can be handled
     *                          by the message system
     * @since 1.4
     */
    @ApiStatus.AvailableSince("1.4")
    void registerThirdPartyMessage(@NotNull String messageKey, @NotNull String thirdPartyMessage, @NotNull CustomMessageDealer dealer);


    /**
     * Checks if a message exists in EN_US for the given message key in the message system.
     *
     * @param messageKey the key of the message to check
     * @return {@code true} if a message exists for the given key, otherwise {@code false}
     */
    boolean hasMessage(@NotNull String messageKey);

    /**
     * Checks if a message exists for the given message key and locale in the message system.
     *
     * @param messageKey the key of the message to check
     * @param locale     the locale of the message to check
     * @return {@code true} if a message exists for the given key and locale, otherwise {@code false}
     */
    boolean hasMessage(@NotNull String messageKey, @NotNull String locale);

    /**
     * Checks if a message exists for the given message key and locale in the message system.
     *
     * @param messageKey the key of the message to check
     * @param locale     the locale of the message to check
     * @return {@code true} if a message exists for the given key and locale, otherwise {@code false}
     */
    boolean hasMessage(@NotNull String messageKey, @NotNull Locale locale);

    /**
     * Checks if a message exists for the given message key and locale in the message system.
     *
     * @param messageKey the key of the message to check
     * @param locale     the locale of the message to check
     * @return {@code true} if a message exists for the given key and locale, otherwise {@code false}
     */
    boolean hasMessage(@NotNull String messageKey, @NotNull DBLocale locale);


    //send

    /**
     * Sends a message to the specified recipient and returns a {@link MessagePostScript} that contains details about
     * the message that was sent. The best locale for the message is determined via the {@link MessageRecipient}.
     *
     * @param messageKey       the key of the message to send.
     * @param messageRecipient the recipient of the message.
     * @return a {@link MessagePostScript} that contains details about the sent message.
     */
    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient);

    /**
     * Sends the message with the specified message key to the given message recipient using the provided override locale.
     *
     * @param messageKey       the message key
     * @param messageRecipient the recipient of the message
     * @param overrideLocale   the locale to use instead of the best available locale for the recipient
     * @return a MessagePostScript providing details about the sent message
     */
    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient, @NotNull Locale overrideLocale);

    /**
     * Sends the message with the specified message key to the given message recipient using the provided override locale.
     *
     * @param messageKey       the message key
     * @param messageRecipient the recipient of the message
     * @param overrideLocale   the locale to use instead of the best available locale for the recipient as a string in the format of "language_country"
     * @return a MessagePostScript providing details about the sent message
     */
    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient, @NotNull String overrideLocale);

    /**
     * Sends the message with the specified message key to the given message recipient using the provided override locale.
     *
     * @param messageKey       the message key
     * @param messageRecipient the recipient of the message
     * @param overrideLocale   the locale to use instead of the best available locale for the recipient
     * @return a MessagePostScript providing details about the sent message
     */
    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient, @NotNull DBLocale overrideLocale);

    /**
     * Sends a message to all available recipients with the specified message key and default locale.
     *
     * @param messageKey    the key of the message to be sent
     * @param defaultLocale the default locale to be used for the message
     * @return a {@link MultiMessagePostScript} describing the details of the messages sent
     */
    @NotNull MultiMessagePostScript broadcastMessage(@NotNull String messageKey, @NotNull Locale defaultLocale);

    /**
     * Sends a message to all available recipients with the specified message key and default locale.
     *
     * @param messageKey    the key of the message to be sent
     * @param defaultLocale the default locale to be used for the message
     * @return a {@link MultiMessagePostScript} describing the details of the messages sent
     */
    @NotNull MultiMessagePostScript broadcastMessage(@NotNull String messageKey, @NotNull String defaultLocale);

    /**
     * Sends a message to all available recipients with the specified message key and default locale.
     *
     * @param messageKey    the key of the message to be sent
     * @param defaultLocale the default locale to be used for the message
     * @return a {@link MultiMessagePostScript} describing the details of the messages sent
     */
    @NotNull MultiMessagePostScript broadcastMessage(@NotNull String messageKey, @NotNull DBLocale defaultLocale);


    /**
     * Sends the message with the given {@code messageKey} to all available recipients and returns the
     * {@link Collection} of {@link MessagePostScript}s containing details about each sent message.
     *
     * @param messageKey the key of the message to send
     * @return a {@link Collection} of {@link MessagePostScript}s containing details about each sent message
     */
    @NotNull Collection<? extends MessagePostScript> broadcastMessage(@NotNull String messageKey);


    /**
     * Sends multiple messages to the same recipient.
     *
     * @param messageKeys      the collection of message keys to send
     * @param messageRecipient the recipient of the messages
     * @return the post-script describing the details of the messages sent
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient);

    /**
     * Sends multiple messages to multiple recipients.
     *
     * @param messageKeys       the collection of message keys to send
     * @param messageRecipients the collection of recipients of the messages
     * @return the post-script describing the details of the messages sent
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients);


    /**
     * Sends multiple messages to the same recipient with the given locale override.
     *
     * @param messageKeys      the collection of message keys
     * @param messageRecipient the message recipient
     * @param overrideLocale   the locale override
     * @return the MultiMessagePostScript representing the sent messages
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient, @NotNull String overrideLocale);

    /**
     * Sends multiple messages to the same recipient with the given locale override.
     *
     * @param messageKeys      the collection of message keys
     * @param messageRecipient the message recipient
     * @param overrideLocale   the locale override
     * @return the MultiMessagePostScript representing the sent messages
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient, @NotNull Locale overrideLocale);

    /**
     * Sends multiple messages to the same recipient with the given locale override.
     *
     * @param messageKeys      the collection of message keys
     * @param messageRecipient the message recipient
     * @param overrideLocale   the locale override
     * @return the MultiMessagePostScript representing the sent messages
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient, @NotNull DBLocale overrideLocale);


    /**
     * Sends a message composed of the given message keys to the specified message recipient, using the specified locale
     * to override the default locale set in the recipient.
     *
     * @param messageKeys       the collection of message keys to compose the message
     * @param messageRecipients the collection of message recipients to send the message to
     * @param overrideLocale    the locale to use for message translation, overriding the recipient's default locale
     * @return a MultiMessagePostScript containing details about the messages that were sent
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull Locale overrideLocale);

    /**
     * Sends a message composed of the given message keys to the specified message recipient, using the specified DBLocale
     * to override the default locale set in the recipient.
     *
     * @param messageKeys       the collection of message keys to compose the message
     * @param messageRecipients the collection of message recipients to send the message to
     * @param overrideLocale    the DBLocale to use for message translation, overriding the recipient's default locale
     * @return a MultiMessagePostScript containing details about the messages that were sent
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull DBLocale overrideLocale);

    /**
     * Sends a message composed of the given message keys to the specified message recipient, using the specified locale
     * code (e.g. "EN_US") to override the default locale set in the recipient.
     *
     * @param messageKeys       the collection of message keys to compose the message
     * @param messageRecipients the collection of message recipients to send the message to
     * @param overrideLocale    the locale code to use for message translation, overriding the recipient's default locale
     * @return a MultiMessagePostScript containing details about the messages that were sent
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull String overrideLocale);

    /**
     * Sends the given messages to the given recipients, replacing any placeholders in the messages with the given replacements.
     * The locale for each message is determined based on the locale of the recipient.
     *
     * @param messageKeys       the keys of the messages to send
     * @param messageRecipients the recipients to send the messages to
     * @param replacement       the replacements to use for any placeholders in the messages
     * @return a collection of MultiMessagePostScript objects describing the messages that were sent
     */
    @NotNull Collection<MultiMessagePostScript> sendMessageSmart(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, String... replacement);

    /**
     * Sends the message with the given message key to the specified collection of recipients.
     *
     * @param messageKey        the message key
     * @param messageRecipients the collection of message recipients
     * @return a collection of {@link MessagePostScript} objects describing the details of each message sent
     */
    @NotNull Collection<MessagePostScript> sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients);

    /**
     * Sends a message with the given message key to a collection of message recipients with an overridden locale.
     *
     * @param messageKey        The message key to use.
     * @param messageRecipients The collection of message recipients to send the message to.
     * @param overrideLocale    The locale to use instead of the recipients' default locale.
     * @return A MultiMessagePostScript containing details about the sent messages.
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull String overrideLocale);

    /**
     * Sends a message with the given message key to a collection of message recipients with an overridden locale.
     *
     * @param messageKey        The message key to use.
     * @param messageRecipients The collection of message recipients to send the message to.
     * @param overrideLocale    The locale to use instead of the recipients' default locale.
     * @return A MultiMessagePostScript containing details about the sent messages.
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull Locale overrideLocale);

    /**
     * Sends a message with the given message key to a collection of message recipients with an overridden locale.
     *
     * @param messageKey        The message key to use.
     * @param messageRecipients The collection of message recipients to send the message to.
     * @param overrideLocale    The locale to use instead of the recipients' default locale.
     * @return A MultiMessagePostScript containing details about the sent messages.
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull DBLocale overrideLocale);

    /**
     * Sends a message with the given message key and replacements to the specified recipients.
     *
     * @param messageKey        the message key for the message to be sent
     * @param messageRecipients the recipients to send the message to
     * @param replacements      replacements to be made in the message
     * @return a collection of {@code MessagePostScript} objects representing the details of the sent messages
     */
    @NotNull Collection<MessagePostScript> sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients, String... replacements);

    /**
     * Sends a message with the given message key, replacements, and locale override to the specified recipients.
     *
     * @param messageKey        the message key for the message to be sent
     * @param messageRecipients the recipients to send the message to
     * @param overrideLocale    the locale to use for the message, overriding any default locale
     * @param replacements      replacements to be made in the message
     * @return a {@code MultiMessagePostScript} object representing the details of the sent messages
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull String overrideLocale, String... replacements);

    /**
     * Sends a message with the given message key, replacements, and locale override to the specified recipients.
     *
     * @param messageKey        the message key for the message to be sent
     * @param messageRecipients the recipients to send the message to
     * @param overrideLocale    the locale to use for the message, overriding any default locale
     * @param replacements      replacements to be made in the message
     * @return a {@code MultiMessagePostScript} object representing the details of the sent messages
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull Locale overrideLocale, String... replacements);

    /**
     * Sends a message with a specific message key to a collection of message recipients,
     * with the option to override the default locale for message retrieval.
     *
     * @param messageKey        the key for the message to be sent
     * @param messageRecipients the collection of message recipients to receive the message
     * @param overrideLocale    the locale to use for message retrieval, overriding the default locale
     * @param replacements      the replacement values to be used in the message, if any
     * @return the post-script for the message sending operation
     */
    @NotNull
    MultiMessagePostScript sendMessage(@NotNull String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients,
                                       @NotNull DBLocale overrideLocale, String... replacements);

    /**
     * Sends the specified message to multiple recipients with the specified default locale.
     *
     * @param messageKeys   a collection of keys of the messages to be sent
     * @param defaultLocale the default locale for the messages
     * @return a {@link MultiMessagePostScript} object representing the result of the messages sent
     */
    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys, @NotNull Locale defaultLocale);

    /**
     * Sends the specified message to multiple recipients with the specified default locale.
     *
     * @param messageKeys   a collection of keys of the messages to be sent
     * @param defaultLocale the default locale for the messages
     * @return a {@link MultiMessagePostScript} object representing the result of the messages sent
     */
    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys, @NotNull String defaultLocale);

    /**
     * Sends the specified message to multiple recipients with the specified default locale.
     *
     * @param messageKeys   a collection of keys of the messages to be sent
     * @param defaultLocale the default locale for the messages
     * @return a {@link MultiMessagePostScript} object representing the result of the messages sent
     */
    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys, @NotNull DBLocale defaultLocale);

    /**
     * Sends the specified message to multiple recipients with the default locale.
     *
     * @param messageKeys a collection of keys of the messages to be sent
     * @return a {@link MultiMessagePostScript} object representing the result of the messages sent
     */
    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys);

    /**
     * Sends a message with replacement values to a single recipient.
     *
     * @param messageKey       The key identifying the message to send.
     * @param messageRecipient The recipient to send the message to.
     * @param replacement      The replacement values to use in the message.
     * @return A {@code MessagePostScript} object representing the sent message.
     */
    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient, String... replacement);

    /**
     * Sends a message with replacement values to a single recipient, overriding the locale of the message.
     *
     * @param messageKey       The key identifying the message to send.
     * @param messageRecipient The recipient to send the message to.
     * @param overrideLocale   The locale to use for the message, overriding the default locale.
     * @param replacement      The replacement values to use in the message.
     * @return A {@code MessagePostScript} object representing the sent message.
     */
    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient, @NotNull Locale overrideLocale, String... replacement);

    /**
     * Sends a message with replacement values to a single recipient, overriding the locale of the message.
     *
     * @param messageKey       The key identifying the message to send.
     * @param messageRecipient The recipient to send the message to.
     * @param overrideLocale   The locale to use for the message, overriding the default locale.
     * @param replacement      The replacement values to use in the message.
     * @return A {@code MessagePostScript} object representing the sent message.
     */
    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient, @NotNull String overrideLocale, String... replacement);

    /**
     * Sends a message with replacement values to a single recipient, overriding the locale of the message.
     *
     * @param messageKey       The key identifying the message to send.
     * @param messageRecipient The recipient to send the message to.
     * @param overrideLocale   The locale to use for the message, overriding the default locale.
     * @param replacement      The replacement values to use in the message.
     * @return A {@code MessagePostScript} object representing the sent message.
     */
    @NotNull MessagePostScript sendMessage(@NotNull String messageKey, @NotNull MessageRecipient messageRecipient, @NotNull DBLocale overrideLocale, String... replacement);


    /**
     * Broadcasts a message with the given key to all message recipients, using the given default locale and replacements.
     *
     * @param messageKey    the key of the message to be broadcasted
     * @param defaultLocale the default locale to use for the broadcast
     * @param replacement   optional replacements for placeholders in the message
     * @return a multi-message post script containing the status of each sent message
     */
    @NotNull MultiMessagePostScript broadcastMessage(@NotNull String messageKey, @NotNull Locale defaultLocale, String... replacement);

    /**
     * Broadcasts a message with the given key to all message recipients, using the given default locale and replacements.
     *
     * @param messageKey    the key of the message to be broadcasted
     * @param defaultLocale the default locale to use for the broadcast
     * @param replacement   optional replacements for placeholders in the message
     * @return a multi-message post script containing the status of each sent message
     */
    @NotNull MultiMessagePostScript broadcastMessage(@NotNull String messageKey, @NotNull String defaultLocale, String... replacement);

    /**
     * Broadcasts a message with the given key to all message recipients, using the given default locale and replacements.
     *
     * @param messageKey    the key of the message to be broadcasted
     * @param defaultLocale the default locale to use for the broadcast
     * @param replacement   optional replacements for placeholders in the message
     * @return a multi-message post script containing the status of each sent message
     */
    @NotNull MultiMessagePostScript broadcastMessage(@NotNull String messageKey, @NotNull DBLocale defaultLocale, String... replacement);


    /**
     * Sends a message with replacements to all recipients, using the default locale.
     *
     * @param messageKey  the key for the message to send
     * @param replacement the replacements for placeholders in the message
     * @return a collection of message post-scripts for each recipient
     */
    @NotNull Collection<? extends MessagePostScript> broadcastMessage(@NotNull String messageKey, String... replacement);


    /**
     * Sends a collection of messages to a single message recipient with optional replacements.
     *
     * @param messageKeys      the collection of message keys to be sent
     * @param messageRecipient the message recipient to send the messages to
     * @param replacement      optional replacement strings to be used in the message templates
     * @return the postscript representing the sending of the messages
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient, String... replacement);

    /**
     * @param messageKeys       The keys of the messages to send.
     * @param messageRecipients The recipients of the messages.
     * @param replacement       The replacements for message placeholders.
     * @return A {@link MultiMessagePostScript} object containing the result of the message sending operation.
     * @deprecated Use {@link MessageApi#sendMessageSmart(Collection, Collection, String...)} instead.
     */
    @Deprecated
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, String... replacement);

    /**
     * Sends a message to a single recipient with the specified locale override and replacement values.
     *
     * @param messageKeys      The keys of the messages to send.
     * @param messageRecipient The recipient of the message.
     * @param overrideLocale   The locale to use instead of the recipient's locale.
     * @param replacement      The replacements for message placeholders.
     * @return A {@link MultiMessagePostScript} object containing the result of the message sending operation.
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient, @NotNull String overrideLocale, String... replacement);

    /**
     * Sends a message to the given message recipient(s) with the specified message keys and replacements.
     *
     * @param messageKeys      the collection of message keys to send
     * @param messageRecipient the message recipient to send the messages to
     * @param overrideLocale   the locale to use when sending the message(s)
     * @param replacement      the array of message replacement values to use when sending the message(s)
     * @return the message post-script
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient, @NotNull Locale overrideLocale, String... replacement);

    /**
     * Sends a message to the given message recipient(s) with the specified message keys and replacements.
     *
     * @param messageKeys      the collection of message keys to send
     * @param messageRecipient the message recipient to send the messages to
     * @param overrideLocale   the DB locale to use when sending the message(s)
     * @param replacement      the array of message replacement values to use when sending the message(s)
     * @return the message post-script
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull MessageRecipient messageRecipient, @NotNull DBLocale overrideLocale, String... replacement);

    /**
     * Sends a message to the given message recipient(s) with the specified message keys and replacements.
     *
     * @param messageKeys       the collection of message keys to send
     * @param messageRecipients the collection of message recipients to send the messages to
     * @param overrideLocale    the locale to use when sending the message(s)
     * @param replacement       the array of message replacement values to use when sending the message(s)
     * @return the collection of message post-scripts
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull String overrideLocale, String... replacement);

    /**
     * Sends a collection of messages with replacements to a collection of message recipients, with the specified locale override.
     *
     * @param messageKeys       the collection of message keys to send
     * @param messageRecipients the collection of message recipients to send the messages to
     * @param overrideLocale    the locale override to use for the messages
     * @param replacement       the array of replacements to use for each message
     * @return a {@link MultiMessagePostScript} representing the post-script of the messages sent
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull Locale overrideLocale, String... replacement);

    /**
     * Sends a collection of messages with replacements to a collection of message recipients, with the specified DB locale override.
     *
     * @param messageKeys       the collection of message keys to send
     * @param messageRecipients the collection of message recipients to send the messages to
     * @param overrideLocale    the DB locale override to use for the messages
     * @param replacement       the array of replacements to use for each message
     * @return a {@link MultiMessagePostScript} representing the post-script of the messages sent
     */
    @NotNull MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, @NotNull DBLocale overrideLocale, String... replacement);

    /**
     * Sends a message broadcast to all recipients with the given message keys and replacement values.
     *
     * @param messageKeys   the message keys to send.
     * @param defaultLocale the default locale to use if a specific locale is not specified.
     * @param replacement   the replacement values to use in the message.
     * @return a {@code MultiMessagePostScript} object representing the result of the operation.
     */
    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys, @NotNull Locale defaultLocale, String... replacement);

    /**
     * Sends a message broadcast to all recipients with the given message keys and replacement values.
     *
     * @param messageKeys   the message keys to send.
     * @param defaultLocale the default locale to use if a specific locale is not specified.
     * @param replacement   the replacement values to use in the message.
     * @return a {@code MultiMessagePostScript} object representing the result of the operation.
     */
    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys, @NotNull String defaultLocale, String... replacement);

    /**
     * Sends a message broadcast to all recipients with the given message keys and replacement values.
     *
     * @param messageKeys   the message keys to send.
     * @param defaultLocale the default locale to use if a specific locale is not specified.
     * @param replacement   the replacement values to use in the message.
     * @return a {@code MultiMessagePostScript} object representing the result of the operation.
     */
    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys, @NotNull DBLocale defaultLocale, String... replacement);

    /**
     * Sends a message broadcast to all recipients with the given message keys and replacement values.
     *
     * @param messageKeys the message keys to send.
     * @param replacement the replacement values to use in the message.
     * @return a {@code MultiMessagePostScript} object representing the result of the operation.
     */
    @NotNull MultiMessagePostScript broadcastMessage(@NotNull Collection<String> messageKeys, String... replacement);


    /**
     * Returns the default locale (EN_US) as a String.
     *
     * @return the default locale as a String
     */
    @NotNull String defaultLocale();

    /**
     * Returns the default locale (EN_US) as a Locale object.
     *
     * @return the default locale as a Locale object
     */
    @NotNull Locale defaultUtilLocale();


    //TagManager getTagManager(); removed in favor of AdventureCore / AdventureAPI
}
