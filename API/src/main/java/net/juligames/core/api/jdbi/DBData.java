package net.juligames.core.api.jdbi;

/**
 * @author Ture Bentzin
 * 23.01.2023
 * @apiNote This is an experimental Idea to allow users of the Core to store data only for meant to be dealt with into
 * MariaDB. You should use a unique keyspace for your data stored in here. If this Feature will not be used, it may be removed
 * with 2.0 - TANDANA
 */
public interface DBData {

    String getKey();

    String getData();

    void setData(String data);

    void setBytesAsBase64(byte[] bytes);

    byte[] getDataFromBase64();

    char[] getDataChars();

    long getDataLong();
}
