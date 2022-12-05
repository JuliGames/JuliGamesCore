package net.juligames.core.api.jdbi.mapper.bean;

import net.juligames.core.api.NoJavaDoc;
import net.juligames.core.api.jdbi.DBReplacementType;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
@NoJavaDoc
public class ReplacementTypeBean implements DBReplacementType {

    private String name;

    public ReplacementTypeBean(String name) {
        this.name = name;
    }

    public ReplacementTypeBean() {

    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
