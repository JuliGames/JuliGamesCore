package net.juligames.core.paper;

import com.hazelcast.core.HazelcastInstance;
import de.bentzin.tools.misc.SubscribableType;
import net.juligames.core.Core;
import net.juligames.core.adventure.AdventureCore;
import net.juligames.core.api.API;
import net.juligames.core.api.jdbi.MessageDAO;
import net.juligames.core.api.minigame.BasicMiniGame;
import net.juligames.core.caching.MessageCaching;
import net.juligames.core.paper.bstats.Metrics;
import net.juligames.core.paper.conversation.ConversationListener;
import net.juligames.core.paper.events.ServerBootFinishedEvent;
import net.juligames.core.paper.minigame.StartCommand;
import net.juligames.core.paper.notification.EventNotificationListener;
import net.juligames.core.paper.perms.PermissionCheckReturn;
import net.juligames.core.paper.plugin.CorePluginLoadManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

/**
 * @author Ture Bentzin
 * 18.11.2022
 */
public class PaperCorePlugin extends JavaPlugin {

    private static PaperCorePlugin plugin;
    private static Metrics metrics;
    private AdventureCore adventureCore;
    private CorePluginLoadManager corePluginLoadManager;

    @Nullable
    public static Metrics getBStatsController() {
        return metrics;
    }

    public static PaperCorePlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        try {
            plugin = this;
            Core core = new Core();
            String serverName = Bukkit.getName() + "@" + ((Bukkit.getServer().getIp().isEmpty()) ? Bukkit.getServer().getIp() + ":" : Bukkit.getServer().getPort());
            getLogger().info("starting core with the following identification: " + serverName);
            core.start("paper-core|" + serverName);
            //start adventureAPI
            getLogger().info("starting adventureCore v." + AdventureCore.API_VERSION);
            adventureCore = new AdventureCore();
            adventureCore.start();
            new PaperConversationManager();
            core.setOnlineRecipientProvider(() -> {
                List<PaperMessageRecipient> paperMessageRecipients = new java.util.ArrayList<>(Bukkit.getOnlinePlayers().stream().map(PaperMessageRecipient::new).toList());
                paperMessageRecipients.add(new PaperMessageRecipient(Bukkit.getConsoleSender()));
                return paperMessageRecipients;
            });

            core.getCommandApi().setCommandHandler(s -> Bukkit.getScheduler().runTask(this, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s)));

            PermissionCheckReturn.registerMessages(API.get().getMessageApi());

            Objects.requireNonNull(getCommand("message")).setExecutor(new MessageCommand());
            Objects.requireNonNull(getCommand("locale")).setExecutor(new LocaleCommand());
            Objects.requireNonNull(getCommand("replacetest")).setExecutor(new ReplaceTestCommand());
            Objects.requireNonNull(getCommand("bctest")).setExecutor(new BCTestCommand());
            Objects.requireNonNull(getCommand("start")).setExecutor(new StartCommand());
            Objects.requireNonNull(getCommand("printMessageCache")).setExecutor(new PrintMessageCacheCommand());

            Bukkit.getPluginManager().registerEvents(new PaperCoreEventListener(), this);
            Bukkit.getPluginManager().registerEvents(new ConversationListener(), this);

            //Register NotificationEvent
            core.getNotificationApi().registerListener(new EventNotificationListener());

