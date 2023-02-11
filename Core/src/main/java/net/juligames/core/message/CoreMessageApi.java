package net.juligames.core.message;

import com.github.benmanes.caffeine.cache.Cache;
import de.bentzin.tools.Hardcode;
import de.bentzin.tools.pair.Pair;
import net.juligames.core.Core;
import net.juligames.core.api.API;
import net.juligames.core.api.jdbi.*;
import net.juligames.core.api.jdbi.mapper.bean.MessageBean;
import net.juligames.core.api.jdbi.mapper.bean.ReplacementBean;
import net.juligames.core.api.message.*;
import net.juligames.core.api.misc.ThrowableDebug;
import net.juligames.core.caching.MessageCaching;
import net.juligames.core.jdbi.CoreMessagePostScript;
import net.juligames.core.jdbi.CoreMultiMessagePostScript;
import org.jdbi.v3.core.extension.ExtensionCallback;
import org.jetbrains.annotations.*;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public class CoreMessageApi implements MessageApi {

    //TODO switch all Collections / Streams to ? extends X for performance reasons (maybe 1.2?)
    //TODO Further testing on automatic fallback (new implementation? Whats current status?)

    public CoreMessageApi() {
        //setup config
        MessageConfigManager.init();
    }


    @Override
    @Hardcode
    @ApiStatus.Internal
    public <R> R callMessageExtension(ExtensionCallback<R, MessageDAO, RuntimeException> extensionCallback) {
        return Core.getInstance().getSQLManager().getJdbi().withExtension(MessageDAO.class, extensionCallback);
    }

    @Override
    @Hardcode
    @ApiStatus.Internal
    public <R> R callLocaleExtension(ExtensionCallback<R, LocaleDAO, RuntimeException> extensionCallback) {
        return Core.getInstance().getSQLManager().getJdbi().withExtension(LocaleDAO.class, extensionCallback);
    }

    @Override
    @Hardcode
    @ApiStatus.Internal
    public <R> R callPreferenceExtension(ExtensionCallback<R, PlayerLocalPreferenceDAO, RuntimeException> extensionCallback) {
        return Core.getInstance().getSQLManager().getJdbi().withExtension(PlayerLocalPreferenceDAO.class, extensionCallback);
    }

    @Override
    @Hardcode
    @ApiStatus.Internal
    public <R> R callReplacementExtension(ExtensionCallback<R, ReplacementDAO, RuntimeException> extensionCallback) {
        return Core.getInstance().getSQLManager().getJdbi().withExtension(ReplacementDAO.class, extensionCallback);
    }

    @Override
    @Hardcode
    @ApiStatus.Internal
    public <R> R callReplacementTypeExtension(ExtensionCallback<R, ReplacementTypeDAO, RuntimeException> extensionCallback) {
        return Core.getInstance().getSQLManager().getJdbi().withExtension(ReplacementTypeDAO.class, extensionCallback);
    }


    @Override
    public CoreMessage getMessage(String messageKey, Locale locale) {
        return getMessage(messageKey, locale, (String[]) null); //the cast is super important here to avoid exception
    }

    /**
     * @param messageKey   the messageKey
     * @param locale       the locale for that specific message
     * @param replacements all the replacements in order stating by 0 or null if nothing should be replaced
     * @return the Message
     */
    @Override
    public CoreMessage getMessage(String messageKey, @NotNull Locale locale, String... replacements) {
        return getMessage(messageKey, locale.toString(), replacements);
    }

    /**
     * @param messageKey
     * @param locale
     * @return
     */
    @Override
    public CoreMessage getMessage(String messageKey, String locale) { //REWRITE
        Optional<DBMessage> messageFromCache = getMessageFromCache(messageKey, locale);
        if (messageFromCache.isPresent()) {
            return CoreMessage.fromData(messageFromCache.get(), messageKey);
        } else {
            //non cached
            DBMessage dbMessage = callMessageExtension(extension -> extension.select(messageKey, locale));
            return CoreMessage.fromData(cache(dbMessage), messageKey);
        }

        /*return CoreMessage.fromData(getMessageFromCache(messageKey, locale).orElseGet(() ->
                cache(callMessageExtension(extension -> extension.select(messageKey, locale)))), locale);

         */
    }

    @Override
    public CoreMessage getMessage(String messageKey, String locale, String... replacements) {
        CoreMessage message = getMessage(messageKey, locale);
        Core.getInstance().getCoreLogger().debug("inserting replacements: " + Arrays.toString(replacements) + " to " + message.getMiniMessage() + "@" + message.getMessageData().getMessageKey());
        insertReplacements(message, replacements);
        Core.getInstance().getCoreLogger().debug("new Data: " + message.getMiniMessage());
        return message;
    }

    @Override
    public CoreMessage getMessage(String messageKey, @NotNull DBLocale dbLocale) {
        return getMessage(messageKey, dbLocale.toUtil());
    }

    @Override
    public CoreMessage getMessage(String messageKey, @NotNull DBLocale dbLocale, String... replacements) {
        return getMessage(messageKey, dbLocale.toUtil(), replacements);
    }

    @Override
    public Collection<CoreMessage> getMessage(String messageKey) {
        List<MessageBean> messageBeans = callMessageExtension(extension -> extension.selectFromKey(messageKey));
        return messageBeans.stream().map(messageBean -> CoreMessage.fromData(cache(messageBean), messageKey)).toList();
    }

    @Override
    public Collection<? extends Message> getMessage(String messageKey, String... replacements) {
        Collection<CoreMessage> message = getMessage(messageKey);
        message.forEach(o -> insertReplacements(o, replacements));
        return message;
    }

    @Override
    public Message getMessageSmart(String messageKey, Locale locale) {
        if (locale == null) return getMessage(messageKey, defaultLocale());
        return getMessageSmart(messageKey, locale.toString());
    }

    @Override
    public Message getMessageSmart(String messageKey, Locale locale, String... replacements) {
        if (locale == null) return getMessage(messageKey, defaultLocale(), replacements);
        return getMessageSmart(messageKey, locale.toString(), replacements);
    }

    @Override
    public Message getMessageSmart(String messageKey, String locale) {
        return findBestMessage(messageKey, locale);
    }

    @Override
    public Message getMessageSmart(String messageKey, String locale, String... replacements) {
        return findBestMessage(messageKey, locale, replacements);
    }

    @Override
    public Message getMessageSmart(String messageKey, @NotNull DBLocale dbLocale) {
        return getMessageSmart(messageKey, dbLocale.toUtil());
    }

    @Override
    public Message getMessageSmart(String messageKey, @NotNull DBLocale dbLocale, String... replacements) {
        return getMessageSmart(messageKey, dbLocale.toUtil(), replacements);
    }

    @Override
    public Collection<CoreMessage> getAllFromLocale(@NotNull Locale locale) {
        return getAllFromLocale(locale.toString());
    }

    @Override
    public Collection<? extends Message> getAllFromLocale(Locale locale, String... replacements) {
        Collection<CoreMessage> message = getAllFromLocale(locale);
        message.forEach(o -> insertReplacements(o, replacements));
        return message;
    }

    @Override
    public Collection<CoreMessage> getAllFromLocale(String locale) {
        //EMPTY COLLECTION
        List<MessageBean> messageBeans = callMessageExtension(extension -> extension.selectFromLocale(locale));
        messageBeans.forEach(this::cache);
        return messageBeans.stream().map(messageBean -> CoreMessage.fromData(messageBean, messageBean.getMessageKey())).toList();
    }

    @Override
    public Collection<CoreMessage> getAllFromLocale(String locale, String... replacements) {
        Collection<CoreMessage> message = getAllFromLocale(locale);
        message.forEach(o -> insertReplacements(o, replacements));
        return message;
    }

    @Override
    public Collection<CoreMessage> getAllFromLocale(@NotNull DBLocale dbLocale) {
        return getAllFromLocale(dbLocale.toUtil());
    }

    @Override
    public Collection<CoreMessage> getAllFromLocale(DBLocale dbLocale, String... replacements) {
        Collection<CoreMessage> message = getAllFromLocale(dbLocale);
        message.forEach(o -> insertReplacements(o, replacements));
        return message;
    }

    @Override
    public Collection<CoreMessage> getAll() {
        List<MessageBean> messageBeans = callMessageExtension(MessageDAO::listAllBeans);
        messageBeans.forEach(this::cache);
        return messageBeans.stream().map(CoreMessage::new).toList();
    }

    @Override
    public Collection<CoreMessage> getAll(String... replacements) {
        List<MessageBean> messageBeans = callMessageExtension(MessageDAO::listAllBeans);
        messageBeans.forEach(this::cache);
        List<CoreMessage> coreMessages = messageBeans.stream().map(CoreMessage::new).toList();
        //coreMessages.forEach(coreMessage -> coreMessage.doWithMiniMessage(insertReplacements(replacements)));
        //TODO messages replacements store to Map (currently set)
        return coreMessages;
    }

    @Override
    public Stream<DBMessage> streamData() {
        List<MessageBean> messageBeans = callMessageExtension(MessageDAO::listAllBeans);
        messageBeans.forEach(this::cache);
        return messageBeans.stream().map(messageBean -> (DBMessage) messageBeans);
    }

    @Override
    public Collection<DBReplacement> getReplacers() {
        List<ReplacementBean> replacementBeans = callReplacementExtension(ReplacementDAO::listAllBeans);
        return replacementBeans.stream().map(replacementBean -> (DBReplacement) replacementBean).toList();
    }

    @Override
    @Deprecated
    public void registerMessage(String messageKey) {
        callMessageExtension(extension -> {
            extension.insert(new MessageBean(messageKey, defaultLocale(), "null"));
            return null;
        });
    }

    @Override
    public void registerMessage(String messageKey, String defaultMiniMessage) {
        callMessageExtension(extension -> {
            extension.insert(new MessageBean(messageKey, defaultLocale(), defaultMiniMessage));
            return null;
        });
    }

    @Override
    public boolean hasMessage(String messageKey) {
        return callMessageExtension(extension -> !extension.selectFromKey(messageKey).isEmpty());
    }

    @Override
    public boolean hasMessage(String messageKey, String locale) {
        return callMessageExtension(extension -> extension.select(messageKey, locale) != null);
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
    public CoreMessagePostScript sendMessage(String messageKey, @NotNull MessageRecipient messageRecipient) {
        return sendMessage(messageKey, messageRecipient, (String[]) null);
    }

    @Override
    public CoreMessagePostScript sendMessage(String messageKey, @NotNull MessageRecipient messageRecipient, @NotNull Locale overrideLocale) {
        return sendMessage(messageKey, messageRecipient, overrideLocale.toString());
    }

    @Override
    public CoreMessagePostScript sendMessage(String messageKey, @NotNull MessageRecipient messageRecipient, String overrideLocale) {
        return sendMessage(messageKey, messageRecipient, overrideLocale, (String[]) null);
    }

    @Override
    public CoreMessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, @NotNull DBLocale overrideLocale) {
        return sendMessage(messageKey, messageRecipient, overrideLocale.toUtil());
    }

    @Override
    public CoreMultiMessagePostScript broadcastMessage(String messageKey, @NotNull Locale defaultLocale) {
        return broadcastMessage(messageKey, defaultLocale.toString());
    }

    @Override
    public CoreMultiMessagePostScript broadcastMessage(String messageKey, String defaultLocale) {
        return broadcastMessage(messageKey, defaultLocale, (String[]) null);
    }

    @Override
    public CoreMultiMessagePostScript broadcastMessage(String messageKey, @NotNull DBLocale defaultLocale) {
        return broadcastMessage(messageKey, defaultLocale.toUtil());
    }

    @Override
    public Collection<CoreMessagePostScript> broadcastMessage(String messageKey) {
        return broadcastMessage(messageKey, (String[]) null);
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient) {
        return sendMessage(messageKeys, List.of(messageRecipient));
    }

    /**
     * @param messageKeys       the keys
     * @param messageRecipients the recipients
     * @return not accurate {@link CoreMultiMessagePostScript}
     * @apiNote This will not return the accurate messages in the PostScript but only the "first" one that was sent of a type
     */
    @Override
    public CoreMultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients) {
        return sendMessage(messageKeys, messageRecipients, (String[]) null);
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, String overrideLocale) {
        return sendMessage(messageKeys, List.of(messageRecipient), overrideLocale);
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, @NotNull Locale overrideLocale) {
        return sendMessage(messageKeys, List.of(messageRecipient), overrideLocale.toString());
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, @NotNull DBLocale overrideLocale) {
        return sendMessage(messageKeys, List.of(messageRecipient), overrideLocale.toUtil());
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, String overrideLocale) {
        return sendMessage(messageKeys, messageRecipients, overrideLocale, (String[]) null);
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, @NotNull Locale overrideLocale) {
        return sendMessage(messageKeys, messageRecipients, overrideLocale.toString());
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, @NotNull DBLocale overrideLocale) {
        return sendMessage(messageKeys, messageRecipients, overrideLocale.toUtil());
    }

    @Override
    public Collection<MessagePostScript> sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients) {
        Collection<MessagePostScript> postScripts = new ArrayList<>();
        for (MessageRecipient messageRecipient : messageRecipients) {
            postScripts.add(sendMessage(messageKey, messageRecipient));
        }
        return postScripts;
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients, String overrideLocale) {
        return sendMessage(List.of(messageKey), messageRecipients, overrideLocale);
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients, Locale overrideLocale) {
        return sendMessage(messageKey, messageRecipients, overrideLocale.toString());
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients, @NotNull DBLocale overrideLocale) {
        return sendMessage(messageKey, messageRecipients, overrideLocale.toUtil());
    }

    @Override
    public Collection<MessagePostScript> sendMessage(String messageKey, @NotNull Collection<? extends MessageRecipient> messageRecipients, String... replacements) {
        Collection<MessagePostScript> postScripts = new ArrayList<>();
        for (MessageRecipient messageRecipient : messageRecipients) {
            postScripts.add(sendMessage(messageKey, messageRecipient, replacements));
        }
        return postScripts;
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients, String overrideLocale, String... replacements) {
        return sendMessage(List.of(messageKey), messageRecipients, overrideLocale, replacements);
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients, @NotNull Locale overrideLocale, String... replacements) {
        return sendMessage(messageKey, messageRecipients, overrideLocale.toString(), replacements);
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(String messageKey, Collection<? extends MessageRecipient> messageRecipients, @NotNull DBLocale overrideLocale, String... replacements) {
        return sendMessage(messageKey, messageRecipients, overrideLocale.toUtil(), replacements);
    }

    @Override
    public CoreMultiMessagePostScript broadcastMessage(Collection<String> messageKeys, @NotNull Locale defaultLocale) {
        return broadcastMessage(messageKeys, defaultLocale.toString());
    }

    @Override
    public CoreMultiMessagePostScript broadcastMessage(Collection<String> messageKeys, String defaultLocale) {
        return sendMessage(messageKeys, Core.getInstance().getOnlineRecipientProvider().get(), defaultLocale);
    }

    @Override
    public CoreMultiMessagePostScript broadcastMessage(Collection<String> messageKeys, @NotNull DBLocale defaultLocale) {
        return broadcastMessage(messageKeys, defaultLocale.toUtil());
    }

    @ApiStatus.Experimental
    @Override
    public CoreMultiMessagePostScript broadcastMessage(Collection<String> messageKeys) {
        return sendMessage(messageKeys, Core.getInstance().getOnlineRecipientProvider().get());
    }

    @Override
    public CoreMessagePostScript sendMessage(String messageKey, @NotNull MessageRecipient messageRecipient, String... replacement) {
        //send it baby
        Message message = findBestMessageForRecipient(messageKey, messageRecipient, replacement);
        messageRecipient.deliver(message);
        return new CoreMessagePostScript(message, messageRecipient, now());
    }

    @Override
    public CoreMessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, @NotNull Locale overrideLocale, String... replacement) {
        return sendMessage(messageKey, messageRecipient, overrideLocale.toString(), replacement);
    }

    @Override
    public CoreMessagePostScript sendMessage(String messageKey, @NotNull MessageRecipient messageRecipient, String overrideLocale, String... replacements) {
        CoreMessage message = getMessage(messageKey, overrideLocale, replacements);
        messageRecipient.deliver(message);
        return new CoreMessagePostScript(message, messageRecipient, now());
    }

    @Override
    public CoreMessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, @NotNull DBLocale overrideLocale, String... replacement) {
        return sendMessage(messageKey, messageRecipient, overrideLocale.toUtil(), replacement);
    }

    @Override
    public CoreMultiMessagePostScript broadcastMessage(String messageKey, @NotNull Locale defaultLocale, String... replacement) {
        return broadcastMessage(messageKey, defaultLocale.toString(), replacement);
    }

    @Override
    public CoreMultiMessagePostScript broadcastMessage(String messageKey, String defaultLocale, String... replacement) {
        //because of performance reasons I will reimplement sendMessage here - it is not "good" code, but I think its worth it!
        Message message = getMessage(messageKey, defaultLocale);
        Collection<? extends MessageRecipient> messageRecipients = Core.getInstance().getOnlineRecipientProvider().get();
        for (MessageRecipient messageRecipient : messageRecipients) {
            messageRecipient.deliver(message);
        }
        return new CoreMultiMessagePostScript(List.of(message), messageRecipients, now());
    }

    @Override
    public CoreMultiMessagePostScript broadcastMessage(String messageKey, @NotNull DBLocale defaultLocale, String... replacement) {
        return broadcastMessage(messageKey, defaultLocale.toUtil(), replacement);
    }

    @Override
    public Collection<CoreMessagePostScript> broadcastMessage(String messageKey, String... replacements) {
        //because of performance reasons I will reimplement sendMessage here - it is not "good" code, but I think its worth it!
        Collection<? extends MessageRecipient> messageRecipients = Core.getInstance().getOnlineRecipientProvider().get();
        Collection<CoreMessagePostScript> postScripts = new ArrayList<>();
        for (MessageRecipient messageRecipient : messageRecipients) {
            CoreMessage message = findBestMessageForRecipient(messageKey, messageRecipient);
            insertReplacements(message, replacements);
            messageRecipient.deliver(message);
            postScripts.add(new CoreMessagePostScript(message, messageRecipient, now()));
        }
        return postScripts;
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, String... replacement) {
        return sendMessage(messageKeys, List.of(messageRecipient), replacement);
    }

    /**
     * @deprecated use {@link CoreMessageApi#sendMessageSmart(Collection, Collection, String...)} instead
     */
    @Override
    @Deprecated
    public CoreMultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, String... replacements) {
        //because of performance reasons I will reimplement sendMessage here - it is not "good" code, but I think its worth it!
        Collection<Message> messages = new ArrayList<>();
        for (String messageKey : messageKeys) {
            for (MessageRecipient messageRecipient : messageRecipients) {
                CoreMessage message = getMessage(messageKey, messageRecipient.supplyLocaleOrDefault()); //no fallback?! oh fuck this could get interesting
                insertReplacements(message, replacements);
                if (messages.stream().noneMatch(message1 -> message1.getMessageData().getMessageKey().equals(message.getMessageData().getMessageKey()))) {
                    messages.add(message);
                }
                messageRecipient.deliver(message);
            }
        }
        return new CoreMultiMessagePostScript(messages, messageRecipients, now());
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, String overrideLocale, String... replacement) {
        return sendMessage(messageKeys, List.of(messageRecipient), overrideLocale, replacement);
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, @NotNull Locale overrideLocale, String... replacement) {
        return sendMessage(messageKeys, messageRecipient, overrideLocale.toString(), replacement);
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, @NotNull DBLocale overrideLocale, String... replacement) {
        return sendMessage(messageKeys, messageRecipient, overrideLocale.toUtil(), replacement);
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, String overrideLocale, String... replacements) {
        //because of performance reasons I will reimplement sendMessage here - it is not "good" code, but I think its worth it!
        Collection<Message> messages = new ArrayList<>();
        for (String messageKey : messageKeys) {
            CoreMessage message = getMessage(messageKey, overrideLocale);
            insertReplacements(message, replacements);
            for (MessageRecipient messageRecipient : messageRecipients) {
                messageRecipient.deliver(message);
            }
            messages.add(message);
        }
        return new CoreMultiMessagePostScript(messages, messageRecipients, now());
    }

    @Override
    public Collection<MultiMessagePostScript> sendMessageSmart(@NotNull Collection<String> messageKeys, @NotNull Collection<? extends MessageRecipient> messageRecipients, String... replacements) {
        //because of performance reasons I will reimplement sendMessage here - it is not "good" code, but I think its worth it!

        Collection<MultiMessagePostScript> messages = new ArrayList<>();

        for (MessageRecipient messageRecipient : messageRecipients) {
            Collection<Message> localeMessage = new ArrayList<>();
            for (String messageKey : messageKeys) {
                CoreMessage message = findBestMessageForRecipient(messageKey, messageRecipient);
                insertReplacements(message, replacements);
                messageRecipient.deliver(message);
                localeMessage.add(message);
            }
            messages.add(new CoreMultiMessagePostScript(localeMessage, List.of(messageRecipient)));
        }

        return messages;
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, @NotNull Locale overrideLocale, String... replacement) {
        return sendMessage(messageKeys, messageRecipients, overrideLocale.toString(), replacement);
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, @NotNull DBLocale overrideLocale, String... replacement) {
        return sendMessage(messageKeys, messageRecipients, overrideLocale.toUtil(), replacement);
    }

    @Override
    public CoreMultiMessagePostScript broadcastMessage(Collection<String> messageKeys, @NotNull Locale defaultLocale, String... replacement) {
        return broadcastMessage(messageKeys, defaultLocale.toString(), replacement);
    }

    @Override
    public CoreMultiMessagePostScript broadcastMessage(Collection<String> messageKeys, String defaultLocale, String... replacement) {
        return sendMessage(messageKeys, Core.getInstance().getOnlineRecipientProvider().get(), defaultLocale, replacement);
    }

    @Override
    public CoreMultiMessagePostScript broadcastMessage(Collection<String> messageKeys, @NotNull DBLocale defaultLocale, String... replacement) {
        return broadcastMessage(messageKeys, defaultLocale.toUtil(), replacement);
    }

    @Override
    public CoreMultiMessagePostScript broadcastMessage(Collection<String> messageKeys, String... replacement) {
        return sendMessage(messageKeys, Core.getInstance().getOnlineRecipientProvider().get(), replacement);
    }

    @Override
    public String defaultLocale() {
        //"master_information"
        return Core.getInstance().getHazelDataApi().getMasterInformation().get("default_locale");
    }

    @Override
    public Locale defaultUtilLocale() {
        return parseFromString(defaultLocale());
    }

    @Contract(" -> new")
    private @NotNull Date now() {
        return Date.from(Instant.now());
    }

    /**
     * Support for the old replacement handling
     *
     * @param replacements the replacements
     * @return the replacement Function
     * @see #insertReplacements(CoreMessage, String...)
     * @deprecated use later inserted replacements
     */
    @Contract(pure = true)
    @Deprecated
    private @NotNull Function<String, String> insertReplacements(@Nullable String... replacements) {
        //noinspection ConstantConditions for 100% security
        if (replacements == null) {
            return miniMessage -> miniMessage;
        }
        return miniMessage -> {
            Core.getInstance().getCoreLogger().debug("insert start: " + miniMessage);
            for (int i = 0; i < replacements.length; i++) {
                if (replacements[i] == null) replacements[i] = "null";
                /* if(replacements[i].contains("</blank>")) {
                    Core.getInstance().getCoreLogger().warning("detected illegal </blank> in replacer! " + replacements[i] +
                            " at [" + i +"]"  + " for message \"" + miniMessage + "\" ");
                }
                 */
                //TODO replacements[i] = replacements[i].replace("</blank>","blank");
                miniMessage = miniMessage.replace(buildPattern(i), replacements[i]);
                Core.getInstance().getCoreLogger().debug("insert step: " + i + " :" + miniMessage);
            }
            Core.getInstance().getCoreLogger().debug("insert end: " + miniMessage);
            return miniMessage;
        };
    }

    private void insertReplacements(CoreMessage coreMessage, String @NotNull ... replacements) {
        Map<Integer, String> map = new HashMap<>();
        if(replacements == null)
            return;
        for (int i = 0; i < replacements.length; i++) {
            String replacement = replacements[i];
            map.putIfAbsent(i, replacement);
        }
        coreMessage.setReplacements(map);
    }

    @ApiStatus.Experimental
    private void insertReplacements(CoreMessage coreMessage, @NotNull Collection<String> replacements) {
        insertReplacements(coreMessage, replacements.toArray(new String[0]));
    }

    @Deprecated(forRemoval = true)
    protected String buildPattern(@Range(from = 0, to = Integer.MAX_VALUE) int i) {
        return "{" + i + "}";
    }

    public String reduce(@NotNull String locale, char delimiter) {
        String s1 = locale.substring(0, locale.lastIndexOf(delimiter));
        return s1.trim();
    }

    public String reduce(String locale) {
        return reduce(locale, '_');
    }


    /**
     * @param messageKey       the messageKey
     * @param messageRecipient the recipient
     * @return the best locale
     * @deprecated use {@link CoreMessageApi#findBestMessageForRecipient(String, MessageRecipient)} instead
     */
    @Deprecated
    public String findBestForRecipient(String messageKey, @NotNull MessageRecipient messageRecipient) {
        return findBest(messageKey, messageRecipient.supplyLocaleOrDefault());
    }

    public CoreMessage findBestMessageForRecipient(String messageKey, @NotNull MessageRecipient messageRecipient) {
        return findBestMessage(messageKey, messageRecipient.supplyLocaleOrDefault());
    }

    public CoreMessage findBestMessageForRecipient(String messageKey, @NotNull MessageRecipient messageRecipient, String... replacements) {
        return findBestMessage(messageKey, messageRecipient.supplyLocaleOrDefault(), replacements);
    }

    /**
     * returns the "best" locale that is available for that messageKey
     *
     * @deprecated use {@link CoreMessageApi#findBestMessage(String, String)} instead
     */
    @Deprecated
    public String findBest(String messageKey, String locale) {
        Message message = API.get().getMessageApi().getMessage(messageKey, locale);
        locale = checkLocale(locale);
        if (message instanceof FallBackMessage) {
            //fallback = not present
            if (!Objects.equals(locale, defaultLocale())) {
                if (locale.contains("_")) {
                    //can go "deeper"
                    return findBest(messageKey, reduce(locale));
                }
            }
            //use defaultLocale or fallback...
            return defaultLocale();

        } else {
            //Message is present...
            return locale;
        }
    }

    /**
     * returns the "best" locale that is available for that messageKey
     */
    public CoreMessage findBestMessage(String messageKey, String locale) {
        return findBestMessage(messageKey, locale, (String[]) null); //cast is very important!!
    }

    /**
     * returns the "best" locale that is available for that messageKey
     */
    public CoreMessage findBestMessage(String messageKey, String locale, @Nullable String... replacements) {
        CoreMessage message;
        if (replacements == null) {
            message = getMessage(messageKey, locale);
        } else
            message = getMessage(messageKey, locale, replacements);

        locale = checkLocale(locale); //May kill here in the future
        if (message instanceof FallBackMessage) {
            //fallback = not present
            if (!locale.equalsIgnoreCase(defaultLocale())) {
                if (locale.contains("_")) {
                    //can go "deeper"
                    return findBestMessage(messageKey, reduce(locale), replacements);
                }
            }
            //use defaultLocale or fallback...
            return getMessage(messageKey, defaultLocale(), replacements);

        } else {
            //Message is present...
            return message;
        }
    }

    /**
     * @param locale the locale to check
     * @return the locale or a trimmed version of it
     */
    @ApiStatus.Internal
    private String checkLocale(@NotNull String locale) {
        final int maxLocaleLength = MessageConfigManager.getMaxLocaleLength();
        if (locale.chars().mapToObj(i -> (char) i).filter(c -> c.equals('_')).count() > maxLocaleLength) {
            if (MessageConfigManager.getWarnOnInvalidLocale())
                Core.getInstance().getCoreLogger().warning("malformed locale that exceeds maximum length of " + maxLocaleLength + " : " + locale);
            if (MessageConfigManager.getThrowOnInvalidLocale())
                throw new InvalidParameterException("malformed locale that exceeds maximum length of " + maxLocaleLength + " : " + locale);
            String[] split = locale.split("_", maxLocaleLength + 1);
            StringJoiner trimmer = new StringJoiner("_");
            try {
                trimmer.add(split[0]);
                trimmer.add(split[1]);
                trimmer.add(split[2]);
            } catch (ArrayIndexOutOfBoundsException e) {
                Core.getInstance().getCoreLogger().error("A critical error occurred while trying to trim locale " + locale + " the messageSystem" +
                        " will continue with the default locale!");
                ThrowableDebug.debug(e);
                return defaultLocale();
            }
            return trimmer.toString();
        }
        return locale;
    }

    /**
     * Can be used to parse input safe to locale
     *
     * @param input
     * @return
     */
    public Locale parseFromString(@NotNull String input) {
        String[] s = input.split("_");
        Locale parse = null;

        if (s.length == 0) {
            parse = Locale.getDefault();
        }
        if (s.length == 1) {
            parse = new Locale(s[0]);
        }
        if (s.length == 2) {
            parse = new Locale(s[0], s[1]);
        }
        if (s.length == 3) {
            parse = new Locale(s[0], s[1], s[2]);
        }

        if (s.length > 3) {
            StringJoiner variant = new StringJoiner("_");
            for (int i = 3; i < s.length; i++) {
                variant.add(s[i]);
            }
            parse = new Locale(s[0], s[1], variant.toString());
        }
        return parse;
    }

    private Cache<Pair<String>, DBMessage> getMessageCache() {
        return MessageCaching.messageCache();
    }

    private Optional<DBMessage> getMessageFromCache(String messageKey, String locale) {
        if (MessageCaching.enabled && getMessageCache() != null)
            return Optional.ofNullable(getMessageCache().getIfPresent(new Pair<>(messageKey, locale)));
        return Optional.empty();
    }

    @Contract("_ -> param1")
    private @Nullable DBMessage cache(DBMessage dbMessage) {
        if (dbMessage == null) {
            return null;
        }
        if (getMessageCache() != null)
            getMessageCache().put(new Pair<>(dbMessage.getMessageKey(), dbMessage.getLocale()), dbMessage);
        return dbMessage;
    }

}
