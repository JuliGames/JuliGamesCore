package net.juligames.core.api.jdbi;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public interface DBReplacement {

    DBReplacementType getReplacementType();

    void setReplacementType(DBReplacementType replacementType);

    String getValue();

    void setValue(String value);

    String getTag();

    void setTag(String tag);
}
