package com.android_1_katzavmall;

import android.widget.ImageView;

public class HighScore
{
    private String rank;
    private int level_img_id;
    private String difficulty;
    private String name;
    private int score;

    public HighScore(String rank, int level_img_id, String difficulty, String name, int score) {
        this.rank = rank;
        this.level_img_id = level_img_id;
        this.difficulty = difficulty;
        this.name = name;
        this.score = score;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getLevel_img_id() {
        return level_img_id;
    }

    public void setLevel_img_id(int level_img_id) {
        this.level_img_id = level_img_id;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
