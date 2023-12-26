package com.example.defensecommander;



import static com.example.defensecommander.MainActivity.screenHeight;
import static com.example.defensecommander.MainActivity.screenWidth;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ParallaxBackground implements Runnable {

    private final Context context;

//    private Boolean alpha = true;

    private int alpha =0;
    private final ViewGroup layout;
    private ImageView backImageA;
    private ImageView backImageB;
    private final long duration;
    private final int resId;
    private static final String TAG = "ParallaxBackground";
    private static boolean running = true;


    ParallaxBackground(Context context, ViewGroup layout, int resId, long duration) {
        this.context = context;
        this.layout = layout;
        this.resId = resId;
        this.duration = duration;

        setupBackground();
    }

    public static void stop() {
        running = false;
    }

    private void setupBackground() {
        backImageA = new ImageView(context);
        backImageB = new ImageView(context);

        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(screenWidth + getBarHeight(), screenHeight);
        backImageA.setLayoutParams(params);
        backImageB.setLayoutParams(params);

        layout.addView(backImageA);
        layout.addView(backImageB);

        Bitmap backBitmapA = BitmapFactory.decodeResource(context.getResources(), resId);
        Bitmap backBitmapB = BitmapFactory.decodeResource(context.getResources(), resId);

        backImageA.setImageBitmap(backBitmapA);
        backImageB.setImageBitmap(backBitmapB);

        backImageA.setScaleType(ImageView.ScaleType.FIT_XY);
        backImageB.setScaleType(ImageView.ScaleType.FIT_XY);

        backImageA.setZ(1);
        backImageB.setZ(1);

        animateBack();
        //new Thread(this).start();
    }

    @Override
    public void run() {


        backImageA.setX(0);
        backImageB.setX(-(screenWidth + getBarHeight()));
        double cycleTime = 25.0;

        double cycles = duration / cycleTime;
        double distance = (screenWidth + getBarHeight()) / cycles;

        while (running) {
            Log.d(TAG, "run: START WHILE");

            long start = System.currentTimeMillis();

            double aX = backImageA.getX() - distance;
            backImageA.setX((float) aX);
            double bX = backImageB.getX() - distance;
            backImageB.setX((float) bX);

            long workTime = System.currentTimeMillis() - start;

            if (backImageA.getX() < -(screenWidth + getBarHeight()))
                backImageA.setX((screenWidth + getBarHeight()));

            if (backImageB.getX() < -(screenWidth + getBarHeight()))
                backImageB.setX((screenWidth + getBarHeight()));

            long sleepTime = (long) (cycleTime - workTime);

            if (sleepTime <= 0) {
                Log.d(TAG, "run: NOT KEEPING UP! " + sleepTime);
                continue;
            }

            try {
                Thread.sleep((long) (cycleTime - workTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "run: END WHILE");

        }
    }

    private void animateBack() {

        float x =0;
        float y=0;
        ValueAnimator animator = ValueAnimator.ofFloat(0.25f, 0.95f);;

        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(duration);
        final float[] prev = {0.0F};
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    float animatedValue = (float) valueAnimator.getAnimatedValue();
//                System.out.println("alpha :"+backImageA.getAlpha());
//                float p = animatedValue - prev[0];
//                System.out.println("animated: "+animatedValue);


                if(alpha%2==0){
                    backImageA.setAlpha(animatedValue);
                    System.out.println("1111: "+ animatedValue);

                    backImageB.setAlpha(animatedValue);

                } else if (alpha%2==1)
                {
                    backImageA.setAlpha(1.00F-animatedValue+20.00F);
                    System.out.println("2222: "+ (1.00F-animatedValue+0.20F));

                    backImageB.setAlpha(1.00F-animatedValue+20.00F);

                }
                if(animatedValue==0.95F){
                    System.out.println("IN");
                    alpha++;}
//                prev[0] = animatedValue;




            }
        });

        animator.addUpdateListener(animation -> {
            if (!running) {
                animator.cancel();
                return;
            }
            final float progress = (float) animation.getAnimatedValue();
            float width = screenWidth + getBarHeight();

            float a_translationX = width * progress;
            float b_translationX = width * progress - width;

            backImageA.setTranslationX(a_translationX);
            backImageB.setTranslationX(b_translationX);

        });
        animator.start();
    }


    private int getBarHeight() {
        return (int) Math.ceil(25 * context.getResources().getDisplayMetrics().density);
    }

}
