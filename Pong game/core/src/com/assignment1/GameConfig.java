package com.assignment1;

import com.badlogic.gdx.graphics.Color;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bengg on 8/29/2014.
 */
public class GameConfig {

    public static final int MAX_ANGLE_REFLECTION = 60;

    public String ballTextureLocation;
    public Color ballColor;
    public int ballWidth;
    public int ballHeight;

    public String paddleTextureLocation;
    public Color paddleColor;
    public int paddleWidth;
    public int paddleHeight;

    public boolean loseOnFirstBallDown;

    public void setNumberOfBalls(int numberOfBalls) {
        this.numberOfBalls = numberOfBalls;
    }

    private int numberOfBalls;

    public void setDifficulty(GameDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    private GameDifficulty difficulty;

    public int getNumberOfBalls() {
        if (difficulty != GameDifficulty.CUSTOM) {
            return difficulty.getNumOfBalls();
        }
        return numberOfBalls;
    }

    private float incrementBallSpeed; // 50% faster each time.

    public float getIncrementBallSpeed() {
        if (difficulty != GameDifficulty.CUSTOM)
            return difficulty.getIncrementSpeed();
        return incrementBallSpeed;
    }

    public void setIncrementBallSpeed(float incrementBallSpeed) {
        this.incrementBallSpeed = incrementBallSpeed;
    }

    private static GameConfig cfg = null;

    private GameConfig() {
        reset();
    }

    public static GameConfig getInstance() {
        if (cfg == null)
            cfg = new GameConfig();
        return cfg;
    }

    public void reset() {
        ballTextureLocation = null;
        ballColor = Color.WHITE;
        ballWidth = 32;
        ballHeight = 32;

        paddleTextureLocation = null;
        paddleColor = Color.WHITE;
        paddleWidth = 150;
        paddleHeight = 20;

        numberOfBalls = 1;
        loseOnFirstBallDown = true;
        difficulty = GameDifficulty.EASY;
        incrementBallSpeed = 1.5f; // 50% faster each time.
    }
}
