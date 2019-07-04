package com.android_1_katzavmall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

public class HighScoreActivity extends AppCompatActivity {

    List<HighScore> highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_high_score);

        RecyclerView recyclerView = findViewById(R.id.recyclerV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        highScores = new ArrayList<>();

        for(int i = 0; i < 3; i++)
        {
            highScores.add(new HighScore(i+1,R.drawable.breakfast,"---","---",0));
        }

        HighScoreAdapter highScoreAdapter = new HighScoreAdapter(highScores);
        recyclerView.setAdapter(highScoreAdapter);


    }

    public boolean isNewHighScore(int score)
    {
        for(int i = 0; i < 10; i++)
        {
            if(score > highScores.get(i).getScore()) return true;
        }
        return false;
    }

    public void insertScore(String name,String difficulty,int level_img_id,int score)
    {
        //insert new score to the table
        int i = 0;
        while (score < highScores.get(i).getScore())
        {
            i++;
        }

        HighScore highScore = new HighScore(i,level_img_id,difficulty,name,score);

        if(i == 9)
        {
            highScores.set(9,highScore);
        }
        else
        {
            for(int j = 9; j > i; j--)
            {
                highScores.set(j,highScores.get(j-1));
            }

            highScores.set(i,highScore);
        }

    }
}
