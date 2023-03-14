package net.juligames.core.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.bentzin.tools.logging.JavaLogger;
import de.bentzin.tools.logging.Logger;
import net.juligames.core.Core;
import net.juligames.core.adventure.AdventureCore;
import net.juligames.core.adventure.api.AudienceMessageRecipient;
import net.juligames.core.api.TODO;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

/**
 * @author Ture Bentzin
 * 03.12.2022
 */

@Plugin(id = "velocitycore", name = "JuliGames Velocity Core", version = "1.5",
        url = "https://github.com/JuliGames/JuliGamesCore", description = "Velocity Client for the core - necessary to provide the API here",
        authors = {"Ture Bentzin"})
public final class VelocityCore {

    private final ProxyServer server;
    private final Logger logger;
    private final Core core;
    private AdventureCore adventureCore;

    @Inject
    public VelocityCore(ProxyServer server, java.util.logging.Logger logger) {
        this.server = server;
        this.logger = new JavaLogger("velocityCore", logger);
        logger.info("Core is starting up...");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Driver issue - please check installation!");
            logger.log(Level.SEVERE, "Core will not boot. All Core Features will be disabled automatically!");
            core = null;
            return;
        }

        core = new Core();
        logger.info("Core is waiting for \"go\" from velocity to boot up");
    }

    @SuppressWarnings("ProtectedMemberInFinalClass")
    protected AdventureCore getAdventureCore() {
        return adventureCore;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("received \"go\" from velocity - starting core!");
        logger.info(" -- handing over logging to different instance! --"); //core will create own logger - no checking possible for this logger
        core.start(assembleName());
        logger.info("core seems to be running... booting AdventureCore");
        adventureCore = new AdventureCore();
        adventureCore.start();

        core.setOnlineRecipientProvider(() -> {
            Collection<AudienceMessageRecipient> recipients = new ArrayList<>();
            //1. players
            server.getAllPlayers().forEach(player -> recipients.add(new VelocityPlayerMessageRecipient(player)));
            //2. servers
            server.getAllServers().forEach(server1 -> recipients.add(new ServerMessageRecipient(server1)));
            //3. ProxyServer
            recipients.add(new ProxyServerMessageRecipient(server));

            return recipients;
        });

        core.getCommandApi().setCommandHandler(s -> server.getCommandManager().executeAsync(server.getConsoleCommandSource(), s));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        logger.info("velocity shutting down...");
        logger.info("disabling AdventureAPI...");
        adventureCore.dropApiService();
        logger.info("disabled AdventureAPI");
        logger.info("stopping core...");
        Core.getInstance().stop();
        logger.info("core was shut down!");
    }


    //unfinished
    @TODO(doNotcall = false)
    private @NotNull String assembleName() {
        return "velocityCore|" + server.getVersion().getName();
    }

}
