package com.example.defensecommander;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity implements View.OnClickListener{

    static int ScoresValue;

    static String name;
    static int level;


    static TextView scoreText;

    private Button button;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        SoundPlayer.getInstance().setupSound(this, "background", R.raw.background,true);

        SoundPlayer.getInstance().setupSound(this, "interceptor_blast", R.raw.interceptor_blast,false);
        SoundPlayer.getInstance().setupSound(this, "launch_interceptor", R.raw.launch_interceptor,false);

        SoundPlayer.getInstance().setupSound(this, "base_blast", R.raw.base_blast,false);

        SoundPlayer.getInstance().setupSound(this, "interceptor_hit_missile", R.raw.interceptor_hit_missile,false);

        SoundPlayer.getInstance().setupSound(this, "launch_missile", R.raw.launch_missile,false);
        SoundPlayer.getInstance().setupSound(this, "missile_miss", R.raw.missile_miss,false);




        setContentView(R.layout.activity_score);
        setupFullScreen();

        scoreText = findViewById(R.id.scoreText);
          button = findViewById(R.id.button);

        ScoresValue = getIntent().getIntExtra("SCORE",0);
        level = getIntent().getIntExtra("LEVEL",1);
        name = getIntent().getStringExtra("NAME");




            DataBaseHandler dbh =
                    new DataBaseHandler(this, null, name, level, ScoresValue);
            new Thread(dbh).start();





    }

    public void doAction(){
        ParallaxBackground.stop();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        System.exit(0);

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
//        SoundPlayer.getInstance().start("background");

    }
    public void setResults(String s) {

        SoundPlayer.getInstance().start("background");

        scoreText.setText(s);
        ParallaxBackground.stop();

    }

    public void Exit(View v){

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {

    }
}