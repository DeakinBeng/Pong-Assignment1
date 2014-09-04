package com.assignment1.GameObjects;

import com.assignment1.Pong;
import com.assignment1.Screens.GameScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by bengg on 8/28/2014.
 */
public class Ball extends GameObject {

    public Ball(int width, int height, GameScreen field) {
        super(width, height, field);
    }

    @Override
    public void reset() {
        move(getFieldWidth() / 2, getFieldHeight() / 1.6f);

        setVelocity(750f, 0);
        Random r = new Random();
        setVelocityAngle((r.nextInt(25) + 20) * (r.nextInt(8) + 1));
        updateBounds();
    }

    @Override
    public void update(float dt) {
        integrate(dt);
        updateBounds();

        if (left() <= getFieldLeft()) {
            move(getFieldLeft(), getY());
            reflect(true, false);
        } else if (right() >= getFieldRight()) {
            move(getFieldRight() - getWidth(), getY());
            reflect(true, false);
        }
        if (top() >= getFieldTop()) {
            move(getX(), getFieldTop() - getHeight());
            reflect(false, true);
        }
    }

    public void reflect(boolean x, boolean y) {
        Vector2 velocity = getVelocity();
        if (x) {
            velocity.x *= -1;
        }
        if (y) {
            velocity.y *= -1;
        }
        setVelocity(velocity);
    }

}
