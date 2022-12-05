package net.juligames.core.hcast;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.SqlConfig;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class HCastConfigProvider {

    public static final String CLUSTER_NAME = "Core Cluster";

    private HCastConfigProvider() {
    }

    public static @NotNull ClientConfig provide(String clientName) {
        ClientConfig config = ClientConfig.load();
        config.setInstanceName(clientName);
        config.setClusterName(CLUSTER_NAME);
        return config;
    }


    @ApiStatus.Internal
    public static @NotNull Config provideMember(String memberName) {
        Config config = Config.load();
        config.setInstanceName(memberName);
        config.setClusterName(CLUSTER_NAME);
        return config;
    }
}
