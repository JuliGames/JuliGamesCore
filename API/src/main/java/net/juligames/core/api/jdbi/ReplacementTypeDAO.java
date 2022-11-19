package net.juligames.core.api.jdbi;

import net.juligames.core.api.API;
import net.juligames.core.api.jdbi.mapper.bean.ReplacementTypeBean;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
@RegisterBeanMapper(ReplacementTypeBean.class)
public interface ReplacementTypeDAO {


    @SqlUpdate("""
            create table if not exists minecraft.replacement_type
             (
                name varchar(100) not null primary key
             );
             
             """)
    void createTable();

    @SqlQuery("SELECT * FROM replacement_type")
    List<DBLocale> listAllBeans();

    @SqlUpdate("INSERT IGNORE INTO replacement_type(name) values (:name)")
    void insert(@BindBean DBReplacementType replacementType);

    @SqlUpdate("DELETE FROM replacement_type WHERE name = :name")
    void delete(@Bind("name") String name);

    @SqlUpdate("UPDATE replacement_type " +
            "SET name = :newName " +
            "WHERE name = :oldName;")
    void update(@Bind("old_name") String oldName, @Bind("newName") String newName);

    @SqlQuery("SELECT * FROM replacement_type where name = :name")
    ReplacementTypeBean selectBean(@Bind("name") String name);

    default DBReplacementType select(String name) {
        return selectBean(name);
    }

}
