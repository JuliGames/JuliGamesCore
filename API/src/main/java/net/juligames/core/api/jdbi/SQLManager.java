package net.juligames.core.api.jdbi;

import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.ApiStatus;

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
}
