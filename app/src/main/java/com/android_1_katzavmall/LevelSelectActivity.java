package com.android_1_katzavmall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LevelSelectActivity extends AppCompatActivity {

    LinearLayout milkLayout;
    LinearLayout meatLayout;
    LinearLayout vegetatianLayout;
    LinearLayout breadsLayout;
    LinearLayout breakfastLayout;
    LinearLayout lunchLayout;
    LinearLayout roshHashanaLayout;
    LinearLayout passoverLayout;
    LinearLayout customChoiceLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.level_select_activity);

        milkLayout = findViewById(R.id.milk_layout);
        meatLayout = findViewById(R.id.meat_layout);
        vegetatianLayout = findViewById(R.id.vegetatian_layout);
        breadsLayout = findViewById(R.id.breads_layout);
        breakfastLayout = findViewById(R.id.breakfast_layout);
        lunchLayout = findViewById(R.id.lunch_layout);
        roshHashanaLayout = findViewById(R.id.rosh_hashana_layout);
        passoverLayout = findViewById(R.id.passover_layout);
        customChoiceLayout = findViewById(R.id.custom_layout);

        milkLayout.setOnClickListener(new selectLevelListener());
        meatLayout.setOnClickListener(new selectLevelListener());
        vegetatianLayout.setOnClickListener(new selectLevelListener());
        breadsLayout.setOnClickListener(new selectLevelListener());
        breakfastLayout.setOnClickListener(new selectLevelListener());
        lunchLayout.setOnClickListener(new selectLevelListener());
        roshHashanaLayout.setOnClickListener(new selectLevelListener());
        passoverLayout.setOnClickListener(new selectLevelListener());
    }

    private class selectLevelListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent levelIntent = new Intent(LevelSelectActivity.this, GameActivity.class);

            ArrayList<FoodType> shoppingList = new ArrayList<>();
            ArrayList<Integer> shoppingListCounts = new ArrayList<>();
            ArrayList<FoodType> forbiddenList = new ArrayList<>();


            switch (view.getId()){
                case R.id.milk_layout:
                    shoppingList.add(FoodType.DANI);
                    shoppingListCounts.add(5);
                    shoppingList.add(FoodType.HARD_CHEESE);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.MILK);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.WHITE_CHEESE);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.CHOCOLATE);
                    shoppingListCounts.add(1);

                    forbiddenList.add(FoodType.BAGUETTE);
                    forbiddenList.add(FoodType.STEAK);
                    forbiddenList.add(FoodType.ORANGE);
                    break;
                case R.id.meat_layout:
                    shoppingList.add(FoodType.STEAK);
                    shoppingListCounts.add(1);
                    shoppingList.add(FoodType.SKEWER);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.SCHNITZEL);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.CHICKEN);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.SAUSAGE);
                    shoppingListCounts.add(8);

                    forbiddenList.add(FoodType.BAGUETTE);
                    forbiddenList.add(FoodType.MILK);
                    forbiddenList.add(FoodType.ORANGE);

                    break;
                case R.id.vegetatian_layout:
                    shoppingList.add(FoodType.WATERMELON);
                    shoppingListCounts.add(5);
                    shoppingList.add(FoodType.ORANGE);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.EGGPLANT);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.BROCOLI);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.AVOKADO);
                    shoppingListCounts.add(1);

                    forbiddenList.add(FoodType.CHICKEN);
                    forbiddenList.add(FoodType.HARD_CHEESE);
                    forbiddenList.add(FoodType.EGGS);
                    break;
                case R.id.breads_layout:
                    shoppingList.add(FoodType.BAGEL);
                    shoppingListCounts.add(5);
                    shoppingList.add(FoodType.DONUTS);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.CROISSANT);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.BREAD);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.BAGUETTE);
                    shoppingListCounts.add(1);

                    forbiddenList.add(FoodType.EGGPLANT);
                    forbiddenList.add(FoodType.AVOKADO);
                    forbiddenList.add(FoodType.WINE);
                    break;
                case R.id.breakfast_layout:
                    shoppingList.add(FoodType.DANI);
                    shoppingListCounts.add(5);
                    shoppingList.add(FoodType.TOMATO);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.PANCAKES);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.EGGS);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.BAGUETTE);
                    shoppingListCounts.add(1);

                    forbiddenList.add(FoodType.STEAK);
                    forbiddenList.add(FoodType.WATERMELON);
                    forbiddenList.add(FoodType.SCHNITZEL);
                    break;
                case R.id.lunch_layout:
                    shoppingList.add(FoodType.TOMATO);
                    shoppingListCounts.add(5);
                    shoppingList.add(FoodType.STEAK);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.POTATO);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.CUCAMBER);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.CHICKEN);
                    shoppingListCounts.add(1);
                    shoppingList.add(FoodType.BEER);
                    shoppingListCounts.add(6);

                    forbiddenList.add(FoodType.MILK);
                    forbiddenList.add(FoodType.EGGS);
                    forbiddenList.add(FoodType.DONUTS);
                    break;
                case R.id.rosh_hashana_layout:
                    shoppingList.add(FoodType.WINE);
                    shoppingListCounts.add(5);
                    shoppingList.add(FoodType.POTATO);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.POMEGRANATE);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.HONEY);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.FISH);
                    shoppingListCounts.add(1);
                    shoppingList.add(FoodType.CARROT);
                    shoppingListCounts.add(1);
                    shoppingList.add(FoodType.BREAD);
                    shoppingListCounts.add(1);
                    shoppingList.add(FoodType.APPLE);
                    shoppingListCounts.add(1);

                    forbiddenList.add(FoodType.BAGUETTE);
                    forbiddenList.add(FoodType.STEAK);
                    forbiddenList.add(FoodType.ORANGE);
                    break;
                case R.id.passover_layout:
                    shoppingList.add(FoodType.WINE);
                    shoppingListCounts.add(5);
                    shoppingList.add(FoodType.POTATO);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.STEAK);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.PEANUTS);
                    shoppingListCounts.add(2);
                    shoppingList.add(FoodType.MATZA);
                    shoppingListCounts.add(1);
                    shoppingList.add(FoodType.EGGS);
                    shoppingListCounts.add(1);
                    shoppingList.add(FoodType.CHOCOLATE);
                    shoppingListCounts.add(1);
                    shoppingList.add(FoodType.CHICKEN);
                    shoppingListCounts.add(1);

                    forbiddenList.add(FoodType.BAGUETTE);
                    forbiddenList.add(FoodType.BREAD);
                    forbiddenList.add(FoodType.DONUTS);
                    break;
                case R.id.custom_layout:

                    break;
            }

            levelIntent.putExtra("shoppingList",shoppingList);
            levelIntent.putExtra("shoppingListCounts",shoppingListCounts);
            levelIntent.putExtra("forbiddenList",forbiddenList);
            startActivity(levelIntent);
        }
    }
}
