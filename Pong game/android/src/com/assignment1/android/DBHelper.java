package com.assignment1.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.assignment1.GameObjects.Ball;
import com.assignment1.GameConfig;
import com.assignment1.GameDifficulty;
import com.assignment1.GameObjects.Paddle;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bengg on 8/30/2014.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Pong";

    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String statement = "CREATE TABLE Highscores (ID INTEGER PRIMARY KEY, " +
                "score INTEGER NOT NULL, " +
                "difficulty TEXT NOT NULL)";
        db.execSQL(statement);
        statement = "CREATE TABLE GameState (ID INTEGER PRIMARY KEY, " +
                "ballcolor TEXT NOT NULL, " +
                "paddlex REAL NOT NULL, " +
                "paddley REAL NOT NULL, " +
                "paddlecolor TEXT NOT NULL, " +
                "score INTEGER NOT NULL, " +
                "difficulty TEXT NOT NULL, " +
                "curballvelocityx REAL NOT NULL, " +
                "curballvelocityy REAL NOT NULL, " +
                "curballvelocityangle REAL NOT NULL, " +
                "velocityincrement REAL NOT NULL)";
        db.execSQL(statement);

        statement = "CREATE TABLE BallPositions (ID INTEGER PRIMARY KEY, " +
                "ballx REAL NOT NULL, " +
                "bally REAL NOT NULL)";
        db.execSQL(statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String statement = "DROP TABLE IF EXISTS Highscores";
        db.execSQL(statement);
        statement = "DROP TABLE IF EXISTS GameState";
        db.execSQL(statement);
        statement = "DROP TABLE IF EXISTS BallPositions";
        db.execSQL(statement);
        onCreate(db);
    }

    public void saveNewHighscore(int score, GameDifficulty difficulty) {
        int lowest = -1;
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM Highscores WHERE difficulty = ? ORDER BY score ASC", new String[]{difficulty.name()});
        if (cursor.moveToFirst()) {
            int count = cursor.getCount();
            if (count >= 10) {
                lowest = cursor.getInt(1);
                score = Math.max(lowest, score);
            }
        }
        cursor.close();
        getReadableDatabase().close();

        ContentValues values = new ContentValues();
        values.put("score", score);
        values.put("difficulty", difficulty.name());
        if (lowest > -1)
            getWritableDatabase().update("Highscores", values, "difficulty =? AND score =?", new String[]{difficulty.name(), Integer.toString(lowest)});
        else
            getWritableDatabase().insert("Highscores", null, values);
        getWritableDatabase().close();
    }

    public HashMap<Integer, ArrayList<Integer>> getHighscores() {
        HashMap<Integer, ArrayList<Integer>> scores = new HashMap<Integer, ArrayList<Integer>>();

        scores.put(0, new ArrayList<Integer>());
        scores.put(1, new ArrayList<Integer>());
        scores.put(2, new ArrayList<Integer>());

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM Highscores ORDER BY score DESC", null);
        String diff;
        int score;
        if (cursor.moveToFirst()) {
            do {
                score = cursor.getInt(1);
                diff = cursor.getString(2);
                if (diff.equals(GameDifficulty.EASY.name())) {
                    scores.get(0).add(score);
                } else if (diff.equals(GameDifficulty.MEDIUM.name())) {
                    scores.get(1).add(score);
                } else if (diff.equals(GameDifficulty.HARD.name())) {
                    scores.get(2).add(score);
                }
            } while (cursor.moveToNext());
        }

        int size = scores.get(0).size();
        for (int i = 0; i < 10 - size; i++) {
            scores.get(0).add(-1);
        }
        size = scores.get(1).size();
        for (int i = 0; i < 10 - size; i++) {
            scores.get(1).add(-1);
        }
        size = scores.get(2).size();
        for (int i = 0; i < 10 - size; i++) {
            scores.get(2).add(-1);
        }
        cursor.close();
        getReadableDatabase().close();
        return scores;
    }

    public int getHighscore() {
        GameDifficulty diff = GameConfig.getInstance().getDifficulty();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT MAX(score) FROM Highscores WHERE difficulty =?", new String[]{diff.name()});
        cursor.moveToFirst();
        int score = cursor.getInt(0);
        cursor.close();
        getReadableDatabase().close();
        return score;
    }

    public void saveGameState(Ball ball, Paddle paddle, GameConfig config, int seconds) {
        ContentValues values = new ContentValues();
        values.put("ballwidth", ball.getWidth());
        values.put("ballheight", ball.getHeight());
        values.put("ballcolor", Color.WHITE.toString());
        values.put("paddlex", paddle.getX());
        values.put("paddley", paddle.getY());
        values.put("paddlewidth", paddle.getWidth());
        values.put("paddleheight", paddle.getHeight());
        values.put("paddlecolor", Color.WHITE.toString());
        values.put("score", seconds);
       values.put("difficulty", config.getDifficulty().name());
        values.put("curballvelocityx", ball.getVelocityX());
        values.put("curballvelocityy", ball.getVelocityY());
        values.put("curballvelocityangle", ball.getVelocity().angle());
        values.put("velocityincrement", config.getDifficulty().getIncrementSpeed());

        getWritableDatabase().insert("GameState", null, values);
        getWritableDatabase().close();
    }
}
