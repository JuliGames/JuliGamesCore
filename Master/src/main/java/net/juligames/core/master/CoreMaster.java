package net.juligames.core.master;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import de.bentzin.tools.logging.Logger;
import net.juligames.core.Core;
import net.juligames.core.api.jdbi.LocaleDAO;
import net.juligames.core.hcast.HCastConfigProvider;
import net.juligames.core.master.data.MasterHazelInformationProvider;
import net.juligames.core.master.logging.MasterLogger;
import net.juligames.core.master.sql.MasterSQLManager;
import net.juligames.core.api.jdbi.SQLManager;

/**
 * @author Ture Bentzin
 * 15.11.2022
 */
public class CoreMaster {

    private static MasterHazelInformationProvider masterHazelInformationProvider;

    private CoreMaster() {}

    public static final Config CONFIG = new Config();
    public static Logger logger;
    private static SQLManager SQLManager;

    public static void main(String[] args) {

        logger  = new MasterLogger("Master", java.util.logging.Logger.getLogger(Core.getShortRelease()));

        //entry point for Master
        CONFIG.setClusterName(HCastConfigProvider.CLUSTER_NAME);
        CONFIG.setInstanceName("Master");
        CONFIG.getJetConfig().setEnabled(true);

        logger.info("welcome to JuliGames-Core Master");
        logger.warning("This is an early development build!");
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
