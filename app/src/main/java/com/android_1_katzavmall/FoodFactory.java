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

        switch (foodType){

            case MILK:
                break;
            case WHITE_CHEESE:
                break;
            case HARD_CHEESE:
                break;
            case CHOCOLATE:
                break;
            case DANI:
                break;
            case STEAK:
                break;
            case CHICKEN:
                break;
            case SAUSAGE:
                break;
            case HAMBURGER:
                break;
            case SCHNITZEL:
                break;
            case WATERMELON:
                break;
            case EGGPLANT:
                break;
            case BROCOLI:
                break;
            case ORANGE:
                break;
            case AVOKADO:
                break;
            case BAGUETTE:
                break;
            case DONUTS:
                break;
            case CROISSANT:
                break;
            case BREAD:
                break;
            case BAGEL:
                break;
            case EGGS:
                break;
            case PANCAKES:
                break;
            case TOMATO:
                break;
            case POTATO:
                break;
            case CUCAMBER:
                break;
            case BEER:
                break;
            case FISH:
                break;
            case POMEGRANATE:
                break;
            case HONEY:
                break;
            case APPLE:
                break;
            case CHALLAH:
                break;
            case WINE:
                break;
            case CARROT:
                break;
            case MATZA:
                break;
            case CHOCOLATE_SPREAD:
                break;
            case BEETS:
                break;
            default:
                break;
        }



        food.setLayoutParams(new FrameLayout.LayoutParams(100,100));

        food.setY(-40);
        food.setX(x);

        frame.addView(food);
        container.addFood(food);

        return food;
    }

}
