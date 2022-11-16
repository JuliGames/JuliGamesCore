package net.juligames.core.hcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import java.util.concurrent.CompletableFuture;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class HazelConnector {
    private final String clientName;

    private final CompletableFuture<HazelcastInstance> instance = new CompletableFuture<>();

    public static HazelConnector getInstance(String name){
        return new HazelConnector(name);
    }

    public static HazelConnector getInstanceAndCo(String name){
        return new HazelConnector(name).connect();
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

    public CompletableFuture<HazelcastInstance> getInstance() {
        return instance;
    }
}
