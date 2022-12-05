package net.juligames.core.master.cmd;

import net.juligames.core.master.CoreMaster;

import java.util.function.Consumer;

/**
 * @author Ture Bentzin
 * 05.12.2022
 */
// Oh, wow that class was minimized a lot… I promise one it was way bigger and had a sence
public class CommandHandler implements Consumer<String> {

    @Override
    public void accept(String s) {
        CoreMaster.getMasterCommandRunner().handleCommand(s);
    }
}
