package com.android_1_katzavmall;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageView logo;
    ImageView man_with_bags;
    Button playBtn;
    Button highScoreBtn;
    Button howToPlayBtn;
    Button settingsBtn;
    List<HighScore> highScores;
    String difficulty;
    String control;
    Boolean isMusic;
    Boolean isSounds;
    SharedPreferences sp;
    HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //test


        loadSettings();
        loadData();
        doBindService();
        if (isMusic)
        {
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
        }

        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();

        logo = findViewById(R.id.logo);
        man_with_bags = findViewById(R.id.man_with_bags);
        playBtn = findViewById(R.id.new_game_btn);
        highScoreBtn = findViewById(R.id.high_score_btn);
        howToPlayBtn = findViewById(R.id.how_to_play_btn);
        settingsBtn = findViewById(R.id.settings_btn);


        //logo animation:
        final Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        BounceInterpolator interpolator = new BounceInterpolator(0.5, 10);
        logoAnimation.setInterpolator(interpolator);
        logo.startAnimation(logoAnimation);

        // slide right animation
        final Animation slideRightMan = AnimationUtils.loadAnimation(this, R.anim.man_with_bags_anim);
        man_with_bags.startAnimation(slideRightMan);

        final Animation slideRightBtns = AnimationUtils.loadAnimation(this, R.anim.main_btns_anim);
        Handler playHandler = new Handler();
        playHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Button play = findViewById(R.id.new_game_btn);
                play.setVisibility(View.VISIBLE);
                play.setEnabled(true);
                play.startAnimation(slideRightBtns);

                Handler highHandler = new Handler();
                highHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Button high = findViewById(R.id.high_score_btn);
                        high.setVisibility(View.VISIBLE);
                        high.setEnabled(true);
                        high.startAnimation(slideRightBtns);


                        Handler howHandler = new Handler();
                        howHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Button how = findViewById(R.id.how_to_play_btn);
                                how.setVisibility(View.VISIBLE);
                                how.setEnabled(true);
                                how.startAnimation(slideRightBtns);


                                Handler settingsHandler = new Handler();
                                settingsHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Button settings = findViewById(R.id.settings_btn);
                                        settings.setVisibility(View.VISIBLE);
                                        settings.setEnabled(true);
                                        settings.startAnimation(slideRightBtns);

                                    }
                                }, 300);

                            }
                        }, 300);
                    }
                }, 300);
            }
        }, 1000);

        // font
        if (Locale.getDefault().toString().equals("iw_IL")) {
            Typeface typeface = ResourcesCompat.getFont(this, R.font.abraham);
            playBtn.setTypeface(typeface);
            highScoreBtn.setTypeface(typeface);
            howToPlayBtn.setTypeface(typeface);
            settingsBtn.setTypeface(typeface);
        }

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent levelIntent = new Intent(MainActivity.this, LevelSelectActivity.class);
                startActivity(levelIntent);
            }
        });

        highScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent highScoreIntent = new Intent(MainActivity.this,HighScoreActivity.class);
                startActivity(highScoreIntent);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                final View dialogView = getLayoutInflater().inflate(R.layout.settings_dialog, null);

                TextView settings_tv = dialogView.findViewById(R.id.settings_tv);
                TextView difficulty_tv = dialogView.findViewById(R.id.difficulty_tv);
                TextView control_tv = dialogView.findViewById(R.id.control_tv);
                TextView music_tv = dialogView.findViewById(R.id.music_tv);

                Button backBtn = dialogView.findViewById(R.id.back_btn);
                Button resetScoreBtn = dialogView.findViewById(R.id.reset_score_table);

                RadioGroup diffRadioGroup = dialogView.findViewById(R.id.difficulty_rg);
                RadioButton easyBtn = dialogView.findViewById(R.id.easy_btn);
                RadioButton mediumBtn = dialogView.findViewById(R.id.medium_btn);
                RadioButton hardBtn = dialogView.findViewById(R.id.hard_btn);

                RadioGroup controlRadioGroup = dialogView.findViewById(R.id.control_rg);
                RadioButton accelerometerBtn = dialogView.findViewById(R.id.accelerometer_btn);
                RadioButton touchBtn = dialogView.findViewById(R.id.touch_btn);

                final ImageView musicImgV = dialogView.findViewById(R.id.music_imgV);
                final ImageView soundImgV = dialogView.findViewById(R.id.sound_imgV);


                final CheckBox musicCheckBox = dialogView.findViewById(R.id.music_checkBox);
                CheckBox soundsCheckBox = dialogView.findViewById(R.id.sounds_checkBox);


                // ZoomIn/Out animation:
                final Animation zoomBtnAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.dlg_btns_anim);
                resetScoreBtn.startAnimation(zoomBtnAnimation);
                backBtn.startAnimation(zoomBtnAnimation);

                //font
                if (Locale.getDefault().toString().equals("iw_IL")) {
                    Typeface typeface1 = ResourcesCompat.getFont(MainActivity.this, R.font.koby);
                    settings_tv.setTypeface(typeface1);
                    difficulty_tv.setTypeface(typeface1);
                    easyBtn.setTypeface(typeface1);
                    mediumBtn.setTypeface(typeface1);
                    hardBtn.setTypeface(typeface1);
                    control_tv.setTypeface(typeface1);
                    accelerometerBtn.setTypeface(typeface1);
                    touchBtn.setTypeface(typeface1);
                    music_tv.setTypeface(typeface1);
                    Typeface typeface2 = ResourcesCompat.getFont(MainActivity.this, R.font.abraham);
                    resetScoreBtn.setTypeface(typeface2);
                    backBtn.setTypeface(typeface2);
                }


                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setView(dialogView);
                dialog.setCanceledOnTouchOutside(false);

                if (difficulty.equalsIgnoreCase(getString(R.string.difficulty1)))
                    easyBtn.setChecked(true);
                else if (difficulty.equalsIgnoreCase(getString(R.string.difficulty2)))
                    mediumBtn.setChecked(true);
                else hardBtn.setChecked(true);

                if (control.equalsIgnoreCase(getString(R.string.control1)))
                    accelerometerBtn.setChecked(true);
                else touchBtn.setChecked(true);

                if (isMusic)
                {
                    musicCheckBox.setChecked(true);
                    musicImgV.setImageResource(R.drawable.music_enabled);
                }
                else
                {
                    musicCheckBox.setChecked(false);
                    musicImgV.setImageResource(R.drawable.music_disabled);
                }

                if (isSounds)
                {
                    soundsCheckBox.setChecked(true);
                    soundImgV.setImageResource(R.drawable.sound_enabled);
                }
                else
                {
                    soundsCheckBox.setChecked(false);
                    soundImgV.setImageResource(R.drawable.sound_disabled);
                }

                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                resetScoreBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
                        sp.edit().remove("score_table").apply();
                        loadData();
                        Toast.makeText(MainActivity.this,R.string.reset_score_toast,Toast.LENGTH_LONG).show();
                    }
                });

                diffRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        int id = radioGroup.getCheckedRadioButtonId();
                        SharedPreferences.Editor editor = sp.edit();

                        switch (id)
                        {
                            case R.id.easy_btn:
                                difficulty = getString(R.string.difficulty1);
                                editor.putString("difficulty",difficulty);
                                editor.apply();
                                break;

                            case R.id.medium_btn:
                                difficulty = getString(R.string.difficulty2);
                                editor.putString("difficulty",difficulty);
                                editor.apply();
                                break;

                            case R.id.hard_btn:
                                difficulty = getString(R.string.difficulty3);
                                editor.putString("difficulty",difficulty);
                                editor.apply();
                                break;
                        }
                    }
                });

                controlRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        int id = radioGroup.getCheckedRadioButtonId();
                        SharedPreferences.Editor editor = sp.edit();

                        switch (id)
                        {
                            case R.id.accelerometer_btn:
                                control = getString(R.string.control1);
                                editor.putString("control",control);
                                editor.apply();
                                break;

                            case R.id.touch_btn:
                                control = getString(R.string.control2);
                                editor.putString("control",control);
                                editor.apply();
                                break;
                        }
                    }
                });

                musicCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        isMusic = compoundButton.isChecked();
                        if (isMusic)
                        {
                            mServ.resumeMusic();
                            musicImgV.setImageResource(R.drawable.music_enabled);
                        }
                        else
                        {
                            mServ.pauseMusic();
                            musicImgV.setImageResource(R.drawable.music_disabled);
                        }

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("music",isMusic);
                        editor.apply();

                    }
                });

                soundsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        isSounds = compoundButton.isChecked();

                        if (isSounds)
                        {
                            soundImgV.setImageResource(R.drawable.sound_enabled);
                        }
                        else
                        {
                            soundImgV.setImageResource(R.drawable.sound_disabled);
                        }

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("sounds",isSounds);
                        editor.apply();
                    }
                });

                dialog.show();
            }
        });
    }

    private void loadData()
    {
        SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("score_table",null);
        Type type = new TypeToken<ArrayList<HighScore>>() {}.getType();
        highScores = gson.fromJson(json,type);

        if(highScores == null)
        {
            highScores = new ArrayList<>();
            for (int i = 0; i < 10; i++)
            {
                highScores.add(new HighScore(i+1,R.drawable.no_image_square,"---","---",0));
            }
            saveData();
        }
    }

    private void saveData()
    {
        SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(highScores);
        editor.putString("score_table",json);
        editor.apply();
    }

    private void loadSettings()
    {
        sp = getSharedPreferences("sp",MODE_PRIVATE);

        difficulty = sp.getString("difficulty","");
        control = sp.getString("control","");
        isMusic = sp.getBoolean("music",true);
        isSounds = sp.getBoolean("sounds",true);

        if (difficulty.equals(""))
        {
            difficulty = getString(R.string.difficulty1);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("difficulty",difficulty);
            editor.apply();
        }

        if (control.equals(""))
        {
            control = getString(R.string.control1);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("control",control);
            editor.apply();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null && isMusic) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        stopService(music);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        //  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        //  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}

