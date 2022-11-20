package net.juligames.core;

import com.hazelcast.core.HazelcastInstance;
import com.mysql.cj.log.Log;
import de.bentzin.tools.logging.JavaLogger;
import de.bentzin.tools.logging.Logger;
import net.juligames.core.api.API;
import net.juligames.core.api.ApiCore;
import net.juligames.core.api.message.MessageRecipient;
import net.juligames.core.cluster.CoreClusterApi;
import net.juligames.core.data.HazelDataCore;
import net.juligames.core.hcast.HazelConnector;
import net.juligames.core.jdbi.CoreSQLManager;
import net.juligames.core.message.CoreMessageApi;
import net.juligames.core.notification.CoreNotificationApi;
import net.juligames.core.notification.TopicNotificationCore;
import net.juligames.core.serialization.SerializedNotification;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public final class Core implements API {


    public static final String CORE_BRAND = "Core";
    public static final String CORE_VERSION_NUMBER = "0.0.1";
    public static final String CORE_SPECIFICATION = "Micheal";
    private static Core core;
    private HazelConnector hazelConnector;
    private TopicNotificationCore topicNotificationCore;
    private CoreNotificationApi coreNotificationApi;
    private CoreClusterApi clusterApi;
    private Logger coreLogger;
    private Logger apiLogger;
    private CoreSQLManager sqlManager;
    private CoreMessageApi messageApi;
    private String core_name;
    @NotNull
    private Supplier<Collection<? extends MessageRecipient>> onlineRecipientProvider = () -> List.of(new DummyMessageRecipient());
    public Core() {
    }

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

    public static Core getInstance() {
        return core;
    }

    @ApiStatus.Internal
    public void start(String core_name, Logger logger, boolean member) {
        this.core_name = core_name;
        if (core != null) throw new IllegalStateException("seems like a core is already running!");
        core = this;
        ApiCore.CURRENT_API = this;
        if (!member)
            hazelConnector = HazelConnector.getInstanceAndConnect(core_name);
        else
            hazelConnector = HazelConnector.getInstanceAndConnectAsMember(core_name);

        coreLogger = logger;
        coreLogger.setDebug(true);
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

        logger.info("connecting to jdbi... <hardcode>");
        sqlManager = new CoreSQLManager("jdbc:mysql://admin@localhost:3306/minecraft", logger);
        logger.info("connected to jdbi -> " + sqlManager);

        topicNotificationCore = new TopicNotificationCore(getOrThrow());
        coreNotificationApi = new CoreNotificationApi();
        clusterApi = new CoreClusterApi();
        messageApi = new CoreMessageApi();

        logger.info("loading replacements from jdbi...");
        messageApi.getTagManager().load();
        logger.info("loaded replacements from jdbi...");
        Core.getInstance().getOrThrow().<SerializedNotification>getTopic("notify: " + Core.getInstance().getClusterApi().getLocalUUID().toString())
                .addMessageListener(coreNotificationApi);
    }

    public void start(String core_name) {
        start(core_name, new JavaLogger(core_name, java.util.logging.Logger.getLogger(getShortCoreName())), false);
    }

    public void await() throws InterruptedException {
        try {
            getHazelConnector().getInstance().get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
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


    public Logger getCoreLogger() {
        return coreLogger;
    }


    public HazelConnector getHazelConnector() {
        return hazelConnector;
    }


    public HazelcastInstance getOrThrow() {
        CompletableFuture<HazelcastInstance> instance = getHazelConnector().getInstance();
        if (instance.isDone()) {
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

    public CoreSQLManager getSQLManager() {
        return sqlManager;
    }

    /**
     * @return The MessageAPI used to send Messages via core to players
     */
    @Override
    public CoreMessageApi getMessageApi() {
        return messageApi;
    }

    public Supplier<Collection<? extends MessageRecipient>> getOnlineRecipientProvider() {
        return onlineRecipientProvider;
    }

    public void setOnlineRecipientProvider(@NotNull Supplier<Collection<? extends MessageRecipient>> onlineRecipientProvider) {
        this.onlineRecipientProvider = onlineRecipientProvider;
    }
}
