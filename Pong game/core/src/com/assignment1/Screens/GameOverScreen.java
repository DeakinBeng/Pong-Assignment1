package com.assignment1.Screens;

import com.assignment1.GameAssetManager;
import com.assignment1.GameConfig;
import com.assignment1.GameDifficulty;
import com.assignment1.Pong;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by bengg on 8/31/2014.
 */
public class GameOverScreen implements Screen {

    private float secondsLeft = 10;

    private Stage stage;
    private TextButton yesBtn;
    private TextButton noBtn;

    private Label secondsLabel;

    private Table tableLayout;

    private GameAssetManager assets;

    private Pong pong;
    private int score;

    public GameOverScreen(Pong pong, int score) {
        this.pong = pong;
        this.score = score;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // draw blackbackground
        if (secondsLeft < 0) {
            pong.gameFinished(score, true);
        } else {
            secondsLeft -= delta;
            secondsLabel.setText(Integer.toString((int) secondsLeft));
        }
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        assets = GameAssetManager.getInstance();
        tableLayout = new Table();
        stage = new Stage();

        tableLayout.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = assets.getGameOverFont();
        style.fontColor = Color.WHITE;

        yesBtn = new TextButton("YES", style);
        yesBtn.pad(20);

        yesBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pong.gameFinished(score, false);
                pong.startGameScreen();
            }
        });
        noBtn = new TextButton("NO", style);
        noBtn.pad(20);
        noBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pong.gameFinished(score, true);
            }
        });

        Label.LabelStyle lStyle = new Label.LabelStyle(assets.getGameOverFont(), Color.RED);
        Label gameoverLabel = new Label("GAME OVER", lStyle);

        secondsLabel = new Label("", lStyle);

        Label.LabelStyle lStyle2 = new Label.LabelStyle(assets.getGameOverFont(), Color.WHITE);
        Label continueLabel = new Label("CONTINUE", lStyle2);

        Label.LabelStyle lStyle3 = new Label.LabelStyle(assets.getGameOverFont(), Color.YELLOW);
        Label newHighscoreLabel = new Label("NEW HIGH SCORE", lStyle3);

        secondsLabel = new Label("", lStyle2);

        tableLayout.add(gameoverLabel).colspan(3).center().spaceBottom(100);
        tableLayout.row();
        if (GameConfig.getInstance().getDifficulty() != GameDifficulty.CUSTOM && score > pong.getHighscore()) {
            tableLayout.add(newHighscoreLabel).colspan(3).center().spaceBottom(350);
            tableLayout.row();
        }
        tableLayout.add(continueLabel).colspan(3).center();
        tableLayout.row();
        tableLayout.add(secondsLabel).colspan(3).center();
        tableLayout.row();
        tableLayout.add(yesBtn).center();
        tableLayout.add();
        tableLayout.add(noBtn).center();
        stage.addActor(tableLayout);

        Gdx.input.setInputProcessor(stage);
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

    }
}
