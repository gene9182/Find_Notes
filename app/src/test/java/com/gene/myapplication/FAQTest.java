package com.gene.myapplication;


import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for FAQ.java
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
@LooperMode(LooperMode.Mode.PAUSED)
public class FAQTest {
    private FAQ fq;

    @Before
    public void setUp() throws Exception{
        fq = Robolectric.buildActivity(FAQ.class).create().resume().get();
    }

    @Test
    public void shouldMenuHomeVisible() throws Exception{
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem(R.id.home);
        fq.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity= Shadows.shadowOf(fq);
        assertTrue(!shadowActivity.isFinishing());
    }

    @Test
    public void shouldMenuFaqVisible() throws Exception{
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem(R.id.faq);
        fq.onCreateOptionsMenu(mu);
        fq.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity= Shadows.shadowOf(fq);
        assertTrue(!shadowActivity.isFinishing());
    }
    @Test
    public void shouldMenuCreditsVisible() throws Exception{
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem(R.id.credits);
        fq.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity= Shadows.shadowOf(fq);
        assertTrue(!shadowActivity.isFinishing());
    }

    @Test
    public void shouldMenuDefaultVisible() throws Exception{
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem();
        fq.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity= Shadows.shadowOf(fq);
        assertTrue(!shadowActivity.isFinishing());

    }
}