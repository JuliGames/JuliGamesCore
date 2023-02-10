package net.juligames.core.api.jdbi;

import net.juligames.core.api.jdbi.mapper.bean.DataBean;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * @author Ture Bentzin
 * 23.01.2023
 */
@RegisterBeanMapper(DataBean.class)
public interface DataDAO {

    @SqlUpdate("""
            create table if not exists data
            (
            data_key varchar(255) primary key,
            data TEXT
            );""")
    void createTable();

    @SqlQuery("SELECT * FROM data")
    List<DataBean> listAllBeans();

    @SqlUpdate("INSERT IGNORE INTO data(data_key, data) values (:key, data)")
    void insert(@BindBean DBData dbData);

    @SqlUpdate("INSERT INTO data (data_key, data) VALUES (?,?)")
    void insert(String key, String data);

    @SqlUpdate("DELETE FROM data WHERE data_key = ?")
    void delete(String key);

    @SqlUpdate("UPDATE data SET data = :data WHERE data_key = :key")
    void update(@Bind("key") String key, @Bind("data") String newData);

    @SqlQuery("SELECT * from data WHERE data_key = ?")
    DataBean selectBean(String key);

    default DBData select(String key) {
        return selectBean(key);
    }

    default List<DBData> listAll() {
        return listAllBeans().stream().map(dataBean -> (DBData) dataBean).toList();
    }
}
