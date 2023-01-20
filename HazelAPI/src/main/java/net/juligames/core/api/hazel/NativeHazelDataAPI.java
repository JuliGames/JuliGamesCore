package net.juligames.core.api.hazel;

import com.hazelcast.collection.IList;
import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ISet;
import com.hazelcast.map.IMap;
import com.hazelcast.topic.ITopic;

/**
 * @author Ture Bentzin
 * 16.11.2022
 * @apiNote This can be used to get the hazel versions of the datastructures - This will be extended in the future
 */
public interface NativeHazelDataAPI {

    /**
     * Used to access the hazelcast implementation of the Map
     *
     * @param hazel the hazel
     * @param <A>   Key
     * @param <B>   Value
     * @return the IMap associated to the hazel
     */
    <A, B> IMap<A, B> getMap(String hazel);

    /**
     * Used to access the hazelcast implementation of the Queue
     *
     * @param hazel the hazel
     * @param <T>   Type
     * @return the IQueue associated to the hazel
     */
    <T> IQueue<T> getQueue(String hazel);

    /**
     * Used to access the hazelcast implementation of the Set
     *
     * @param hazel the hazel
     * @param <T>   Type
     * @return the ISet associated to the hazel
     */
    <T> ISet<T> getSet(String hazel);

    /**
     * Used to access the hazelcast implementation of the List
     *
     * @param hazel the hazel
     * @param <E>   Type
     * @return the IList associated to the hazel
     */
    <E> IList<E> getList(String hazel);

    /**
     * Used to access the hazelcast Topic via the given hazel
     *
     * @param hazel the hazel
     * @param <T>   Type
     * @return the ITopic associated to the hazel
     */
    <T> ITopic<T> getTopic(String hazel);
}
