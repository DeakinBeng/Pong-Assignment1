package com.assignment1.GameObjects;

import com.assignment1.Pong;
import com.assignment1.Screens.GameScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by bengg on 8/28/2014.
 */
public abstract class GameObject {

    private Vector2 position = new Vector2();
    private Vector2 velocity = new Vector2();

    private Rectangle bounds = new Rectangle();

    private GameScreen field;

    protected GameObject(int width, int height, GameScreen field) {
        bounds.setWidth(width);
        bounds.setHeight(height);
        this.field = field;
    }

    public float getFieldBottom() {
        return field.getFieldBottom();
    }

    public float getFieldTop() {
        return field.getFieldTop();
    }

    public float getFieldLeft() {
        return field.getFieldLeft();
    }

    public float getFieldRight() {
        return field.getFieldRight();
    }

    public float getFieldWidth() {
        return field.getFieldWidth();
    }

    public float getFieldHeight() {
        return field.getFieldHeight();
    }

    public Rectangle getBounds() {
        updateBounds();
        return bounds;
    }


    public void updateBounds() {
        bounds.setX(position.x);
        bounds.setY(position.y);
    }

    public float getWidth() {
        return bounds.getWidth();
    }

    public float getHeight() {
        return bounds.getHeight();
    }

    public float bottom() {
        return bounds.y;
    }

    public float top() {
        return bounds.y + getHeight();
    }

    public float left() {
        return bounds.x;
    }

    public float right() {
        return bounds.x + getWidth();
    }

    public void move(float x, float y) {
        position.set(x, y);
    }

    public void translate(float x, float y) {
        position.add(x, y);
    }

    public void integrate(float dt) {
        position.add(velocity.x * dt, velocity.y * dt);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void setVelocity(float x, float y) {
        this.velocity.set(x, y);
    }

    public void setVelocityAngle(float angle) {
        this.velocity.setAngle(angle);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getVelocityX() {
        return velocity.x;
    }

    public float getVelocityY() {
        return velocity.y;
    }

    public abstract void update(float dt);

    public abstract void reset();
}
