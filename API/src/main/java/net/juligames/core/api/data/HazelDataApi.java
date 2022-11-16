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
     *
     * @param hazel the name of the map in the cluster
     * @return the map from hazelCast
     * @param <A> key
     * @param <B> value
     */
    <A,B> Map<A,B> getMap(String hazel);
    <T> Queue<T> getQueue(String hazel);
    <T> Set<T> getSet(String hazel);
    <E> List<E> getList(String hazel);


}
