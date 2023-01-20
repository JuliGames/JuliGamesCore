package net.juligames.core.message;

import de.bentzin.tools.Hardcode;
import net.juligames.core.Core;
import net.juligames.core.api.API;
import net.juligames.core.api.jdbi.*;
import net.juligames.core.api.jdbi.mapper.bean.MessageBean;
import net.juligames.core.api.jdbi.mapper.bean.ReplacementBean;
import net.juligames.core.api.message.Message;
import net.juligames.core.api.message.MessageApi;
import net.juligames.core.api.message.MessageRecipient;
import net.juligames.core.jdbi.CoreMessagePostScript;
import net.juligames.core.jdbi.CoreMultiMessagePostScript;
import org.jdbi.v3.core.extension.ExtensionCallback;
import org.jetbrains.annotations.*;

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

    @Override
    public CoreMessage getMessage(String messageKey, String locale) {
        DBMessage dbMessage = callMessageExtension(extension -> extension.select(messageKey, locale));
        return CoreMessage.fromData(dbMessage, locale);
    }

    @Override
    public CoreMessage getMessage(String messageKey, String locale, String... replacements) {
        DBMessage dbMessage = callMessageExtension(extension -> extension.select(messageKey, locale));
        CoreMessage message = CoreMessage.fromData(dbMessage, messageKey);
        message.doWithMiniMessage(insertReplacements(replacements));
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
        return messageBeans.stream().map(messageBean -> CoreMessage.fromData(messageBean, messageKey)).toList();
    }

    @Override
    public Collection<? extends Message> getMessage(String messageKey, String... replacements) {
        Collection<CoreMessage> message = getMessage(messageKey);
        message.forEach(o -> o.doWithMiniMessage(insertReplacements(replacements)));
        return message;
    }

    @Override
    public Collection<CoreMessage> getAllFromLocale(@NotNull Locale locale) {
        return getAllFromLocale(locale.toString());
    }

    @Override
    public Collection<? extends Message> getAllFromLocale(Locale locale, String... replacements) {
        Collection<CoreMessage> message = getAllFromLocale(locale);
        message.forEach(o -> o.doWithMiniMessage(insertReplacements(replacements)));
        return message;
    }

    @Override
    public Collection<CoreMessage> getAllFromLocale(String locale) {
        //EMPTY COLLECTION
        List<MessageBean> messageBeans = callMessageExtension(extension -> extension.selectFromLocale(locale));
        return messageBeans.stream().map(CoreMessage::fromData).toList();
    }

    @Override
    public Collection<CoreMessage> getAllFromLocale(String locale, String... replacements) {
        Collection<CoreMessage> message = getAllFromLocale(locale);
        message.forEach(o -> o.doWithMiniMessage(insertReplacements(replacements)));
        return message;
    }

    @Override
    public Collection<CoreMessage> getAllFromLocale(@NotNull DBLocale dbLocale) {
        return getAllFromLocale(dbLocale.toUtil());
    }

    @Override
    public Collection<CoreMessage> getAllFromLocale(DBLocale dbLocale, String... replacements) {
        Collection<CoreMessage> message = getAllFromLocale(dbLocale);
        message.forEach(o -> o.doWithMiniMessage(insertReplacements(replacements)));
        return message;
    }

    @Override
    public Collection<CoreMessage> getAll() {
        List<MessageBean> messageBeans = callMessageExtension(MessageDAO::listAllBeans);
        return messageBeans.stream().map(CoreMessage::new).toList();
    }

    @Override
    public Collection<CoreMessage> getAll(String... replacements) {
        List<MessageBean> messageBeans = callMessageExtension(MessageDAO::listAllBeans);
        List<CoreMessage> coreMessages = messageBeans.stream().map(CoreMessage::new).toList();
        coreMessages.forEach(coreMessage -> coreMessage.doWithMiniMessage(insertReplacements(replacements)));
        return coreMessages;
    }

    @Override
    public Stream<DBMessage> streamData() {
        List<MessageBean> messageBeans = callMessageExtension(MessageDAO::listAllBeans);
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
        Message message = getMessage(messageKey, findBestForRecipient(messageKey, messageRecipient), replacement);
        messageRecipient.deliver(message);
        return new CoreMessagePostScript(message, messageRecipient, now());
    }

    @Override
    public CoreMessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, @NotNull Locale overrideLocale, String... replacement) {
        return sendMessage(messageKey, messageRecipient, overrideLocale.toString(), replacement);
    }

    @Override
    public CoreMessagePostScript sendMessage(String messageKey, @NotNull MessageRecipient messageRecipient, String overrideLocale, String... replacement) {
        Message message = getMessage(messageKey, overrideLocale, replacement);
        message.doWithMiniMessage(insertReplacements(replacement));
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
    public Collection<CoreMessagePostScript> broadcastMessage(String messageKey, String... replacement) {
        //because of performance reasons I will reimplement sendMessage here - it is not "good" code, but I think its worth it!
        Collection<? extends MessageRecipient> messageRecipients = Core.getInstance().getOnlineRecipientProvider().get();
        Collection<CoreMessagePostScript> postScripts = new ArrayList<>();
        for (MessageRecipient messageRecipient : messageRecipients) {
            CoreMessage message = getMessage(messageKey, findBestForRecipient(messageKey, messageRecipient));
            message.doWithMiniMessage(insertReplacements(replacement));
            messageRecipient.deliver(message);
            postScripts.add(new CoreMessagePostScript(message, messageRecipient, now()));
        }
        return postScripts;
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient, String... replacement) {
        return sendMessage(messageKeys, List.of(messageRecipient), replacement);
    }

    @Override
    public CoreMultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, String... replacement) {
        //because of performance reasons I will reimplement sendMessage here - it is not "good" code, but I think its worth it!
        Collection<Message> messages = new ArrayList<>();
        for (String messageKey : messageKeys) {
            for (MessageRecipient messageRecipient : messageRecipients) {
                Message message = getMessage(messageKey, messageRecipient.supplyLocaleOrDefault());
                message.doWithMiniMessage(insertReplacements(replacement));
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
    public CoreMultiMessagePostScript sendMessage(@NotNull Collection<String> messageKeys, Collection<? extends MessageRecipient> messageRecipients, String overrideLocale, String... replacement) {
        //because of performance reasons I will reimplement sendMessage here - it is not "good" code, but I think its worth it!
        Collection<Message> messages = new ArrayList<>();
        for (String messageKey : messageKeys) {
            Message message = getMessage(messageKey, overrideLocale);
            message.doWithMiniMessage(insertReplacements(replacement));
            for (MessageRecipient messageRecipient : messageRecipients) {
                messageRecipient.deliver(message);
            }
            messages.add(message);
        }
        return new CoreMultiMessagePostScript(messages, messageRecipients, now());
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

    public String defaultLocale() {
        //"master_information"
        return Core.getInstance().getHazelDataApi().getMasterInformation().get("default_locale");
    }

    @Contract(" -> new")
    private @NotNull Date now() {
        return Date.from(Instant.now());
    }

    @Contract(pure = true)
    private @NotNull Function<String, String> insertReplacements(@Nullable String @NotNull ... replacements) {
        //noinspection ConstantConditions for 100% security
        if (replacements == null) {
            return miniMessage -> miniMessage;
        }
        return miniMessage -> {
            Core.getInstance().getCoreLogger().debug("insert start: " + miniMessage);
            for (int i = 0; i < replacements.length; i++) {
                assert replacements[i] != null;
                miniMessage = miniMessage.replace(buildPattern(i), replacements[i]);
                Core.getInstance().getCoreLogger().debug("insert step: " + i + " :" + miniMessage);
            }
            Core.getInstance().getCoreLogger().debug("insert end: " + miniMessage);
            return miniMessage;
        };
    }

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


    public String findBestForRecipient(String messageKey, @NotNull MessageRecipient messageRecipient) {
        return findBest(messageKey, messageRecipient.supplyLocaleOrDefault());
    }

    public String findBest(String messageKey, String locale) {
        Message message = API.get().getMessageApi().getMessage(messageKey, locale);
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


}
