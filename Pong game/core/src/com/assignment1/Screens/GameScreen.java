package com.assignment1.Screens;

import com.assignment1.GameAssetManager;
import com.assignment1.GameConfig;
import com.assignment1.GameDifficulty;
import com.assignment1.GameFinishCallback;
import com.assignment1.GameObjects.Ball;
import com.assignment1.GameObjects.Paddle;
import com.assignment1.GameState;
import com.assignment1.Pong;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bengg on 8/31/2014.
 */
public class GameScreen implements Screen {
    private GameState state;

    private Rectangle field = new Rectangle();

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public float getAccumulatedTime() {
        return accumulatedTime;
    }

    private ArrayList<Ball> balls;

    private Paddle paddle;

    private SpriteBatch spriteBatch;

    private float fieldBottom;
    private float fieldTop;
    private float fieldLeft;
    private float fieldRight;

    private float accumulatedTime = 0;

    private GameAssetManager assets;
    private Pong pong;

    public GameScreen(Pong pong) {
        this.pong = pong;
    }

    private void update(float dt) {
        for (Ball ball : balls)
            ball.update(dt);
        paddle.update(dt);
    }

    private boolean checkForBallPaddleCollision() {
        for (Ball ball : balls) {
            if (Intersector.overlaps(ball.getBounds(), paddle.getBounds())) {
                ball.move(ball.getX(), paddle.top());
                ball.reflect(false, true);
                // float relativeIntersectX = (paddle.getX() +(paddle.getWidth() / 2)) + (ball.getX() + (ball.getWidth() / 2));

                Vector2 vector = ball.getVelocity();
                //vector.setAngle((relativeIntersectX / (paddle.getWidth() / 2)) * GameConfig.MAX_ANGLE_REFLECTION);
                vector.set(vector.x * GameConfig.getInstance().getIncrementBallSpeed(), vector.y);
                ball.setVelocity(vector);
                assets.getPaddleCollision_sound().play();
                Gdx.input.vibrate(20);
                return true;
            }
        }
        return false;
    }

    private boolean checkForGameLost() {
        int count = 0;
        for (Ball ball : balls) {
            if (ball.bottom() <= fieldBottom) {
                if (GameConfig.getInstance().loseOnFirstBallDown) {
                    return true;
                } else {
                    count++;
                    if (count == balls.size())
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void render(float delta) {
        float dt = Gdx.graphics.getDeltaTime();
        if (state == GameState.RUNNING) {
            accumulatedTime += Gdx.graphics.getRawDeltaTime();
            update(dt);
            if (!checkForBallPaddleCollision()) { // if ball did not hit paddle
                if (checkForGameLost()) { // and ball is below
                    state = GameState.OVER;
                }
            }
        }
        draw(dt);
    }

    private void draw(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        if (state != GameState.OVER) {
            for (Ball ball : balls) {
                spriteBatch.draw(assets.getBallTexture(), ball.getX(), ball.getY());
            }
            spriteBatch.draw(assets.getPaddleTexture(), paddle.getX(), paddle.getY());
            String time = Integer.toString((int) accumulatedTime);
            assets.getTimeFont().draw(spriteBatch, time, (getFieldWidth() - assets.getTimeFont().getBounds(time).width) / 2, getFieldHeight() - 200);
        } else {
            pong.setScreen(new GameOverScreen(pong, (int) accumulatedTime));
        }
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        assets = GameAssetManager.getInstance();

        balls = new ArrayList<Ball>();
        for (int i = 0; i < GameConfig.getInstance().getNumberOfBalls(); i++) {
            balls.add(new Ball(GameConfig.getInstance().ballWidth, GameConfig.getInstance().ballHeight, GameScreen.this));
        }
        paddle = new Paddle(GameConfig.getInstance().paddleWidth, GameConfig.getInstance().paddleHeight, GameScreen.this);

        field.set(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fieldBottom = field.y;
        fieldTop = field.y + field.height;
        fieldLeft = field.x;
        fieldRight = field.x + field.width;

        spriteBatch = assets.getSpriteBatch();

        reset();
        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (state == GameState.RUNNING || state == GameState.STILL) {
                    int deltax = Gdx.input.getDeltaX();
                    if (paddle.left() + deltax < fieldLeft) {
                        paddle.move(fieldLeft, Paddle.PADDLE_ELEVATION);
                    } else if (paddle.right() + deltax > fieldRight) {
                        paddle.move(fieldRight - paddle.getWidth(), Paddle.PADDLE_ELEVATION);
                    } else if (paddle.left() >= fieldLeft && paddle.right() <= fieldRight) {
                        paddle.translate(deltax, 0);
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        });

    }

    Timer.Task starterTask;

    public void reset() {
        state = GameState.STILL;
        starterTask = new Timer.Task() { // delay start by 2 seconds
            @Override
            public void run() {
                state = GameState.RUNNING;
            }
        };
        Timer.schedule(starterTask, 1.5f);
        assets.getBackground_music().stop();
        assets.getBackground_music().play();
        accumulatedTime = 0;
        for (Ball ball : balls)
            ball.reset();
        paddle.reset();
    }


    @Override
    public void hide() {

    }

    @Override
    public void pause() {
        if (state != GameState.OVER) {
            if (state == GameState.STILL)
                starterTask.cancel();
            state = GameState.PAUSED;
            assets.getBackground_music().pause();
        }
    }

    @Override
    public void resume() {
        if (state != GameState.OVER) {
            state = GameState.RUNNING;
            assets.getBackground_music().play();
        }
    }

    public float getFieldBottom() {
        return fieldBottom;
    }

    public float getFieldTop() {
        return fieldTop;
    }

    public float getFieldLeft() {
        return fieldLeft;
    }

    public float getFieldRight() {
        return fieldRight;
    }

    public float getFieldWidth() {
        return field.getWidth();
    }

    public float getFieldHeight() {
        return field.getHeight();
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    @Override
    public void dispose() {
    }
}
