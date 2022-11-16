package net.juligames.core.hcast;

import com.hazelcast.client.config.ClientConfig;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class HCastConfigProvider {

    public static final String CLUSTER_NAME = "Core Cluster";

    private HCastConfigProvider() {
    }

    public static ClientConfig provide(String memberName) {
        ClientConfig config = new ClientConfig();
        config.setInstanceName(memberName);
        config.setClusterName(CLUSTER_NAME);
        return config;
    }
}
