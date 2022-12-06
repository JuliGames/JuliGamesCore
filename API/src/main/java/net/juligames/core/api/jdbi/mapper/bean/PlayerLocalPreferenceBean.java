package net.juligames.core.api.jdbi.mapper.bean;

import net.juligames.core.api.NoJavaDoc;
import net.juligames.core.api.jdbi.DBPlayerLocalPreference;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
@NoJavaDoc
public class PlayerLocalPreferenceBean implements DBPlayerLocalPreference {

    private String uuid;
    private String locale;
    private String fallback;

    public PlayerLocalPreferenceBean(String uuid, String locale, String fallback) {
        this.uuid = uuid;
        this.locale = locale;
        this.fallback = fallback;
    }

    private PlayerLocalPreferenceBean() {

    }

    @Override
    public String uuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String locale() {
        return locale;
    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String fallback() {
        return fallback;
    }

    @Override
    public void setFallback(String fallback) {
        this.fallback = fallback;
    }
}
