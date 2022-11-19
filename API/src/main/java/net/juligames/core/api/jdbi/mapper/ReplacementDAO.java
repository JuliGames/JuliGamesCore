package net.juligames.core.api.jdbi.mapper;

import net.juligames.core.api.jdbi.DBReplacement;
import net.juligames.core.api.jdbi.mapper.bean.ReplacementBean;
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
@RegisterBeanMapper(ReplacementBean.class)
public interface ReplacementDAO {

    @SqlUpdate("""
            create table if not exists minecraft.replacement
            (
            tag varchar(100) not null,
            replacementType varchar(100) not null primary key,
            value varchar(255) not null
            );""")
    void createTable();

    @SqlQuery("SELECT * FROM replacement")
    List<ReplacementBean> listAllBeans();

    @SqlUpdate("INSERT INTO replacement(replacementType,tag, value) values (:replacementType," +
            " :tag, :value)")
    void insert(@BindBean DBReplacement locale);

    @SqlUpdate("DELETE FROM replacement WHERE tag = :tag")
    void delete(@Bind("tag") String tag);

    @SqlUpdate("UPDATE minecraft.replacement " +
            "SET value = :value " +
            "WHERE tag LIKE :tag;")
    void update(@Bind("tag") String tag, @Bind("value") String newValue);

    @SqlQuery("SELECT * FROM replacement where tag = :tag")
    ReplacementBean selectBean(@Bind("tag") String tag);

    default DBReplacement select(String locale) {
        return selectBean(locale);
    }

    default List<DBReplacement> listAll() {
        return listAllBeans().stream().map(bean -> (DBReplacement) bean).toList();
    }


}
