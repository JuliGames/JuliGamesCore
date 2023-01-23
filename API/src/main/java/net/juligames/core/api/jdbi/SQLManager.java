package net.juligames.core.api.jdbi;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.sql.Connection;

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
     * @see DBData
     * @param key key
     * @return the data assisted with this key
     */
    @ApiStatus.Experimental
    String getData(String key);

    /**
     * @see Jdbi#open()
     * @return a new Handle
     */
    Handle openHandle();

    /**
     * @apiNote Only use if you know what you are doing
     * This can be used to connect to a second party Database
     * @param connection connection to use
     * @return the {@link Jdbi} instance
     */
    Jdbi getCustomJDBI(Connection connection);

    /**
     * @apiNote Only use if you know what you are doing. Modifications here will not affect JDBI
     * @see Jdbi#getConfig()
     * @see ConfigRegistry#createCopy()
     * @return the {@link ConfigRegistry} used with JDBI
     */
    ConfigRegistry getConfig();
}
