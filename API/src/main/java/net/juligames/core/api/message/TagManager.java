package net.juligames.core.api.message;

import net.juligames.core.api.NoJavaDoc;
import net.juligames.core.api.jdbi.DBReplacement;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public interface TagManager {
    void register(@NotNull DBReplacement dbReplacement);

    @NoJavaDoc
    void reload();
}
