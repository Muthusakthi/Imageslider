package com.sllider.alto.imageslider;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;


public class Imageslider extends Activity {
    private LinearLayout  horizontalOuterLayout;
    private HorizontalScrollView horizontalScrollview;
    RelativeLayout netflix_layout;
    private int scrollMax;
    private int scrollPos =	0;
    ImageView play;
    private TimerTask clickSchedule;
    private TimerTask scrollerSchedule;
    private TimerTask faceAnimationSchedule;
    private Button clickedButton	=	null;
    private Timer scrollTimer		=	null;
    private Timer clickTimer		=	null;
    private Timer faceTimer         =   null;
    private Boolean isFaceDown      =   true;
    private String[] imageNameArray = {"come_sunday", "game_over", "anni", "mute", "first_match","clover"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_slider);
        netflix_layout = (RelativeLayout) findViewById(R.id.netflix_relative);
        horizontalScrollview  = (HorizontalScrollView) findViewById(R.id.horiztonal_scrollview_id);
        horizontalOuterLayout =	(LinearLayout)findViewById(R.id.horiztonal_outer_layout_id);
        play = (ImageView) findViewById(R.id.play);


        isFaceDown = true;
        addImagesToView();

        ViewTreeObserver vto 		=	horizontalOuterLayout.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                horizontalOuterLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                getScrollMaxAmount();
                startAutoScrolling();

                play.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));


            }

        });
    }
    /** Adds the images to view. */

    public void getScrollMaxAmount(){
        // int actualWidth = (horizontalOuterLayout.getMeasuredWidth()-512);
        int actualWidth = (horizontalOuterLayout.getMeasuredWidth() - getWindowManager().getDefaultDisplay().getWidth());

        scrollMax   = actualWidth;
    }

    public void startAutoScrolling(){
        // play.setImageResource(R.drawable.pause_netflix1);
        if (scrollTimer == null) {
            scrollTimer					=	new Timer();
            final Runnable Timer_Tick 	= 	new Runnable() {
                public void run() {
                    moveScrollView();
                }
            };

            if(scrollerSchedule != null){
                scrollerSchedule.cancel();
                scrollerSchedule = null;
            }
            scrollerSchedule = new TimerTask(){
                @Override
                public void run(){
                    runOnUiThread(Timer_Tick);
                }
            };

            scrollTimer.schedule(scrollerSchedule, 30, 30);
            //play.setImageResource(R.drawable.pause_netflix1);
        }

    }

    public void moveScrollView(){
        scrollPos							= 	(int) (horizontalScrollview.getScrollX() + 1.0);
        if(scrollPos >= scrollMax){
            addImagesToView();
            getScrollMaxAmount();
        }

        horizontalScrollview.scrollTo(scrollPos, 0);

    }
    public void addImagesToView(){
        for (int i=0;i<imageNameArray.length;i++){
            final Button imageButton =	new Button(this);
            int imageResourceId		 =	getResources().getIdentifier(imageNameArray[i], "drawable",getPackageName());
            Drawable image 			 =	this.getResources().getDrawable(imageResourceId);
            imageButton.setBackgroundDrawable(image);
            imageButton.setTag(i);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (isFaceDown == true) {
                        stopAutoScrolling();
                        play.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                        isFaceDown = false;
                    } else if (isFaceDown == false) {
                        startAutoScrolling();
                        isFaceDown = true;
                        play.setBackground(getResources().getDrawable(android.R.drawable.ic_media_pause));
                    }

                }
            });


            LinearLayout.LayoutParams params = 	new LinearLayout.LayoutParams(256,320);
            params.setMargins(2, 0, 0, 0);
            imageButton.setLayoutParams(params);
            horizontalOuterLayout.addView(imageButton);

            //play.setBackground(getResources().getDrawable(R.drawable.pause_netflix1));
        }


    }



    public void stopAutoScrolling(){
        if (scrollTimer != null) {
            scrollTimer.cancel();
            scrollTimer	=	null;
        }
    }



    public void onPause() {
        super.onPause();
        finish();
    }

    public void onDestroy(){
        clearTimerTaks(clickSchedule);
        clearTimerTaks(scrollerSchedule);
        clearTimerTaks(faceAnimationSchedule);
        clearTimers(scrollTimer);
        clearTimers(clickTimer);
        clearTimers(faceTimer);

        clickSchedule         = null;
        scrollerSchedule      = null;
        faceAnimationSchedule = null;
        scrollTimer           = null;
        clickTimer            = null;
        faceTimer             = null;
        super.onDestroy();
    }

    private void clearTimers(Timer timer){
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void clearTimerTaks(TimerTask timerTask){
        if(timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}