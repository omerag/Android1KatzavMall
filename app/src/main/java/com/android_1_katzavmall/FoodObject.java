package com.android_1_katzavmall;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.animation.AccelerateInterpolator;

import androidx.appcompat.widget.AppCompatImageView;

public class FoodObject extends AppCompatImageView {

    private boolean isAnimated = false;
    private FoodType foodType;
    private long diffultyFoodVel;
    private int screenHeight;
    ObjectAnimator animation;

    public FoodObject(Context context,FoodType foodType,long diffultyFoodVel,int screemHeight){
        super(context);
        this.foodType = foodType;
        this.diffultyFoodVel = diffultyFoodVel;
        this.screenHeight = screemHeight;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    public void startMoveAnimation(){
        animation = ObjectAnimator.ofFloat(this, "translationY", screenHeight);
        animation.setDuration(diffultyFoodVel);
        animation.setInterpolator(new AccelerateInterpolator());
        setAnimated(true);



        animation.start();
    }

    public FoodType getType() {
        return foodType;
    }

    public void setAnimated(boolean animated) {
        isAnimated = animated;
    }

    public ObjectAnimator getObjectAnimator(){
        return animation;
    }
}
