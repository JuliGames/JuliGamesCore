package net.juligames.core.api;

import com.google.errorprone.annotations.DoNotCall;
import org.checkerframework.checker.optional.qual.MaybePresent;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @apiNote This is for distribution of the API
 * @author Ture Bentzin
 * 16.11.2022
 */
public class ApiCore {

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
        //2. uncomplete completableFuture...
        completableFuture = new CompletableFuture<>();
    }

    public static API CURRENT_API;

    @Nullable
    public static API getAPI() {
        return CURRENT_API;
    }

    @MaybePresent
    public static Optional<API> optionalApi() {
        return Optional.ofNullable(CURRENT_API);
    }

    private static CompletableFuture<API> completableFuture = new CompletableFuture<>();

    public static Future<API> futureApi() {
        return completableFuture;
    }
}
