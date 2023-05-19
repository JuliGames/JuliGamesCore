package net.juligames.core.data;

import com.hazelcast.collection.IList;
import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ISet;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.topic.ITopic;
import net.juligames.core.Core;
import net.juligames.core.api.data.HazelDataApi;
import net.juligames.core.api.hazel.NativeHazelDataAPI;
import net.juligames.core.api.misc.APIUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public final class HazelDataCore implements HazelDataApi, NativeHazelDataAPI {
    private static @NotNull HazelcastInstance getHazelcastInstance() {
        return Core.getInstance().getOrThrow();
    }

    /**
     * @param hazel the name of the map in the cluster
     * @return the map from hazelCast
     */
    @Override
    public <A, B> @NotNull IMap<A, B> getMap(@NotNull String hazel) {
        return getHazelcastInstance().getMap(hazel);
    }

    @Override
    public <T> @NotNull IQueue<T> getQueue(@NotNull String hazel) {
        return getHazelcastInstance().getQueue(hazel);
    }

    @Override
    public <T> @NotNull ISet<T> getSet(@NotNull String hazel) {
        return getHazelcastInstance().getSet(hazel);
    }

    @Override
    public <E> @NotNull IList<E> getList(@NotNull String hazel) {
        return getHazelcastInstance().getList(hazel);
    }

    @Override
    public <T> @NotNull ITopic<T> getTopic(@NotNull String hazel) {
        return getHazelcastInstance().getTopic(hazel);
    }

    @Override
    public @NotNull Collection<DistributedObject> getAll() {
        return getHazelcastInstance().getDistributedObjects();
    }

    /**
     * Retrieves an instance of the specified type that extends DistributedObject, with the given name.
     *
     * @param hazel the name of the distributed object to retrieve
     * @param <T>   the type of the distributed object to retrieve
     * @return an Optional containing the instance of the distributed object, if found; otherwise, an empty Optional
     * @throws NullPointerException if the hazel parameter is null
     */
    @Override
    public <T extends DistributedObject> @NotNull Optional<T> get(@NotNull String hazel) {
        //noinspection unchecked
        return APIUtils.mapOrDrop(getAll()
                        .stream()
                        .filter(distributedObject ->
                                distributedObject.getName().equals(hazel)),
                distributedObject -> (T) distributedObject).findAny();
    }


}
