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

                shoppingList.add(FoodType.DONUTS);
                shoppingListCounts.add(10);
                shoppingList.add(FoodType.PANCAKES);
                shoppingListCounts.add(13);

                forbiddenList.add(FoodType.BAGUETTE);
                forbiddenList.add(FoodType.MILK);

                levelIntent.putExtra("shoppingList",shoppingList);
                levelIntent.putExtra("shoppingListCounts",shoppingListCounts);
                levelIntent.putExtra("forbiddenList",forbiddenList);
                startActivity(levelIntent);
            }
        });
    }
}
