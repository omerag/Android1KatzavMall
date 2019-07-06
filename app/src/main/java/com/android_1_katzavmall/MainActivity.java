package com.android_1_katzavmall;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //test

        logo = findViewById(R.id.logo);
        man_with_bags = findViewById(R.id.man_with_bags);
        playBtn = findViewById(R.id.new_game_btn);
        highScoreBtn = findViewById(R.id.high_score_btn);
        howToPlayBtn = findViewById(R.id.how_to_play_btn);
        settingsBtn = findViewById(R.id.settings_btn);

        loadData();

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
                final View dialogView = getLayoutInflater().inflate(R.layout.settings_dialog,null);

                Button saveBtn = dialogView.findViewById(R.id.save_settings_btn);
                Button cancelBtn = dialogView.findViewById(R.id.btn_cancel);
                Button resetScoreBtn = dialogView.findViewById(R.id.reset_score_table);
                Spinner difficultySpinner = dialogView.findViewById(R.id.difficulty_spinner);
                Spinner controlSpinner = dialogView.findViewById(R.id.control_spinner);
                CheckBox musicCheckBox = dialogView.findViewById(R.id.music_checkBox);
                CheckBox soundsCheckBox = dialogView.findViewById(R.id.sounds_checkBox);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setView(dialogView);
                dialog.setCanceledOnTouchOutside(false);

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
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
                highScores.add(new HighScore(i+1,R.drawable.breakfast,"---","---",0));
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


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}

