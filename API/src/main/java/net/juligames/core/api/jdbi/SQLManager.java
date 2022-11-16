package net.juligames.core.api.jdbi;

import org.jdbi.v3.core.Jdbi;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface SQLManager {


    void createTables();

    Jdbi getJdbi();
}
