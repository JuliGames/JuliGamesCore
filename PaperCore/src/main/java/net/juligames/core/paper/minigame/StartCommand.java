package net.juligames.core.paper.minigame;

import net.juligames.core.api.API;
import net.juligames.core.api.minigame.BasicMiniGame;
import net.juligames.core.paper.PaperMessageRecipient;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * 23.12.2022
 */
public class StartCommand implements CommandExecutor {

    public StartCommand() {
        API.get().getMessageApi().registerMessage("internal.paper.internal.cmd.start.attempt",
                "<green>Starting: <yellow>{0}</yellow>!");
        API.get().getMessageApi().registerMessage("internal.paper.internal.cmd.start.success",
                "<green>Started: <yellow>{0}</yellow>!");
        API.get().getMessageApi().registerMessage("internal.paper.internal.cmd.start.notfound",
                "<red>Seems like there is no MiniGame installed on this core!");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //This will be properly developed later
        if (sender.isOp()) { //temporary check
            @Nullable final BasicMiniGame miniGame = API.get().getLocalMiniGame().getOr(null);
            if (miniGame != null) {
                String name = miniGame.getDescription();
                API.get().getMessageApi().
                        sendMessage("internal.paper.internal.cmd.start.attempt", new PaperMessageRecipient(sender), new String[]{name});
                miniGame.start();
                API.get().getMessageApi().
                        sendMessage("internal.paper.internal.cmd.start.success", new PaperMessageRecipient(sender), new String[]{name});
            } else {
                API.get().getMessageApi().
                        sendMessage("internal.paper.internal.cmd.start.notfound", new PaperMessageRecipient(sender));
            }
        }
        return true;
    }
}
