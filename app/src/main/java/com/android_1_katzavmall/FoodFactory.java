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

        final FoodObject flower = new FoodObject(context,foodType);

        switch (foodType){

            case food1:
                flower.setImageResource(R.drawable.ic_flower_purple);
                break;

            case food2:
                flower.setImageResource(R.drawable.ic_flower_red);
                break;

            case food3:
                flower.setImageResource(R.drawable.ic_flower_orange);
                break;

            case food4:
                flower.setImageResource(R.drawable.ic_flower_blue);
                break;

            default:
                break;
        }



        flower.setLayoutParams(new FrameLayout.LayoutParams(100,100));

        flower.setY(-40);
        flower.setX(x);

        frame.addView(flower);
        container.addFlower(flower);

        return flower;
    }

}
