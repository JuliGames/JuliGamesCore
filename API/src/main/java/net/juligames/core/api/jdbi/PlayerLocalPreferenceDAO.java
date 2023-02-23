package net.juligames.core.api.jdbi;

import net.juligames.core.api.jdbi.mapper.bean.PlayerLocalPreferenceBean;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
@Deprecated(forRemoval = true)
@RegisterBeanMapper(PlayerLocalPreferenceBean.class)
public interface PlayerLocalPreferenceDAO {

    @SqlUpdate("""
            create table if not exists player_locale_preference
            (
            uuid        char(36) not null primary key,
            locale      varchar(10)            not null,
            fallback varchar(10) default 'EN_US' not null
            );""")
    void createTable();

    @SqlQuery("SELECT * FROM player_locale_preference")
    List<DBPlayerLocalPreference> listAllBeans();

    @SqlUpdate("INSERT IGNORE INTO player_locale_preference(uuid, locale, fallback) values (:uuid, :locale, :fallback)")
    void insert(@BindBean DBPlayerLocalPreference preferenceBean);

    @SqlUpdate("DELETE FROM player_locale_preference WHERE uuid = :uuid")
    void delete(@Bind("uuid") String uuid);

    @SqlUpdate("UPDATE player_locale_preference " +
            "SET locale = :locale " +
            "WHERE uuid LIKE :uuid;")
    void update(@Bind("uuid") String uuid, @Bind("locale") String newLocale);

    @SqlUpdate("UPDATE player_locale_preference " +
            "SET fallback = :locale " +
            "WHERE uuid LIKE :uuid;")
    void updateFallback(@Bind("uuid") String uuid, @Bind("locale") String newLocale);

    default void delete(@NotNull UUID uuid) {
        delete(uuid.toString());
    }

    @SqlQuery("SELECT * FROM player_locale_preference where uuid = :uuid")
    DBPlayerLocalPreference selectBean(@Bind("uuid") String uuid);

    default DBPlayerLocalPreference select(String uuid) {
        return selectBean(uuid);
    }

    default List<DBPlayerLocalPreference> listAll() {
        return listAllBeans().stream().map(bean -> (DBPlayerLocalPreference) bean).toList();
    }
}
