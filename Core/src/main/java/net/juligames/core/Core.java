package net.juligames.core;

import com.hazelcast.client.HazelcastClient;
import net.juligames.core.hcast.HazelConnector;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public final class Core {

    private static Core core;
    private HazelcastClient hazelcastClient;

    public static Core getInstance() {
        return core;
    }

    @ApiStatus.Internal
    public void start(String core_name) {
        if (core != null) throw new IllegalStateException("seems like a core is already running!");
        core = this;
        HazelConnector.getInstanceAndConnect(core_name);
    }
}
