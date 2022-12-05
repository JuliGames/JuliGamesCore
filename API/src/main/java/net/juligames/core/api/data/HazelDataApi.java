package net.juligames.core.api.data;

import de.bentzin.tools.Hardcode;

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
     * @param <B>   value
     * @return the {@link Map} from hazelcast
     */
    <A, B> Map<A, B> getMap(String hazel);

    /**
     * @param hazel the name of the queue in the cluster
     * @return the Queue from hazelcast
     * @param <T> Type Parameter for the {@link Queue}
     */
    <T> Queue<T> getQueue(String hazel);

    /**
     * @param hazel the name of the set in the cluster
     * @return the Queue from hazelcast
     * @param <T> Type Parameter for the {@link Set}
     */
    <T> Set<T> getSet(String hazel);

    /**
     * @param hazel the name of the set in the cluster
     * @return the Queue from hazelcast
     * @param <E> Type Parameter for the {@link List}
     */
    <E> List<E> getList(String hazel);

    /**
     * @apiNote For further details of this map look at the wiki
     * @return a {@link Map} with general core information provided by the master.
     */
    @Hardcode
    default Map<String, String> getMasterInformation() {
        //API.get().getHazelDataApi().<String, String>getMap("master_information").get("default_locale")
        return getMap("master_information");
    }


}
