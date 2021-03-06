package com.gene.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;


/**
* @author Generico Garofano
* @description This activity provide a splash screen for the app
* @version FindNotes.0.0.1
*/

@VisibleForTesting
public class MainActivity extends AppCompatActivity {

    @Override
    @VisibleForTesting
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        runAnimation();

        //Tiene lo schermo acceso
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(), ChooseActivity.class));
                finish();
            }
        }, 3000);

    }


    /**
     * @return void
     * @description this methon provide an animation for a String 
     */

    @VisibleForTesting
    public void runAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.textView6);
        tv.clearAnimation();
        tv.startAnimation(a);
    }


}
