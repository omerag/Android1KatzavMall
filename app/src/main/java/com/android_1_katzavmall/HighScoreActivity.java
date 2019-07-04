package com.android_1_katzavmall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

        loadData();
        RecyclerView recyclerView = findViewById(R.id.recyclerV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        HighScoreAdapter highScoreAdapter = new HighScoreAdapter(highScores);
        recyclerView.setAdapter(highScoreAdapter);


    }

    private void loadData()
    {
        SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("score_table",null);
        Type type = new TypeToken<ArrayList<HighScore>>() {}.getType();
        highScores = gson.fromJson(json,type);
    }
}
