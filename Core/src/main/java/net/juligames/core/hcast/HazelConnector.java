package net.juligames.core.hcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.checkerframework.checker.optional.qual.MaybePresent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class HazelConnector {
    private final @NotNull String clientName;

    private final @NotNull CompletableFuture<HazelcastInstance> instance = new CompletableFuture<>();

    private HazelConnector(@NotNull String clientName) {
        this.clientName = clientName;
    }

    @Contract("_ -> new")
    public static @NotNull HazelConnector getInstance(@NotNull String name) {
        return new HazelConnector(name);
    }

    public static @NotNull HazelConnector getInstanceAndConnect(@NotNull String name) {
        return new HazelConnector(name).connect();
    }

    @ApiStatus.Internal
    public static @NotNull HazelConnector getInstanceAndConnectAsMember(@NotNull String name) {
        return new HazelConnector(name).connectMember();
    }

    public @NotNull HazelConnector connect() {
        ClientConfig clientConfig = HCastConfigProvider.provide(clientName);
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        instance.complete(hazelcastInstance);
        return this;
    }

    @ApiStatus.Internal
    public @NotNull HazelConnector connectMember() {
        Config config = HCastConfigProvider.provideMember(clientName);
        config.getJetConfig().setEnabled(true);
        HazelcastInstance hazelcastInstance = Hazelcast.getOrCreateHazelcastInstance(config);
        instance.complete(hazelcastInstance);
        return this;
    }

    public void disconnect() {
        try {
            instance.get().shutdown();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method acts like {@link #disconnect()} but cant throw an {@link Exception} and does not wait for population of
     * the {@link #instance} field!
     * @return an Optional that might contain an {@link Exception} that was thrown while trying to kill hazelcast
     */
    @ApiStatus.Internal
    @MaybePresent
    public @NotNull Optional<Exception> kill() {
        try {
            Objects.requireNonNull(instance.getNow(null), "cant kill instance! Your cluster might be bricked now...")
                    .shutdown();
            return Optional.empty();
        }catch (Exception e) {
            e.printStackTrace(); //print but do not propagate!
            return Optional.of(e);
        }
    }

    @ApiStatus.Internal
    @Nullable
    public HazelcastInstance getForce() {
        return getInstance().getNow(null);
    }

    public @NotNull CompletableFuture<HazelcastInstance> getInstance() {
        return instance;
    }

    /**
     * Get the configured {@link #clientName}.
     * This represents a memberName if this {@link HazelConnector} is used for members (masters) or a real clientName if it is
     * used for creating members
     * @return the clientName
     */
    protected @NotNull String getClientName() {
        return clientName;
    }
}
