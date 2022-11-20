package net.juligames.core.api.data;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public interface HazelDataApi {

    /**
     * @param hazel the name of the map in the cluster
     * @param <A>   key
     * @param <B>   getValue
     * @return the map from hazelCast
     */
    <A, B> Map<A, B> getMap(String hazel);

    <T> Queue<T> getQueue(String hazel);

    <T> Set<T> getSet(String hazel);

    <E> List<E> getList(String hazel);

    default Map<String, String> getMasterInformation() {
        //API.get().getHazelDataApi().<String, String>getMap("master_information").get("default_locale")
        return getMap("master_information");
    }


}
