package net.juligames.core.master.cmd;

/**
 * @author Ture Bentzin
 * 17.11.2022
 */
public abstract class MasterCommand {
    private final String commandName;

    public MasterCommand(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

    public abstract void executeCommand(String commandString);

}
