package com.android_1_katzavmall;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private LinearLayout groceriesLayout;
    private LinearLayout level_name_layout;
    private TextView scoreLabel;
    private TextView scoreTV;
    private TextView startLabel;
    private TextView levelName;
    private ImageView cart;
    private FrameLayout frame;
    boolean isCartAnimated = false;
    private List<HighScore> highScores;

    private FoodContainer container;
    private FoodFactory factory;

    private Handler handler = new Handler();
    private Timer timer = new Timer();


    ////////////
    private SensorManager mSensorManager;
    private Sensor accelerometer;


    float[] accelerometerData = new float[3];

    private float lastX = 0;
    private float deltaX = 0;
    private float deltaXMax = 0;
    private float move = 0;


    private int shopCounter = 180;
    private int shopCounterReset = shopCounter;
    private boolean shopFlag = false;

    private long lastUpdate = 0;


    ////////////////

    private int screenHeight;
    private int screenWidth;

    private boolean startFlag = false;
    private boolean moveRight = false;
    private boolean moveLeft = false;
    private boolean isResumed = false;

    private int score = 0;
    private int lives = 3;

    private List<FoodType> shoppingList;
    private List<Integer> shoppingListCounts;

    private int[] foodStatusArray = {R.id.foodStatus0, R.id.foodStatus1, R.id.foodStatus2, R.id.foodStatus3,
            R.id.foodStatus4, R.id.foodStatus5, R.id.foodStatus6, R.id.foodStatus7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);

        groceriesLayout = findViewById(R.id.groceriesLayout);
        level_name_layout = findViewById(R.id.level_name_layout);
        scoreLabel = findViewById(R.id.scoreLabel);
        scoreTV = findViewById(R.id.scoreTV);

        startLabel = findViewById(R.id.startLabel);
        levelName = findViewById(R.id.level_name_tv);
        cart = findViewById(R.id.cart);
        frame = findViewById(R.id.frame);


        //Bounce animation:
        final Animation bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.groceries_list_anim);
        BounceInterpolator interpolator = new BounceInterpolator(0.5, 10);
        bounceAnimation.setInterpolator(interpolator);
        groceriesLayout.startAnimation(bounceAnimation);

        // ZoomIn animation:
        final Animation zoomAnimation = AnimationUtils.loadAnimation(this, R.anim.level_name_anim);
        level_name_layout.startAnimation(zoomAnimation);



        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;


        //getting shoppingListName shoppingList and forbiddenList from previous activity
        String shoppingListStr = getIntent().getStringExtra("shoppingListStr");
        shoppingList = (ArrayList<FoodType> )getIntent().getSerializableExtra("shoppingList");
        shoppingListCounts = getIntent().getIntegerArrayListExtra("shoppingListCounts");
        List<FoodType> forbiddenList = (ArrayList<FoodType> )getIntent().getSerializableExtra("forbiddenList");

        levelName.setText("" + shoppingListStr);

        container = new FoodContainer(shoppingList,forbiddenList);
        factory = new FoodFactory(this,container,frame);

        setShoppingListLayout(shoppingList,shoppingListCounts);


        ///////////

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = Objects.requireNonNull(mSensorManager).getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(GameActivity.this,accelerometer,SensorManager.SENSOR_DELAY_GAME);

        //////////

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(startFlag && lives > 0){
                            updatePositions();
                            createFood();
                            winCheck();
                        }

                    }
                });

            }
        },0,16);

    }

    @Override
    protected void onPause() {

        mSensorManager.unregisterListener(this);
        super.onPause();
        startFlag = false;
        isResumed = true;
    }

    @Override
    protected void onResume() {

        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
        if(isResumed){
            startFlag = true;
            startLabel.setVisibility(View.GONE);
            isResumed = false;
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerData = event.values;
            //     cart.onSensorEvent(event);

            long curTime = System.currentTimeMillis();


            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float deltaX = Math.abs(event.values[0] - lastX);
                if (deltaX < 2.0f ) {

                    move = event.values[0];
                }
                else{
                    move = 0;
                }
                lastX = event.values[0];
            }


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    public synchronized void updatePositions(){

        //move cart with accelerometer
        if(cart.getX() < 0){
            cart.setX(0);
        }
        else if(cart.getX() + cart.getWidth() > screenWidth){
            cart.setX(screenWidth - cart.getWidth());
        }
        else{
            cart.setX(cart.getX() + move*(-1)*10);
        }


        //move food
        List<FoodObject> foodList = container.getFoodList();
        for(int i = 0; i < foodList.size(); i++ ){
            final FoodObject foodObject = foodList.get(i);

            if(checkCollision(cart,foodObject)){
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(container.getShoppingList().contains(foodObject.getType())){
                            score++;
                            updateFoodStatus(foodObject.getType());
                        }
                        else{
                            updateLives();

                        }
                        scoreTV.setText("" + score);
                        frame.removeView(foodObject);
                        container.removeFood(foodObject);
                    }
                });
            }
            else if((int)foodObject.getY() > screenHeight){

                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(container.getShoppingList().contains(foodObject.getType())){
                            updateLives();
                            scoreTV.setText("" + score);

                        }

                        frame.removeView(foodObject);
                        container.removeFood(foodObject);
                    }
                });

            }
            else if(!foodObject.isAnimated()){
                ObjectAnimator animation = ObjectAnimator.ofFloat(foodObject, "translationY", screenHeight);
                long velFood = 4000;
                animation.setDuration(velFood);
                animation.setInterpolator(new AccelerateInterpolator());
                animation.start();
                foodObject.setAnimated(true);
                // foodObject.setY(foodObject.getY() + velFlower);
            }

        }

    }


    private synchronized void createFood(){


        shopCounter--;
        if (shopCounter < 1){
            shopCounter = shopCounterReset;

            if(shopFlag){
                shopFlag =false;
                this.runOnUiThread(new Runnable() {
                    final Random rand = new Random();

                    @Override
                    public void run() {

                        int size = container.getShoppingList().size();

                        if(size == 0){
                            return;
                        }

                        int pick = rand.nextInt(size);

                        int x = ((rand.nextInt(100)*10 + 40) % (screenWidth - cart.getWidth())); //new flowers wont be too close to each other
                        factory.addFood(x,container.getShoppingList().get(pick));
                    }
                });
            }else{
                shopFlag = true;
                this.runOnUiThread(new Runnable() {
                    final Random rand = new Random();

                    @Override
                    public void run() {


                        int pick = rand.nextInt(container.getForbiddenList().size());

                        int x = rand.nextInt(screenWidth - cart.getWidth());
                        factory.addFood(x,container.getForbiddenList().get(pick));
                    }
                });
            }

        }

 /*
        //create item from shopping list
        if(System.currentTimeMillis() % 1000 < 5){
            final Random rand = new Random();
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    int size = container.getShoppingList().size();

                    if(size == 0){
                        return;
                    }

                    int pick = rand.nextInt(size);

                    int x = ((rand.nextInt(100)*10 + 40) % (screenWidth - cart.getWidth())); //new flowers wont be too close to each other
                    factory.addFood(x,container.getShoppingList().get(pick));
                }
            });
        }else if (System.currentTimeMillis() % 1000 > 500 && System.currentTimeMillis() % 1000 < 505){
            //create item from forbidden list

            final Random rand = new Random();
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    int pick = rand.nextInt(container.getForbiddenList().size());

                    int x = rand.nextInt(screenWidth - cart.getWidth());
                    factory.addFood(x,container.getForbiddenList().get(pick));
                }
            });
        }

        */

    }

    private boolean checkCollision(View v1,View v2) {
        Rect rect1 = new Rect();
        Rect rect2 = new Rect();
        v1.getHitRect(rect1);
        v2.getHitRect(rect2);
        return Rect.intersects(rect1,rect2);
    }

    private void winCheck(){

        boolean win = true;
        TextView foodStatus;
        for(int i = 0; i < container.getStaticShoppingList().size();i++){
            foodStatus = findViewById(foodStatusArray[i]);
            if(!foodStatus.getText().toString().equals("") && Integer.parseInt(foodStatus.getText().toString()) > 0){
                win = false;
            }
        }

        if(win){
            startFlag = false;

            if (isNewHighScore(score))
            {
                final AlertDialog dialog = new AlertDialog.Builder(GameActivity.this).create();
                final View dialogView = getLayoutInflater().inflate(R.layout.win_new_highscore_dialog, null);

                Button saveScoreBtn = dialogView.findViewById(R.id.save_score_btn);
                Button cancelBtn = dialogView.findViewById(R.id.cancel_btn);
                final EditText nameEt = dialogView.findViewById(R.id.name_et);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setView(dialogView);
                dialog.setCanceledOnTouchOutside(false);

                saveScoreBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = nameEt.getText().toString();
                        int img_id = getIntent().getIntExtra("level_img",0);

                        insertScore(name,"easy",img_id,score);
                        Toast.makeText(GameActivity.this,"Score Saved",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(GameActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(GameActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                dialog.show();
            }
            else
            {
                final AlertDialog dialog = new AlertDialog.Builder(GameActivity.this).create();
                final View dialogView = getLayoutInflater().inflate(R.layout.win_no_highscore_dialog, null);

                Button playAgainBtn = dialogView.findViewById(R.id.btn_play_again);
                Button homeBtn = dialogView.findViewById(R.id.home_btn);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setView(dialogView);
                dialog.setCanceledOnTouchOutside(false);

                playAgainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resetLevel();
                        dialog.dismiss();
                    }
                });

                homeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GameActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.show();
            }

        }

    }

    private void updateFoodStatus(FoodType foodType){
        TextView textView;
        int index = container.getStaticShoppingList().indexOf(foodType);
        switch (index){
            case 0:
                textView = findViewById(R.id.foodStatus0);
                break;
            case 1:
                textView = findViewById(R.id.foodStatus1);
                break;
            case 2:
                textView = findViewById(R.id.foodStatus2);
                break;
            case 3:
                textView = findViewById(R.id.foodStatus3);
                break;
            case 4:
                textView = findViewById(R.id.foodStatus4);
                break;
            case 5:
                textView = findViewById(R.id.foodStatus5);
                break;
            case 6:
                textView = findViewById(R.id.foodStatus6);
                break;
            case 7:
                textView = findViewById(R.id.foodStatus7);
                break;
            default:
                return;
        }

        int lefts = Integer.parseInt(textView.getText().toString());
        lefts--;
        textView.setText( "" + lefts);
        if(lefts == 0){
            textView.setText("");
            textView.setBackgroundResource(R.drawable.v_sign_with_background);
            container.getForbiddenList().add(foodType);
            container.getShoppingList().remove(foodType);
        }
    }

    private void setShoppingListLayout(List<FoodType> shoppingList,List<Integer> shoppingListCounts){
        //setting shoppingListLayout with shopping list food images and counts


        int[] foodArray = {R.id.food0, R.id.food1, R.id.food2, R.id.food3, R.id.food4, R.id.food5,
                R.id.food6, R.id.food7};
        int[] foodStatusArray = {R.id.foodStatus0, R.id.foodStatus1, R.id.foodStatus2, R.id.foodStatus3,
                R.id.foodStatus4, R.id.foodStatus5, R.id.foodStatus6, R.id.foodStatus7};

        for (int i = 0; i < 8; i++){
            ImageView foodImage = findViewById(foodArray[i]);
            TextView textView = findViewById(foodStatusArray[i]);
            textView.setBackgroundResource(R.drawable.shape1);

            if(i < shoppingList.size()){
                foodImage.setBackgroundResource(container.getFoodTypeDrawable(shoppingList.get(i)));
                textView.setText("" + shoppingListCounts.get(i));
            }
            else {
                foodImage.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
            }
        }

    }


    private void updateLives(){
        ImageView cartLives;


        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }


        switch (lives){
            case 3:
                cartLives = findViewById(R.id.cart_life_1);
                break;
            case 2:
                cartLives = findViewById(R.id.cart_life_2);
                break;
            case 1:
                cartLives = findViewById(R.id.cart_life_3);
                break;
            default:
                return;
        }
        lives--;
        cartLives.setVisibility(View.INVISIBLE);
        if(lives == 0){
            startLabel.setText("GAME OVER");
            startLabel.setVisibility(View.VISIBLE);
            cleanLevel();

            final AlertDialog dialog = new AlertDialog.Builder(GameActivity.this).create();
            final View dialogView = getLayoutInflater().inflate(R.layout.game_over_dialog,null);

            Button homeBtn = dialogView.findViewById(R.id.home_btn);
            Button playAgainBtn = dialogView.findViewById(R.id.play_again_btn);

            // ZoomIn/Out animation:
            final Animation zoomBtnAnimation = AnimationUtils.loadAnimation(this, R.anim.dlg_btns_anim);
            homeBtn.startAnimation(zoomBtnAnimation);
            playAgainBtn.startAnimation(zoomBtnAnimation);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setView(dialogView);
            dialog.setCanceledOnTouchOutside(false);

            homeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GameActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            playAgainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetLevel();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent me){

        if(startFlag){
            if(me.getAction() == MotionEvent.ACTION_DOWN){
                if(me.getRawX() - cart.getX() > 0){
                    moveRight = true;
                }
                else{
                    moveLeft = true;
                }
            }else if(me.getAction() == MotionEvent.ACTION_UP){
                moveRight = false;
                moveLeft = false;
            }
        }
        else{
            startFlag = true;
            startLabel.setVisibility(View.GONE);
        }

        if(startFlag  && !isCartAnimated && me.getAction() == MotionEvent.ACTION_UP){

            float target = me.getRawX() - (float)cart.getLeft() - (float)cart.getWidth()/2;

            ObjectAnimator animationCart = ObjectAnimator.ofFloat(cart, "translationX" ,target);
            animationCart.setDuration(200);
            animationCart.setInterpolator(new LinearInterpolator());
            animationCart.addListener(new Animator.AnimatorListener(){


                @Override
                public void onAnimationStart(Animator animator) {
                    isCartAnimated = true;

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    isCartAnimated = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) { isCartAnimated = false; }

                @Override
                public void onAnimationRepeat(Animator animator) { }
            });

            animationCart.start();
        }

        return true;
    }

    private void resetLevel() {
        ImageView cartLife1 = findViewById(R.id.cart_life_1);
        ImageView cartLife2 = findViewById(R.id.cart_life_2);
        ImageView cartLife3 = findViewById(R.id.cart_life_3);

        lives = 3;
        cartLife1.setVisibility(View.VISIBLE);
        cartLife2.setVisibility(View.VISIBLE);
        cartLife3.setVisibility(View.VISIBLE);

        score = 0;
        scoreTV.setText("" + score);
        startLabel.setVisibility(View.GONE);

        setShoppingListLayout(shoppingList,shoppingListCounts);
        container.resetShoppingList();
    }

    private void cleanLevel(){

        List<FoodObject> foodList = container.getFoodList();
        int size = foodList.size();
        for(int i = 0; i < size; i++){
            frame.removeView(foodList.get(i));
        }
        foodList.clear();
    }

    public boolean isNewHighScore(int score)
    {
        loadData();

        for(int i = 0; i < 10; i++)
        {
            if(score >= highScores.get(i).getScore()) return true;
        }
        return false;
    }

    public void insertScore(String name,String difficulty,int level_img_id,int score)
    {
        loadData();

        //insert new score to the table
        int i = 0;
        while (score < highScores.get(i).getScore())
        {
            i++;
        }

        HighScore highScore = new HighScore(i+1,level_img_id,difficulty,name,score);

        if(i == 9)
        {
            highScores.set(9,highScore);
        }
        else
        {
            for(int j = 9; j > i; j--)
            {
                HighScore tempObj = highScores.get(j-1);
                tempObj.setRank(tempObj.getRank()+1);
                highScores.set(j,highScores.get(j-1));
            }

            highScores.set(i,highScore);
        }

        saveData();

    }

    private void loadData()
    {
        SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("score_table",null);
        Type type = new TypeToken<ArrayList<HighScore>>() {}.getType();
        highScores = gson.fromJson(json,type);
    }

    private void saveData()
    {
        SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(highScores);
        editor.putString("score_table",json);
        editor.apply();
    }

}
