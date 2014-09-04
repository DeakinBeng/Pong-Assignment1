package com.assignment1.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.assignment1.GameConfig;
import com.assignment1.GameDifficulty;
import com.assignment1.GameFinishCallback;
import com.assignment1.GameState;
import com.assignment1.Pong;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;


public class GameActivity extends AndroidApplication {

    private Pong pong;
    private DBHelper db;

    public static final String DIFFICULTY_KEY = "diff";
    public static final String NUM_OF_BALLS = "numballs";
    public static final String BALL_SPEED = "ballspeed";
    public static final String LOSE_ON_FIRST_BALL = "loseonfirstball";
    public static final String PADDLE_COLOR = "paddlecolor";
    public static final String BALL_COLOR = "ballcolor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        db = new DBHelper(this);
        GameConfig cfg = GameConfig.getInstance();
        cfg.reset();
        Intent intent = getIntent();
        String diff = intent.getStringExtra("diff");
        if (diff.equals(GameDifficulty.EASY.name())) {
            cfg.setDifficulty(GameDifficulty.EASY);
        } else if (diff.equals(GameDifficulty.MEDIUM.name())) {
            cfg.setDifficulty(GameDifficulty.MEDIUM);
        } else if (diff.equals(GameDifficulty.HARD.name())) {
            cfg.setDifficulty(GameDifficulty.HARD);
        } else if (diff.equals(GameDifficulty.CUSTOM.name())) {
            cfg.setDifficulty(GameDifficulty.CUSTOM);
            cfg.setNumberOfBalls(intent.getIntExtra(GameActivity.NUM_OF_BALLS, 2));
            cfg.setIncrementBallSpeed(intent.getFloatExtra(GameActivity.BALL_SPEED, 1.2f));
            cfg.loseOnFirstBallDown = intent.getBooleanExtra(GameActivity.LOSE_ON_FIRST_BALL, true);
            cfg.paddleColor = Color.valueOf(Integer.toHexString(intent.getIntExtra(GameActivity.PADDLE_COLOR, 0)).substring(2));
            cfg.ballColor = Color.valueOf(Integer.toHexString(intent.getIntExtra(GameActivity.BALL_COLOR, 0)).substring(2));
        }
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;

        FrameLayout view = (FrameLayout) findViewById(R.id.game_container);
        pong = new Pong();

        pong.setOnGameFinish(new GameFinishCallback() {

            @Override
            public void gameComplete(int secondsLasted, boolean returnToMenu) {
                if (secondsLasted > 0)
                    db.saveNewHighscore(secondsLasted, GameConfig.getInstance().getDifficulty());
                if (returnToMenu)
                    finish();
            }

            @Override
            public int getCurrentHighscore() {
                return db.getHighscore();
            }

            @Override
            public void saveGameState() {
                if (pong.getGameState() == GameState.RUNNING) { // save everything here
                }
            }
        });
        view.addView(initializeForView(pong, config));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.game_menu_reset) {
            pong.reset();
            return true;
        } else if (id == R.id.game_menu_difficulty) {
            finish(); // send to previous screen
        } else if (id == R.id.game_menu_pause) {
            if (item.getTitle().toString().equals("PAUSE")) {
                pong.pause();
                item.setTitle("RESUME");
            } else {
                pong.resume();
                item.setTitle("PAUSE");
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
