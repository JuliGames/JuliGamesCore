package net.juligames.core.master;

import com.hazelcast.client.Client;
import com.hazelcast.client.ClientListener;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.internal.server.tcp.ChannelInitializerFunction;
import net.juligames.core.hcast.HCastConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author Ture Bentzin
 * 15.11.2022
 */
public class CoreMaster {

    public static final Config CONFIG = new Config();
    public static final Logger logger = LoggerFactory.getLogger(CoreMaster.class);

    public static void main(String[] args) {
        //entry point for Master
        CONFIG.setClusterName(HCastConfigProvider.CLUSTER_NAME);
        CONFIG.setInstanceName("Master");
        CONFIG.getJetConfig().setEnabled(true);

        logger.info("welcome to JuliGames-Core Master");
        logger.warn("This is an early development build!");
        logger.info("booting hazelcast:");
        HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance(CONFIG);
        logger.info("hazelcast boot was initiated");
    }
}
