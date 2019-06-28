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
}
