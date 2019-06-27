package com.android_1_katzavmall;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

public class FoodObject extends AppCompatImageView {

    private boolean isAnimated = false;
    private FoodType foodType;

    public FoodObject(Context context,FoodType foodType){
        super(context);
        this.foodType = foodType;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    public FoodType getType() {
        return foodType;
    }

    public void setAnimated(boolean animated) {
        isAnimated = animated;
    }
}
