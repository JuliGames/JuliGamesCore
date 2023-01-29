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
