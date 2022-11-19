package net.juligames.core.api.jdbi.mapper.bean;

import net.juligames.core.api.jdbi.DBReplacementType;

/**
 * @author Ture Bentzin
 * 19.11.2022
 */
public class DBReplacementTypeBean implements DBReplacementType {

    private String name;

    public DBReplacementTypeBean(String name) {
        this.name = name;
    }

    public DBReplacementTypeBean() {

    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
