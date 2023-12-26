package com.example.defensecommander;

import static com.example.defensecommander.Interceptor.INTERCEPTOR_BLAST;

import android.animation.AnimatorSet;
import android.util.Log;

import java.util.ArrayList;

public class MissileMaker implements Runnable{


    public  static Boolean maker= true;
    private static final String TAG = "PlaneMaker";
    private final MainActivity mainActivity;
    private boolean isRunning;
    private final ArrayList<Missile> activePlanes = new ArrayList<>();
    private final int screenWidth;
    private final int screenHeight;
    private static final int NUM_LEVELS = 5;

    private static int levelNum;



    MissileMaker(MainActivity mainActivity, int screenWidth, int screenHeight) {
        this.mainActivity = mainActivity;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }


    @Override
    public void run() {
        long delay = NUM_LEVELS * 1000;


        while (maker) {
            SoundPlayer.getInstance().start("launch_missile");


            int resId = R.drawable.missile;


            long planeTime = (long) ((delay * 0.5) + (Math.random() * delay));
            final Missile missile = new Missile(screenWidth, screenHeight, planeTime, mainActivity);
            activePlanes.add(missile);
            final AnimatorSet as = missile.setData(resId);

            mainActivity.runOnUiThread(as::start);

            try {
                Thread.sleep((long) (delay * 0.333));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            delay -= 10;
            if (delay <= 0)
                delay = 10;
            Log.d(TAG, "run: DELAY: " + delay);

             levelNum = (int) ((NUM_LEVELS * 4) - (delay / 250));
            Log.d(TAG, "run: LEVEL " + levelNum);
            mainActivity.setLevel(levelNum);

            if(MainActivity.base1==null && MainActivity.base2==null && MainActivity.base3==null){
                maker=false;
                mainActivity.gameOver();
            }

        }
    }

    void removeMissile(Missile p) {
        activePlanes.remove(p);
    }


    void applyInterceptorBlast(Interceptor interceptor, int id) {
        Log.d(TAG, "applyInterceptorBlast: -------------------------- " + id);

        float x1 = interceptor.getX();
        float y1 = interceptor.getY();

        Log.d(TAG, "applyInterceptorBlast: INTERCEPTOR: " + x1 + ", " + y1);

        ArrayList<Missile> nowGone = new ArrayList<>();
        ArrayList<Missile> temp = new ArrayList<>(activePlanes);

        for (Missile m : temp) {

            float x2 = (int) (m.getX() + (0.5 * m.getWidth()));
            float y2 = (int) (m.getY() + (0.5 * m.getHeight()));

            Log.d(TAG, "applyInterceptorBlast:    Missile: " + x2 + ", " + y2);


            float f = (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
            Log.d(TAG, "applyInterceptorBlast:    DIST: " + f);

            if (f < INTERCEPTOR_BLAST) {

                SoundPlayer.getInstance().start("interceptor_hit_missile");
                mainActivity.incrementScore();
                Log.d(TAG, "applyInterceptorBlast:    Hit: " + f);
                m.interceptorBlast(x2, y2);
                nowGone.add(m);
            } else{
                SoundPlayer.getInstance().start("missile_miss");

            }






            Log.d(TAG, "applyInterceptorBlast: --------------------------");


        }

        for (Missile m : nowGone) {
            activePlanes.remove(m);
        }
    }


    public  void stopMakers(){
        maker=false;
    }
}
