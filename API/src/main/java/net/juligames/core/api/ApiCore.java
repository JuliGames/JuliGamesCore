package net.juligames.core.api;

import org.checkerframework.checker.optional.qual.MaybePresent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author Ture Bentzin
 * 16.11.2022
 * @apiNote This is for distribution of the API. If you want to get the API then please use {@link API#get()}
 */
public class ApiCore {

    public static API CURRENT_API;
    private static CompletableFuture<API> completableFuture = new CompletableFuture<>();

    @ApiStatus.Internal
    public static void insert(API api) {
        //1. set CURRENT_API
        CURRENT_API = api;
        //2. complete Future
        completableFuture.complete(api);
    }

    @ApiStatus.Internal
    public static void drop() {
        //1. drop CURRENT_API
        CURRENT_API = null;
        //2. uncompleted completableFuture...
        completableFuture = new CompletableFuture<>();
    }

    @Nullable
    public static API getAPI() {
        return CURRENT_API;
    }

    @MaybePresent
    public static Optional<API> optionalApi() {
        return Optional.ofNullable(CURRENT_API);
    }

    public static Future<API> futureApi() {
        return completableFuture;
    }
}
