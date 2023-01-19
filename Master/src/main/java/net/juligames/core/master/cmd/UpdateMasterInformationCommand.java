package net.juligames.core.master.cmd;

import net.juligames.core.Core;
import net.juligames.core.master.CoreMaster;

/**
 * @author Ture Bentzin
 * 17.01.2023
 */
public class UpdateMasterInformationCommand extends MasterCommand{

    public UpdateMasterInformationCommand() {
        super("updateInfo");
    }

    @Override
    public void executeCommand(String commandString) {
        CoreMaster.getMasterHazelInformationProvider().update();
        Core.getInstance().getCoreLogger().info("updated master_information");
    }
}
