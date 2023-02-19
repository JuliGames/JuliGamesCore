package net.juligames.core.api.jdbi.mapper.bean;

import net.juligames.core.api.NoJavaDoc;
import net.juligames.core.api.jdbi.DBMessage;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
@NoJavaDoc
public class MessageBean implements DBMessage {

    private String messageKey;
    private String locale;
    private String miniMessage;

    public MessageBean(String messageKey, String locale, String miniMessage) {
        this.messageKey = messageKey;
        this.locale = locale;
        this.miniMessage = miniMessage;
    }

    public MessageBean() {

    }

    @Override
    public String getMiniMessage() {
        return miniMessage;
    }

    @Override
    public void setMiniMessage(String miniMessage) {
        this.miniMessage = miniMessage;
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    @Override
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public DBMessage clone() {
        return new MessageBean(messageKey, locale, miniMessage);
    }
}
