package net.juligames.core.command.inbuild;

import de.bentzin.tools.logging.Logger;
import de.bentzin.tools.register.Registerator;
import net.juligames.core.Core;
import net.juligames.core.command.inbuild.commands.ShutdownCommand;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Ture Bentzin
 * 17.02.2023
 */
@ApiStatus.Experimental
public class InbuiltCommandManager extends Registerator<InbuiltCommand> {

    static Logger staticLogger;
    final static String PREFIX = "inbuild:";
    private static final String REGEX =  PREFIX + "([A-Za-z])";

    public InbuiltCommandManager() {
        staticLogger = Core.getInstance().getCoreLogger().adopt("inbuiltCmd");
        try {
            registerInternalCommands();
        } catch (Exception e) {
            staticLogger.error("Error while processing internal Commands: " + e.getMessage());
        }
    }

    private void registerInternalCommands() throws DuplicateEntryException {
        register(new ShutdownCommand());
    }

    public boolean handle(@NotNull String command) {
        if(check(command)) {
            return onCommand(command.replaceFirst(PREFIX,""));
        }
        return false;
    }


    protected boolean onCommand(String command) {
        for (InbuiltCommand index : getIndex()) {
            if(index.acceptsWithCurrent(command)) {
                return index.execute(index,command);
            }
        }
        return false;
    }

    //https://rb.gy/vuzh4z
    public static boolean check(final String input) {
        final Pattern pattern = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

}
