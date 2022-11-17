package net.juligames.core;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import de.bentzin.tools.logging.JavaLogger;
import de.bentzin.tools.logging.Logger;
import net.juligames.core.api.API;
import net.juligames.core.api.ApiCore;
import net.juligames.core.api.cluster.ClusterApi;
import net.juligames.core.api.data.HazelDataApi;
import net.juligames.core.api.notification.NotificationApi;
import net.juligames.core.cluster.CoreClusterApi;
import net.juligames.core.data.HazelDataCore;
import net.juligames.core.hcast.HazelConnector;
import net.juligames.core.notification.CoreNotification;
import net.juligames.core.notification.CoreNotificationApi;
import net.juligames.core.notification.TopicNotificationCore;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public final class Core implements API {


    public static final String CORE_BRAND = "Core";
    public static final String CORE_VERSION_NUMBER = "0.0.1";
    public static final String CORE_SPECIFICATION = "Micheal";


    @Contract(pure = true)
    public static @NotNull String getFullCoreName() {
        return CORE_BRAND + "-" + CORE_VERSION_NUMBER + " " + CORE_SPECIFICATION;
    }

    @Contract(pure = true)
    public static @NotNull String getShortCoreName() {
        return CORE_BRAND + "-" + CORE_VERSION_NUMBER;
    }

    @Contract(pure = true)
    public static @NotNull String getShortRelease() {
        return CORE_BRAND + "-" + CORE_SPECIFICATION;
    }


    private static Core core;
    private HazelConnector hazelConnector;
    private TopicNotificationCore topicNotificationCore;
    private CoreNotificationApi coreNotificationApi;
    private CoreClusterApi clusterApi;

    private Logger coreLogger;
    private Logger apiLogger;

    private String core_name;

    public static Core getInstance() {
        return core;
    }

    @ApiStatus.Internal
    public void start(String core_name, Logger logger, boolean member) {
        this.core_name = core_name;
        if (core != null) throw new IllegalStateException("seems like a core is already running!");
        core = this;
        ApiCore.CURRENT_API = this;
        if(!member)
            hazelConnector = HazelConnector.getInstanceAndConnect(core_name);
        else
            hazelConnector = HazelConnector.getInstanceAndConnectAsMember(core_name);

        coreLogger = logger;
        apiLogger = coreLogger.adopt("api");

        coreLogger.info(core_name + " was started! - waiting for HazelCast to connect!");

        try {
            hazelConnector.getInstance().get();
            coreLogger.info("connected to hazelcast!");
        } catch (InterruptedException | ExecutionException e) {
            coreLogger.error("connection to hazelcast failed! connection cant be reestablished!");
            coreLogger.error(e.getClass().getName() + " : " + e.getMessage());
            e.printStackTrace();
        }

        topicNotificationCore = new TopicNotificationCore(getOrThrow());
        coreNotificationApi = new CoreNotificationApi();
        clusterApi = new CoreClusterApi();

        Core.getInstance().getOrThrow().<CoreNotification>getTopic("notify: " + Core.getInstance().getClusterApi().getLocalUUID().toString())
                .addMessageListener(coreNotificationApi);
    }

    public void start(String core_name) {
        start(core_name,new JavaLogger(core_name, java.util.logging.Logger.getLogger(getShortCoreName())),false);
    }

    public void stop(){
        coreLogger.info("stopping hazelcast client connection");
        hazelConnector.disconnect();
        coreLogger.info("goodbye!");
    }

    /**
     * @return the DataAPI
     */
    @Contract(value = " -> new", pure = true)
    @Override
    public @NotNull HazelDataCore getHazelDataApi() {
        return new HazelDataCore();
    }

    /**
     * @return The NotificationApi for this core
     */
    @Contract(value = " -> new", pure = true)
    @Override
    public @NotNull CoreNotificationApi getNotificationApi() {
        return coreNotificationApi;
    }

    /**
     * @return The ClusterApi for this core
     */
    @Contract(value = " -> new", pure = true)
    @Override
    public @NotNull CoreClusterApi getClusterApi() {
        return clusterApi;
    }

    /**
     * @return Logger for use when accessing via API
     */
    @Override
    public Logger getAPILogger() {
        return apiLogger;
    }


    public Logger getCoreLogger(){
        return coreLogger;
    }


    public HazelConnector getHazelConnector() {
        return hazelConnector;
    }


    public HazelcastInstance getOrThrow(){
        CompletableFuture<HazelcastInstance> instance = getHazelConnector().getInstance();
        if(instance.isDone()){
           return instance.getNow(null);
       }
        throw new NoSuchElementException("HazelcastInstance is not present!");
    }

    public HazelcastInstance getOrWait() throws ExecutionException, InterruptedException {
        CompletableFuture<HazelcastInstance> instance = getHazelConnector().getInstance();
        return instance.get();
    }

    public TopicNotificationCore getNotificationCore() {
        return topicNotificationCore;
    }

}
