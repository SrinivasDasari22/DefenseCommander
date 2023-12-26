package com.example.defensecommander;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public ArrayList<Integer> topScoreList = new ArrayList<>();


    public static Boolean launcher1=true;
    public static Boolean launcher2=true;

    public static Boolean launcher3=true;

    public DataBaseHandler dbh;





    public static float b1X;
    public static float b1Y;
    public static float b2X;
    public static float b2Y;
    public static float b3X;
    public static float b3Y;
    public static float baseHeight;
    public static float baseWidth;



    public static  ImageView base1;
    public  static ImageView base2;
    public static ImageView base3;

    private static final String TAG = "MainActivity";
    private ConstraintLayout layout;
    public  static int screenHeight;
    public static int screenWidth;
    private MissileMaker missileMaker;

    private TextView score;
    private TextView level;
    Thread t1;
    private ImageView launcher;
    private static int scoreValue;
    private static int levelValue;

//    private TextView score, level;

    private  MainActivity mainActivity1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFullScreen();
        getScreenDimensions();
        setContentView(R.layout.activity_main);
        SoundPlayer.getInstance().setupSound(this, "background", R.raw.background,true);

        SoundPlayer.getInstance().setupSound(this, "interceptor_blast", R.raw.interceptor_blast,false);
        SoundPlayer.getInstance().setupSound(this, "launch_interceptor", R.raw.launch_interceptor,false);

        SoundPlayer.getInstance().setupSound(this, "base_blast", R.raw.base_blast,false);

        SoundPlayer.getInstance().setupSound(this, "interceptor_hit_missile", R.raw.interceptor_hit_missile,false);

        SoundPlayer.getInstance().setupSound(this, "launch_missile", R.raw.launch_missile,false);
        SoundPlayer.getInstance().setupSound(this, "missile_miss", R.raw.missile_miss,false);


        SoundPlayer.getInstance().start("background");

        base1= findViewById(R.id.base1);
        base2 = findViewById(R.id.base2);
        base3  = findViewById(R.id.base3);
        score =findViewById(R.id.score);
        level = findViewById(R.id.level);
        layout = findViewById(R.id.layout);
        mainActivity1 = this;







        layout.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                handleTouch(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        });

        new ParallaxBackground(this, layout, R.drawable.clouds, 100000);







        missileMaker = new MissileMaker(this, screenWidth, screenHeight);
//        new Thread(missileMaker).start();
        t1 = new Thread(missileMaker);
        t1.start();


    }

    public float distBtwPoints(float x1,float y1,float x2,float y2){

        float deltaX = x2 - x1;
        float deltaY = y2 - y1;
        return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }



    public int getSmall(float num1,float num2,float num3){
        if (num1 < num2) {
            if (num1 < num3) {

                return 1;
            } else {

                return 3;
            }
        } else {
            if (num2 < num3) {
                return 2;

            } else {

                return 3;
            }
        }
    }
    public void handleTouch(float x1, float y1) {

        HashMap<Integer,Float> hashMap = new HashMap<Integer, Float>();

        float num1 = 0,num2=0,num3=0;
        launcher = null;

        if(launcher1){
            b1X = base1.getX();
            b1Y = base1.getY();



            num1 = distBtwPoints(b1X,b1Y,x1,y1);
            hashMap.put(1,num1);
        }
        if (launcher2){

            b2X = base2.getX();
            b2Y = base2.getY();
            num2 = distBtwPoints(b2X,b2Y,x1,y1);
            hashMap.put(2,num2);
        }
        if (launcher3){
            b3X = base3.getX();
            b3Y = base3.getY();

            num3 = distBtwPoints(b3X,b3Y,x1,y1);
            hashMap.put(3,num3);
        }







        int min =0;
        float mini=0;

        if(hashMap.values().size()!=0){
             mini = Collections.min(hashMap.values());
        } else{
            missileMaker.stopMakers();
        }
        for (HashMap.Entry<Integer, Float> entry : hashMap.entrySet()) {
            if (entry.getValue() == mini && entry.getValue()!=0) {
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
                min = entry.getKey();
            }
        }


        System.out.println("ds++" + b1X);






//        System.out.println(num1+ ","+ num2 + ","+ num3);


//        float minDist = Math.min(dis1, Math.min(dis2, dis3));

//        int min = getSmall(num1,num2,num3);

        System.out.println(min);





//        if (launcher == null){

            if(min==1){
                launcher = base1;
            } else if (min==2) {
                launcher = base2;

            }else if (min==3){
                launcher = base3;

            }
//        }

        double startX=0;
        double startY=0;


//            launcher = findViewById(R.id.base2);
        if(MainActivity.base1!=null || MainActivity.base2!=null || MainActivity.base3!=null) {
            startX = launcher.getX() + (0.5 * launcher.getWidth());
            startY = launcher.getY() + (0.5 * launcher.getHeight());
            float a = calculateAngle(startX, startY, x1, y1);
//        launcher.setRotation(a-70);
            Log.d(TAG, "handleTouch: " + a);
            Interceptor i = new Interceptor(this,  (float) (startX - 10), (float) (startY - 30), x1, y1);
            SoundPlayer.getInstance().start("launch_interceptor");
            i.launch();

        } else{
            missileMaker.stopMakers();
        }

    }
    public float calculateAngle(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil(-angle / 360) * 360;
        return (float) (190.0f - angle);
    }

    private void getScreenDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }

    private void setupFullScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    public ConstraintLayout getLayout() {
        return layout;
    }

    public void removeMissile(Missile p) {
        missileMaker.removeMissile(p);
    }

    public void incrementScore() {
        scoreValue++;
        score.setText(String.format(Locale.getDefault(), "%d", scoreValue));
    }

    public void setLevel(final int value1) {

        levelValue = value1;
        runOnUiThread(() -> level.setText(String.format(Locale.getDefault(), "Level: %d", value1)));
    }

