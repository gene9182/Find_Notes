package com.gene.myapplication;


import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

/**
 * Unit tests for MainActivity.java
 * @author Generico Garofano & Nicola Malgieri
 * @version FindNotes.1.0.0
 * @description This class provide a unit test for MainActivity.java
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
@LooperMode(LooperMode.Mode.PAUSED)
public class MainActivityTest {
    private MainActivity ma;
    private Context cont;

    @Before
    public void setUp() throws Exception{
        ma = Robolectric.buildActivity(MainActivity.class).create().start().resume().get();
        cont = ma.getApplicationContext();
    }

    @Test
    public void runTest() throws Exception{

        shadowOf(getMainLooper()).idle();
        Thread.sleep(3000);
        Intent startedIntent = shadowOf(ma).getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertEquals(ChooseActivity.class, shadowIntent.getIntentClass());

    }


    @Test
    public void runAnimationTest() throws Exception{

        ShadowActivity sh = shadowOf(ma);
        shadowOf(getMainLooper()).idle();
        assertTrue(!sh.isFinishing());

    }


}
