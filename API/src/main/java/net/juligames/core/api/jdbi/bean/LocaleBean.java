package net.juligames.core.api.jdbi.bean;

import net.juligames.core.api.jdbi.Locale;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class LocaleBean implements Locale {


    private String locale;
    private String description;

    public LocaleBean(String locale, String description) {
        this.locale = locale;
        this.description = description;
    }
    public LocaleBean() {

    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public final @NotNull String getInfo() {
        return getLocale() + ":" + getDescription();
    }

    @Override
    public final java.util.Locale toUtil() {
        return java.util.Locale.forLanguageTag(getLocale());
    }
}