//    public void doStop(View v) {
//        missileMaker.setRunning(false);
//        finish();
//    }

    public void applyInterceptorBlast(Interceptor interceptor, int id) {
        missileMaker.applyInterceptorBlast(interceptor, id);
    }

    public void gameOver() {

        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.game_over);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (screenWidth*0.8), (int) (screenHeight*.40));

// Set the LayoutParams on the ImageView
//        imageView2.setLayoutParams(layoutParams);
        imageView2.setZ(2);
        imageView2.setX((float) (screenWidth*0.10));
        imageView2.setY((float) ((screenHeight/2)-100));




        HandlerThread handlerThread = new HandlerThread("MyThread");
        handlerThread.start();

        Handler handler = new Handler(handlerThread.getLooper());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator yAnim1 = ObjectAnimator.ofFloat(imageView2, "alpha", 0, 1);
                yAnim1.setDuration(3000);

                yAnim1.start();
                imageView2.setLayoutParams(layoutParams);
                layout.addView(imageView2);

                yAnim1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {


                        scoreCheck();

//                        openScoreActivity();

                    }
                });
            }
        });






    }



    public void scoreCheck(){


        dbh =
                new DataBaseHandler(null,this, "dsd",scoreValue, levelValue);
        new Thread(dbh).start();
    }
    public void openScoreActivity(String name){
        SoundPlayer.getInstance().stop("background");

        Intent i = new Intent(mainActivity1, ScoreActivity.class);
        i.putExtra("NAME",name);
        i.putExtra("SCORE",scoreValue);
        i.putExtra("LEVEL",Integer.parseInt(level.getText().toString().split(": ")[1]));
        startActivity(i);

    }

    public void getScoresList(ArrayList<Integer> arrayList) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Boolean go= true;
//                String name=null;

                topScoreList.clear();
                topScoreList.addAll(arrayList);
                for (Integer i : topScoreList) {
                    go = true;
                    System.out.println("scores:" + i);
                    if (scoreValue > i) {
                        go = false;
                        final EditText input = new EditText(mainActivity1);

                        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity1);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked Yes button
                                String name = input.getText().toString();
                                openScoreActivity(name);


                            }
                        });



                        builder.setNegativeButton("NO", (dialog, which) -> {
//                            dialog.dismiss();
                            MainActivity.super.onBackPressed();
                        });

                        builder.setTitle(" You are a Top-Player!");
                        builder.setMessage("Please enter your initials (upto 3 characters):");


                        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

                        InputFilter[] filters = new InputFilter[1];
                        filters[0] = new InputFilter.LengthFilter(3);
                        input.setFilters(filters);

//                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }else{
//                        openScoreActivity(null);
                    }
                }
                if(go) {
                    openScoreActivity(null);
                }

            }
            });
    }
}