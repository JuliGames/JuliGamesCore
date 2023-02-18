package net.juligames.core.api;

import de.bentzin.tools.logging.Logger;
import de.bentzin.tools.misc.SubscribableType;
import net.juligames.core.api.cacheing.CacheApi;
import net.juligames.core.api.cluster.ClusterApi;
import net.juligames.core.api.command.CommandApi;
import net.juligames.core.api.config.ConfigurationAPI;
import net.juligames.core.api.data.HazelDataApi;
import net.juligames.core.api.err.APIException;
import net.juligames.core.api.jdbi.SQLManager;
import net.juligames.core.api.message.MessageApi;
import net.juligames.core.api.message.MessageRecipient;
import net.juligames.core.api.minigame.BasicMiniGame;
import net.juligames.core.api.notification.NotificationApi;
import org.checkerframework.checker.optional.qual.MaybePresent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 16.11.2022
 * @implNote Main class to be used to get the different parts of the api
 */
public interface API {

    /**
     * This is the primary way for getting your API Instance. With this Instance you have access to all build in features in the core
     * that are explicitly exposed to the API. If the API should not be provided by a core then this will throw a {@link APIException}
     * to indicate that a Core is necessary to get this Instance. For further Information on how to use the {@link API} please check out
     * the GitHub Wiki. For further information on this method check out {@link ApiProvider}
     *
     * @return your API Instance
     * @throws APIException if no API is present
     */
    static @NotNull API get() throws APIException {
        @MaybePresent Optional<API> api = ApiProvider.optionalApi();
        if (api.isEmpty()) {
            throw new APIException();
        }
        return api.get();
    }

    /**
     * Get the first result to your query. The Optional might not be present if your query had no results!
     *
     * @param searchQuery the query to filter for
     * @return an NonNull Optional that contains the first result for your query. The Optional might be empty
     */
    @MaybePresent
    @NotNull Optional<? extends MessageRecipient> findRecipient(@NotNull Predicate<MessageRecipient> searchQuery);

    @MaybePresent
    @NotNull Optional<? extends MessageRecipient> findRecipientByName(@NotNull String name);

    /**
     * @return the {@link HazelDataApi}
     */
    @NotNull HazelDataApi getHazelDataApi();

    /**
     * @return The {@link NotificationApi} for this core
     */
    @NotNull NotificationApi getNotificationApi();

    /**
     * @return The {@link ClusterApi} for this core
     */
    @NotNull ClusterApi getClusterApi();

    /**
     * @return de.bentzin.tools.logging.Logger for use when accessing via API
     */
    @NotNull Logger getAPILogger();

    /**
     * @return net.juligames.core.api.jdbi.SQLManager to get {@link org.jdbi.v3.core.Jdbi}
     */
    @NotNull SQLManager getSQLManager();

    /**
     * @return The {@link MessageApi} used to send Messages via core to players
     */
    @NotNull MessageApi getMessageApi();

    /**
     * @return the {@link ConfigurationAPI}
     */
    @NotNull ConfigurationAPI getConfigurationApi();

    /**
     * @return the {@link CommandApi}
     */
    @NotNull CommandApi getCommandApi();

    /**
     * @return a possible local running {@link BasicMiniGame}
     */
    @NotNull SubscribableType<BasicMiniGame> getLocalMiniGame();

    /**
     * @return CacheAPI for use with Caffeine
     */
    @NotNull CacheApi getCacheAPI();

    /**
     * @return The Name this core is assigned to
     */
    @NotNull String getName();

    @NotNull String getVersion();

    @NotNull String getBuildVersion();

    @NotNull Map<String, String> getJavaEnvironment();

    @NotNull Runtime getJavaRuntime();

    @ApiStatus.Internal
    void collectGarbage();

    @NotNull Supplier<Collection<? extends MessageRecipient>> getOnlineRecipientProvider();

    @NotNull Collection<? extends MessageRecipient> supplyOnlineRecipients();

    /*
     *
     * @return if the core is connected and ready for operation
     */
    // boolean isAlive();
}
