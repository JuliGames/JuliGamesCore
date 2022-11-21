package net.juligames.core.api;

import de.bentzin.tools.logging.Logger;
import net.juligames.core.api.cluster.ClusterApi;
import net.juligames.core.api.data.HazelDataApi;
import net.juligames.core.api.err.APIException;
import net.juligames.core.api.jdbi.SQLManager;
import net.juligames.core.api.message.MessageApi;
import net.juligames.core.api.notification.NotificationApi;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

/**
 * @implNote Main class to be used to get the different parts of the api
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface API {

    static API get() throws APIException {
        API api = ApiCore.getAPI();
        if(api == null) {
            throw new APIException();
        }
        return ApiCore.getAPI();
    }

    /**
     *
     * @return the DataAPI
     */
    HazelDataApi getHazelDataApi();

    /**
     *
     * @return The NotificationApi for this core
     */
    NotificationApi getNotificationApi();

    /**
     *
     * @return The ClusterApi for this core
     */
    ClusterApi getClusterApi();

    /**
     * @return Logger for use when accessing via API
     */
    Logger getAPILogger();

    /**
     *
     * @return SQL Manager to get JDBI
     */
    SQLManager getSQLManager();

    /**
     *
     * @return The MessageAPI used to send Messages via core to players
     */
    MessageApi getMessageApi();

    /**
     *
     * @return The Name this core is assigned to
     */
    String getName();

    String getVersion();

    Map<String,String> getJavaEnvironment();

    Runtime getJavaRuntime();

    @ApiStatus.Internal
    void collectGarbage();

    /*
     *
     * @return if the core is connected and ready for operation
     */
   // boolean isAlive();



}
