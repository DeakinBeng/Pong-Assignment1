package com.assignment1;

import com.assignment1.Screens.GameScreen;
import com.assignment1.Screens.LoadingScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Logger;

public class Pong extends Game {

    private GameScreen game;
    private GameFinishCallback gameFinishCallback;

    @Override
    public void create() {
        setScreen(new LoadingScreen(this));
    }

    public void startGameScreen() {
        game = new GameScreen(this);
        super.setScreen(game);
    }

    public void gameFinished(int score, boolean returnToMenu) {
        gameFinishCallback.gameComplete(score, returnToMenu);
    }

    public int getHighscore() {
        return gameFinishCallback.getCurrentHighscore();
    }

    @Override
    public void pause() {
        if (game != null) {
            game.pause();
        }
    }

    @Override
    public void resume() {
        if (game != null)
            game.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
        GameAssetManager.getInstance().dispose();
        if (game != null)
            game.dispose();
    }

    public void reset() {
        if (game != null)
            game.reset();
    }

    public GameState getGameState() {
        if (game != null)
            return game.getState();
        return GameState.OVER;
    }

    public void setOnGameFinish(GameFinishCallback gfcb) {
        gameFinishCallback = gfcb;
    }
}
