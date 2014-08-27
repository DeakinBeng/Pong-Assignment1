package com.assignment1.android;
/*
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.assignment1.Pong;


public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        initialize(new Pong(), config);

	}
}
*/


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.assignment1.android.GifDecoder.GifAnimationDrawable;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.io.IOException;
import java.util.Arrays;


public class MainMenuActivity extends Activity {

    Typeface arcadeFont;

    MediaPlayer mp;

    //Image
    ImageButton muteSound;
    ImageView aboutAnimation;

    //Text views
    TextView header;

    //List view
    ListView mainMenuListView;
    ListView newGameListView;

    // View Animator
    ViewAnimator viewContainer;

    private static final String MENU_SCREEN_KEY = "menu";

    private static final int MAIN_MENU = 0;
    private static final int NEW_GAME = 1;
    private static final int ABOUT = 2;

    private boolean soundOn = true;

    public static String user = "asdasd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mp = MediaPlayer.create(MainMenuActivity.this, R.raw.background_song);
        mp.setLooping(true);
        mp.start();

        muteSound = (ImageButton) findViewById(R.id.muteButton);
        muteSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundOn) {
                    muteSound.setImageResource(R.drawable.speaker);
                    mp.pause();
                } else {
                    muteSound.setImageResource(R.drawable.sound);
                    mp.start();
                }
                soundOn = !soundOn;
            }
        });

        arcadeFont = Typeface.createFromAsset(getAssets(), "fonts/ARCADECLASSIC.TTF");
        header = (TextView) findViewById(R.id.header);
        header.setTypeface(arcadeFont);

        viewContainer = (ViewAnimator) findViewById(R.id.layout_container);


        mainMenuListView = (ListView) findViewById(R.id.main_menu_list_view);

        //String[] mainMenuBtns = Arrays.copyOfRange(getResources().getStringArray(R.array.main_menu), 1, getResources().getStringArray(R.array.main_menu).length);
        String[] mainMenuBtns = Arrays.copyOfRange(getResources().getStringArray(R.array.main_menu), 0, getResources().getStringArray(R.array.main_menu).length);

        ArcadeFontButtonAdapter mainMenuAdapter = new ArcadeFontButtonAdapter(this, R.layout.menu_item, mainMenuBtns);

        mainMenuListView.setAdapter(mainMenuAdapter);

        mainMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ImageView im = (ImageView) view.findViewById(R.id.bullet);
                final TextView text = (TextView) view.findViewById(R.id.menu_item);
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

                        } else if (option.equals("NEW GAME")) {
                            viewContainer.setDisplayedChild(NEW_GAME);
                        } else if (option.equals("SCORES")) {
                            Toast.makeText(MainMenuActivity.this, "Feature not implemented yet.", Toast.LENGTH_SHORT).show();
                        } else if (option.equals("ABOUT")) {
                            viewContainer.setDisplayedChild(ABOUT);
                            try {
                                GifAnimationDrawable pong_animation = new GifAnimationDrawable(getResources().openRawResource(R.raw.pong_game));
                                //  pong_animation.set
                                aboutAnimation = (ImageView) findViewById(R.id.animatedPong);
                                aboutAnimation.setImageDrawable(pong_animation);
                            } catch (IOException io) {

                            }
                        } else if (option.equals("EXIT")) {
                            finish();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                im.animate();
            }
        });

        newGameListView = (ListView) findViewById(R.id.new_game_list_view);
        String[] newGameBtns = getResources().getStringArray(R.array.new_game);

        ArcadeFontButtonAdapter newGameAdapter = new ArcadeFontButtonAdapter(this, R.layout.menu_item, newGameBtns);
        newGameListView.setAdapter(newGameAdapter);
        newGameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String option = ((TextView) view.findViewById(R.id.menu_item)).getText().toString();
                if (option.equals("EASY")) {
                    startActivity(new Intent(MainMenuActivity.this, GameStarter.class));
                } else if (option.equals("MEDIUM")) {
                    Toast.makeText(MainMenuActivity.this, "Feature not implemented yet.", Toast.LENGTH_SHORT).show();
                } else if (option.equals("HARD")) {
                    Toast.makeText(MainMenuActivity.this, "Feature not implemented yet.", Toast.LENGTH_SHORT).show();
                } else if (option.equals("CUSTOM")) {
                    Toast.makeText(MainMenuActivity.this, "Feature not implemented yet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (savedInstanceState != null) {
            viewContainer.setDisplayedChild(savedInstanceState.getInt(MENU_SCREEN_KEY, 0));
            if (viewContainer.getDisplayedChild() == ABOUT) {
                try {
                    GifAnimationDrawable pong_animation = new GifAnimationDrawable(getResources().openRawResource(R.raw.pong_game));
                    //  pong_animation.set
                    aboutAnimation = (ImageView) findViewById(R.id.animatedPong);
                    aboutAnimation.setImageDrawable(pong_animation);
                } catch (IOException io) {

                }
            }
        }
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
            }
            viewContainer.setDisplayedChild(MAIN_MENU);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (soundOn)
            mp.start();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }





 @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.sound) {
            if (soundOn) {
                item.setIcon(android.R.drawable.ic_lock_silent_mode);
            } else {
                item.setIcon(android.R.drawable.ic_lock_silent_mode_off);
            }
            soundOn = !soundOn;
        }
        return super.onOptionsItemSelected(item);
    }
    */

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
}
