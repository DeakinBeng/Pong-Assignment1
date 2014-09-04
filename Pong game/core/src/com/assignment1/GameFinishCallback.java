package com.assignment1;

import com.assignment1.GameObjects.Ball;
import com.assignment1.GameObjects.Paddle;

/**
 * Created by beng on 8/30/2014.
 */
public interface GameFinishCallback {

    public void gameComplete(int secondsLasted, boolean returnToMenu);

    public int getCurrentHighscore();

    public void saveGameState();

}
