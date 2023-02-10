package net.juligames.core.master.cmd;

import de.bentzin.tools.logging.Logger;
import de.bentzin.tools.register.Registerator;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Ture Bentzin
 * 17.11.2022
 */
public class MasterCommandRunner extends Registerator<MasterCommand> {

    public final Logger cmd;
    private final InputStream stream;

    public MasterCommandRunner(@NotNull Logger parentLogger, InputStream inputStream) {
        cmd = parentLogger.adopt("cmd");
        stream = inputStream;
        try {
            register(new HelpCommand(this));
        } catch (DuplicateEntryException e) {
            throw new RuntimeException(e);
        }
    }

    public MasterCommandRunner(@NotNull Logger parentLogger) {
        this(parentLogger, System.in);
    }

    public void run() {
        cmd.warning("Master is now in cmd mode! Master will accept command input through this commandLine!");


        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

        String input;
        while (true) {
            try {
                if ((input = bufferedReader.readLine()) == null) break;
                handleCommand(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public synchronized void handleCommand(String command) {
        try {
            //loop
            String[] s = command.split(" ");
            if (s.length < 1) {
                throw new Exception("input is malformed");
            }
            String a = s[0];
            boolean found = false;
            for (MasterCommand index : getIndex()) {
                if (index.getCommandName().equalsIgnoreCase(a)) {
                    index.executeCommand(command.replaceFirst(a, ""));
                    found = true;
                }
            }
            if (!found) {
                cmd.info("command \"" + a + "\" not found - try help");
            }
        } catch (Exception e) {
            cmd.error("error while master command execution: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public InputStream getStream() {
        return stream;
    }

}
