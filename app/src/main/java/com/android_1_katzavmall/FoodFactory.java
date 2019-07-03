package com.android_1_katzavmall;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class FoodFactory {

    private Context context;
    private FoodContainer container;
    private FrameLayout frame;
    FoodFactory(Context context, FoodContainer container , FrameLayout frame){
        this.context = context;
        this.container = container;
        this.frame = frame;
    }

    public ImageView addFood(float x, FoodType foodType){

        final FoodObject food = new FoodObject(context,foodType);

        food.setImageResource(container.getFoodTypeDrawable(foodType));

        food.setLayoutParams(new FrameLayout.LayoutParams(160,160));

        food.setY(-40);
        food.setX(x);

        frame.addView(food);
        container.addFood(food);

        return food;
    }

}
