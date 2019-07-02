package com.android_1_katzavmall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class HighScoreActivity extends AppCompatActivity {

    List<HighScore> highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        RecyclerView recyclerView = findViewById(R.id.recyclerV);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        highScores = new ArrayList<>();

        highScores.add(new HighScore(1,R.drawable.breakfast,"---","---",0));
        highScores.add(new HighScore(2,R.drawable.breakfast,"---","---",0));
        highScores.add(new HighScore(3,R.drawable.breakfast,"---","---",0));
        highScores.add(new HighScore(4,R.drawable.breakfast,"---","---",0));
        highScores.add(new HighScore(5,R.drawable.breakfast,"---","---",0));
        highScores.add(new HighScore(6,R.drawable.breakfast,"---","---",0));
        highScores.add(new HighScore(7,R.drawable.breakfast,"---","---",0));
        highScores.add(new HighScore(8,R.drawable.breakfast,"---","---",0));
        highScores.add(new HighScore(9,R.drawable.breakfast,"---","---",0));
        highScores.add(new HighScore(10,R.drawable.breakfast,"---","---",0));

        HighScoreAdapter highScoreAdapter = new HighScoreAdapter(highScores);
        recyclerView.setAdapter(highScoreAdapter);


    }

    public boolean isNewHighScore(int score)
    {
        for(int i = 0; i < highScores.size(); i++)
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

    }
}
