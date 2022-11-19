package net.juligames.core.api.jdbi;

import net.juligames.core.api.API;
import net.juligames.core.api.jdbi.mapper.bean.MessageBean;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jetbrains.annotations.NotNull;

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
    List<MessageBean> listAllBeans();

    @SqlQuery("SELECT * FROM message WHERE messageKey = :key")
    List<MessageBean> selectFromKey(@Bind("key") String key);

    @SqlQuery("SELECT * FROM message WHERE locale = :locale")
    List<MessageBean> selectFromLocale(@Bind("locale") String locale);

    @SqlUpdate("INSERT IGNORE INTO message(messageKey, locale, miniMessage) values (:messageKey, :locale, :miniMessage)")
    void insert(@BindBean DBMessage message);

    @SqlUpdate("DELETE FROM message WHERE messageKey = :key")
    void delete(@Bind("key") String messageKey);

    @SqlUpdate("UPDATE minecraft.message " +
            "SET miniMessage = :newMiniMessage " +
            "WHERE locale = :locale AND messageKey;")
    void update(@Bind("key") String key,
                @Bind("locale")  String locale,
                @Bind("miniMessage") String newMiniMesssage);

    @SqlQuery("SELECT * FROM message where messageKey = :key AND locale = :locale")
    MessageBean selectBean(@Bind("key") String key, @Bind("locale") String locale);

    default DBMessage select(String key, String locale) {
        return selectBean(key,locale);
    }

    default MessageBean selectBean(String key, @NotNull DBLocale locale) {
        return selectBean(key, locale.getLocale());
    }

    /**
     * This will return the default message
     *
     * @param key the key
     * @return the Message
     */
    default MessageBean select(String key) {
        return selectBean(key, API.get().getHazelDataApi().<String, String>getMap("master_information").get("default_locale"));
    }


}
