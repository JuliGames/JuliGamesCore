package net.juligames.core.api.message;

import net.juligames.core.api.jdbi.*;
import org.jdbi.v3.core.extension.ExtensionCallback;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

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

    //DAO
    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R>
    R callMessageExtension(ExtensionCallback<R, MessageDAO, RuntimeException> extensionCallback);

    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R>
    R callLocaleExtension(ExtensionCallback<R, LocaleDAO, RuntimeException> extensionCallback);

    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R>
    R callPreferenceExtension(ExtensionCallback<R, PlayerLocalPreferenceDAO, RuntimeException> extensionCallback);

    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R>
    R callReplacementExtension(ExtensionCallback<R, ReplacementDAO, RuntimeException> extensionCallback);

    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R>
    R callReplacementTypeExtension(ExtensionCallback<R, ReplacementTypeDAO, RuntimeException> extensionCallback);

    //get
    Message getMessage(String messageKey, Locale locale);

    Message getMessage(String messageKey, Locale locale, String... replacements);

    Message getMessage(String messageKey, String locale);

    Message getMessage(String messageKey, String locale, String... replacements);

    Message getMessage(String messageKey, DBLocale dbLocale);

    Message getMessage(String messageKey, DBLocale dbLocale, String... replacements);

    Collection<? extends Message> getMessage(String messageKey);

    Collection<? extends Message> getMessage(String messageKey, String... replacements);

    Message getMessageSmart(String messageKey, Locale locale);

    Message getMessageSmart(String messageKey, Locale locale, String... replacements);

    Message getMessageSmart(String messageKey, String locale);

    Message getMessageSmart(String messageKey, String locale, String... replacements);

    Message getMessageSmart(String messageKey, DBLocale dbLocale);

    Message getMessageSmart(String messageKey, DBLocale dbLocale, String... replacements);

    Collection<? extends Message> getAllFromLocale(Locale locale);

    Collection<? extends Message> getAllFromLocale(Locale locale, String... replacements);

    Collection<? extends Message> getAllFromLocale(String locale);

    Collection<? extends Message> getAllFromLocale(String locale, String... replacements);

    Collection<? extends Message> getAllFromLocale(DBLocale dbLocale);

    Collection<? extends Message> getAllFromLocale(DBLocale dbLocale, String... replacements);

    @ApiStatus.Experimental
    Collection<? extends Message> getAll();

    @ApiStatus.Experimental
    Collection<? extends Message> getAll(String... replacements);

    @ApiStatus.Internal
    Stream<? extends DBMessage> streamData();

    Collection<? extends DBReplacement> getReplacers();

    //register

    @Deprecated
    void registerMessage(String messageKey);

    void registerMessage(String messageKey, String defaultMiniMessage);

    boolean hasMessage(String messageKey);

    boolean hasMessage(String messageKey, String locale);

    boolean hasMessage(String messageKey, Locale locale);

    boolean hasMessage(String messageKey, DBLocale locale);

    MessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient);

    MessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, Locale overrideLocale);

    MessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, String overrideLocale);

    MessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, DBLocale overrideLocale);

    MultiMessagePostScript broadcastMessage(String messageKey, Locale defaultLocale);

    MultiMessagePostScript broadcastMessage(String messageKey, String defaultLocale);

    MultiMessagePostScript broadcastMessage(String messageKey, DBLocale defaultLocale);

    Collection<? extends MessagePostScript> broadcastMessage(String messageKey);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, String overrideLocale);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, Locale overrideLocale);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, DBLocale overrideLocale);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, String overrideLocale);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, Locale overrideLocale);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, DBLocale overrideLocale);

    Collection<MultiMessagePostScript> sendMessageSmart(@NotNull Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, String... replacement);

    Collection<MessagePostScript> sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients);

    MultiMessagePostScript sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients, String overrideLocale);

    MultiMessagePostScript sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients, Locale overrideLocale);

    MultiMessagePostScript sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients, DBLocale overrideLocale);

    Collection<MessagePostScript> sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients, String... replacements);

    MultiMessagePostScript sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients, String overrideLocale, String... replacements);

    MultiMessagePostScript sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients, Locale overrideLocale, String... replacements);

    MultiMessagePostScript sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients, DBLocale overrideLocale, String... replacements);


    MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, Locale defaultLocale);

    MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, String defaultLocale);

    MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, DBLocale defaultLocale);

    MultiMessagePostScript broadcastMessage(Collection<String> messageKeys);

    //sending with replacements
    MessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, String... replacement);

    MessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, Locale overrideLocale, String... replacement);

    MessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, String overrideLocale, String... replacement);

    MessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, DBLocale overrideLocale, String... replacement);

    MultiMessagePostScript broadcastMessage(String messageKey, Locale defaultLocale, String... replacement);

    MultiMessagePostScript broadcastMessage(String messageKey, String defaultLocale, String... replacement);

    MultiMessagePostScript broadcastMessage(String messageKey, DBLocale defaultLocale, String... replacement);

    Collection<? extends MessagePostScript> broadcastMessage(String messageKey, String... replacement);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, String... replacement);

    /**
     * @deprecated use {@link MessageApi#sendMessageSmart(Collection, Collection, String...)} instead
     */
    @Deprecated
    MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, String... replacement);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, String overrideLocale, String... replacement);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, Locale overrideLocale, String... replacement);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, DBLocale overrideLocale, String... replacement);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, String overrideLocale, String... replacement);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, Locale overrideLocale, String... replacement);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, DBLocale overrideLocale, String... replacement);

    MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, Locale defaultLocale, String... replacement);

    MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, String defaultLocale, String... replacement);

    MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, DBLocale defaultLocale, String... replacement);

    MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, String... replacement);

    String defaultLocale();
    Locale defaultUtilLocale();

    //TagManager getTagManager(); removed in favor of AdventureCore / AdventureAPI
}
