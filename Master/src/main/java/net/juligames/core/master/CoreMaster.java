package net.juligames.core.master;

import com.hazelcast.core.HazelcastInstance;
import de.bentzin.tools.logging.Logger;
import de.bentzin.tools.register.Registerator;
import net.juligames.core.Core;
import net.juligames.core.api.jdbi.LocaleDAO;
import net.juligames.core.api.jdbi.SQLManager;
import net.juligames.core.jdbi.CoreSQLManager;
import net.juligames.core.master.cmd.*;
import net.juligames.core.master.config.MasterConfigManager;
import net.juligames.core.master.data.MasterHazelInformationProvider;
import net.juligames.core.master.logging.MasterLogger;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Ture Bentzin
 * 15.11.2022
 */
public class CoreMaster {

    private static final CompletableFuture<Long> bootMillis = new CompletableFuture<>();
    private static MasterHazelInformationProvider masterHazelInformationProvider;
    private static HazelcastInstance hazelcast;
    private static Logger logger;
    private static CoreSQLManager SQLManager;
    private static MasterCommandRunner masterCommandRunner;
    private static MasterConfigManager masterConfigManager;

    private CoreMaster() {
    }

    public static void main(String[] args) {

        //Setup
        MasterLogger.setupJavaLogging();

        logger = new MasterLogger("Master", java.util.logging.Logger.getLogger(Core.getShortRelease()));

        //entry point for Master
        masterCommandRunner = new MasterCommandRunner(logger);
        masterConfigManager = new MasterConfigManager();

        logger.info("Welcome to " + Core.getFullCoreName() + " Master by Ture Bentzin <bentzin@tdrstudios.de>");


        logger.info("Preparing directory...");
        masterConfigManager.createDatabaseConfiguration();

        logger.info("Booting hazelcast (MEMBER CORE):");
        Core core = new Core();
        try {
            core.getHazelcastPostPreparationWorkers().register(hazelcastInstance -> {
                logger.info("Loading config");
                masterConfigManager.load();
            });
        } catch (Registerator.DuplicateEntryException ignored) {
        }
        bootMillis.complete(System.currentTimeMillis()); // set boot date - published via master_information

        //start Core
        core.start("Master", logger, true);

        try {
            hazelcast = core.getOrWait();
        } catch (ExecutionException | InterruptedException e) {
            logger.error("FAILED TO SETUP HAZELCAST - Master will probably start anyway but the master should be restarted as soon as possible");
        }

        logger.info("Hazelcast boot was completed - advice: hazelcast could potentially fail to boot for a variety of reasons, so if you should see" +
                "an error above then you might want to restart the master. In the case that the Clients are put on hold by the core you should also" +
                "consider restarting.");

        logger.debug("SQL: start");
        SQLManager = Core.getInstance().getSQLManager();

        SQLManager.createTables(); //MASTER CREATES TABLES (NOT THE CORE!!!)
        SQLManager.getJdbi().withExtension(LocaleDAO.class, extension -> {
            extension.insert(CoreSQLManager.defaultEnglish()); //adding english
            return null;
        });


        //Data
        masterHazelInformationProvider = new MasterHazelInformationProvider(hazelcast);
        logger.warning("Not all code execution on master is stable because the master DOES NOT PROVIDE a fully usable core!!!");
        try {
            registerCommands();
        } catch (Registerator.DuplicateEntryException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        //CommandSystem
        logger.info("Registering CommandHandler...");
        core.getCommandApi().setCommandHandler(new CommandHandler());
        logger.info("Master is now ready to receive commands from hazelcast");

        //HOOK

        Core.getInstance().getJavaRuntime().addShutdownHook(new Thread(() -> {
            try {
                logger.info("Master is going down!!");
            } catch (Exception ignored) {
                System.out.println("Seems like everything is cursed right now. Please report this!");
            }
            masterConfigManager.storeAll();
        }));

        //LAST!!!
        masterCommandRunner.run();

    }

    public static SQLManager getMasterSQLManager() {
        return SQLManager;
    }

    public static Optional<Long> getBootMillis() {
        return Optional.ofNullable(bootMillis.getNow(null));
    }

    public static void registerCommands() throws Registerator.DuplicateEntryException {
        masterCommandRunner.register(new MasterCommand("bc-all") {
            @Override
            public void executeCommand(String commandString) {
                logger.info("send message to all: " + commandString);

                Core.getInstance().getNotificationApi().getNotificationSender().broadcastNotification("bc-all", commandString);
            }
        });
        masterCommandRunner.register(new ListObjectsCommand());
        masterCommandRunner.register(new ReloadConfigCommand());
        masterCommandRunner.register(new SaveConfigCommand());
        masterCommandRunner.register(new PrintObjectCommand());
        masterCommandRunner.register(new ExecuteCommand());
        masterCommandRunner.register(new ListCommand());
        masterCommandRunner.register(new PrintMapCommand());
        masterCommandRunner.register(new UpdateMasterInformationCommand());
    }

    public static MasterConfigManager masterConfigManager() {
        return masterConfigManager;
    }

    public static HazelcastInstance getHazelcast() {
        return hazelcast;
    }

    public static MasterCommandRunner getMasterCommandRunner() {
        return masterCommandRunner;
    }

    public static MasterHazelInformationProvider getMasterHazelInformationProvider() {
        return masterHazelInformationProvider;
    }
}
