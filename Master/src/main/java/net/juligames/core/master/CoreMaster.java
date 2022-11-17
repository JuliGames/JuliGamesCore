package net.juligames.core.master;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import de.bentzin.tools.logging.Logger;
import de.bentzin.tools.register.Registerator;
import net.juligames.core.Core;
import net.juligames.core.api.jdbi.LocaleDAO;
import net.juligames.core.api.notification.SimpleNotification;
import net.juligames.core.hcast.HCastConfigProvider;
import net.juligames.core.master.cmd.MasterCommand;
import net.juligames.core.master.cmd.MasterCommandRunner;
import net.juligames.core.master.data.MasterHazelInformationProvider;
import net.juligames.core.master.logging.MasterLogger;
import net.juligames.core.master.sql.MasterSQLManager;
import net.juligames.core.api.jdbi.SQLManager;
import net.juligames.core.notification.TopicNotificationCore;
import org.checkerframework.checker.units.qual.C;

import java.util.concurrent.ExecutionException;

/**
 * @author Ture Bentzin
 * 15.11.2022
 */
public class CoreMaster {

    private static MasterHazelInformationProvider masterHazelInformationProvider;
    private static HazelcastInstance hazelcast;

    private CoreMaster() {}

    public static Logger logger;
    private static SQLManager SQLManager;
    private static MasterCommandRunner masterCommandRunner;

    public static void main(String[] args) {

        logger = new MasterLogger("Master", java.util.logging.Logger.getLogger(Core.getShortRelease()));

        //entry point for Master
        masterCommandRunner = new MasterCommandRunner(logger);

        logger.info("welcome to JuliGames-Core Master");
        logger.warning("This is an early development build!");
        logger.info("booting hazelcast (MEMBERCORE):");
        Core core = new Core();
        core.start("Master",logger,true);

        try {
            hazelcast = core.getOrWait();
        } catch (ExecutionException | InterruptedException e) {
            logger.error("FAILED TO SETUP HAZELCAST - Master will possibly start anyway but the master should be restarted as soon as possible");
        }

        logger.info("hazelcast boot was initiated");

        logger.debug("sql: start");
        SQLManager = new MasterSQLManager("jdbc:mysql://admin@localhost:3306/minecraft");
        SQLManager.createTables();

        SQLManager.getJdbi().withExtension(LocaleDAO.class, extension -> {
            extension.listAll();
            return null;
        });

        //Data
        masterHazelInformationProvider = new MasterHazelInformationProvider(hazelcast);
        logger.warning("not all code execution on master is stable because the master DOES NOT PROVIDE a usable core!!!");
        try {
            registerCommands();
        } catch (Registerator.DuplicateEntryException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        masterCommandRunner.run();
    }

    public static SQLManager getMasterSQLManager() {
        return SQLManager;
    }

    public static void registerCommands() throws Registerator.DuplicateEntryException {
        masterCommandRunner.register(new MasterCommand("bc-all") {
            @Override
            public void executeCommand(String commandString) {
                logger.info("send message to all: " + commandString);

               Core.getInstance().getNotificationApi().getNotificationSender().broadcastNotification("bc-all",commandString);
            }
        });
    }
}
