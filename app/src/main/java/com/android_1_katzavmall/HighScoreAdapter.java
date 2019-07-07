package com.android_1_katzavmall;

import android.graphics.Typeface;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.HighScoreViewHolder> {

    private List<HighScore> highScores;

    public HighScoreAdapter(List<HighScore> highScores) {
        this.highScores = highScores;
    }


    public class HighScoreViewHolder extends RecyclerView.ViewHolder{

        TextView rankTv;
        ImageView level_img;
        TextView difficultyTv;
        TextView nameTv;
        TextView scoreTv;

        public HighScoreViewHolder(View itemView) {
            super(itemView);
            rankTv = itemView.findViewById(R.id.rankTv);
            level_img = itemView.findViewById(R.id.level_img);
            difficultyTv = itemView.findViewById(R.id.difTv);
            nameTv = itemView.findViewById(R.id.nameTv);
            scoreTv = itemView.findViewById(R.id.high_score_tv);

        }
    }

    @NonNull
    @Override
    public HighScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.high_score_cell,parent,false);
        HighScoreViewHolder highScoreViewHolder = new HighScoreViewHolder(view);
        return highScoreViewHolder;
    }

    @Override
    public void onBindViewHolder(HighScoreViewHolder holder, int position) {

        HighScore highScore = highScores.get(position);
        holder.rankTv.setText(highScore.getRank()+"");
        holder.level_img.setImageResource(highScore.getLevel_img_id());
        holder.difficultyTv.setText(highScore.getDifficulty());
        holder.nameTv.setText(highScore.getName());
        holder.scoreTv.setText(highScore.getScore()+"");
    }

    @Override
    public int getItemCount() {
        return highScores.size();
    }
}
