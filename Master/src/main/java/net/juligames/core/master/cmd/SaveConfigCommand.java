package net.juligames.core.master.cmd;

import de.bentzin.tools.logging.Logger;
import net.juligames.core.Core;
import net.juligames.core.master.CoreMaster;

/**
 * @author Ture Bentzin
 * 27.11.2022
 */
public class SaveConfigCommand extends MasterCommand{

    public SaveConfigCommand() {
        super("svconfig");
    }
    @Override
    public void executeCommand(String commandString) {
        Logger logger = Core.getInstance().getCoreLogger();
        logger.info("storing config:");
        CoreMaster.masterConfigManager().storeAll();
    }
}
