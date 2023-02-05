package net.juligames.core.api.jdbi;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.result.ResultIterable;
import org.jetbrains.annotations.ApiStatus;

import java.sql.Connection;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface SQLManager {

    /**
     * used by the core to create the tables in the database.
     *
     * @apiNote this could be called at any time without bad effects... but its highly unrecommended unless you know what you are doing
     */
    @ApiStatus.Internal
    void createTables();

    /**
     * @return the {@link Jdbi} instance supplied by the core
     */
    Jdbi getJdbi();

    /**
     * @param key key
     * @return the data assisted with this key
     * @see DBData
     */
    @ApiStatus.Experimental
    String getData(String key);

    /**
     * @return a new Handle
     * @see Jdbi#open()
     */
    Handle openHandle();

    /**
     * You can use this to execute stuff on JDBI fast and secure
     * @return a new Handle
     * @see SQLManager#openHandle()
     */
    <R> R useHandle(Function<Handle,R> handleFunction);

    @ApiStatus.Experimental
    ResultIterable<Map<String, Object>> mapQuery(String sql);

    @ApiStatus.Experimental
    <T> ResultIterable<Map<String, T>> mapDefinedQuery(String sql, Class<T> valueClass);

    /**
     * @param connection connection to use
     * @return the {@link Jdbi} instance
     * @apiNote Only use if you know what you are doing
     * This can be used to connect to a second party Database
     */
    Jdbi getCustomJDBI(Connection connection);

    /**
     * @return the {@link ConfigRegistry} used with JDBI
     * @apiNote Only use if you know what you are doing. Modifications here will not affect JDBI
     * @see Jdbi#getConfig()
     * @see ConfigRegistry#createCopy()
     */
    ConfigRegistry getConfig();
}
