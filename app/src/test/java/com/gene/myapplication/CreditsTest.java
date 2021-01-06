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
 * Unit tests for Credits.java
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
@LooperMode(LooperMode.Mode.PAUSED)
public class CreditsTest {
    private Credits cr;

    @Before
    public void setUp() throws Exception{
        cr = Robolectric.buildActivity(Credits.class).create().resume().get();
    }

    @Test
    public void shouldMenuHomeVisible() throws Exception{
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem(R.id.home);
        cr.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity= Shadows.shadowOf(cr);
        assertTrue(!shadowActivity.isFinishing());
    }

    @Test
    public void shouldMenuFaqVisible() throws Exception{
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem(R.id.faq);
        cr.onCreateOptionsMenu(mu);
        cr.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity= Shadows.shadowOf(cr);
        assertTrue(!shadowActivity.isFinishing());
    }
    @Test
    public void shouldMenuCreditsVisible() throws Exception{
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem(R.id.credits);
        cr.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity= Shadows.shadowOf(cr);
        assertTrue(!shadowActivity.isFinishing());
    }

    @Test
    public void shouldMenuDefaultVisible() throws Exception{
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem();
        cr.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity= Shadows.shadowOf(cr);
        assertTrue(!shadowActivity.isFinishing());

    }
}
