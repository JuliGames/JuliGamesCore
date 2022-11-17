package net.juligames.core.hcast;

import com.hazelcast.map.IMap;
import net.juligames.core.Core;
import org.checkerframework.checker.units.qual.C;

import java.util.concurrent.ExecutionException;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
public class HazelDebug {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Core core = new Core();
        core.start("test");
    }
}
