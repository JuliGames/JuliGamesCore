package net.juligames.core.master.cmd;

import com.hazelcast.core.DistributedObject;
import de.bentzin.tools.logging.Logger;
import net.juligames.core.Core;

/**
 * @author Ture Bentzin
 * 27.11.2022
 */
public class ListObjectsCommand extends MasterCommand {
    public ListObjectsCommand() {
        super("listobjects");
    }

    @Override
    public void executeCommand(String commandString) {
        Logger coreLogger = Core.getInstance().getCoreLogger();
        for (DistributedObject distributedObject : Core.getInstance().getOrThrow().getDistributedObjects()) {
            coreLogger.info(distributedObject.toString());
        }
    }
}
