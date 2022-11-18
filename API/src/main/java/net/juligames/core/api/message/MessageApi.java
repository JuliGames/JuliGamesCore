package net.juligames.core.api.message;

import de.bentzin.tools.pair.Pair;
import net.juligames.core.api.jdbi.DBLocale;
import net.juligames.core.api.jdbi.MessageDAO;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public interface MessageApi {

    //get
    Message getMessage(String messageKey, Locale locale);
    Message getMessage(String messageKey, String locale);
    Message getMessage(String messageKey, DBLocale dbLocale);

    Collection<Message> getMessage(String messageKey);
    Collection<Message> getAllFromLocale(Locale locale);
    Collection<Message> getAllFromLocale(String locale);
    Collection<Message> getAllFromLocale(DBLocale dbLocale);

    Collection<Pair<String>> getReplacers();

    //register

    @Deprecated
    boolean registerMessage(String messageKey);
    boolean registerMessage(String messageKey, String defaultMiniMessage);

    MessagePostScript sendMessage(String messageKey);
    //TODO messageRecipient and preferd locals

}
