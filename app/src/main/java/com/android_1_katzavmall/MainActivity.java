package com.android_1_katzavmall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView logo;
    ImageView man_with_bags;
    Button playBtn;
    Button highScoreBtn;

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

        //Bounce animation:
        final Animation bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        BounceInterpolator interpolator = new BounceInterpolator(0.5, 10);
        bounceAnimation.setInterpolator(interpolator);
        logo.startAnimation(bounceAnimation);

        // slide right animation
        final Animation slideRightAnimation = AnimationUtils.loadAnimation(this, R.anim.man_with_bags_anim);
        man_with_bags.startAnimation(slideRightAnimation);


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
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}

