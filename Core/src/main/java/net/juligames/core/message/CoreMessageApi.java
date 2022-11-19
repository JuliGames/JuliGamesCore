package net.juligames.core.message;

import de.bentzin.tools.Hardcode;
import de.bentzin.tools.pair.Pair;
import net.juligames.core.Core;
import net.juligames.core.api.jdbi.*;
import net.juligames.core.api.jdbi.mapper.bean.MessageBean;
import net.juligames.core.api.message.*;
import net.juligames.core.message.adventure.AdventureTagManager;
import org.jdbi.v3.core.extension.ExtensionCallback;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public class CoreMessageApi implements MessageApi {

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
    public Message getMessage(String messageKey, DBLocale dbLocale) {
        return null;
    }

    @Override
    public Collection<Message> getMessage(String messageKey) {
        return null;
    }

    @Override
    public Collection<Message> getAllFromLocale(Locale locale) {
        return null;
    }

    @Override
    public Collection<Message> getAllFromLocale(String locale) {
        return null;
    }

    @Override
    public Collection<Message> getAllFromLocale(DBLocale dbLocale) {
        return null;
    }

    @Override
    public Collection<Message> getAll() {
        return null;
    }

    @Override
    public Stream<DBMessage> streamData() {
        return null;
    }

    @Override
    public Collection<Pair<String>> getReplacers() {
        return null;
    }

    @Override
    public boolean registerMessage(String messageKey) {
        return false;
    }

    @Override
    public boolean registerMessage(String messageKey, String defaultMiniMessage) {
        return false;
    }

    @Override
    public MessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient) {
        return null;
    }

    @Override
    public MessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, Locale overrideLocale) {
        return null;
    }

    @Override
    public MessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, String overrideLocale) {
        return null;
    }

    @Override
    public MessagePostScript sendMessage(String messageKey, MessageRecipient messageRecipient, DBLocale overrideLocale) {
        return null;
    }

    @Override
    public MultiMessagePostScript broadcastMessage(String messageKey, Locale defaultLocale) {
        return null;
    }

    @Override
    public MultiMessagePostScript broadcastMessage(String messageKey, String defaultLocale) {
        return null;
    }

    @Override
    public MultiMessagePostScript broadcastMessage(String messageKey, DBLocale defaultLocale) {
        return null;
    }

    @Override
    public MultiMessagePostScript broadcastMessage(String messageKey) {
        return null;
    }

    @Override
    public MultiMessagePostScript sendMessage(Collection<String> messageKeys, MessageRecipient messageRecipient) {
        return null;
    }

    @Override
    public MultiMessagePostScript sendMessage(Collection<String> messageKeys, Collection<MessageRecipient> messageRecipients) {
        return null;
    }

    @Override
    public MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, Locale defaultLocale) {
        return null;
    }

    @Override
    public MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, String defaultLocale) {
        return null;
    }

    @Override
    public MultiMessagePostScript broadcastMessage(Collection<String> messageKeys, DBLocale defaultLocale) {
        return null;
    }

    @Override
    public MultiMessagePostScript broadcastMessage(Collection<String> messageKeys) {
        return null;
    }

    @Override
    public AdventureTagManager getTagManager() {
        return adventureTagManager;
    }
}
