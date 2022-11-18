package net.juligames.core.api.jdbi;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface DBLocale {
    String getDescription();

    void setDescription(String description);

    String getLocale();

    void setLocale(String locale);

    @NotNull String getInfo();

    java.util.Locale toUtil();
}
