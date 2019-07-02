package com.android_1_katzavmall;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import static com.android_1_katzavmall.FoodType.MILK;
import static com.android_1_katzavmall.FoodType.WHITE_CHEESE;

public class FoodContainer {
    private List<FoodObject> foodList = new ArrayList<>();
    private List<FoodType> ForbiddenList = new ArrayList<>();
    private List<FoodType> ShoppingList = new ArrayList<>();
    private List<FoodType> StaticShoppingList = new ArrayList<>();

    public FoodContainer(List<FoodType> ShoppingList, List<FoodType> ForbiddenList){
        this.ShoppingList.addAll(ShoppingList);
        this.ForbiddenList.addAll(ForbiddenList);
        this.StaticShoppingList.addAll(ShoppingList);
    }

    public  void addFood(FoodObject food){
        foodList.add(food);
    }

    public void removeFood(FoodObject food){
        foodList.remove(food);
    }

    public List<FoodObject> getFoodList(){
        return foodList;
    }

    public List<FoodType> getStaticShoppingList() {
        return StaticShoppingList;
    }

    public List<FoodType> getShoppingList() {
        return ShoppingList;
    }

    public List<FoodType> getForbiddenList() {
        return ForbiddenList;
    }



    public int getFoodTypeDrawable(FoodType foodType){


        if(foodType == null){
            System.out.println("foodType = null!!!!!!!");
            return 0;
        }
        else{
            System.out.println("" + foodType + "is fine");
        }

        switch (foodType){

            case MILK: return R.drawable.milk_em;
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
            case BAGUETTE: return R.drawable.baguette_em;
            case DONUTS: return R.drawable.donutes_em;
            case CROISSANT:
                break;
            case BREAD:
                break;
            case BAGEL:
                break;
            case EGGS:
                break;
            case PANCAKES: return R.drawable.pancakes_em;
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
        }

        //foodType not found
        System.out.println("foodType NOT FOUND!!!!!");
        return 0;

    }
}
