package net.juligames.core.message;

import net.juligames.core.api.jdbi.mapper.bean.MessageBean;

/**
 * @apiNote This Bean is not associated with JDBI or data that is stored in JDBI
 * @author Ture Bentzin
 * 20.11.2022
 */
public class CreativeMessageBean extends MessageBean {

    public CreativeMessageBean(String messageKey, String locale, String miniMessage) {
        super(messageKey, locale, miniMessage);
    }

    public CreativeMessageBean() {
    }
}
