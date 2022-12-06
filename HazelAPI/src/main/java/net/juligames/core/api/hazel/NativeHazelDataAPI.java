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


    <A, B> IMap<A, B> getMap(String hazel);

    <T> IQueue<T> getQueue(String hazel);

    <T> ISet<T> getSet(String hazel);

    <E> IList<E> getList(String hazel);

    <T> ITopic<T> getTopic(String hazel);
}
