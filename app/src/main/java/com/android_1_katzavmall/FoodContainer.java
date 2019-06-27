package com.android_1_katzavmall;

import java.util.ArrayList;
import java.util.List;

public class FoodContainer {
    private List<FoodObject> flowerList = new ArrayList<>();
    private List<FoodType> ForbiddenList = new ArrayList<>();
    private List<FoodType> ShoppingList = new ArrayList<>();

    public FoodContainer(List<FoodType> ShoppingList, List<FoodType> ForbiddenList){
        this.ShoppingList.addAll(ShoppingList);
        this.ForbiddenList.addAll(ForbiddenList);
    }

    public  void addFlower(FoodObject flower){
        flowerList.add(flower);
    }

    public void removeFlower(FoodObject flower){
        flowerList.remove(flower);
    }

    public List<FoodObject> getFlowerList(){
        return flowerList;
    }

    public List<FoodType> getShoppingList() {
        return ShoppingList;
    }

    public List<FoodType> getForbiddenList() {
        return ForbiddenList;
    }
}
