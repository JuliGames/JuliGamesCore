package net.juligames.core.master;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import net.juligames.core.api.jdbi.LocaleDAO;
import net.juligames.core.hcast.HCastConfigProvider;
import net.juligames.core.master.data.MasterHazelInformationProvider;
import net.juligames.core.master.sql.MasterSQLManager;
import net.juligames.core.api.jdbi.SQLManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ture Bentzin
 * 15.11.2022
 */
public class CoreMaster {

    private static MasterHazelInformationProvider masterHazelInformationProvider;

    private CoreMaster() {}

    public static final Config CONFIG = new Config();
    public static final Logger logger = LoggerFactory.getLogger(CoreMaster.class);
    private static SQLManager SQLManager;

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

        logger.debug("sql: start");
        SQLManager = new MasterSQLManager("jdbc:mysql://admin@localhost:3306/minecraft");
        SQLManager.createTables();

        SQLManager.getJdbi().withExtension(LocaleDAO.class,extension -> {
            extension.listAll();
            return null;
        });

        //Data
        masterHazelInformationProvider = new MasterHazelInformationProvider(hazelcast);

    }

    public static SQLManager getMasterSQLManager() {
        return SQLManager;
    }
}
