package com.android_1_katzavmall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class LevelSelectActivity extends AppCompatActivity {

    LinearLayout milk_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.level_select_activity);

        milk_layout = findViewById(R.id.milk_layout);

        milk_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent levelIntent = new Intent(LevelSelectActivity.this, GameActivity.class);

                ArrayList<FoodType> shoppingList = new ArrayList<>();
                ArrayList<Integer> shoppingListCounts = new ArrayList<>();
                ArrayList<FoodType> forbiddenList = new ArrayList<>();

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

                levelIntent.putExtra("shoppingList",shoppingList);
                levelIntent.putExtra("shoppingListCounts",shoppingListCounts);
                levelIntent.putExtra("forbiddenList",forbiddenList);
                startActivity(levelIntent);
            }
        });
    }
}
