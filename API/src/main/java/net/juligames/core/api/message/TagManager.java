package net.juligames.core.api.message;

import net.juligames.core.api.jdbi.DBReplacement;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public interface TagManager {
    void register(DBReplacement dbReplacement);
}
