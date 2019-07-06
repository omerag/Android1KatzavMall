package com.android_1_katzavmall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Locale;

public class LevelSelectActivity extends AppCompatActivity {

    private TextView what_meal;

    private LinearLayout milkLayout;
    private LinearLayout meatLayout;
    private LinearLayout vegetarianLayout;
    private LinearLayout backeryLayout;
    private LinearLayout breakfastLayout;
    private LinearLayout lunchLayout;
    private LinearLayout roshHashanaLayout;
    private LinearLayout passoverLayout;
    private LinearLayout customChoiceLayout;

    private TextView milkTV;
    private TextView meatTV;
    private TextView vegetarianTV;
    private TextView backeryTV;
    private TextView breakfastTV;
    private TextView lunchTV;
    private TextView roshHashanaTV;
    private TextView passoverTV;
    private TextView customChoiceTV;

    private String shoppingListName;

    private ImageView breakfastLock;
    private ImageView lunchLock;
    private ImageView roshHashanaLock;
    private ImageView passoverLock;
    private ImageView customLock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.level_select_activity);

        what_meal = findViewById(R.id.what_meal);

        milkLayout = findViewById(R.id.milk_layout);
        meatLayout = findViewById(R.id.meat_layout);
        vegetarianLayout = findViewById(R.id.vegetarian_layout);
        backeryLayout = findViewById(R.id.backery_layout);
        breakfastLayout = findViewById(R.id.breakfast_layout);
        lunchLayout = findViewById(R.id.lunch_layout);
        roshHashanaLayout = findViewById(R.id.rosh_hashana_layout);
        passoverLayout = findViewById(R.id.passover_layout);
        customChoiceLayout = findViewById(R.id.custom_layout);

        milkTV = findViewById(R.id.milk_tv);
        meatTV = findViewById(R.id.meat_tv);
        vegetarianTV = findViewById(R.id.vegetarian_tv);
        backeryTV = findViewById(R.id.bakery_tv);
        breakfastTV = findViewById(R.id.breakfast_tv);
        lunchTV = findViewById(R.id.lunch_tv);
        roshHashanaTV = findViewById(R.id.rosh_hashana_tv);
        passoverTV = findViewById(R.id.passover_tv);
        customChoiceTV = findViewById(R.id.custom_tv);

        breakfastLock = findViewById(R.id.breakfast_lock);
        lunchLock = findViewById(R.id.lunch_lock);
        roshHashanaLock = findViewById(R.id.rosh_hashana_lock);
        passoverLock = findViewById(R.id.passover_lock);
        customLock = findViewById(R.id.custom_lock);

        milkLayout.setOnClickListener(new selectLevelListener());
        meatLayout.setOnClickListener(new selectLevelListener());
        vegetarianLayout.setOnClickListener(new selectLevelListener());
        backeryLayout.setOnClickListener(new selectLevelListener());
        breakfastLayout.setOnClickListener(new selectLevelListener());
        lunchLayout.setOnClickListener(new selectLevelListener());
        roshHashanaLayout.setOnClickListener(new selectLevelListener());
        passoverLayout.setOnClickListener(new selectLevelListener());


        // font
        if (Locale.getDefault().toString().equals("iw_IL")) {
            Typeface typeface1 = ResourcesCompat.getFont(this, R.font.koby);
            what_meal.setTypeface(typeface1);
            Typeface typeface2 = ResourcesCompat.getFont(this, R.font.abraham);
            milkTV.setTypeface(typeface2);
            meatTV.setTypeface(typeface2);
            vegetarianTV.setTypeface(typeface2);
            backeryTV.setTypeface(typeface2);
            breakfastTV.setTypeface(typeface2);
            lunchTV.setTypeface(typeface2);
            roshHashanaTV.setTypeface(typeface2);
            passoverTV.setTypeface(typeface2);
            customChoiceTV.setTypeface(typeface2);
        }

        // ZoomIn/Out animation:
        final Animation zoomBtnAnimation = AnimationUtils.loadAnimation(this, R.anim.level_slct_btns_anim);
        milkLayout.startAnimation(zoomBtnAnimation);
        meatLayout.startAnimation(zoomBtnAnimation);
        vegetarianLayout.startAnimation(zoomBtnAnimation);
        backeryLayout.startAnimation(zoomBtnAnimation);
        breakfastLayout.startAnimation(zoomBtnAnimation);
        breakfastLock.startAnimation(zoomBtnAnimation);
        lunchLayout.startAnimation(zoomBtnAnimation);
        lunchLock.startAnimation(zoomBtnAnimation);
        roshHashanaLayout.startAnimation(zoomBtnAnimation);
        roshHashanaLock.startAnimation(zoomBtnAnimation);
        passoverLayout.startAnimation(zoomBtnAnimation);
        passoverLock.startAnimation(zoomBtnAnimation);
        customChoiceLayout.startAnimation(zoomBtnAnimation);
        customLock.startAnimation(zoomBtnAnimation);

    }

    private class selectLevelListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent levelIntent = new Intent(LevelSelectActivity.this, GameActivity.class);

            SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
            String difficultyStr = sp.getString("difficulty","");
            int difficulty = 1;

            if(difficultyStr.equalsIgnoreCase("medium")){
                difficulty = 2;
            }
            else if (difficultyStr.equalsIgnoreCase("hard")){
                difficulty = 3;
            }


            ArrayList<FoodType> shoppingList = new ArrayList<>();
            ArrayList<Integer> shoppingListCounts = new ArrayList<>();
            ArrayList<FoodType> forbiddenList = new ArrayList<>();
            int level_img_id = 0;


            switch (view.getId()){
                case R.id.milk_layout:
                    shoppingListName = milkTV.getText().toString();

                    shoppingList.add(FoodType.DANI);
                    shoppingListCounts.add(5*difficulty);
                    shoppingList.add(FoodType.HARD_CHEESE);
                    shoppingListCounts.add(3*difficulty);
                    shoppingList.add(FoodType.MILK);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.WHITE_CHEESE);
                    shoppingListCounts.add(3*difficulty);
                    shoppingList.add(FoodType.CHOCOLATE);
                    shoppingListCounts.add(1*difficulty);

                    forbiddenList.add(FoodType.BAGUETTE);
                    forbiddenList.add(FoodType.STEAK);
                    forbiddenList.add(FoodType.ORANGE);

                    level_img_id = R.drawable.milk;
                    break;

                case R.id.meat_layout:
                    shoppingListName = meatTV.getText().toString();

                    shoppingList.add(FoodType.STEAK);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.SKEWER);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.SCHNITZEL);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.CHICKEN);
                    shoppingListCounts.add(3*difficulty);
                    shoppingList.add(FoodType.SAUSAGE);
                    shoppingListCounts.add(8*difficulty);

                    forbiddenList.add(FoodType.BAGUETTE);
                    forbiddenList.add(FoodType.MILK);
                    forbiddenList.add(FoodType.ORANGE);

                    level_img_id = R.drawable.meat;

                    break;

                case R.id.vegetarian_layout:
                    shoppingListName = vegetarianTV.getText().toString();

                    shoppingList.add(FoodType.WATERMELON);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.ORANGE);
                    shoppingListCounts.add(7*difficulty);
                    shoppingList.add(FoodType.EGGPLANT);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.BROCOLI);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.AVOKADO);
                    shoppingListCounts.add(5*difficulty);

                    forbiddenList.add(FoodType.CHICKEN);
                    forbiddenList.add(FoodType.HARD_CHEESE);
                    forbiddenList.add(FoodType.EGGS);

                    level_img_id = R.drawable.vegetarian;

                    break;

                case R.id.backery_layout:
                    shoppingListName = backeryTV.getText().toString();

                    shoppingList.add(FoodType.BAGEL);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.DONUTS);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.CROISSANT);
                    shoppingListCounts.add(3*difficulty);
                    shoppingList.add(FoodType.BREAD);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.BAGUETTE);
                    shoppingListCounts.add(6*difficulty);

                    forbiddenList.add(FoodType.EGGPLANT);
                    forbiddenList.add(FoodType.AVOKADO);
                    forbiddenList.add(FoodType.WINE);

                    level_img_id = R.drawable.bakery;

                    break;

                case R.id.breakfast_layout:
                    shoppingListName = breakfastTV.getText().toString();

                    shoppingList.add(FoodType.DANI);
                    shoppingListCounts.add(5*difficulty);
                    shoppingList.add(FoodType.TOMATO);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.PANCAKES);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.EGGS);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.BAGUETTE);
                    shoppingListCounts.add(3*difficulty);

                    forbiddenList.add(FoodType.STEAK);
                    forbiddenList.add(FoodType.WATERMELON);
                    forbiddenList.add(FoodType.SCHNITZEL);

                    level_img_id = R.drawable.breakfast;

                    break;

                case R.id.lunch_layout:
                    shoppingListName = lunchTV.getText().toString();

                    shoppingList.add(FoodType.TOMATO);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.STEAK);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.POTATO);
                    shoppingListCounts.add(5*difficulty);
                    shoppingList.add(FoodType.CUCAMBER);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.CHICKEN);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.BEER);
                    shoppingListCounts.add(6*difficulty);

                    forbiddenList.add(FoodType.MILK);
                    forbiddenList.add(FoodType.EGGS);
                    forbiddenList.add(FoodType.DONUTS);

                    level_img_id = R.drawable.lunch;

                    break;

                case R.id.rosh_hashana_layout:
                    shoppingListName = roshHashanaTV.getText().toString();

                    shoppingList.add(FoodType.WINE);
                    shoppingListCounts.add(3*difficulty);
                    shoppingList.add(FoodType.POTATO);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.POMEGRANATE);
                    shoppingListCounts.add(5*difficulty);
                    shoppingList.add(FoodType.HONEY);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.FISH);
                    shoppingListCounts.add(1*difficulty);
                    shoppingList.add(FoodType.CARROT);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.BREAD);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.APPLE);
                    shoppingListCounts.add(8*difficulty);

                    forbiddenList.add(FoodType.BAGUETTE);
                    forbiddenList.add(FoodType.STEAK);
                    forbiddenList.add(FoodType.ORANGE);

                    level_img_id = R.drawable.rosh_hashana;

                    break;

                case R.id.passover_layout:
                    shoppingListName = passoverTV.getText().toString();

                    shoppingList.add(FoodType.WINE);
                    shoppingListCounts.add(3*difficulty);
                    shoppingList.add(FoodType.POTATO);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.STEAK);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.PEANUTS);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.MATZA);
                    shoppingListCounts.add(10*difficulty);
                    shoppingList.add(FoodType.EGGS);
                    shoppingListCounts.add(6*difficulty);
                    shoppingList.add(FoodType.CHOCOLATE_SPREAD);
                    shoppingListCounts.add(1*difficulty);
                    shoppingList.add(FoodType.CHICKEN);
                    shoppingListCounts.add(4*difficulty);

                    forbiddenList.add(FoodType.BAGUETTE);
                    forbiddenList.add(FoodType.BREAD);
                    forbiddenList.add(FoodType.DONUTS);

                    level_img_id = R.drawable.passover;

                    break;

                case R.id.custom_layout:
                    shoppingListName = customChoiceTV.getText().toString();
                    level_img_id = R.drawable.custom;

                    break;
            }

            //String shoppingListStr = Integer.toString(shoppingListIntName);
            levelIntent.putExtra("shoppingListStr", shoppingListName);
            levelIntent.putExtra("shoppingList",shoppingList);
            levelIntent.putExtra("shoppingListCounts",shoppingListCounts);
            levelIntent.putExtra("forbiddenList",forbiddenList);
            levelIntent.putExtra("level_img",level_img_id);
            startActivity(levelIntent);
        }
    }
}
