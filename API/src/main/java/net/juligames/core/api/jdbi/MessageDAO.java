package net.juligames.core.api.jdbi;

import net.juligames.core.api.API;
import net.juligames.core.api.jdbi.mapper.bean.MessageBean;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
@SuppressWarnings("SqlResolve")
@RegisterBeanMapper(MessageBean.class)
public interface MessageDAO {

    @SqlUpdate("""
            create table if not exists minecraft.message
             (
                 messageKey  varchar(100) not null,
                 locale      varchar(10)  not null,
                 miniMessage TEXT         not null,
                 constraint message_pk
                     primary key (messageKey, locale)
             );
             
             """)
    void createTable();

    @SqlQuery("SELECT * FROM message")
    List<Locale> listAllBeans();

    @SqlUpdate("INSERT INTO message(messageKey, locale, miniMessage) values (:messageKey, :locale, :miniMessage)")
    void insert(@BindBean Message message);

    @SqlUpdate("DELETE FROM message WHERE messageKey = :key")
    void delete(@Bind("key") String messageKey);

    @SqlQuery("SELECT * FROM message where messageKey = :key AND locale = :locale")
    Message select(@Bind("key") String key, @Bind("locale") String locale);

    default Message selectBean(String key, Locale locale) {
        return select(key, locale.getLocale());
    }

    /**
     * This will return the default message
     *
     * @param key the key
     * @return the Message
     */
    default Message select(String key) {
        return select(key, API.get().getHazelDataApi().<String, String>getMap("master_information").get("default_locale"));
    }


}
