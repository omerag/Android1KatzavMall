package com.android_1_katzavmall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class HighScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        RecyclerView recyclerView = findViewById(R.id.recyclerV);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<HighScore> highScores = new ArrayList<>();

        highScores.add(new HighScore("1",R.drawable.breakfast,"hard","Player",10000));
        highScores.add(new HighScore("2",R.drawable.breakfast,"hard","Player1",9000));
        highScores.add(new HighScore("3",R.drawable.breakfast,"hard","Player4",8000));
        highScores.add(new HighScore("4",R.drawable.breakfast,"hard","Player7",7000));
        highScores.add(new HighScore("5",R.drawable.breakfast,"medium","Player3",6000));
        highScores.add(new HighScore("6",R.drawable.breakfast,"medium","Player9",5000));
        highScores.add(new HighScore("7",R.drawable.breakfast,"medium","Player6",4000));
        highScores.add(new HighScore("8",R.drawable.breakfast,"easy","Player2",3000));
        highScores.add(new HighScore("9",R.drawable.breakfast,"easy","Player8",2000));
        highScores.add(new HighScore("10",R.drawable.breakfast,"easy","Player5",1000));

        HighScoreAdapter highScoreAdapter = new HighScoreAdapter(highScores);
        recyclerView.setAdapter(highScoreAdapter);


    }
}
