package net.juligames.core.api.jdbi.mapper.bean;

import net.juligames.core.api.jdbi.Message;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class MessageBean implements Message {

    public MessageBean(String messageKey, String locale, String miniMessage) {
        this.messageKey = messageKey;
        this.locale = locale;
        this.miniMessage = miniMessage;
    }

    public MessageBean() {

    }

    private String messageKey;
    private String locale;
    private String miniMessage;


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
}
