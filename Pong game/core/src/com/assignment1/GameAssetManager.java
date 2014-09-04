package com.assignment1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by bengg on 8/31/2014.
 */
public class GameAssetManager {

    public AssetManager assetManager;

    private Texture ballTexture;
    private Texture paddleTexture;

    private Music background_music;
    private Sound paddleCollision_sound;

    private BitmapFont timeFont;
    private BitmapFont gameOverFont;

    private SpriteBatch spriteBatch;

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    private static GameAssetManager instance;

    private GameAssetManager() {
        assetManager = new AssetManager();
        spriteBatch = new SpriteBatch();
        assetManager.load("background.mp3", Music.class);
        assetManager.load("paddlecollisionsound.wav", Sound.class);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ARCADECLASSIC.TTF"));
        timeFont = generator.generateFont((int) (80 * Gdx.graphics.getDensity()));
        gameOverFont = generator.generateFont((int) (50 * Gdx.graphics.getDensity()));
        generator.dispose();
    }

    public void setUpBallTexture() {
        if (GameConfig.getInstance().ballTextureLocation == null) {
            Pixmap ballPixMap = new Pixmap(GameConfig.getInstance().ballWidth, GameConfig.getInstance().ballHeight, Pixmap.Format.RGB888);
            ballPixMap.setColor(GameConfig.getInstance().ballColor);
            ballPixMap.fill();
            ballTexture = new Texture(ballPixMap);
            ballPixMap.dispose();
        } else {

        }
    }

    public void setUpPaddleTexture() {
        if (GameConfig.getInstance().paddleTextureLocation == null) {
            Pixmap paddlePixmap = new Pixmap(GameConfig.getInstance().paddleWidth, GameConfig.getInstance().paddleHeight, Pixmap.Format.RGB888);
            paddlePixmap.setColor(GameConfig.getInstance().paddleColor);
            paddlePixmap.fill();
            paddleTexture = new Texture(paddlePixmap);
            paddlePixmap.dispose();
        } else {

        }
    }


    public static GameAssetManager getInstance() {
        if (instance == null) {
            instance = new GameAssetManager();
        }
        return instance;
    }

    public void dispose() {
        ballTexture.dispose();
        paddleTexture.dispose();
        if (background_music != null)
            background_music.dispose();
        if (paddleCollision_sound != null)
            paddleCollision_sound.dispose();
        timeFont.dispose();
        gameOverFont.dispose();
        spriteBatch.dispose();
        assetManager.dispose();
        instance = null;
    }

    public Texture getBallTexture() {
        return ballTexture;
    }

    public Texture getPaddleTexture() {
        return paddleTexture;
    }

    public Music getBackground_music() {
        if (background_music == null)
            background_music = assetManager.get("background.mp3", Music.class);

        return background_music;
    }

    public Sound getPaddleCollision_sound() {
        if (paddleCollision_sound == null)
            paddleCollision_sound = assetManager.get("paddlecollisionsound.wav", Sound.class);
        return paddleCollision_sound;
    }

    public BitmapFont getTimeFont() {
        return timeFont;
    }

    public BitmapFont getGameOverFont() {
        return gameOverFont;
    }
}