            //Try to load miniGame (if present)
            getServer().getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.getPluginManager().callEvent(new ServerBootFinishedEvent(core))); //experimental use of core

            //BStats
            {
                addBStats();
            }

            //CorePlugin
            {
                final File file = Bukkit.getPluginsFolder();
                corePluginLoadManager = new CorePluginLoadManager(file, Bukkit.getServer());
                loadCorePlugins();
            }
        } catch (Exception e) {
            Core core = Core.getInstance();
            core.getCoreLogger().info(e + " -> Client shutdown!");
            killClient(core);
            throw new RuntimeException(e);
        }

    }

    /**
     * This disconnects this Server from Hazelcast and drops the API Service.
     * After execution of this Method, the Core is disabled!
     *
     * @param core the core
     */
    protected void killClient(@NotNull Core core) {
        adventureCore.dropApiService();
        //Try to abort miniGame (if present)
        core.getLocalMiniGame().ifPresent(basicMiniGame -> {
            getLogger().info("aborting MiniGame: " + basicMiniGame.getPlainName());
            basicMiniGame.abort();
        });
        core.stop();
    }

    @Override
    public void onDisable() {
        Core core = Core.getInstance();
        core.getCoreLogger().info("onDisable() -> Client shutdown!");
        killClient(core);
        metrics.shutdown();
    }

    private void addBStats() {
        {

            metrics = new Metrics(this, 17761);
            metrics.addCustomChart(new Metrics.SimplePie("master", () -> Core.getInstance().getHazelDataApi().getMasterInformation().get("master_version")));
            metrics.addCustomChart(new Metrics.SimplePie("api", Core.getInstance()::getVersion));
            metrics.addCustomChart(new Metrics.SimplePie("name", Core::getFullCoreName));
            metrics.addCustomChart(new Metrics.SimplePie("release", Core::getShortRelease));

            metrics.addCustomChart(new Metrics.SingleLineChart("messages", () -> {
                //number of messages in DB
                Integer size = Core.getInstance().getSQLManager().withExtension(MessageDAO.class,
                        extension -> extension.listAllBeans().size());
                Core.getInstance().getCoreLogger().debug("scanned " + size + " messages for BStats");
                return size;
            }));

            metrics.addCustomChart(new Metrics.SimplePie("minigame", () -> {
                SubscribableType<BasicMiniGame> localMiniGame = Core.getInstance().getLocalMiniGame();
                boolean present = localMiniGame.isPresent();
                return present + "";
            }));

            Metrics.DrilldownPie drilldownPie = new Metrics.DrilldownPie("running_minigame", () -> {
                SubscribableType<BasicMiniGame> localMiniGame = Core.getInstance().getLocalMiniGame();
                if (localMiniGame.isPresent()) {
                    BasicMiniGame miniGame = localMiniGame.get();
                    Map<String, Map<String, Integer>> top = new HashMap<>();

                    Map<String, Integer> entry = new HashMap<>();
                    entry.put(miniGame.getVersion(), 1);
                    top.put(miniGame.getPlainName(), entry);

                    return top;
                }
                return null;
            });
            metrics.addCustomChart(drilldownPie);

            metrics.addCustomChart(new Metrics.SingleLineChart("backend_objects", () -> {
                try {
                    HazelcastInstance hazelcastInstance = Core.getInstance().getOrThrow();
                    return hazelcastInstance.getDistributedObjects().size();
                } catch (Exception ignored) {
                    return null;
                }
            }));

            //Caching?
            Metrics.SimplePie pie = new Metrics.SimplePie("caching", () -> String.valueOf(MessageCaching.enabled));
            metrics.addCustomChart(pie);

            //Cache Size
            Metrics.SingleLineChart cacheSize = new Metrics.SingleLineChart("cacheSize", () -> {
                if (MessageCaching.enabled) {
                    long l = MessageCaching.messageCache().estimatedSize();
                    getLogger().info("collecting: cacheSize for BStats");
                    return Math.toIntExact(l);
                }
                return null;
            });
            metrics.addCustomChart(cacheSize);
            metrics.addCustomChart(pie);


            metrics.addCustomChart(new Metrics.SimplePie("minigame_developer", () -> {
                SubscribableType<BasicMiniGame> localMiniGame = Core.getInstance().getLocalMiniGame();
                if (localMiniGame.isPresent()) {
                    return localMiniGame.get().getDeveloperName();
                }
                return null;
            }));
        }
    }


    private void loadCorePlugins() {
        if (Boolean.getBoolean("corePlugins"))
            if (corePluginLoadManager != null) {
                try {
                    corePluginLoadManager.load(); // Experimental
                } catch (Exception e) {
                    getLogger().log(Level.SEVERE, "An issue was recorded while trying to load experimental CorePlugins: " + e.getMessage());
                    e.printStackTrace();
                    getLogger().log(Level.SEVERE, "Please be aware that issues regarding this system will have low priority duo to the unclear" +
                            " situation about the future of this system!");
                }

            } else {
                getLogger().log(Level.SEVERE, "cant load Plugins!");
            }
    }
}
