package net.juligames.core.master.cmd;

import net.juligames.core.Core;

/**
 * @author Ture Bentzin
 * 17.11.2022
 */
public class HelpCommand extends MasterCommand{
    private final MasterCommandRunner runner;

    public HelpCommand(MasterCommandRunner runner) {
        super("help");
        this.runner = runner;
    }

    @Override
    public void executeCommand(String commandString) {
        Core.getInstance().getCoreLogger().info("List of MasterCommands:");
        for (MasterCommand masterCommand : runner) {
            Core.getInstance().getCoreLogger().info("-> " + masterCommand.getCommandName());
        }
    }
}
