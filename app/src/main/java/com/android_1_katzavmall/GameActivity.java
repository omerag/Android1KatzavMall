package com.android_1_katzavmall;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.solver.widgets.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private LinearLayout groceriesLayout;
    private TextView scoreTV;
    private TextView startLabel;
    private TextView levelName;
    private ImageView cart;
    private FrameLayout frame;
    boolean isCartAnimated = false;
    private ObjectAnimator animationCart;

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

    private long lastUpdate = 0;
    private static final int SHAKE_THRESHOLD = 600;


    ////////////////

    private int screenHeight;
    private int screenWidth;

    private boolean startFlag = false;
    private boolean moveRight = false;
    private boolean moveLeft = false;
    private boolean isResumed = false;

    private long velFlower = 4000;
    private int score = 0;
    private int lives = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);

        groceriesLayout = findViewById(R.id.groceriesLayout);
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
        startLabel.startAnimation(zoomAnimation);
        levelName.startAnimation(zoomAnimation);


        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;


        //getting shoppingListName shoppingList and forbiddenList from previous activity
        String shoppingListStr = getIntent().getStringExtra("shoppingListStr");
        List<FoodType> shoppingList = (ArrayList<FoodType> )getIntent().getSerializableExtra("shoppingList");
        List<Integer> shoppingListCounts = getIntent().getIntegerArrayListExtra("shoppingListCounts");
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
                        if(startFlag && lives > -99){
                            updatePositions();
                            createFood();
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

        //move cart

        if(!isCartAnimated && false){


            float target = 0;
            if(move != 0 && cart.getX() > 0 && cart.getX() + cart.getWidth() < screenWidth){
                target = - move * 80;
            }
            else{
                //target = cart.getX();
            }

            animationCart = ObjectAnimator.ofFloat(cart, "translationX" ,target);
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

        if(cart.getX() < 0){
            cart.setX(0);
        }
        else if(cart.getX() + cart.getWidth() > screenWidth){
            cart.setX(screenWidth - cart.getWidth());
        }
        else{
            cart.setX(cart.getX() + move*(-1)*10);
        }


       /* if(cart.getX() < 0 && isCartAnimated){
            animationCart.cancel();
            cart.setX(0);
        }else if(cart.getX() + cart.getWidth() > screenWidth && isCartAnimated){
            animationCart.cancel();
            cart.setX(screenWidth - cart.getWidth());
        }

*/


        /*
            if(moveRight && cart.getX() < screenWidth - cart.getWidth()){
                cart.setX(cart.getX() + 20);
            }
            else if(moveLeft && cart.getX() > 0){
                cart.setX(cart.getX() - 20);
            }
*/

        //move flowers
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
                        scoreTV.setText("Score: " + score);
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
                            scoreTV.setText("Score: " + score);

                        }

                        frame.removeView(foodObject);
                        container.removeFood(foodObject);
                    }
                });

            }
            else if(!foodObject.isAnimated()){
                ObjectAnimator animation = ObjectAnimator.ofFloat(foodObject, "translationY", screenHeight);
                animation.setDuration(velFlower);
                animation.start();
                foodObject.setAnimated(true);
                // foodObject.setY(foodObject.getY() + velFlower);
            }

        }

    }


    private synchronized void createFood(){


        //create item from shopping list
        if(System.currentTimeMillis() % 1000 < 10){
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
                    System.out.println("new flower at " + x);
                    factory.addFood(x,container.getShoppingList().get(pick));
                }
            });
        }else if (System.currentTimeMillis() % 1000 > 10 && System.currentTimeMillis() % 1000 < 20){
            //create item from forbidden list

            final Random rand = new Random();
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    System.out.println("ForbiddenList: " + container.getForbiddenList().size());


                    int pick = rand.nextInt(container.getForbiddenList().size());

                    System.out.println("Pick: " + pick);

                    int x = rand.nextInt(screenWidth - cart.getWidth());
                    factory.addFood(x,container.getForbiddenList().get(pick));
                }
            });
        }
    }

    public boolean checkCollision(View v1,View v2) {
        Rectangle R1=new Rectangle();
        Rectangle R2=new Rectangle();

        R1.x = (int)v1.getX();
        R1.y = (int)v1.getY();
        R1.width = v1.getWidth();
        R1.height = v1.getHeight();

        R2.x = (int)v2.getX();
        R2.y = (int)v2.getY();
        R2.width = v2.getWidth();
        R2.height = v2.getHeight();
        return R1.contains(R2.x + R2.width, R2.y + R2.height);
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



            if(i < shoppingList.size()){
                System.out.println("foodType id = " + container.getFoodTypeDrawable(shoppingList.get(i)));
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
        switch (lives){
            case 3:
                cartLives = findViewById(R.id.cart_life_3);
                break;
            case 2:
                cartLives = findViewById(R.id.cart_life_2);
                break;
            case 1:
                cartLives = findViewById(R.id.cart_life_1);
                break;
            default:
                return;
        }
        lives--;
        cartLives.setVisibility(View.INVISIBLE);
        if(lives == 0){
            startLabel.setText("GAME OVER");
            startLabel.setVisibility(View.VISIBLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.game_over_dialog,null);

            Button homeBtn = dialogView.findViewById(R.id.home_btn);
            Button playAgainBtn = dialogView.findViewById(R.id.play_again_btn);

            builder.setView(dialogView);
            homeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GameActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });

            playAgainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetLevel();
                }
            });
            builder.show();
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
        return true;
    }

    public void resetLevel()
    {

    }
}
