package com.android_1_katzavmall;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.content.res.ResourcesCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LevelSelectActivity extends AppCompatActivity {

    private TextView what_meal;

    private LinearLayout milkLayout;
    private LinearLayout meatLayout;
    private LinearLayout vegetarianLayout;
    private LinearLayout bakeryLayout;
    private LinearLayout breakfastLayout;
    private LinearLayout lunchLayout;
    private LinearLayout roshHashanaLayout;
    private LinearLayout passoverLayout;
    private LinearLayout customChoiceLayout;

    private TextView milkTV;
    private TextView meatTV;
    private TextView vegetarianTV;
    private TextView bakeryTV;
    private TextView breakfastTV;
    private TextView lunchTV;
    private TextView roshHashanaTV;
    private TextView passoverTV;
    private TextView customChoiceTV;

    private String shoppingListName;

    private ImageView meatLock;
    private ImageView vegetarianLock;
    private ImageView bakeryLock;
    private ImageView breakfastLock;
    private ImageView lunchLock;
    private ImageView roshHashanaLock;
    private ImageView passoverLock;
    private ImageView customLock;


    Button selectCustomDialogBtn;
    HomeWatcher mHomeWatcher;
    SharedPreferences sp;
    boolean isMusic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.level_select_activity);

        sp = getSharedPreferences("sp",MODE_PRIVATE);
        isMusic = sp.getBoolean("music",true);

        doBindService();
        if (isMusic)
        {
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
        }

        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();

        what_meal = findViewById(R.id.what_meal);

        milkLayout = findViewById(R.id.milk_layout);
        meatLayout = findViewById(R.id.meat_layout);
        vegetarianLayout = findViewById(R.id.vegetarian_layout);
        bakeryLayout = findViewById(R.id.bakery_layout);
        breakfastLayout = findViewById(R.id.breakfast_layout);
        lunchLayout = findViewById(R.id.lunch_layout);
        roshHashanaLayout = findViewById(R.id.rosh_hashana_layout);
        passoverLayout = findViewById(R.id.passover_layout);
        customChoiceLayout = findViewById(R.id.custom_layout);


        milkTV = findViewById(R.id.milk_tv);
        meatTV = findViewById(R.id.meat_tv);
        vegetarianTV = findViewById(R.id.vegetarian_tv);
        bakeryTV = findViewById(R.id.bakery_tv);
        breakfastTV = findViewById(R.id.breakfast_tv);
        lunchTV = findViewById(R.id.lunch_tv);
        roshHashanaTV = findViewById(R.id.rosh_hashana_tv);
        passoverTV = findViewById(R.id.passover_tv);
        customChoiceTV = findViewById(R.id.custom_tv);


        meatLock = findViewById(R.id.meat_lock);
        vegetarianLock = findViewById(R.id.vegetarian_lock);
        bakeryLock = findViewById(R.id.bakery_lock);
        breakfastLock = findViewById(R.id.breakfast_lock);
        lunchLock = findViewById(R.id.lunch_lock);
        roshHashanaLock = findViewById(R.id.rosh_hashana_lock);
        passoverLock = findViewById(R.id.passover_lock);
        customLock = findViewById(R.id.custom_lock);

        milkLayout.setOnClickListener(new selectLevelListener());
        meatLayout.setOnClickListener(new selectLevelListener());
        vegetarianLayout.setOnClickListener(new selectLevelListener());
        bakeryLayout.setOnClickListener(new selectLevelListener());
        breakfastLayout.setOnClickListener(new selectLevelListener());
        lunchLayout.setOnClickListener(new selectLevelListener());
        roshHashanaLayout.setOnClickListener(new selectLevelListener());
        passoverLayout.setOnClickListener(new selectLevelListener());

        customChoiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(LevelSelectActivity.this).create();
                final View dialogView = getLayoutInflater().inflate(R.layout.custom_game_dialog, null);

                Button selectCustomDialogBtn = dialogView.findViewById(R.id.select_btn);
                selectCustomDialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        PopupMenu popupMenu = new PopupMenu(LevelSelectActivity.this, view);
                        popupMenu.getMenuInflater().inflate(R.menu.groceries_menu, popupMenu.getMenu());
                        popupMenu.show();
                    }
                });


                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setView(dialogView);
                dialog.setCanceledOnTouchOutside(false);

                dialog.show();
            }

        });


        // font
        if (Locale.getDefault().toString().equals("iw_IL")) {
            Typeface typeface1 = ResourcesCompat.getFont(this, R.font.koby);
            what_meal.setTypeface(typeface1);
            Typeface typeface2 = ResourcesCompat.getFont(this, R.font.abraham);
            milkTV.setTypeface(typeface2);
            meatTV.setTypeface(typeface2);
            vegetarianTV.setTypeface(typeface2);
            bakeryTV.setTypeface(typeface2);
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
        meatLock.startAnimation(zoomBtnAnimation);
        vegetarianLayout.startAnimation(zoomBtnAnimation);
        vegetarianLock.startAnimation(zoomBtnAnimation);
        bakeryLayout.startAnimation(zoomBtnAnimation);
        bakeryLock.startAnimation(zoomBtnAnimation);
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
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.HARD_CHEESE);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.MILK);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.WHITE_CHEESE);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.CHOCOLATE);
                    shoppingListCounts.add(3*difficulty);

                    forbiddenList.add(FoodType.BAGEL);
                    forbiddenList.add(FoodType.CHICKEN);
                    forbiddenList.add(FoodType.FISH);
                    forbiddenList.add(FoodType.ORANGE);
                    forbiddenList.add(FoodType.BEER);
                    forbiddenList.add(FoodType.AVOKADO);
                    forbiddenList.add(FoodType.SAUSAGE);
                    forbiddenList.add(FoodType.CARROT);
                    forbiddenList.add(FoodType.EGGPLANT);
                    forbiddenList.add(FoodType.PEANUTS);
                    forbiddenList.add(FoodType.PANCAKES);
                    forbiddenList.add(FoodType.BROCOLI);


                    level_img_id = R.drawable.milk;
                    break;

                case R.id.meat_layout:
                    shoppingListName = meatTV.getText().toString();

                    shoppingList.add(FoodType.STEAK);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.SKEWER);
                    shoppingListCounts.add(5*difficulty);
                    shoppingList.add(FoodType.SCHNITZEL);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.CHICKEN);
                    shoppingListCounts.add(3*difficulty);
                    shoppingList.add(FoodType.SAUSAGE);
                    shoppingListCounts.add(6*difficulty);

                    forbiddenList.add(FoodType.EGGPLANT);
                    forbiddenList.add(FoodType.MILK);
                    forbiddenList.add(FoodType.WATERMELON);
                    forbiddenList.add(FoodType.HARD_CHEESE);
                    forbiddenList.add(FoodType.CHOCOLATE);
                    forbiddenList.add(FoodType.TOMATO);
                    forbiddenList.add(FoodType.DONUTS);
                    forbiddenList.add(FoodType.CHOCOLATE_SPREAD);
                    forbiddenList.add(FoodType.EGGS);
                    forbiddenList.add(FoodType.BEER);
                    forbiddenList.add(FoodType.DANI);
                    forbiddenList.add(FoodType.ORANGE);


                    level_img_id = R.drawable.meat;

                    break;

                case R.id.vegetarian_layout:
                    shoppingListName = vegetarianTV.getText().toString();

                    shoppingList.add(FoodType.WATERMELON);
                    shoppingListCounts.add(7*difficulty);
                    shoppingList.add(FoodType.ORANGE);
                    shoppingListCounts.add(6*difficulty);
                    shoppingList.add(FoodType.EGGPLANT);
                    shoppingListCounts.add(5*difficulty);
                    shoppingList.add(FoodType.BROCOLI);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.AVOKADO);
                    shoppingListCounts.add(5*difficulty);

                    forbiddenList.add(FoodType.CHICKEN);
                    forbiddenList.add(FoodType.CHOCOLATE);
                    forbiddenList.add(FoodType.EGGS);
                    forbiddenList.add(FoodType.PANCAKES);
                    forbiddenList.add(FoodType.CROISSANT);
                    forbiddenList.add(FoodType.STEAK);
                    forbiddenList.add(FoodType.SKEWER);
                    forbiddenList.add(FoodType.WINE);
                    forbiddenList.add(FoodType.FISH);
                    forbiddenList.add(FoodType.STEAK);
                    forbiddenList.add(FoodType.MILK);
                    forbiddenList.add(FoodType.BAGEL);


                    level_img_id = R.drawable.vegetarian;

                    break;

                case R.id.bakery_layout:
                    shoppingListName = bakeryTV.getText().toString();

                    shoppingList.add(FoodType.BAGEL);
                    shoppingListCounts.add(6*difficulty);
                    shoppingList.add(FoodType.DONUTS);
                    shoppingListCounts.add(5*difficulty);
                    shoppingList.add(FoodType.CROISSANT);
                    shoppingListCounts.add(7*difficulty);
                    shoppingList.add(FoodType.BREAD);
                    shoppingListCounts.add(3*difficulty);
                    shoppingList.add(FoodType.BAGUETTE);
                    shoppingListCounts.add(4*difficulty);

                    forbiddenList.add(FoodType.EGGPLANT);
                    forbiddenList.add(FoodType.AVOKADO);
                    forbiddenList.add(FoodType.WINE);
                    forbiddenList.add(FoodType.WHITE_CHEESE);
                    forbiddenList.add(FoodType.CHOCOLATE);
                    forbiddenList.add(FoodType.POMEGRANATE);
                    forbiddenList.add(FoodType.TOMATO);
                    forbiddenList.add(FoodType.CHOCOLATE_SPREAD);
                    forbiddenList.add(FoodType.EGGS);
                    forbiddenList.add(FoodType.SCHNITZEL);
                    forbiddenList.add(FoodType.SAUSAGE);
                    forbiddenList.add(FoodType.CUCAMBER);


                    level_img_id = R.drawable.bakery;

                    break;

                case R.id.breakfast_layout:
                    shoppingListName = breakfastTV.getText().toString();

                    shoppingList.add(FoodType.DANI);
                    shoppingListCounts.add(3*difficulty);
                    shoppingList.add(FoodType.PANCAKES);
                    shoppingListCounts.add(7*difficulty);
                    shoppingList.add(FoodType.EGGS);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.TOMATO);
                    shoppingListCounts.add(5*difficulty);
                    shoppingList.add(FoodType.BAGUETTE);
                    shoppingListCounts.add(3*difficulty);

                    forbiddenList.add(FoodType.STEAK);
                    forbiddenList.add(FoodType.WATERMELON);
                    forbiddenList.add(FoodType.SCHNITZEL);
                    forbiddenList.add(FoodType.FISH);
                    forbiddenList.add(FoodType.POTATO);
                    forbiddenList.add(FoodType.BEER);
                    forbiddenList.add(FoodType.ORANGE);
                    forbiddenList.add(FoodType.PEANUTS);
                    forbiddenList.add(FoodType.SKEWER);
                    forbiddenList.add(FoodType.EGGPLANT);
                    forbiddenList.add(FoodType.CUCAMBER);
                    forbiddenList.add(FoodType.SAUSAGE);



                    level_img_id = R.drawable.breakfast;

                    break;

                case R.id.lunch_layout:
                    shoppingListName = lunchTV.getText().toString();

                    shoppingList.add(FoodType.POTATO);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.TOMATO);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.CHICKEN);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.CUCAMBER);
                    shoppingListCounts.add(3*difficulty);
                    shoppingList.add(FoodType.STEAK);
                    shoppingListCounts.add(3*difficulty);
                    shoppingList.add(FoodType.BEER);
                    shoppingListCounts.add(5*difficulty);

                    forbiddenList.add(FoodType.MILK);
                    forbiddenList.add(FoodType.EGGS);
                    forbiddenList.add(FoodType.DONUTS);
                    forbiddenList.add(FoodType.AVOKADO);
                    forbiddenList.add(FoodType.CROISSANT);
                    forbiddenList.add(FoodType.DANI);
                    forbiddenList.add(FoodType.WHITE_CHEESE);
                    forbiddenList.add(FoodType.CHOCOLATE_SPREAD);
                    forbiddenList.add(FoodType.WATERMELON);
                    forbiddenList.add(FoodType.CHOCOLATE);
                    forbiddenList.add(FoodType.PEANUTS);
                    forbiddenList.add(FoodType.BAGUETTE);

                    level_img_id = R.drawable.lunch;

                    break;

                case R.id.rosh_hashana_layout:
                    shoppingListName = roshHashanaTV.getText().toString();

                    shoppingList.add(FoodType.WINE);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.POTATO);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.POMEGRANATE);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.HONEY);
                    shoppingListCounts.add(3*difficulty);
                    shoppingList.add(FoodType.FISH);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.CARROT);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.BREAD);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.APPLE);
                    shoppingListCounts.add(6*difficulty);

                    forbiddenList.add(FoodType.BAGUETTE);
                    forbiddenList.add(FoodType.STEAK);
                    forbiddenList.add(FoodType.ORANGE);
                    forbiddenList.add(FoodType.MILK);
                    forbiddenList.add(FoodType.SAUSAGE);
                    forbiddenList.add(FoodType.PANCAKES);
                    forbiddenList.add(FoodType.CUCAMBER);
                    forbiddenList.add(FoodType.EGGPLANT);
                    forbiddenList.add(FoodType.EGGS);
                    forbiddenList.add(FoodType.CHOCOLATE_SPREAD);
                    forbiddenList.add(FoodType.BROCOLI);
                    forbiddenList.add(FoodType.WATERMELON);

                    level_img_id = R.drawable.rosh_hashana;

                    break;

                case R.id.passover_layout:
                    shoppingListName = passoverTV.getText().toString();

                    shoppingList.add(FoodType.POTATO);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.STEAK);
                    shoppingListCounts.add(4*difficulty);
                    shoppingList.add(FoodType.PEANUTS);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.MATZA);
                    shoppingListCounts.add(7*difficulty);
                    shoppingList.add(FoodType.CHOCOLATE_SPREAD);
                    shoppingListCounts.add(5*difficulty);
                    shoppingList.add(FoodType.WINE);
                    shoppingListCounts.add(2*difficulty);
                    shoppingList.add(FoodType.EGGS);
                    shoppingListCounts.add(3*difficulty);
                    shoppingList.add(FoodType.CHICKEN);
                    shoppingListCounts.add(5*difficulty);

                    forbiddenList.add(FoodType.FISH);
                    forbiddenList.add(FoodType.BREAD);
                    forbiddenList.add(FoodType.DONUTS);
                    forbiddenList.add(FoodType.PANCAKES);
                    forbiddenList.add(FoodType.BEER);
                    forbiddenList.add(FoodType.POMEGRANATE);
                    forbiddenList.add(FoodType.BROCOLI);
                    forbiddenList.add(FoodType.BAGUETTE);
                    forbiddenList.add(FoodType.CROISSANT);
                    forbiddenList.add(FoodType.AVOKADO);
                    forbiddenList.add(FoodType.APPLE);
                    forbiddenList.add(FoodType.BAGEL);

                    level_img_id = R.drawable.passover;

                    break;

                case R.id.custom_layout:
                    shoppingListName = customChoiceTV.getText().toString();
                    level_img_id = R.drawable.custom;

                    forbiddenList.add(FoodType.EGGPLANT);
                    forbiddenList.add(FoodType.BAGEL);
                    forbiddenList.add(FoodType.HONEY);
                    forbiddenList.add(FoodType.POMEGRANATE);
                    forbiddenList.add(FoodType.CUCAMBER);
                    break;
            }

            //String shoppingListStr = Integer.toString(shoppingListIntName);
            levelIntent.putExtra("shoppingListStr", shoppingListName);
            levelIntent.putExtra("shoppingList",shoppingList);
            levelIntent.putExtra("shoppingListCounts",shoppingListCounts);
            levelIntent.putExtra("forbiddenList",forbiddenList);
            levelIntent.putExtra("level_img",level_img_id);
            mServ.pauseMusic();
            mHomeWatcher.stopWatch();
            startActivity(levelIntent);

        }

    }

    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null && isMusic) {
            mHomeWatcher.startWatch();
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        stopService(music);

    }

    @Override
    protected void onPause() {
        super.onPause();

        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        //  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        //  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}
