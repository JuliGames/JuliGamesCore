package net.juligames.core.api.config.mapbacked;

import net.juligames.core.api.API;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

/**
 * This {@link MapFeedInterpreter} uses {@link net.juligames.core.api.data.HazelDataApi#getMap(String)} get supply the map
 * @author Ture Bentzin
 * 07.03.2023
 */
public final class HazelFeedMapInterpreter<E> extends MapFeedInterpreter<E>{

    /**
     *
     * @deprecated This serves as an example and will be removed after testing is completed
     */
    @Deprecated(forRemoval = true)
    @Contract(" -> new")
    @ApiStatus.Experimental
    public static @NotNull HazelFeedMapInterpreter<String> master_information() {
        return new HazelFeedMapInterpreter<>(() -> API.get().getHazelDataApi().getMasterInformation());
    }

    public static <E> @NotNull Map<String,E> getStringEHazelMap(String hazel) {
        return API.get().getHazelDataApi().getMap(hazel);
    }


    public HazelFeedMapInterpreter(@NotNull String hazel) {
        super(() -> getStringEHazelMap(hazel));
    }

    private HazelFeedMapInterpreter(Supplier<Map<String,E>> supplier) {
        super(supplier);
    }


}
