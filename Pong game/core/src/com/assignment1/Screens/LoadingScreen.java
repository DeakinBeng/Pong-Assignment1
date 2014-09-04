package com.assignment1.Screens;

import com.assignment1.GameAssetManager;
import com.assignment1.Pong;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by bengg on 8/31/2014.
 */
public class LoadingScreen implements Screen {

    private SpriteBatch spriteBatch;
    private Sprite sprite;
    private TextureAtlas textureAtlas;
    private int currentFrame = 0;

    private int animationLoops = 0;

    private Pong pong;

    public LoadingScreen(Pong pong) {
        this.pong = pong;
        GameAssetManager gameAssetManager = GameAssetManager.getInstance();
        gameAssetManager.setUpBallTexture();
        gameAssetManager.setUpPaddleTexture();

        spriteBatch = GameAssetManager.getInstance().getSpriteBatch();
        textureAtlas = new TextureAtlas(Gdx.files.internal("loading_animation/atlas.txt"));
        TextureAtlas.AtlasRegion region = textureAtlas.findRegion("0");
        sprite = new Sprite(region);
        sprite.scale(3.5f);
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                currentFrame++;
                if (currentFrame > 7) {
                    currentFrame = 0;
                    animationLoops++;
                }
                String curFrame = Integer.toString(currentFrame);
                sprite.setRegion(textureAtlas.findRegion(curFrame));
            }
        }, 0, 0.04f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // draw blackbackground

        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();

        if (GameAssetManager.getInstance().assetManager.update() && animationLoops >= 2) {
            pong.startGameScreen();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        Timer.instance().clear();
        textureAtlas.dispose();
        spriteBatch.dispose();
    }
}
