package com.example.defensecommander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 7000;

    private static final int LOCATION_REQUEST = 111;
    private static final int BACKGROUND_LOCATION_REQUEST = 222;

    private int screenHeight;
    private int screenWidth;

    private  ImageView imageView;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);


        SoundPlayer.getInstance().setupSound(this, "background", R.raw.background,true);

        SoundPlayer.getInstance().setupSound(this, "interceptor_blast", R.raw.interceptor_blast,false);
        SoundPlayer.getInstance().setupSound(this, "launch_interceptor", R.raw.launch_interceptor,false);

        SoundPlayer.getInstance().setupSound(this, "base_blast", R.raw.base_blast,false);

        SoundPlayer.getInstance().setupSound(this, "interceptor_hit_missile", R.raw.interceptor_hit_missile,false);

        SoundPlayer.getInstance().setupSound(this, "launch_missile", R.raw.launch_missile,false);
        SoundPlayer.getInstance().setupSound(this, "missile_miss", R.raw.missile_miss,false);





        imageView = findViewById(R.id.splash_title);


        setupFullScreen();
        getScreenDimensions();

//        SoundPlayer.getInstance().start("background");




        ObjectAnimator xAnim = ObjectAnimator.ofFloat(imageView, "alpha", 0, 1);
        xAnim.setDuration(SPLASH_TIME_OUT);
        xAnim.start();






            new Handler().postDelayed(() -> {
                SoundPlayer.getInstance().start("background");




                // This method will be executed once the timer is over
                // Start your app main activity


                Intent i =
                        new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
//                overridePendingTransition(R.anim.slide_in, R.anim.slide_out); // new act, old act
                // close this activity
                finish();
            }, SPLASH_TIME_OUT);








    }



    private void getScreenDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
//        SoundPlayer.getInstance().start("background");
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

}