package net.juligames.core.data;

import com.hazelcast.collection.IList;
import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.topic.ITopic;
import net.juligames.core.Core;
import net.juligames.core.api.data.HazelDataApi;
import net.juligames.core.api.hazel.NativeHazelDataAPI;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public final class HazelDataCore implements HazelDataApi, NativeHazelDataAPI {
    /**
     * @param hazel the name of the map in the cluster
     * @return the map from hazelCast
     */
    @Override
    public <A, B> @NotNull IMap<A, B> getMap(String hazel) {
        return getHazelcastInstance().getMap(hazel);
    }

    private static HazelcastInstance getHazelcastInstance() {
        return Core.getInstance().getOrThrow();
    }

    @Override
    public <T> @NotNull IQueue<T> getQueue(String hazel) {
        return getHazelcastInstance().getQueue(hazel);
    }

    @Override
    public <T> @NotNull ISet<T> getSet(String hazel) {
        return getHazelcastInstance().getSet(hazel);
    }

    @Override
    public <E> @NotNull IList<E> getList(String hazel) {
        return getHazelcastInstance().getList(hazel);
    }

    @Override
    public <T> @NotNull ITopic<T> getTopic(String hazel) {
        return getHazelcastInstance().getTopic(hazel);
    }


}
