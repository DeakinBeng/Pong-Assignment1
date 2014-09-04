package com.assignment1.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.assignment1.GameAssetManager;
import com.assignment1.GameDifficulty;
import com.assignment1.android.ColorPickerDialog.ColorPickerDialog;
import com.assignment1.android.GifDecoder.GifAnimationDrawable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MainMenuActivity extends Activity {

    private Typeface arcadeFont;
    private DBHelper db;

    //Image
    private ImageView aboutAnimation;

    //Text views
    private TextView header;

    //List view
    private ListView mainMenuListView;
    private ListView newGameListView;

    private ListView easyScoresListView;
    private ListView mediumScoresListView;
    private ListView hardScoresListView;

    // View Animator
    private ViewAnimator viewContainer;

    //Tab host
    private TabHost tabHost;

    private static final String MENU_SCREEN_KEY = "menu";

    private static final int MAIN_MENU = 0;
    private static final int NEW_GAME = 1;
    private static final int ABOUT = 2;
    private static final int SCORES = 3;

    private static final int CUSTOM_DIALOG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        db = new DBHelper(this);

        arcadeFont = Typeface.createFromAsset(getAssets(), "fonts/ARCADECLASSIC.TTF");
        header = (TextView) findViewById(R.id.header);
        header.setTypeface(arcadeFont);

        viewContainer = (ViewAnimator) findViewById(R.id.layout_container);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mainMenuListView = (ListView) findViewById(R.id.main_menu_list_view);

            //String[] mainMenuBtns = Arrays.copyOfRange(getResources().getStringArray(R.array.main_menu), 1, getResources().getStringArray(R.array.main_menu).length);
            String[] mainMenuBtns = Arrays.copyOfRange(getResources().getStringArray(R.array.main_menu), 0, getResources().getStringArray(R.array.main_menu).length);

            ArcadeFontButtonAdapter mainMenuAdapter = new ArcadeFontButtonAdapter(this, R.layout.menu_item, mainMenuBtns);

            mainMenuListView.setAdapter(mainMenuAdapter);

            mainMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    handleOnClick(view);
                }
            });
        } else { // set up landscape
            LinearLayout newGame = (LinearLayout) findViewById(R.id.main_menu_newgame);
            TextView newgame_tv = (TextView) newGame.findViewById(R.id.menu_item);
            newgame_tv.setTypeface(arcadeFont);
            newgame_tv.setText("NEW GAME");
            newGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOnClick(v);
                }
            });

            LinearLayout scores = (LinearLayout) findViewById(R.id.main_menu_scores);
            TextView scores_tv = (TextView) scores.findViewById(R.id.menu_item);
            scores_tv.setTypeface(arcadeFont);
            scores_tv.setText("SCORES");
            scores.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOnClick(v);
                }
            });

            LinearLayout about = (LinearLayout) findViewById(R.id.main_menu_about);
            TextView about_tv = (TextView) about.findViewById(R.id.menu_item);
            about_tv.setTypeface(arcadeFont);
            about_tv.setText("ABOUT");
            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOnClick(v);
                }
            });

            LinearLayout exit = (LinearLayout) findViewById(R.id.main_menu_exit);
            TextView exit_tv = (TextView) exit.findViewById(R.id.menu_item);
            exit_tv.setTypeface(arcadeFont);
            exit_tv.setText("EXIT");
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOnClick(v);
                }
            });
        }

        newGameListView = (ListView) findViewById(R.id.new_game_list_view);
        String[] newGameBtns = getResources().getStringArray(R.array.new_game);

        ArcadeFontButtonAdapter newGameAdapter = new ArcadeFontButtonAdapter(this, R.layout.menu_item, newGameBtns);
        newGameListView.setAdapter(newGameAdapter);
        newGameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleOnClick(view);
            }
        });

        easyScoresListView = (ListView) findViewById(R.id.easyTab);
        mediumScoresListView = (ListView) findViewById(R.id.mediumTab);
        hardScoresListView = (ListView) findViewById(R.id.hardTab);

        tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Easy");
        tabSpec1.setContent(R.id.easyTab);
        tabSpec1.setIndicator("Easy");

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Medium");
        tabSpec2.setContent(R.id.mediumTab);
        tabSpec2.setIndicator("Medium");

        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("Hard");
        tabSpec3.setContent(R.id.hardTab);
        tabSpec3.setIndicator("Hard");

        tabHost.addTab(tabSpec1);
        tabHost.addTab(tabSpec2);
        tabHost.addTab(tabSpec3);

        for (int tabIndex = 0; tabIndex < tabHost.getTabWidget().getTabCount(); tabIndex++) {
            View tab = tabHost.getTabWidget().getChildTabViewAt(tabIndex);
            TextView t = (TextView) tab.findViewById(android.R.id.title);
            t.setTextColor(getResources().getColor(R.color.font));
        }

        if (savedInstanceState != null) {
            viewContainer.setDisplayedChild(savedInstanceState.getInt(MENU_SCREEN_KEY, 0));
            if (viewContainer.getDisplayedChild() == ABOUT) {
                try {
                    GifAnimationDrawable pong_animation = new GifAnimationDrawable(getResources().openRawResource(R.raw.pong_game));
                    aboutAnimation = (ImageView) findViewById(R.id.animatedPong);
                    aboutAnimation.setImageDrawable(pong_animation);
                } catch (IOException io) {

                }
            }
        }

        //load assets
        //GameAssetManager.getInstance();
    }

    private void setUpHighscores() {
        HashMap<Integer, ArrayList<Integer>> scores = db.getHighscores();

        RankedListViewAdapter easy = new RankedListViewAdapter(MainMenuActivity.this, R.layout.score_item, scores.get(0));
        RankedListViewAdapter medium = new RankedListViewAdapter(MainMenuActivity.this, R.layout.score_item, scores.get(1));
        RankedListViewAdapter hard = new RankedListViewAdapter(MainMenuActivity.this, R.layout.score_item, scores.get(2));

        easyScoresListView.setAdapter(easy);
        mediumScoresListView.setAdapter(medium);
        hardScoresListView.setAdapter(hard);
    }

    private void handleOnClick(View clicked) {
        final ImageView im = (ImageView) clicked.findViewById(R.id.bullet);
        final TextView text = (TextView) clicked.findViewById(R.id.menu_item);
        im.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(MainMenuActivity.this, R.anim.arrow_move);
        im.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                im.setVisibility(View.INVISIBLE);
                String option = text.getText().toString();
                if (option.equals("CONTINUE")) {
                    Toast.makeText(MainMenuActivity.this, "Feature not implemented yet.", Toast.LENGTH_SHORT).show();
                } else if (option.equals("NEW GAME")) {
                    viewContainer.setDisplayedChild(NEW_GAME);
                } else if (option.equals("SCORES")) {
                    viewContainer.setDisplayedChild(SCORES);
                    setUpHighscores();

                } else if (option.equals("ABOUT")) {
                    viewContainer.setDisplayedChild(ABOUT);
                    try {
                        GifAnimationDrawable pong_animation = new GifAnimationDrawable(getResources().openRawResource(R.raw.pong_game));
                        aboutAnimation = (ImageView) findViewById(R.id.animatedPong);
                        aboutAnimation.setImageDrawable(pong_animation);
                    } catch (IOException io) {

                    }
                } else if (option.equals("EXIT")) {
                    finish();
                } else if (option.equals("EASY")) {
                    Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                    intent.putExtra(GameActivity.DIFFICULTY_KEY, GameDifficulty.EASY.name());
                    startActivity(intent);
                } else if (option.equals("MEDIUM")) {
                    Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                    intent.putExtra(GameActivity.DIFFICULTY_KEY, GameDifficulty.MEDIUM.name());
                    startActivity(intent);
                } else if (option.equals("HARD")) {
                    Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                    intent.putExtra(GameActivity.DIFFICULTY_KEY, GameDifficulty.HARD.name());
                    startActivity(intent);
                } else if (option.equals("CUSTOM")) {
                    showDialog(CUSTOM_DIALOG);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        im.animate();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case CUSTOM_DIALOG:
                View v = getLayoutInflater().inflate(R.layout.custom_dialog, null);

                final View pColor = v.findViewById(R.id.paddle_color);
                final ColorDrawable cdPaddle = (ColorDrawable) pColor.getBackground();
                final View bColor = v.findViewById(R.id.ball_color);
                final ColorDrawable cdBall = (ColorDrawable) bColor.getBackground();
                final TextView ball_inc = (TextView) v.findViewById(R.id.ball_inc_speed_tv);
                final TextView num_balls = (TextView) v.findViewById(R.id.num_balls_tv);
                final CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox3);

                SeekBar numBallsSeek = (SeekBar) v.findViewById(R.id.num_balls_seekbar);
                numBallsSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser)
                            num_balls.setText(Integer.toString(progress + 1));

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                SeekBar ballIncSeek = (SeekBar) v.findViewById(R.id.ball_inc_speed_seekbar);
                ballIncSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser)
                            ball_inc.setText(Float.toString((progress + 12f) / 10f));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                pColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(MainMenuActivity.this, cdPaddle.getColor(), new ColorPickerDialog.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int color) {
                                pColor.setBackgroundColor(color);
                            }
                        });
                        colorPickerDialog.show();
                    }
                });

                bColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(MainMenuActivity.this, cdBall.getColor(), new ColorPickerDialog.OnColorSelectedListener() {

                            @Override
                            public void onColorSelected(int color) {
                                bColor.setBackgroundColor(color);
                            }

                        });
                        colorPickerDialog.show();
                    }
                });


                AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
                builder.setView(v);
                builder.setTitle("Custom game");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                        intent.putExtra(GameActivity.DIFFICULTY_KEY, GameDifficulty.CUSTOM.name());
                        intent.putExtra(GameActivity.NUM_OF_BALLS, Integer.parseInt(num_balls.getText().toString()));
                        intent.putExtra(GameActivity.BALL_SPEED, Float.parseFloat(ball_inc.getText().toString()));
                        intent.putExtra(GameActivity.LOSE_ON_FIRST_BALL, checkBox.isChecked());
                        intent.putExtra(GameActivity.PADDLE_COLOR, cdPaddle.getColor());
                        intent.putExtra(GameActivity.BALL_COLOR, cdBall.getColor());
                        startActivity(intent);
                    }
                });
                return builder.create();
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MENU_SCREEN_KEY, viewContainer.getDisplayedChild());

    }

    @Override
    public void onBackPressed() {
        int displayedScreen = viewContainer.getDisplayedChild();
        if (displayedScreen == MAIN_MENU) {
            finish();
        } else {
            if (displayedScreen == ABOUT) {
                ((GifAnimationDrawable) aboutAnimation.getDrawable()).stop();
                aboutAnimation.setImageDrawable(null);
            } else if (displayedScreen == SCORES) {
                setUpHighscores();
            }
            viewContainer.setDisplayedChild(MAIN_MENU);
        }
    }


    private class ArcadeFontButtonAdapter extends ArrayAdapter<String> {

        private String[] objs;

        public ArcadeFontButtonAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            objs = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.menu_item, null);
            }

            TextView mainMenuItem = (TextView) convertView.findViewById(R.id.menu_item);
            mainMenuItem.setTypeface(arcadeFont);
            mainMenuItem.setText(objs[position]);

            return convertView;
        }
    }

    private class RankedListViewAdapter extends ArrayAdapter<Integer> {

        List<Integer> objs;

        public RankedListViewAdapter(Context context, int resource, List<Integer> objects) {
            super(context, resource, objects);
            objs = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.score_item, null);
            }

            TextView scoreIndex = (TextView) convertView.findViewById(R.id.score_index);
            TextView score = (TextView) convertView.findViewById(R.id.score_textview);

            scoreIndex.setText(Integer.toString(position + 1) + ".");
            if (objs.get(position) != -1) {
                score.setText(Integer.toString(objs.get(position)) + " seconds");
            } else
                score.setText("");
            return convertView;
        }
    }
}
