package net.juligames.core;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import net.juligames.core.api.API;
import net.juligames.core.api.ApiCore;
import net.juligames.core.api.cluster.ClusterApi;
import net.juligames.core.api.data.HazelDataApi;
import net.juligames.core.api.notification.NotificationApi;
import net.juligames.core.cluster.CoreClusterApi;
import net.juligames.core.data.HazelDataCore;
import net.juligames.core.hcast.HazelConnector;
import net.juligames.core.notification.CoreNotificationApi;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public final class Core implements API {

    private static Core core;
    private HazelcastClient hazelcastClient;
    private HazelConnector hazelConnector;

    public static Core getInstance() {
        return core;
    }

    @ApiStatus.Internal
    public void start(String core_name) {
        if (core != null) throw new IllegalStateException("seems like a core is already running!");
        core = this;
        ApiCore.CURRENT_API = this;
        hazelConnector = HazelConnector.getInstanceAndConnect(core_name);
    }

    /**
     * @return the DataAPI
     */
    @Contract(value = " -> new", pure = true)
    @Override
    public @NotNull HazelDataApi getHazelDataApi() {
        return new HazelDataCore();
    }

    /**
     * @return The NotificationApi for this core
     */
    @Contract(value = " -> new", pure = true)
    @Override
    public @NotNull NotificationApi getNotificationApi() {
        return new CoreNotificationApi();
    }

    /**
     * @return The ClusterApi for this core
     */
    @Contract(value = " -> new", pure = true)
    @Override
    public @NotNull ClusterApi getClusterApi() {
        return new CoreClusterApi();
    }

    public HazelConnector getHazelConnector() {
        return hazelConnector;
    }

    public HazelcastClient getHazelcastClient() {
        return hazelcastClient;
    }

    public HazelcastInstance getOrThrow(){
        CompletableFuture<HazelcastInstance> instance = getHazelConnector().getInstance();
        if(instance.isDone()){
           return instance.getNow(null);
       }
        throw new NoSuchElementException("HazelcastInstance is not present!");
    }
}
