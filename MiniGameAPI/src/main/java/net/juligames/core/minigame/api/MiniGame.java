package net.juligames.core.minigame.api;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * 18.12.2022
 */
public abstract class MiniGame {

    private final String plainName;

    public MiniGame(
            @NotNull final String plainName

                    ) {
        this.plainName = plainName;
    }

    //Description
    public final String getPlainName(){
        return plainName;
    }

    //Events?
    /**
     * Will be called as soon as the MiniGame is prepared by the Core
     */
     protected abstract void onLoad();

    /**
     * This will start the MiniGame
     * @return true if start was successfully and false if not
     */
    protected abstract boolean onStart();

    /**
     * Will be called to indicate that the game regardless of the current state should be stopped and prepared for shutdown
     */
    protected abstract void onAbort();

    /**
     * Will be called if the game finished criteria was meet
     */
    protected abstract void onFinish();


    public void load(){
        //load
        onLoad();
    }

    public void start(){
        //start routine
        onStart();
    }

    public void abort(){
        //ABORT
        onAbort();
    }
}
