package com.assignment1;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bengg on 8/30/2014.
 */
public enum GameDifficulty {
    EASY(1.1f, 1), MEDIUM(1.25f, 2), HARD(1.5f, 3), CUSTOM(0.0f, 0);

    private float value;

    private int numOfBalls;

    private GameDifficulty(float value, int numBalls) {
        this.value = value;
        this.numOfBalls = numBalls;
    }

    public float getIncrementSpeed() {
        return value;
    }

    public int getNumOfBalls() {
        return numOfBalls;
    }

}
