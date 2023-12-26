package com.example.defensecommander;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import android.widget.ImageView;

import androidx.annotation.NonNull;

public class Missile {

    static int count = 0;

    public static Boolean launcher1M=true;
    public static Boolean launcher2M=true;

    public static Boolean launcher3M=true;

    private final MainActivity mainActivity;
    private final ImageView imageView;
    private final AnimatorSet aSet = new AnimatorSet();
    private final int screenHeight;
    private final int screenWidth;
    private final long screenTime;
    private static final String TAG = "Plane";
    private boolean hit = false;


    Missile(int screenWidth, int screenHeight, long screenTime, final MainActivity mainActivity) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.screenTime = screenTime;
        this.mainActivity = mainActivity;


        imageView = new ImageView(mainActivity);
//        hit = false;

        imageView.setY(-500);
//        imageView.setX(-500);


        mainActivity.runOnUiThread(() -> mainActivity.getLayout().addView(imageView));

    }


    public float distBtwPoints(float x1,float y1,float x2,float y2){

        float deltaX = x2 - x1;
        float deltaY = y2 - y1;
        return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }


    AnimatorSet setData(final int drawId) {
        mainActivity.runOnUiThread(() -> imageView.setImageResource(drawId));

        final ImageView iv1 = new ImageView(mainActivity);
        iv1.setImageResource(R.drawable.i_explode);




        int startX = (int) (Math.random() * screenWidth * 0.8);
        int endX = (startX + (Math.random() < 0.5 ? 150 : -150));


        ObjectAnimator xAnim = ObjectAnimator.ofFloat(imageView, "y", -200, (screenHeight ));
        xAnim.setInterpolator(new LinearInterpolator());
        xAnim.setDuration(screenTime);

        xAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {

                ImageView temp;

                imageView.setTranslationY(imageView.getTranslationY());


                float y1 = imageView.getY();
                float x1 = imageView.getX();

//                System.out.println("x1 and y1 "+ x1+","+ y1);


                float x2=0;
                float y2=0;

                if(launcher1M) {


                    x2 = MainActivity.base1.getX() - (MainActivity.base1.getHeight() / 2);
                    y2 = MainActivity.base1.getY() + (MainActivity.base1.getWidth() / 2);
                    float f = (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));

//                    System.out.println("Float "+ f);

                    if(f<180){
                        interceptorBaseBlast(x1,y1,MainActivity.base1);
                        launcher1M = false;
                        MainActivity.launcher1 = false;
                        MainActivity.base1 = null;


                    }

                }
                if(launcher2M) {


                    x2 = MainActivity.base2.getX() - (MainActivity.base2.getHeight() / 2);
                    y2 = MainActivity.base2.getY() + (MainActivity.base2.getWidth() / 2);
                    float f = (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));

//                    System.out.println("Float "+ f);

                    if(f<180){

                        interceptorBaseBlast(x1,y1,MainActivity.base2);
                        launcher2M = false;
                        MainActivity.launcher2 = false;

                        MainActivity.base2 = null;




                    }

                }
                if(launcher3M) {


                    x2 = MainActivity.base3.getX() - (MainActivity.base3.getHeight() / 2);
                    y2 = MainActivity.base3.getY() + (MainActivity.base3.getWidth() / 2);
                    float f = (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));

//                    System.out.println("Float "+ f);

                    if(f<180){

                        interceptorBaseBlast(x1,y1,MainActivity.base3);
                        launcher3M = false;
                        MainActivity.launcher3 = false;

                        MainActivity.base3 = null;


                    }

                }






            }
        });





        xAnim.addListener(new AnimatorListenerAdapter() {


            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.runOnUiThread(() -> {
                    if (!hit) {

                        mainActivity.getLayout().removeView(imageView);
                        mainActivity.removeMissile(Missile.this);
                        iv1.setX(endX);
                        iv1.setY(screenHeight-20);
                        iv1.setZ(-10);

                        ObjectAnimator yAnim1 = ObjectAnimator.ofFloat(iv1, "alpha", 1, 0);
                        yAnim1.setDuration(2000);

                        yAnim1.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mainActivity.runOnUiThread(() -> {




                                        mainActivity.getLayout().removeView(iv1);

                                    Log.d(TAG, "run11: NUM VIEWS " +
                                            mainActivity.getLayout().getChildCount()+",,,"+ Missile.this.getX()+","+ Missile.this.getY());
                                });

                            }
                        });

//                        System.out.println("Done");
                        yAnim1.start();


                        mainActivity.getLayout().addView(iv1);



                    }
                    Log.d(TAG, "run: NUM VIEWS "+
                            mainActivity.getLayout().getChildCount());
                });

            }
        });



        ObjectAnimator yAnim = ObjectAnimator.ofFloat(imageView, "x", startX, endX);
        yAnim.setInterpolator(new LinearInterpolator());
        yAnim.setDuration(screenTime);
        yAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                imageView.setTranslationX(imageView.getTranslationX());
            }
        });





        aSet.playTogether(xAnim, yAnim);
        return aSet;

    }


    void stop() {aSet.cancel();}

    float getX() {
        return imageView.getX();
    }

    float getY() {
        return imageView.getY();
    }

    float getWidth() {
        return imageView.getWidth();
    }

    float getHeight() {
        return imageView.getHeight();
    }

    void interceptorBlast(float x, float y) {
        hit = true;
        SoundPlayer.getInstance().start("interceptor_blast");

        final ImageView iv = new ImageView(mainActivity);
        iv.setImageResource(R.drawable.explode);

        iv.setTransitionName("Missile Intercepted Blast");

        int w = imageView.getDrawable().getIntrinsicWidth();
        int offset = (int) (w * 0.5);

        iv.setX(x - offset);
        iv.setY(y - offset);
        iv.setRotation((float) (360.0 * Math.random()));

        aSet.cancel();


        mainActivity.getLayout().removeView(imageView);
        mainActivity.getLayout().addView(iv);

        final ObjectAnimator alpha = ObjectAnimator.ofFloat(iv, "alpha", 0.0f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.getLayout().removeView(imageView);
            }
        });
        alpha.start();
        hit = false;
    }

    void interceptorBaseBlast(float x, float y,ImageView launcher) {



        final ImageView iv = new ImageView(mainActivity);
        iv.setImageResource(R.drawable.blast);

        iv.setTransitionName("Missile Intercepted Blast");
        SoundPlayer.getInstance().start("base_blast");


        int w = imageView.getDrawable().getIntrinsicWidth();
        int offset = (int) (w * 0.5);

        iv.setX(x );
        iv.setY(y);
//        iv.setZ(-10);
//        iv.setRotation((float) (360.0 * Math.random()));

        aSet.cancel();

        mainActivity.getLayout().removeView(imageView);
        mainActivity.getLayout().addView(iv);




        mainActivity.getLayout().removeView(launcher);
//        launcher = null;


        final ObjectAnimator alpha = ObjectAnimator.ofFloat(iv, "alpha", 1,0);
       // alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(2000);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.getLayout().removeView(imageView);
//                MainActivity.base1.invsi
            }
        });
        alpha.start();
//        MainActivity.base1.setVisibility(View.GONE);

    }
}
