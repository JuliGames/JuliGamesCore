package net.juligames.core.api.data;

import de.bentzin.tools.Hardcode;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
@SuppressWarnings("unused")
public interface HazelDataApi {

    /**
     * @param hazel the name of the map in the cluster
     * @param <A>   key
     * @param <B>   value
     * @return the {@link Map} from hazelcast
     */
    @NotNull <A, B> Map<A, B> getMap(@NotNull String hazel);

    /**
     * @param hazel the name of the queue in the cluster
     * @param <T>   Type Parameter for the {@link Queue}
     * @return the {@link Queue} from hazelcast
     */
    @NotNull <T> Queue<T> getQueue(@NotNull String hazel);

    /**
     * @param hazel the name of the set in the cluster
     * @param <T>   Type Parameter for the {@link Set}
     * @return the {@link Set} from hazelcast
     */
    @NotNull <T> Set<T> getSet(@NotNull String hazel);

    /**
     * @param hazel the name of the list in the cluster
     * @param <E>   Type Parameter for the {@link List}
     * @return the {@link List} from hazelcast
     */
    @NotNull <E> List<E> getList(@NotNull String hazel);

    /**
     * @return a {@link Map} with general core information provided by the master.
     * @apiNote For further details of this map look at the wiki
     */
    @Hardcode
    @NotNull
    default Map<String, String> getMasterInformation() {
        //API.get().getHazelDataApi().<String, String>getMap("master_information").get("default_locale")
        return getMap("master_information");
    }

    /**
     * This method trys to guess if {@link #getMasterInformation()} will provide a populated map.
     * It provides no information about the last update
     */
    @ApiStatus.AvailableSince("1.6")
    boolean isMasterInformationAvailable();


}
