package net.juligames.core.api;

import de.bentzin.tools.logging.Logger;
import net.juligames.core.api.cluster.ClusterApi;
import net.juligames.core.api.command.CommandApi;
import net.juligames.core.api.config.ConfigurationAPI;
import net.juligames.core.api.data.HazelDataApi;
import net.juligames.core.api.err.APIException;
import net.juligames.core.api.jdbi.SQLManager;
import net.juligames.core.api.message.MessageApi;
import net.juligames.core.api.message.MessageRecipient;
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
     * This is the primary way for getting your API Instance. WIth this Instance you have access to all build in features in the core
     * that are explicitly exposed to the API. If the API should not be provided by a core then this will throw a {@link APIException}
     * to indicate that a Core is necessary to get this Instance. For further Information on how to use the {@link API} please check out
     * the GitHub Wiki. For further information on this method check out {@link ApiCore}
     *
     * @return your API Instance
     * @throws APIException if no API is present
     */
    static @NotNull API get() throws APIException {
        @MaybePresent Optional<API> api = ApiCore.optionalApi();
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
    @NotNull Optional<? extends MessageRecipient> findRecipient(Predicate<MessageRecipient> searchQuery);

    @MaybePresent
    @NotNull Optional<? extends MessageRecipient> findRecipientByName(String name);

    /**
     * @return the DataAPI
     */
    HazelDataApi getHazelDataApi();

    /**
     * @return The NotificationApi for this core
     */
    NotificationApi getNotificationApi();

    /**
     * @return The ClusterApi for this core
     */
    ClusterApi getClusterApi();

    /**
     * @return Logger for use when accessing via API
     */
    Logger getAPILogger();

    /**
     * @return SQL Manager to get JDBI
     */
    SQLManager getSQLManager();

    /**
     * @return The MessageAPI used to send Messages via core to players
     */
    MessageApi getMessageApi();

    /**
     * @return the {@link ConfigurationAPI}
     */
    ConfigurationAPI getConfigurationApi();

    /**
     * @return the {@link CommandApi}
     */
    CommandApi getCommandApi();

    /**
     * @return The Name this core is assigned to
     */
    String getName();

    String getVersion();

    String getBuildVersion();

    Map<String, String> getJavaEnvironment();

    Runtime getJavaRuntime();

    @ApiStatus.Internal
    void collectGarbage();

    Supplier<Collection<? extends MessageRecipient>> getOnlineRecipientProvider();

    Collection<? extends MessageRecipient> supplyOnlineRecipients();

    /*
     *
     * @return if the core is connected and ready for operation
     */
    // boolean isAlive();


}
