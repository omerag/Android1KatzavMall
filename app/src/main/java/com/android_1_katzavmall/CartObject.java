package com.android_1_katzavmall;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

public class CartObject extends AppCompatImageView {

    int orginalWidth;
    int originalHeight;

    private boolean isAnimated = false;


    public CartObject(Context context, AttributeSet st) {
        super(context, st);

        orginalWidth = getWidth();
        originalHeight = getHeight();
        setImageResource(R.drawable.carton_bag);
    }

    void animateCoach(){

        ScaleAnimation scaleUp = new ScaleAnimation(1f, 1.25f, 1f, 1.25f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleUp.setDuration(300);
        scaleUp.setRepeatMode(Animation.REVERSE);
        scaleUp.setRepeatCount(1);
        scaleUp.setInterpolator(new LinearInterpolator());
        startAnimation(scaleUp);

    }


}
