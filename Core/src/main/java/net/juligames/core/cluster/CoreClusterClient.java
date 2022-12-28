package net.juligames.core.cluster;

import com.hazelcast.client.Client;
import net.juligames.core.api.cluster.ClusterClient;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.UUID;

/**
 * @author Ture Bentzin
 * 27.12.2022
 */
public record CoreClusterClient(UUID uuid, InetSocketAddress inetSocketAddress, String clientType, String name, Set<String> labels) implements ClusterClient {

    @Contract("_ -> new")
    public static @NotNull CoreClusterClient ofHazelcast(@NotNull Client client) {
        return new CoreClusterClient(client.getUuid(),client.getSocketAddress(),client.getClientType(),client.getName(),client.getLabels());
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public InetSocketAddress getSocketAddress() {
        return inetSocketAddress;
    }

    @Override
    public String getClientType() {
        return clientType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<String> getLabels() {
        return labels;
    }
}
