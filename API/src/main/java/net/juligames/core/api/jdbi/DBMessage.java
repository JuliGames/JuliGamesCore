package net.juligames.core.api.jdbi;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface DBMessage {
    String getMiniMessage();

    void setMiniMessage(String miniMessage);

    String getLocale();

    void setLocale(String locale);

    String getMessageKey();
}
