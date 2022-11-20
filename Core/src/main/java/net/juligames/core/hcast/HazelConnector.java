package net.juligames.core.hcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class HazelConnector {
    private final String clientName;

    private final CompletableFuture<HazelcastInstance> instance = new CompletableFuture<>();

    @Contract("_ -> new")
    public static @NotNull HazelConnector getInstance(String name){
        return new HazelConnector(name);
    }

    public static HazelConnector getInstanceAndConnect(String name){
        return new HazelConnector(name).connect();
    }

    @ApiStatus.Internal
    public static HazelConnector getInstanceAndConnectAsMember(String name) {
        return new HazelConnector(name).connectMember();
    }


    private HazelConnector(String memberName){
        this.clientName = memberName;
    }

    public HazelConnector connect(){
        ClientConfig clientConfig = HCastConfigProvider.provide(clientName);
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        instance.complete(hazelcastInstance);
        return this;
    }

    @ApiStatus.Internal
    public HazelConnector connectMember(){
        Config config = HCastConfigProvider.provideMember(clientName);
        config.getJetConfig().setEnabled(true);
        HazelcastInstance hazelcastInstance = Hazelcast.getOrCreateHazelcastInstance(config);
        instance.complete(hazelcastInstance);
        return this;
    }

    public void disconnect(){
        try {
            instance.get().shutdown();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiStatus.Internal
    public HazelcastInstance getForce() {
        return getInstance().getNow(null);
    }

    public CompletableFuture<HazelcastInstance> getInstance() {
        return instance;
    }
}
