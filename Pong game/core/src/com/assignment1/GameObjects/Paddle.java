package com.assignment1.GameObjects;

import com.assignment1.Pong;
import com.assignment1.Screens.GameScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by bengg on 8/28/2014.
 */
public class Paddle extends GameObject {

    Texture paddleTexture;
    public static final int PADDLE_ELEVATION = 30;

    public Paddle(int width, int height, GameScreen field) {
        super(width, height, field);
        Pixmap paddlePixMap = new Pixmap(width, height, Pixmap.Format.RGB888);
        paddlePixMap.setColor(Color.WHITE);
        paddlePixMap.fill();
        paddleTexture = new Texture(paddlePixMap);
        paddlePixMap.dispose();
    }

    @Override
    public void reset() {
        move((getFieldWidth() - getWidth()) / 2, PADDLE_ELEVATION);
        updateBounds();
    }

    @Override
    public void update(float dt) {
        integrate(dt);
        updateBounds();
    }
}
