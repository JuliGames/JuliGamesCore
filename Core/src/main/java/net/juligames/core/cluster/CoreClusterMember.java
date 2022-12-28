package net.juligames.core.cluster;

import com.hazelcast.cluster.Member;
import net.juligames.core.api.cluster.ClusterMember;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ture Bentzin
 * 27.12.2022
 */
@ApiStatus.Internal
public record CoreClusterMember(boolean localMember, boolean liteMember,
                                InetSocketAddress inetSocketAddress, UUID uuid, Map<String,String> attributes) implements ClusterMember {

    @Contract("_ -> new")
    public static @NotNull CoreClusterMember ofHazelcast(@NotNull Member member) {
        return new CoreClusterMember(member.localMember(),member.isLiteMember(),member.getSocketAddress(),member.getUuid(),member.getAttributes());
    }

    @Override
    public boolean localMember() {
        return localMember;
    }

    @Override
    public boolean isLiteMember() {
        return liteMember;
    }

    @Override
    public InetSocketAddress getSocketAddress() {
        return inetSocketAddress;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String getAttribute(String key) {
        return getAttributes().get(key);
    }
}
