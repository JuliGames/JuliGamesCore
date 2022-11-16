package net.juligames.core.api;

import net.juligames.core.api.data.HazelDataApi;

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

}
