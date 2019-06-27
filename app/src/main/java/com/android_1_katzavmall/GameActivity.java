package com.android_1_katzavmall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements SensorEventListener {


    private TextView scoreTV;
    private TextView startLabel;
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

        scoreTV = findViewById(R.id.scoreTV);
        startLabel = findViewById(R.id.startLabel);
        cart = findViewById(R.id.cart);
        frame = findViewById(R.id.frame);


        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        List<FoodType> shoppingList = new ArrayList<>(Arrays.asList(FoodType.food1,FoodType.food2));
        List<FoodType> forbiddenList = new ArrayList<>(Arrays.asList(FoodType.food3,FoodType.food4));

        container = new FoodContainer(shoppingList,forbiddenList);
        factory = new FoodFactory(this,container,frame);


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
                            createFlowers();
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
        List<FoodObject> foodList = container.getFlowerList();
        for(int i = 0; i < foodList.size(); i++ ){
            final FoodObject foodObject = foodList.get(i);

            if(checkCollision(cart,foodObject)){


                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(container.getShoppingList().contains(foodObject.getType())){
                            score++;
                            updateCheckBox(foodObject.getType());
                        }
                        else{
                            updateLives();

                        }
                        scoreTV.setText("Score: " + score);
                        frame.removeView(foodObject);
                        container.removeFlower(foodObject);
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
                        container.removeFlower(foodObject);
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


    private synchronized void createFlowers(){


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

                    int x = ((rand.nextInt(100)*10 + 40) % screenWidth); //new flowers wont be too close to each other
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

    private void updateCheckBox(FoodType foodType){
        CheckBox checkBox;
        switch (foodType){
            case food1:
                checkBox = findViewById(R.id.checkbox_red);
                break;
            case food2:
                checkBox = findViewById(R.id.checkbox_blue);
                break;
            case food3:
                checkBox = findViewById(R.id.checkbox_purple);
                break;
            case food4:
                checkBox = findViewById(R.id.checkbox_orange);
                break;
            default:
                return;
        }

        int lefts = Integer.parseInt(checkBox.getText().toString());
        lefts--;
        checkBox.setText( "" + (lefts));
        if(lefts == 0){
            checkBox.setChecked(true);
            container.getForbiddenList().add(foodType);
            container.getShoppingList().remove(foodType);
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
}
