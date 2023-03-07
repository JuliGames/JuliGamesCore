package net.juligames.core.api.config.env;

import org.jetbrains.annotations.ApiStatus;

/**
 * @author Ture Bentzin
 * 07.03.2023
 */
@ApiStatus.AvailableSince("1.5")
public class EnvironmentVariableInterpreter extends MapFeedInterpreter<String> {


    public EnvironmentVariableInterpreter() {
        super(System::getenv, (s, ignored) -> new EnvironmentVariable(s));
    }


}
