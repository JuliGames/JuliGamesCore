package net.juligames.core.master.cmd;

import com.hazelcast.core.DistributedObject;
import net.juligames.core.Core;

/**
 * @author Ture Bentzin
 * 01.12.2022
 */
public class PrintObjectCommand extends MasterCommand{

    public PrintObjectCommand(){
        super("printObj");
    }

    @Override
    public void executeCommand(String commandString) {
        try {
            if (commandString != null) {
                int cut = commandString.indexOf(" ");
                String service = commandString.substring(0, cut);
                String hazel = commandString.substring(cut);
                DistributedObject distributedObject = Core.getInstance().getOrThrow()
                        .getDistributedObject(service, hazel);
                Core.getInstance().getCoreLogger().info("Object: " + hazel + "@@" + service + ": " + distributedObject);
            } else {
                Core.getInstance().getCoreLogger().error("command input malformed! - > expect hazel");
            }
        }catch (Exception e){
            Core.getInstance().getCoreLogger().error("failed to print object: " + e.getMessage());
        }
    }
}
