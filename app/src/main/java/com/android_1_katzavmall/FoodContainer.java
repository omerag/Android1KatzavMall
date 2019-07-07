package com.android_1_katzavmall;

import java.util.ArrayList;
import java.util.List;

public class FoodContainer {
    private List<FoodObject> foodList = new ArrayList<>();
    private List<FoodType> forbiddenList = new ArrayList<>();
    private List<FoodType> shoppingList = new ArrayList<>();
    private List<FoodType> staticShoppingList = new ArrayList<>();

    public FoodContainer(List<FoodType> ShoppingList, List<FoodType> ForbiddenList){
        this.shoppingList.addAll(ShoppingList);
        this.forbiddenList.addAll(ForbiddenList);
        this.staticShoppingList.addAll(ShoppingList);
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
        return staticShoppingList;
    }

    public List<FoodType> getShoppingList() {
        return shoppingList;
    }

    public List<FoodType> getForbiddenList() {
        return forbiddenList;
    }

    public void resetShoppingList(){
        shoppingList.clear();
        this.shoppingList.addAll(staticShoppingList);
    }

    public int getFoodTypeDrawable(FoodType foodType){

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
            case PIE: return R.drawable.em_pie;
            case LOLLIPOP: return R.drawable.em_lollipop;
            case ICE_CREAM: return R.drawable.em_ice_cream;
            case CUPCAKE: return R.drawable.em_cupcake;
            case COOKIE: return R.drawable.em_cookie;
            case CHOCOLATE_BAR: return R.drawable.em_chocolate_bar;
            case CANDY: return R.drawable.em_candy;
            case BIRTHDAY_CAKE: return R.drawable.em_birthday_cake;
        }

        //foodType not found
        System.out.println("foodType NOT FOUND!!!!!");
        return 0;

    }
}
