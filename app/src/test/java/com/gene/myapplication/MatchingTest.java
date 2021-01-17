package com.gene.myapplication;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import java.io.FileOutputStream;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Unit tests for MatchingActivity.java
 * @author SaraAP0, NMalgieri, gene9182
 */


@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
@LooperMode(LooperMode.Mode.PAUSED)
public class MatchingTest {

    private Matching matching;
    private Button matchButton, scarica;
    private Mat img = null;
    private ChooseActivity chooseActivity;
    private Context ct;


    @Before
    public void setUp () throws Exception{
        shadowOf(getMainLooper()).idle();
         matching = Robolectric.buildActivity(Matching.class).create().start().resume().get();
         chooseActivity = Robolectric.buildActivity(ChooseActivity.class).create().start().resume().postResume().get();
         ct = matching.getApplicationContext();
         matchButton = matching.findViewById(R.id.myButton2);
         scarica = matching.findViewById(R.id.myButton3);
    }

    @Test
    public void shouldMenuHomeVisible() throws Exception {
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem(R.id.home);
        matching.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity = shadowOf(matching);
        assertTrue(!shadowActivity.isFinishing());
    }

    @Test
    public void shouldMenuFaqVisible() throws Exception {
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem(R.id.faq);
        matching.onCreateOptionsMenu(mu);
        matching.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity = shadowOf(matching);
        assertTrue(!shadowActivity.isFinishing());
    }

    @Test
    public void shouldMenuCreditsVisible() throws Exception {
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem(R.id.credits);
        matching.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity = shadowOf(matching);
        assertTrue(!shadowActivity.isFinishing());
    }

    @Test
    public void shouldMenuDefaultVisible() throws Exception {
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem();
        matching.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity = shadowOf(matching);
        assertTrue(!shadowActivity.isFinishing());

    }

    @Test
    public void matchTest() throws Exception{
        shadowOf(getMainLooper()).idle();
        matchButton.performClick();
        Intent result = shadowOf(matching).getResultIntent();

        Intent intent= matching.getIntent();
        ShadowIntent shadowIntent1 = shadowOf(intent);

        Bundle extras = intent.getExtras();

        ShadowActivity shadowActivity = shadowOf(matching);



        Mat img;
        img= Imgcodecs.imread(String.valueOf(R.drawable.biscroma), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        matching.multiMatching(img);
    }



    @Test
    public void scaricaTest() throws Exception{
        shadowOf(getMainLooper()).idle();
        scarica.performClick();
    }

    @Test
    public void saveImgTest() throws Exception {
        shadowOf(getMainLooper()).idle();
        FileOutputStream mockFos = mock(FileOutputStream.class);
        mockFos.flush();
        mockFos.close();
        verify(mockFos).close();

    }



}
