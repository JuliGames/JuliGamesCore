package net.juligames.core.minigame.api.test;

import net.juligames.core.api.API;
import net.juligames.core.minigame.api.SimpleMiniGame;

/**
 * @author Ture Bentzin
 * 19.12.2022
 */
public class ExampleMiniGame extends SimpleMiniGame {
    private boolean running = false;
    private boolean finished = false;

    public ExampleMiniGame() {
        super("Example", "0.1", "Ture Bentzin", "bentzin@tdrstudios,de", API.get().getAPILogger());
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    protected void onLoad() {
        getLogger().info("LOAD!!!!");
    }

    @Override
    protected boolean onStart() {
        getLogger().info("START!!!!");
        return true;
    }

    @Override
    protected void onAbort() {
        getLogger().info("Abort!!!!");
    }

    @Override
    protected void onFinish() {
        getLogger().info("Finish!!!!");
    }
}
