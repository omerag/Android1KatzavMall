package com.android_1_katzavmall;

import java.util.ArrayList;
import java.util.List;

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

            case MILK: return R.drawable.em_milk;
            case WHITE_CHEESE: return R.drawable.em_cream_cheese;
            case HARD_CHEESE: return R.drawable.em_yellow_cheese;
            case CHOCOLATE: return R.drawable.em_chocolate_milk;
            case DANI: return R.drawable.em_yogurt;
            case STEAK: return R.drawable.em_steik;
            case CHICKEN: return R.drawable.em_chicken;
            case SAUSAGE: return R.drawable.em_hotdog;
            case SKEWER: return R.drawable.em_skewer;
            case SCHNITZEL: return R.drawable.em_schnitzel;
            case WATERMELON: return R.drawable.em_watermelon;
            case EGGPLANT: return R.drawable.em_eggplant;
            case BROCOLI: return R.drawable.em_broccoli;
            case ORANGE: return R.drawable.em_tangerine;
            case AVOKADO:return R.drawable.em_avocado;
            case BAGUETTE: return R.drawable.em_baguette;
            case DONUTS: return R.drawable.em_doughnut;
            case CROISSANT: return R.drawable.em_croissant;
            case BREAD: return R.drawable.em_bread;
            case BAGEL: return R.drawable.em_pretzel;
            case EGGS:return R.drawable.em_egg;
            case PANCAKES: return R.drawable.em_pancakes;
            case TOMATO: return R.drawable.em_tomato;
            case POTATO: return R.drawable.em_potato;
            case CUCAMBER: return R.drawable.em_cucumber;
            case BEER: return R.drawable.em_beer;
            case FISH: return R.drawable.em_fish;
            case POMEGRANATE: return R.drawable.em_pomegranate;
            case HONEY: return R.drawable.em_honey;
            case APPLE: return R.drawable.em_apple;
            case PEANUTS: return R.drawable.em_peanuts;
            case WINE: return R.drawable.em_wine;
            case CARROT: return R.drawable.em_carrot;
            case MATZA:return R.drawable.em_matzah;
            case CHOCOLATE_SPREAD: return R.drawable.em_chocolate_jar;
        }

        //foodType not found
        System.out.println("foodType NOT FOUND!!!!!");
        return 0;

    }
}
