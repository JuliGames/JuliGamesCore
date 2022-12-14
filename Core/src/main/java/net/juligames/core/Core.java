package net.juligames.core;

import com.hazelcast.core.HazelcastInstance;
import de.bentzin.tools.logging.JavaLogger;
import de.bentzin.tools.logging.Logger;
import de.bentzin.tools.misc.SubscribableType;
import de.bentzin.tools.register.Registerator;
import net.juligames.core.api.API;
import net.juligames.core.api.ApiCore;
import net.juligames.core.api.TODO;
import net.juligames.core.api.command.CommandApi;
import net.juligames.core.api.config.ConfigurationAPI;
import net.juligames.core.api.err.dev.TODOException;
import net.juligames.core.api.message.MessageRecipient;
import net.juligames.core.api.minigame.BasicMiniGame;
import net.juligames.core.cluster.CoreClusterApi;
import net.juligames.core.command.CoreCommandApi;
import net.juligames.core.config.CoreConfigurationApi;
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

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public final class Core implements API {

    /**
     * This can be set depending on the build of the Core
     */
    public static final String CORE_BRAND = "Core";
    public static final String CORE_VERSION_NUMBER = "1.0";
    public static final String CORE_SPECIFICATION = "Gustav";//Development Specification: Michael(.n)
    private static final String BUILD_VERSION = "1.0"; //POM VERSION
    private static Core core;
    private HazelConnector hazelConnector;
    private TopicNotificationCore topicNotificationCore;
    private CoreNotificationApi coreNotificationApi;
    private CoreClusterApi clusterApi;
    private Logger coreLogger;
    private Logger apiLogger;
    private CoreSQLManager sqlManager;
    private CoreMessageApi messageApi;
    private CoreConfigurationApi configurationAPI;
    private CoreCommandApi coreCommandApi;
    private SubscribableType<BasicMiniGame> basicMiniGame;
    private String core_name;
    private Registerator<Consumer<HazelcastInstance>> hazelcastPostPreparationWorkers = new Registerator<>("hazelcastPostPreparationWorkers");
    @NotNull
    private Supplier<Collection<? extends MessageRecipient>> onlineRecipientProvider = () -> List.of(new DummyMessageRecipient());

    public Core() {
    }

    /**
     * This will create AND START the Core!
     *
     * @param core_name the core name
     */
    @ApiStatus.Experimental
    public Core(String core_name) {
        start(core_name);
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
        coreLogger.info("------> " + getFullCoreName());
        coreLogger.info(core_name + " was started! - waiting for HazelCast to connect!");

        try {
            hazelConnector.getInstance().get();
            coreLogger.info("connected to hazelcast!");
        } catch (InterruptedException | ExecutionException e) {
            coreLogger.error("connection to hazelcast failed! connection cant be reestablished!");
            coreLogger.error(e.getClass().getName() + " : " + e.getMessage());
            e.printStackTrace();
        }
        //postPreperation
        getHazelcastPostPreparationWorkers().forEach(hazelcastInstanceConsumer -> hazelcastInstanceConsumer.accept(getOrThrow()));

        configurationAPI = new CoreConfigurationApi();

        logger.info("connecting to jdbi... <loading from config>");
        Optional<String> jdbc = getConfigurationApi().database().getString("jdbc");
        if (jdbc.isEmpty()) {
            Core.getInstance().coreLogger.warning("cant read jdbc data in database...");
        }
        logger.warning("database: " + getConfigurationApi().database().cloneToProperties().toString());
        sqlManager = new CoreSQLManager(jdbc.orElse("jdbc:mysql://root@localhost:3306"), logger); //jdbc:mysql://admin@localhost:3306/minecraft
        logger.info("connected to jdbi -> " + sqlManager);

        topicNotificationCore = new TopicNotificationCore(getOrThrow());
        coreNotificationApi = new CoreNotificationApi();
        coreCommandApi = new CoreCommandApi();
        clusterApi = new CoreClusterApi();
        messageApi = new CoreMessageApi();
        basicMiniGame = new SubscribableType<>();


        Core.getInstance().getOrThrow().<SerializedNotification>getTopic("notify: " + Core.getInstance().getClusterApi().getLocalUUID().toString())
                .addMessageListener(coreNotificationApi);

        logger.info("hooking to shutdown...");
        getJavaRuntime().addShutdownHook(new Thread(() -> {
            try {
                getOrThrow().shutdown();
            } catch (Exception e) {
                coreLogger.error(e.getMessage());
            }

        }));
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
        coreLogger.info("dropping api...");
        dropApiService();
        coreLogger.info("api is now offline!");


        try {
            Core.getInstance().getOrThrow().getTopic("notify: " + Core.getInstance().getClusterApi().getLocalUUID()).destroy();
        }catch(NoSuchElementException noSuchElementException){
            coreLogger.error("failed to destroy hazel -- master reboot maybe required :: " +noSuchElementException.getMessage());
        }
        coreLogger.info("stopping hazelcast client connection");
        hazelConnector.disconnect();
        coreLogger.info("goodbye!");
    }

    public @NotNull Optional<? extends MessageRecipient> findRecipient(Predicate<MessageRecipient> searchQuery) {
        return onlineRecipientProvider.get().stream().filter(searchQuery).findFirst();
    }

    public @NotNull Optional<? extends MessageRecipient> findRecipientByName(String name) {
        return findRecipient(messageRecipient -> messageRecipient.getName().equals(name));
    }

    @ApiStatus.Internal
    private void dropApiService() {
        ApiCore.CURRENT_API = null;
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

    private HazelcastInstance getForce() {
        return getHazelConnector().getForce();
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

    /**
     * @return the {@link ConfigurationAPI}
     */
    @Override
    public CoreConfigurationApi getConfigurationApi() {
        return configurationAPI;
    }

    /**
     * @return the {@link CommandApi}
     */
    @Override
    public CoreCommandApi getCommandApi() {
        return coreCommandApi;
    }

    @Override
    public SubscribableType<BasicMiniGame> getLocalMiniGame() {
        return basicMiniGame;
    }

    public void introduceMiniGame(BasicMiniGame basicMiniGame) {
        this.basicMiniGame.set(basicMiniGame);
    }

    /**
     * @return The Name this core is assigned to
     */
    @Override
    public String getName() {
        return core_name;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String getVersion() {
        return getFullCoreName();
    }

    @Override
    public String getBuildVersion() {
        return BUILD_VERSION;
    }

    @Override
    public Map<String, String> getJavaEnvironment() {
        return System.getenv();
    }

    @Override
    public Runtime getJavaRuntime() {
        return Runtime.getRuntime();
    }

    @Override
    public void collectGarbage() {
        getJavaRuntime().gc();
    }

    /**
     * @return if the core is connected and ready for operation
     */
    @TODO(doNotcall = true)
    public boolean isAlive() {
        throw new TODOException();
    }

    public Supplier<Collection<? extends MessageRecipient>> getOnlineRecipientProvider() {
        return onlineRecipientProvider;
    }

    public void setOnlineRecipientProvider(@NotNull Supplier<Collection<? extends MessageRecipient>> onlineRecipientProvider) {
        this.onlineRecipientProvider = onlineRecipientProvider;
    }

    @Override
    public Collection<? extends MessageRecipient> supplyOnlineRecipients() {
        return getOnlineRecipientProvider().get();
    }

    public Registerator<Consumer<HazelcastInstance>> getHazelcastPostPreparationWorkers() {
        return hazelcastPostPreparationWorkers;
    }
}
