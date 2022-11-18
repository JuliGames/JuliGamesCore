package net.juligames.core.hcast;

import net.juligames.core.Core;

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
