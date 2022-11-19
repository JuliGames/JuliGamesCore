package net.juligames.core.api.jdbi;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public interface DBReplacement {
    String replacementType();

    DBReplacementType getReplacementType();

    void setReplacementType(String replacementType);

    String value();

    void setValue(String value);

    String getTag();

    void setTag(String tag);
}
