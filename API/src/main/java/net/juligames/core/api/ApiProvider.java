package net.juligames.core.api;

import de.bentzin.tools.misc.SubscribableType;
import org.checkerframework.checker.optional.qual.MaybePresent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author Ture Bentzin
 * 16.11.2022
 * @apiNote This is for distribution of the API. If you want to get the API then please use {@link API#get()}
 */
@ApiStatus.Internal
public class ApiProvider {

    public static @Nullable API CURRENT_API;
    private static @NotNull CompletableFuture<API> completableFuture = new CompletableFuture<>();
    private static final @NotNull SubscribableType<API> subscribableType = new SubscribableType<>();

    @ApiStatus.Internal
    public static void insert(@Nullable API api) {
        //1. set CURRENT_API
        CURRENT_API = api;
        //2. complete Future
        completableFuture.complete(api);
        updateType();
    }

    @ApiStatus.Internal
    public static void drop() {
        //1. drop CURRENT_API
        CURRENT_API = null;
        //2. uncompleted completableFuture...
        completableFuture = new CompletableFuture<>();
        updateType();
    }

    protected static void updateType() {
        subscribableType.set(CURRENT_API);
    }

    @Nullable
    public static API getAPI() {
        return CURRENT_API;
    }

    @MaybePresent
    public static @NotNull Optional<API> optionalApi() {
        return Optional.ofNullable(CURRENT_API);
    }

    public static @NotNull Future<API> futureApi() {
        return completableFuture;
    }

    @ApiStatus.Experimental
    @ApiStatus.Internal
    protected @NotNull SubscribableType<API> typeApi() {
        return subscribableType;
    }
}
