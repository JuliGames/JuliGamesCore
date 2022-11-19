package net.juligames.core.message;

import de.bentzin.tools.Hardcode;
import de.bentzin.tools.pair.Pair;
import de.bentzin.tools.register.Registerator;
import net.juligames.core.Core;
import net.juligames.core.api.jdbi.*;
import net.juligames.core.api.jdbi.mapper.bean.MessageBean;
import net.juligames.core.api.jdbi.mapper.bean.ReplacementBean;
import net.juligames.core.api.message.*;
import net.juligames.core.jdbi.CoreMessagePostScript;
import net.juligames.core.jdbi.CoreMultiMessagePostScript;
import net.juligames.core.message.adventure.AdventureTagManager;
import org.jdbi.v3.core.extension.ExtensionCallback;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public class CoreMessageApi implements MessageApi {

    //TODO switch all Collections / Streams to ? extends X for performance reasons

    private final AdventureTagManager adventureTagManager;

    public CoreMessageApi(){
        this.adventureTagManager = new AdventureTagManager();
    }


    @Override
    @Hardcode
    @ApiStatus.Internal
    public <R> R callMessageExtension(ExtensionCallback<R, MessageDAO, RuntimeException> extensionCallback) {
        return Core.getInstance().getSQLManager().getJdbi().withExtension(MessageDAO.class,extensionCallback);
    }

    @Override
    @Hardcode
    @ApiStatus.Internal
    public <R> R callLocaleExtension(ExtensionCallback<R, LocaleDAO, RuntimeException> extensionCallback) {
        return Core.getInstance().getSQLManager().getJdbi().withExtension(LocaleDAO.class,extensionCallback);
    }

    @Override
    @Hardcode
    @ApiStatus.Internal
    public <R> R callPreferenceExtension(ExtensionCallback<R, PlayerLocalPreferenceDAO, RuntimeException> extensionCallback) {
        return Core.getInstance().getSQLManager().getJdbi().withExtension(PlayerLocalPreferenceDAO.class,extensionCallback);
    }

    @Override
    @Hardcode
    @ApiStatus.Internal
    public <R> R callReplacementExtension(ExtensionCallback<R, ReplacementDAO, RuntimeException> extensionCallback) {
        return Core.getInstance().getSQLManager().getJdbi().withExtension(ReplacementDAO.class,extensionCallback);
    }

    @Override
    @Hardcode
    @ApiStatus.Internal
    public <R> R callReplacementTypeExtension(ExtensionCallback<R, ReplacementTypeDAO, RuntimeException> extensionCallback) {
        return Core.getInstance().getSQLManager().getJdbi().withExtension(ReplacementTypeDAO.class,extensionCallback);
    }


    @Override
    public Message getMessage(String messageKey, Locale locale) {
        DBMessage dbMessage = callMessageExtension(extension -> extension.select(messageKey, locale.toString()));
        return new CoreMessage(dbMessage);
    }

    @Override
    public Message getMessage(String messageKey, String locale) {
        DBMessage dbMessage = callMessageExtension(extension -> extension.select(messageKey,locale));
        return new CoreMessage(dbMessage);
    }

    @Override
    public Message getMessage(String messageKey, @NotNull DBLocale dbLocale) {
        return getMessage(messageKey, dbLocale.toUtil());
    }

    @Override
    public Collection<Message> getMessage(String messageKey) {
        List<MessageBean> messageBeans = callMessageExtension(extension -> extension.selectFromKey(messageKey));
        return messageBeans.stream().map(messageBean -> (Message) new CoreMessage(messageBean)).toList();
    }

    @Override
    public Collection<Message> getAllFromLocale(@NotNull Locale locale) {
        return getAllFromLocale(locale.toString());
    }

    @Override
    public Collection<Message> getAllFromLocale(String locale) {
        List<MessageBean> messageBeans = callMessageExtension(extension -> extension.selectFromLocale(locale));
        return messageBeans.stream().map(messageBean -> (Message) new CoreMessage(messageBean)).toList();
    }

    @Override
    public Collection<Message> getAllFromLocale(@NotNull DBLocale dbLocale) {
        return getAllFromLocale(dbLocale.toUtil());
    }

    @Override
    public Collection<Message> getAll() {
        List<MessageBean> messageBeans = callMessageExtension(MessageDAO::listAllBeans);
        return messageBeans.stream().map(messageBean -> (Message) new CoreMessage(messageBean)).toList();
    }

    @Override
    public Stream<DBMessage> streamData() {
        List<MessageBean> messageBeans = callMessageExtension(MessageDAO::listAllBeans);
        return messageBeans.stream().map(messageBean -> (DBMessage) messageBeans);
    }

    @Override
    public Collection<DBReplacement> getReplacers() {
        List<ReplacementBean> replacementBeans = callReplacementExtension(ReplacementDAO::listAllBeans);
        return  replacementBeans.stream().map(replacementBean -> (DBReplacement) replacementBean).toList();
    }

    @Override
    @Deprecated
    public void registerMessage(String messageKey) {
        callMessageExtension(extension -> {
            extension.insert(new MessageBean(messageKey,defaultLocale(),"null"));
            return null;
        });
    }

    @Override
    public void registerMessage(String messageKey, String defaultMiniMessage) {
        callMessageExtension(extension -> {
            extension.insert(new MessageBean(messageKey,defaultLocale(),defaultMiniMessage));
            return null;
        });
    }

    @Override
    public boolean hasMessage(String messageKey) {
        return callMessageExtension(extension -> !extension.selectFromKey(messageKey).isEmpty());
    }

    @Override
    public boolean hasMessage(String messageKey, String locale) {
        return callMessageExtension(extension -> extension.select(messageKey,locale) != null);
    }

    @Override
    public boolean hasMessage(String messageKey, @NotNull Locale locale) {
        return hasMessage(messageKey, locale.toString());
    }

    @Override
    public boolean hasMessage(String messageKey, @NotNull DBLocale locale) {
        return hasMessage(messageKey, locale.toUtil());
    }

    @Override
    public MessagePostScript sendMessage(String messageKey, @NotNull MessageRecipient messageRecipient) {
        //send it baby
        Message message = getMessage(messageKey,messageRecipient.supplyLocaleOrDefault());
        messageRecipient.deliver(message);
        return new CoreMessagePostScript(message,messageRecipient,now());
    }

    @Override
    public MessagePostScript sendMessage(String messageKey, @NotNull MessageRecipient messageRecipient, @NotNull Locale overrideLocale) {
        return sendMessage(messageKey, messageRecipient, overrideLocale.toString());
    }

    @Override
    public MessagePostScript sendMessage(String messageKey, @NotNull MessageRecipient messageRecipient, String overrideLocale) {
        Message message = getMessage(messageKey,overrideLocale);
        messageRecipient.deliver(message);
        return new CoreMessagePostScript(message,messageRecipient,now());
    }

    @Override
    public MessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, @NotNull DBLocale overrideLocale) {
        return sendMessage(messageKey,messageRecipient,overrideLocale.toUtil());
    }

    @Override
    public MultiMessagePostScript broadcastMessage(String messageKey, @NotNull Locale defaultLocale) {
        return broadcastMessage(messageKey,defaultLocale.toString());
    }

    @Override
    public MultiMessagePostScript broadcastMessage(String messageKey, String defaultLocale) {
       //because of performance reasons I will reimplement sendMessage here - it is not "good" code, but I think its worth it!
        Message message = getMessage(messageKey,defaultLocale);
        Collection<MessageRecipient> messageRecipients = Core.getInstance().getOnlineRecipientProvider().get();
        for (MessageRecipient messageRecipient : messageRecipients) {
            messageRecipient.deliver(message);
        }
        return new CoreMultiMessagePostScript(List.of(message),messageRecipients,now());
    }

    @Override
    public MultiMessagePostScript broadcastMessage(String messageKey, @NotNull DBLocale defaultLocale) {
        return broadcastMessage(messageKey,defaultLocale.toUtil());
    }

    @Override
    public MultiMessagePostScript broadcastMessage(String messageKey) {
        return broadcastMessage(messageKey,defaultLocale());
    }

    @Override
    public MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient) {
        return sendMessage(messageKeys,List.of(messageRecipient));
    }

    /**
     * @apiNote This will not return the accurate messages in the PostScript but only the "first" one that was sent of a type
     * @param messageKeys the keys
     * @param messageRecipients the recipients
     * @return not accurate {@link MultiMessagePostScript}
     */
    @Override
    public MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, Collection<MessageRecipient> messageRecipients) {
        //because of performance reasons I will reimplement sendMessage here - it is not "good" code, but I think its worth it!
        Collection<Message> messages = new ArrayList<>();
        for (String messageKey : messageKeys) {
            for (MessageRecipient messageRecipient : messageRecipients) {
                Message message = getMessage(messageKey, messageRecipient.supplyLocaleOrDefault());
                if(!messages.stream().anyMatch(message1 -> message1.getMessageData().getMessageKey().equals(message.getMessageData().getMessageKey()))){
                    messages.add(message);
                }
                messageRecipient.deliver(message);
            }
        }
        return new CoreMultiMessagePostScript(messages,messageRecipients,now());
    }

    @Override
    public MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, String overrideLocale) {
        return sendMessage(messageKeys,List.of(messageRecipient), overrideLocale);
    }

    @Override
    public MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, @NotNull Locale overrideLocale) {
        return sendMessage(messageKeys,List.of(messageRecipient), overrideLocale.toString());
    }

    @Override
    public MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, @NotNull DBLocale overrideLocale) {
        return sendMessage(messageKeys,List.of(messageRecipient), overrideLocale.toUtil());
    }

    @Override
    public MultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, Collection<MessageRecipient> messageRecipients, String overrideLocale) {
        //because of performance reasons I will reimplement sendMessage here - it is not "good" code, but I think its worth it!
        Collection<Message> messages = new ArrayList<>();
        for (String messageKey : messageKeys) {
            Message message = getMessage(messageKey, overrideLocale);
            for (MessageRecipient messageRecipient : messageRecipients) {
                messageRecipient.deliver(message);
            }
            messages.add(message);
        }
        return new CoreMultiMessagePostScript(messages,messageRecipients,now());
    }

    @Override
    public MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<MessageRecipient> messageRecipients, @NotNull Locale overrideLocale) {
        return sendMessage(messageKeys,messageRecipients,overrideLocale.toString());
    }

    @Override
    public MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<MessageRecipient> messageRecipients, @NotNull DBLocale overrideLocale) {
        return sendMessage(messageKeys,messageRecipients,overrideLocale.toUtil());
    }

    @Override
    public MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, @NotNull Locale defaultLocale) {
        return broadcastMessage(messageKeys,defaultLocale.toString());
    }

    @Override
    public MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, String defaultLocale) {
        return sendMessage(messageKeys,Core.getInstance().getOnlineRecipientProvider().get(),defaultLocale);
    }

    @Override
    public MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, @NotNull DBLocale defaultLocale) {
        return broadcastMessage(messageKeys,defaultLocale.toUtil());
    }

    @ApiStatus.Experimental
    @Override
    public MultiMessagePostScript broadcastMessage(Collection<String> messageKeys) {
        return sendMessage(messageKeys,Core.getInstance().getOnlineRecipientProvider().get());
    }

    @Override
    public AdventureTagManager getTagManager() {
        return adventureTagManager;
    }

    public String defaultLocale(){
        //"master_information"
        return Core.getInstance().getHazelDataApi().getMasterInformation().get("default_locale");
    }

    @Contract(" -> new")
    private @NotNull Date now() {
        return Date.from(Instant.now());
    }
}
