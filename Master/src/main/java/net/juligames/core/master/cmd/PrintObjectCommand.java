package net.juligames.core.master.cmd;

import com.hazelcast.core.DistributedObject;
import net.juligames.core.Core;

/**
 * @author Ture Bentzin
 * 01.12.2022
 */
public class PrintObjectCommand extends MasterCommand {

    public PrintObjectCommand() {
        super("printObj");
    }

    @Override
    public void executeCommand(String commandString) {
        try {
            if (commandString != null) {
                commandString = commandString.replaceFirst(" ", "");
                String[] s = commandString.split(" ");
                if (s.length != 2) {
                    throw new IllegalArgumentException("\"" + commandString + "\" was not accepted!");
                }
                String service = s[0];
                String hazel = s[1];
                DistributedObject distributedObject = Core.getInstance().getOrThrow()
                        .getDistributedObject(service, hazel);
                Core.getInstance().getCoreLogger().info("Object: " + hazel + "@@" + service + ": " + distributedObject);
            } else {
                Core.getInstance().getCoreLogger().error("command input malformed! - > expect hazel");
            }
        } catch (Exception e) {
            Core.getInstance().getCoreLogger().error("failed to print object: " + e.getMessage());
        }
    }
}
