package net.juligames.core.api.jdbi;

import net.juligames.core.api.jdbi.mapper.bean.LocaleBean;
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
@RegisterBeanMapper(LocaleBean.class)
public interface LocaleDAO {

    @SqlUpdate("""
            create table if not exists minecraft.locale
            (
            locale      varchar(10)            not null primary key ,
            miniMessage varchar(20) default '' not null
            );""")
    void createTable();

    @SqlQuery("SELECT * FROM locale")
    List<LocaleBean> listAllBeans();

    @SqlUpdate("INSERT INTO locale(locale, description) values (:locale, :description)")
    void insert(@BindBean DBLocale locale);

    @SqlUpdate("DELETE FROM locale WHERE locale = :locale")
    void delete(@Bind("locale") String locale);

    @SqlQuery("SELECT * FROM locale where locale = :locale")
    LocaleBean selectBean(@Bind("locale") String locale);

    default DBLocale select(String locale) {
        return selectBean(locale);
    }

    default List<DBLocale> listAll() {
        return listAllBeans().stream().map(localeBean -> (DBLocale) localeBean).toList();
    }


}
