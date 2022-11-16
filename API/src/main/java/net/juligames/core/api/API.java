package net.juligames.core.api;

import net.juligames.core.api.cluster.ClusterApi;
import net.juligames.core.api.data.HazelDataApi;
import net.juligames.core.api.notification.NotificationApi;

/**
 * @implNote Main class to be used to get the different parts of the api
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface API {

    static API get() {
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
}
