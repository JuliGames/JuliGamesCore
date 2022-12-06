package net.juligames.core.master.cmd;

import de.bentzin.tools.logging.Logger;
import net.juligames.core.Core;
import net.juligames.core.master.CoreMaster;

/**
 * @author Ture Bentzin
 * 27.11.2022
 */
public class ReloadConfigCommand extends MasterCommand {

    public ReloadConfigCommand() {
        super("rlconfig");
    }

    @Override
    public void executeCommand(String commandString) {
        Logger logger = Core.getInstance().getCoreLogger();
        logger.info("reloading config:");
        CoreMaster.masterConfigManager().load();
    }
}
