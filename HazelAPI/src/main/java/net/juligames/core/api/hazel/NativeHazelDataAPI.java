package net.juligames.core.api.hazel;

import com.hazelcast.collection.IList;
import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ISet;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.map.IMap;
import com.hazelcast.topic.ITopic;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

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

    @ApiStatus.Experimental
    @ApiStatus.AvailableSince("1.5")
    Collection<DistributedObject> getAll();

    /**
     * Retrieves an instance of the specified type that extends DistributedObject, with the given name.
     * This method can be used to automatically determine the type of hazel!
     * The implementation might be changed over a version.
     * This one should not be used outside user interface applications
     *
     * @param hazel hazel
     * @param <T>   the implementation of {@link DistributedObject}
     * @return the optional that may contain a  {@link DistributedObject}
     * @apiNote The implementation currently uses {@link #getAll()} and {@link java.util.function.Predicate}s to
     * determine what will be returned.
     */
    @ApiStatus.AvailableSince("1.6")
    <T extends DistributedObject> Optional<T> get(@NotNull String hazel);
}
