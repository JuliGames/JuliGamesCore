package net.juligames.core.config;

import net.juligames.core.api.TODO;
import net.juligames.core.api.config.Configuration;

import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * 26.11.2022
 */
@TODO(doNotcall = true)
public class CoreConfiguration implements Configuration {

    @Override
    public Properties cloneToProperties() {
        return null;
    }

    @Override
    public void configurate(Consumer<Properties> actionToPerform) {

    }

    @Override
    public <T> T configurateAndRead(Function<Properties, T> actionToPerform) {
        return null;
    }
}
