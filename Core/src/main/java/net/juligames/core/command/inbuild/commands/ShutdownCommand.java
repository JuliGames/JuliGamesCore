package net.juligames.core.command.inbuild.commands;

import net.juligames.core.api.API;
import net.juligames.core.command.inbuild.AbstractInbuiltCommand;
import net.juligames.core.command.inbuild.InbuiltCommand;

/**
 * @author Ture Bentzin
 * 17.01.2023
 */

public class ShutdownCommand extends AbstractInbuiltCommand {
    public ShutdownCommand() {
        super("shutdown", "Shut the current Java VM down!");
    }

    @Override
    public boolean execute(InbuiltCommand command, String input) {
        getLogger().warning("Core was commanded to terminate this JavaVM!");
        API.get().getJavaRuntime().exit(0); //0 to fire hooks
        return true; // executes never...
    }
}
