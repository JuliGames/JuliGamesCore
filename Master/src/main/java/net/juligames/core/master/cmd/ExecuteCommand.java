package net.juligames.core.master.cmd;

import net.juligames.core.Core;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Ture Bentzin
 * 05.12.2022
 */
//execute <uuid>/@all <command...>
public class ExecuteCommand extends MasterCommand{
    public ExecuteCommand() {
        super("execute");
    }

    @Override
    public void executeCommand(@NotNull String commandString) {
        commandString = commandString.replaceFirst(" ", "");
        String[] s = commandString.split(" ");
        String command = commandString.substring(commandString.indexOf(" ")).replaceFirst(" ", "");
        if(s.length >= 2) {
            String target = s[0];
            Core.getInstance().getCoreLogger().warning("trying to execute \"" + command + "\" on \"" + target + "\"");
            if(target.equalsIgnoreCase("@all")) {
                Core.getInstance().getCoreLogger().warning("@all is unsafe to use!");
                Core.getInstance().getCommandApi().broadcastCommand(command);
            }
            else {
                Core.getInstance().getCommandApi().sendCommand(command, UUID.fromString(target));
            }

        }else {
            Core.getInstance().getCoreLogger().error("execute <uuid>/@all <command...>");
        }
    }
}
