package net.juligames.core.api.jdbi;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public interface DBPlayerLocalPreference {
    String uuid();

    void setUuid(String uuid);

    String locale();

    void setLocale(String locale);

    String fallback();

    void setFallback(String fallback);
}
