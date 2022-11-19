package net.juligames.core.api.message;

import de.bentzin.tools.pair.Pair;
import net.juligames.core.api.jdbi.*;
import org.jdbi.v3.core.extension.ExtensionCallback;
import org.jetbrains.annotations.ApiStatus;

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
    R callMessageExtension(ExtensionCallback<R,MessageDAO,RuntimeException> extensionCallback);

    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R>
    R callLocaleExtension(ExtensionCallback<R,LocaleDAO,RuntimeException> extensionCallback);

    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R>
    R callPreferenceExtension(ExtensionCallback<R,PlayerLocalPreferenceDAO,RuntimeException> extensionCallback);

    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R>
    R callReplacementExtension(ExtensionCallback<R,ReplacementDAO,RuntimeException> extensionCallback);

    @ApiStatus.Internal
    @ApiStatus.Experimental
    <R>
    R callReplacementTypeExtension(ExtensionCallback<R,ReplacementTypeDAO,RuntimeException> extensionCallback);

    //get
    Message getMessage(String messageKey, Locale locale);

    Message getMessage(String messageKey, String locale);

    Message getMessage(String messageKey, DBLocale dbLocale);

    Collection<Message> getMessage(String messageKey);

    Collection<Message> getAllFromLocale(Locale locale);

    Collection<Message> getAllFromLocale(String locale);

    Collection<Message> getAllFromLocale(DBLocale dbLocale);

    Collection<Message> getAll();

    @ApiStatus.Internal
    Stream<DBMessage> streamData();

    Collection<DBReplacement> getReplacers();

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

    MultiMessagePostScript broadcastMessage(String messageKey);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, String overrideLocale);
    MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, Locale overrideLocale);
    MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, DBLocale overrideLocale);

    MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, String overrideLocale);
    MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, Locale overrideLocale);
    MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, DBLocale overrideLocale);

    MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, Locale defaultLocale);

    MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, String defaultLocale);

    MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, DBLocale defaultLocale);

    MultiMessagePostScript broadcastMessage(Collection<String> messageKeys);
    //TODO messageRecipient and prefer locals

    TagManager getTagManager();
}
