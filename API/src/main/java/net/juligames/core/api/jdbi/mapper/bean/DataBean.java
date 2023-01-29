package net.juligames.core.api.jdbi.mapper.bean;

import net.juligames.core.api.NoJavaDoc;
import net.juligames.core.api.jdbi.DBData;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 23.01.2023
 */
@NoJavaDoc
public class DataBean implements DBData {

    @ColumnName("data_key")
    private final String key;
    private String data;

    public DataBean(String key, String data) {
        this.key = key;
        this.data = data;
    }

    public DataBean() {
        key = null;
        data = null;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public void setBytesAsBase64(byte[] bytes) {
        this.data = Base64.getEncoder().encodeToString(bytes);
    }

    @Override
    public byte[] getDataFromBase64() {
        return Base64.getDecoder().decode(data);
    }

    @Override
    public char[] getDataChars() {
        return data.toCharArray();
    }

    @Override
    public long getDataLong() {
        return deal(Long::parseLong);
    }

    /**
     * @see DataBean#getDataLong()
     */
    @ApiStatus.Experimental
    public <T> T deal(@NotNull Function<String, T> dealer) {
        return dealer.apply(data);
    }
}
