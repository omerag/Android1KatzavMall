package com.android_1_katzavmall;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

public class CartObject extends AppCompatImageView {



    private boolean isAnimated = false;
    private MediaPlayer goodSoundPlayer = MediaPlayer.create(getContext(),R.raw.good_item_sound);
    private MediaPlayer badSoundPlayer = MediaPlayer.create(getContext(),R.raw.bad_item_sound);


    public CartObject(Context context, AttributeSet st) {
        super(context, st);

        setImageResource(R.drawable.carton_bag);
    }

    void animateCoach(){

        ScaleAnimation scaleUp = new ScaleAnimation(1f, 1.15f, 1f, 1.08f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
        scaleUp.setDuration(150);
        scaleUp.setRepeatMode(Animation.REVERSE);
        scaleUp.setRepeatCount(1);
        scaleUp.setInterpolator(new LinearInterpolator());
        startAnimation(scaleUp);

    }

    public boolean isAnimated() {
        return isAnimated;
    }

    public void setAnimated(boolean animated) {
        isAnimated = animated;
    }

    public void playGoodSound()
    {
        goodSoundPlayer.start();
    }

    public void playBadSound()
    {
        badSoundPlayer.start();
    }


}
