package com.android_1_katzavmall;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
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

import com.github.jinatonic.confetti.CommonConfetti;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    // private ImageView cart;
    private CartObject cart;
    private FrameLayout frame;
    private List<HighScore> highScores;
    private SharedPreferences sp;
    private MediaPlayer winner_sound_player;
    private MediaPlayer looser_sound_player;
    private MediaPlayer level_sound_player;
    boolean isMusic;
    boolean isSounds;

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

    ////////////////

    private int shopCounter = 100;
    private int shopCounterReset = shopCounter;
    private boolean shopFlag = false;
    private boolean bonusFlag = true;
    private boolean isBonusStated = false;
    private boolean bonusFoodMissed = false;
    private int bonusCounter = 500;
    private int bonusCounterReset = bonusCounter;

    private long lastUpdate = 0;

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

    private long diffultyFoodVel = 4000;
    private boolean controlSensor;
    private int scoreMultiplier = 1;

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

        winner_sound_player = MediaPlayer.create(this, R.raw.winner);
        looser_sound_player = MediaPlayer.create(this, R.raw.looser);

        sp = getSharedPreferences("sp", MODE_PRIVATE);
        isMusic = sp.getBoolean("music", true);
        isSounds = sp.getBoolean("sounds", true);

        if (isMusic) {
            level_sound_player = MediaPlayer.create(this, R.raw.game_level_sound);
            level_sound_player.setLooping(true);
            level_sound_player.setVolume(0.1f, 0.1f);
            level_sound_player.start();
        }


        // font
        if (Locale.getDefault().toString().equals("iw_IL")) {
            Typeface typeface1 = ResourcesCompat.getFont(this, R.font.koby);
            levelName.setTypeface(typeface1);
            startLabel.setTypeface(typeface1);
            scoreLabel.setTypeface(typeface1);
            scoreTV.setTypeface(typeface1);
        }

        // groceries list animation
        final Animation listAnimation = AnimationUtils.loadAnimation(this, R.anim.groceries_list_anim);
        BounceInterpolator interpolator = new BounceInterpolator(0.5, 10);
        listAnimation.setInterpolator(interpolator);
        groceriesLayout.startAnimation(listAnimation);

        // ZoomIn animation
        final Animation zoomAnimation = AnimationUtils.loadAnimation(GameActivity.this, R.anim.level_name_anim);
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

        setSettings();

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
                            if(bonusFlag && checkWin()){
                                bonusFlag = false;
                                levelName.setText(R.string.bonus);
                                startLabel.setVisibility(View.VISIBLE);
                                startLabel.setText(R.string.collect_all);
                                final Animation zoomAnimation = AnimationUtils.loadAnimation(GameActivity.this, R.anim.level_name_anim);
                                level_name_layout.startAnimation(zoomAnimation);
                                startBonus();
                            }
                        }

                    }
                });

            }
        },0,16);

    }

    @Override
    protected void onPause() {

        if (isMusic) level_sound_player.pause();
        mSensorManager.unregisterListener(this);
        super.onPause();
        startFlag = false;
        isResumed = true;
    }

    @Override
    protected void onResume() {

        if (isMusic) level_sound_player.start();

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

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && controlSensor) {
            accelerometerData = event.values;
            //     cart.onSensorEvent(event);

            long curTime = System.currentTimeMillis();


            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float deltaX = Math.abs(event.values[0] - lastX);
                if (deltaX < 2.0f  ) {

                    move = event.values[0];
                }
                else{
                   // move = 0;
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

            //check collision with food
            if(checkCollision(cart,foodObject)){
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //if we took food is in our shopping list
                        if(container.getShoppingList().contains(foodObject.getType())){
                            score = score + 10*scoreMultiplier;
                            if (isSounds) cart.playGoodSound();
                            cart.animateCoach();
                            updateFoodStatus(foodObject.getType());
                            scoreTV.setText("" + score);

                        }
                        else { //if we took food that is not in our shopping list
                            if (isSounds) cart.playBadSound();
                            updateLives();

                        }
                        frame.removeView(foodObject);
                        container.removeFood(foodObject);
                    }
                });
            }//if object out of the screen
            else if((int)foodObject.getY() > screenHeight){

                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(container.getShoppingList().contains(foodObject.getType()) && !isBonusStated){
                            updateLives();

                        }
                        else if(isBonusStated){
                            vibrate();
                            bonusFoodMissed = true;
                        }

                        frame.removeView(foodObject);
                        container.removeFood(foodObject);
                    }
                });

            }
            else if(!foodObject.isAnimated()){
                ObjectAnimator animation = ObjectAnimator.ofFloat(foodObject, "translationY", screenHeight);
                animation.setDuration(diffultyFoodVel);
                animation.setInterpolator(new AccelerateInterpolator());
                foodObject.setAnimated(true);

                animation.start();
            }
        }
    }

    private synchronized void createFood(){


        shopCounter--;
        if (shopCounter < 1){
            shopCounter = shopCounterReset;
            //check is it shopping list turn to create food or if it's  the bonus level
            if(shopFlag || isBonusStated){
                shopFlag = false;
                this.runOnUiThread(new Runnable() {
                    final Random rand = new Random();

                    @Override
                    public void run() {

                        int size = container.getShoppingList().size();

                        if(size == 0){
                            return;
                        }

                        int pick = rand.nextInt(size);

                        int x = ((rand.nextInt(100)*10 + 40) % (screenWidth - cart.getWidth())); //new food wont be too close to each other
                        factory.addFood(x,container.getShoppingList().get(pick));
                    }
                });
            }
            else {
                shopFlag = true;
                this.runOnUiThread(new Runnable() {
                    final Random rand = new Random();

                    @Override
                    public void run() {


                        int pick = rand.nextInt(container.getForbiddenList().size());

                        int x = ((rand.nextInt(100)*10 + 40) % (screenWidth - cart.getWidth()));
                        factory.addFood(x,container.getForbiddenList().get(pick));
                    }
                });
            }

        }

        if(isBonusStated && bonusCounter > 0){
            bonusCounter--;
        }
        else if(bonusCounter < 1 && !bonusFoodMissed && diffultyFoodVel > 1000  && shopCounter < 2){
            diffultyFoodVel = diffultyFoodVel - (long)50;
        }
        else if(bonusCounter < 1 && bonusFoodMissed){
            endBonus();
        }

    }

    private boolean checkCollision(View v1,View v2) {
        Rect rect1 = new Rect();
        Rect rect2 = new Rect();
        v1.getHitRect(rect1);
        v2.getHitRect(rect2);
        return Rect.intersects(rect1,rect2);
    }

    private boolean checkWin(){


        TextView foodStatus;
        for(int i = 0; i < container.getStaticShoppingList().size();i++){
            foodStatus = findViewById(foodStatusArray[i]);
            if(!foodStatus.getText().toString().equals("") && Integer.parseInt(foodStatus.getText().toString()) > 0){
                return  false;
            }
        }

        return true;
    }

    private void startBonus(){

        isBonusStated = true;
        shopCounter = 30;
        shopCounterReset = shopCounter;

        cleanLevel();

        container.getShoppingList().add(FoodType.PIE);
        container.getShoppingList().add(FoodType.ICE_CREAM);
        container.getShoppingList().add(FoodType.LOLLIPOP);
        container.getShoppingList().add(FoodType.CUPCAKE);
        container.getShoppingList().add(FoodType.COOKIE);
        container.getShoppingList().add(FoodType.CHOCOLATE_BAR);
        container.getShoppingList().add(FoodType.CANDY);
        container.getShoppingList().add(FoodType.BIRTHDAY_CAKE);

    }

    private void endBonus(){

        cleanLevel();
        startFlag = false;

        if (isNewHighScore(score))
        {
            /* Confetti 1 */

            CommonConfetti.rainingConfetti(frame, new int[] { Color.MAGENTA,Color.RED,Color.YELLOW, Color.GREEN, Color.BLUE })
                    .infinite();

            if (isMusic)level_sound_player.stop();
            final AlertDialog dialog = new AlertDialog.Builder(GameActivity.this).create();
            final View dialogView = getLayoutInflater().inflate(R.layout.win_new_highscore_dialog, null);
            winner_sound_player.start();

            TextView congratulations_tv = dialogView.findViewById(R.id.congratulations_tv);
            TextView new_record_tv = dialogView.findViewById(R.id.new_record_tv);
            Button saveScoreBtn = dialogView.findViewById(R.id.save_score_btn);
            Button cancelBtn = dialogView.findViewById(R.id.cancel_btn);
            final EditText nameEt = dialogView.findViewById(R.id.name_et);

            // font
            if (Locale.getDefault().toString().equals("iw_IL")) {
                Typeface typeface1 = ResourcesCompat.getFont(this, R.font.koby);
                congratulations_tv.setTypeface(typeface1);
                new_record_tv.setTypeface(typeface1);
                nameEt.setTypeface(typeface1);
                Typeface typeface2 = ResourcesCompat.getFont(this, R.font.abraham);
                saveScoreBtn.setTypeface(typeface2);
                cancelBtn.setTypeface(typeface2);
            }

            // ZoomIn/Out animation:
            final Animation zoomBtnAnimation = AnimationUtils.loadAnimation(this, R.anim.dlg_btns_anim);
            saveScoreBtn.startAnimation(zoomBtnAnimation);
            cancelBtn.startAnimation(zoomBtnAnimation);


            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setView(dialogView);
            dialog.setCanceledOnTouchOutside(false);

            saveScoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = nameEt.getText().toString();
                    sp = getSharedPreferences("sp",MODE_PRIVATE);
                    String difficulty_str = sp.getString("difficulty","");

                    int img_id = getIntent().getIntExtra("level_img",0);

                    insertScore(name,difficulty_str,img_id,score);
                    Toast.makeText(GameActivity.this,R.string.record_saved_toast,Toast.LENGTH_LONG).show();

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
            /* Confetti 2 */

            CommonConfetti.rainingConfetti(frame, new int[] { Color.GREEN,Color.BLUE,Color.BLACK })
                    .infinite();

            if (isMusic)level_sound_player.pause();
            final AlertDialog dialog = new AlertDialog.Builder(GameActivity.this).create();
            final View dialogView = getLayoutInflater().inflate(R.layout.win_no_highscore_dialog, null);
            winner_sound_player.start();

            TextView you_won_tv = dialogView.findViewById(R.id.you_won_tv);
            TextView want_record_tv = dialogView.findViewById(R.id.want_record_tv);
            Button playAgainBtn = dialogView.findViewById(R.id.btn_play_again);
            Button homeBtn = dialogView.findViewById(R.id.home_btn);


            // font
            if (Locale.getDefault().toString().equals("iw_IL")) {
                Typeface typeface1 = ResourcesCompat.getFont(this, R.font.koby);
                you_won_tv.setTypeface(typeface1);
                want_record_tv.setTypeface(typeface1);
                Typeface typeface2 = ResourcesCompat.getFont(this, R.font.abraham);
                playAgainBtn.setTypeface(typeface2);
                homeBtn.setTypeface(typeface2);
            }

            // ZoomIn/Out animation:
            final Animation zoomBtnAnimation = AnimationUtils.loadAnimation(this, R.anim.dlg_btns_anim);
            playAgainBtn.startAnimation(zoomBtnAnimation);
            homeBtn.startAnimation(zoomBtnAnimation);


            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setView(dialogView);
            dialog.setCanceledOnTouchOutside(false);

            playAgainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isMusic)level_sound_player.start();
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



    private void updateFoodStatus(FoodType foodType){
        TextView textView;
        int index = container.getStaticShoppingList().indexOf(foodType);

        if(isBonusStated){
            return;
        }
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
        String leftStr = textView.getText().toString();
        int lefts = Integer.parseInt(leftStr);
        lefts--;
        textView.setText( "" + lefts);
        if(lefts < 1){
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

    private void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }


    private void updateLives(){
        ImageView cartLives;

        vibrate();
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
            cleanLevel();

            if (isMusic)level_sound_player.pause();
            final AlertDialog dialog = new AlertDialog.Builder(GameActivity.this).create();
            final View dialogView = getLayoutInflater().inflate(R.layout.game_over_dialog,null);
            looser_sound_player.start();

            TextView game_over_tv = dialogView.findViewById(R.id.game_over_tv);
            Button homeBtn = dialogView.findViewById(R.id.home_btn);
            Button playAgainBtn = dialogView.findViewById(R.id.play_again_btn);

            // font
            if (Locale.getDefault().toString().equals("iw_IL")) {
                Typeface typeface1 = ResourcesCompat.getFont(this, R.font.koby);
                game_over_tv.setTypeface(typeface1);
                Typeface typeface2 = ResourcesCompat.getFont(this, R.font.abraham);
                homeBtn.setTypeface(typeface2);
                playAgainBtn.setTypeface(typeface2);
            }

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
                    if (isMusic)level_sound_player.start();
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

        if(!controlSensor && startFlag  && !cart.isAnimated() && me.getAction() == MotionEvent.ACTION_UP){

            float target = me.getRawX() - (float)cart.getLeft() - (float)cart.getWidth()/2;

            ObjectAnimator animationCart = ObjectAnimator.ofFloat(cart, "translationX" ,target);
            animationCart.setDuration(200);
            animationCart.setInterpolator(new LinearInterpolator());
            animationCart.addListener(new Animator.AnimatorListener(){


                @Override
                public void onAnimationStart(Animator animator) {
                    cart.setAnimated(true);

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    cart.setAnimated(false);
                }

                @Override
                public void onAnimationCancel(Animator animator) { cart.setAnimated(false); }

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

        bonusCounter = bonusCounterReset;

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

    private void setSettings(){
        SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
        String difficultyStr = sp.getString("difficulty","");

        String controlStr = sp.getString("control","");

        //if true game will use sensor else will use touch controls
        controlSensor = controlStr.equalsIgnoreCase(getString(R.string.control1));

        if(difficultyStr.equalsIgnoreCase(getString(R.string.difficulty2))){
            scoreMultiplier = 2;
            shopCounter = 80;
            shopCounterReset = shopCounter;
            bonusCounter = 1000;
            bonusCounterReset = bonusCounter;
            diffultyFoodVel = 3000;
        }
        else if (difficultyStr.equalsIgnoreCase(getString(R.string.difficulty3))){
            scoreMultiplier = 3;
            shopCounter = 60;
            shopCounterReset = shopCounter;
            bonusCounter = 1500;
            bonusCounterReset = bonusCounter;
            diffultyFoodVel = 2000;
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

    @Override
    public void onBackPressed() {

        startFlag = false;
        if (isMusic)level_sound_player.pause();
        final AlertDialog dialog = new AlertDialog.Builder(GameActivity.this).create();
        final View dialogView = getLayoutInflater().inflate(R.layout.exit_dialog, null);

        TextView exit_tv = dialogView.findViewById(R.id.exit_tv);
        Button homeBtn = dialogView.findViewById(R.id.back_to_home_btn);
        Button continueBtn = dialogView.findViewById(R.id.continue_btn);

        // font
        if (Locale.getDefault().toString().equals("iw_IL")) {
            Typeface typeface1 = ResourcesCompat.getFont(this, R.font.koby);
            exit_tv.setTypeface(typeface1);
            Typeface typeface2 = ResourcesCompat.getFont(this, R.font.abraham);
            homeBtn.setTypeface(typeface2);
            continueBtn.setTypeface(typeface2);
        }

        // ZoomIn/Out animation:
        final Animation zoomBtnAnimation = AnimationUtils.loadAnimation(this, R.anim.dlg_btns_anim);
        homeBtn.startAnimation(zoomBtnAnimation);
        continueBtn.startAnimation(zoomBtnAnimation);


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

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFlag = true;
                if (isMusic)level_sound_player.start();
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}

