package com.lnsel.erp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import com.lnsel.erp.R;
import com.lnsel.erp.constant.Sharepreferences;
import com.lnsel.erp.settergetter.UserInfo;

public class SplashScreen extends Activity implements Animation.AnimationListener{

    private static int SPLASH_TIME_OUT = 1000;

    ImageView image_view_1;

    protected int seconds = 2;
    Handler handler1 = new Handler();
    Handler handler2 = new Handler();
    Handler handler3 = new Handler();
    Handler handler4 = new Handler();

    // Animation
    Animation animlogo,anim_img_bg1,anim_img_bg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                UserInfo rememberData= Sharepreferences.getUserinfo(SplashScreen.this);
                if(rememberData.getLogin()) {

                    if ((rememberData.getReporting_person().equalsIgnoreCase("1"))) {
                        startActivity(new Intent(SplashScreen.this, HomeScreen.class));
                        finish();
                    }else {
                        startActivity(new Intent(SplashScreen.this, HomeScreen_2.class));
                        finish();
                    }

                }else {
                    Intent i = new Intent(SplashScreen.this, LoginScreen.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

        //map_icon=(ImageView)findViewById(R.id.map_icon);
       // logo=(ImageView)findViewById(R.id.img_icon);

        /*image_view_1=(ImageView)findViewById(R.id.img_icon);

        // load the animation
        animlogo = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        anim_img_bg1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        anim_img_bg2 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);

        // set animation listener
        animlogo.setAnimationListener(this);
        anim_img_bg1.setAnimationListener(this);
        anim_img_bg2.setAnimationListener(this);

        handler1.removeCallbacks(runnable1);
        handler1.postDelayed(runnable1, 100);*/

    }


    private Runnable runnable1 = new Runnable() {
        public void run() {
            long currentMilliseconds = System.currentTimeMillis();
            seconds--;
            if (seconds > 0) {
                handler1.postAtTime(this, currentMilliseconds);
                handler1.postDelayed(runnable1, 100);
            } else {
                image_view_1.setVisibility(View.VISIBLE);
                // start the animation
                image_view_1.startAnimation(animlogo);

                handler4.removeCallbacks(runnable4);
                handler4.postDelayed(runnable4, 2000);

            }
        }
    };


    private Runnable runnable2 = new Runnable() {
        public void run() {
            long currentMilliseconds = System.currentTimeMillis();
            seconds--;
            if (seconds > 0) {
                handler2.postAtTime(this, currentMilliseconds);
                handler2.postDelayed(runnable2, 1000);
            } else {

                image_view_1.setVisibility(View.VISIBLE);
                // start the animation
                image_view_1.startAnimation(anim_img_bg1);
               /* handler3.removeCallbacks(runnable3);
                handler3.postDelayed(runnable3, 1000);*/
                handler4.removeCallbacks(runnable4);
                handler4.postDelayed(runnable4, 2000);

            }
        }
    };


    private Runnable runnable4 = new Runnable() {
        public void run() {
            long currentMilliseconds = System.currentTimeMillis();
            seconds--;
            if (seconds > 0) {
                handler4.postAtTime(this, currentMilliseconds);
                handler4.postDelayed(runnable4, 2000);
            } else {
                UserInfo rememberData= Sharepreferences.getUserinfo(SplashScreen.this);
                if(rememberData.getLogin()) {

                    if ((rememberData.getReporting_person().equalsIgnoreCase("1"))) {

                        Intent i = new Intent(SplashScreen.this, HomeScreen.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);//for left to right transition
                        finish();

                    }else {

                        Intent i = new Intent(SplashScreen.this, HomeScreen_2.class);
                        //overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);//for left to right transition
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);//for left to right transition
                        finish();


                       // startActivity(new Intent(SplashScreen.this, HomeScreen_2.class));
                       // overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);//for left to right transition
                       // finish();
                    }

                }else {
                    Intent i = new Intent(SplashScreen.this, LoginScreen.class);
                    //overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);//for left to right transition
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);//for left to right transition
                    finish();
                }

                // close this activity
            }
        }
    };


    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for fade in animation
        if (animation == animlogo) {
            /*Toast.makeText(getApplicationContext(), "Animation Stopped",
                    Toast.LENGTH_SHORT).show();*/
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }


}
