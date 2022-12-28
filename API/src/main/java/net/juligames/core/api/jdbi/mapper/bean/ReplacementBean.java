package net.juligames.core.api.jdbi.mapper.bean;

import net.juligames.core.api.NoJavaDoc;
import net.juligames.core.api.jdbi.DBReplacement;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
@NoJavaDoc
public class ReplacementBean implements DBReplacement {

    private String replacementType;
    private String value;
    private String tag;

    public ReplacementBean(String replacementType, String value, String tag) {
        this.replacementType = replacementType;
        this.value = value;
        this.tag = tag;
    }

    public ReplacementBean() {
    }


    @Override
    public String getReplacementType() {
        return replacementType;
    }

    @Override
    public void setReplacementType(String replacementType) {
        this.replacementType = replacementType;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
