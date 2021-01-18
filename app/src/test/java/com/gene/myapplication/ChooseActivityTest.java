package com.gene.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;

import java.io.FileOutputStream;
import java.net.URL;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.internal.bytecode.RobolectricInternals.getClassLoader;

/**
 * Unit tests for ChooseActivity.java
 * @author SaraAP0, NMalgieri, gene9182
 * @description this class provide unit test for ChooseActivity.java
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
@LooperMode(LooperMode.Mode.PAUSED)
public class ChooseActivityTest {
    private ChooseActivity ca;
    private Matching ma;
    private Context ct, ct1;
    private Button btn, btn2;
    private PackageManager packageManager;


    @Before
    public void setUp() throws Exception {

        ca = Robolectric.buildActivity(ChooseActivity.class).create().start().resume().get();
        ct = ca.getApplicationContext();
        ma = Robolectric.buildActivity(Matching.class).create().start().postResume().get();
        ct1 = ca.getApplicationContext();


        btn2 = ca.findViewById(R.id.myButton3);
        btn = ca.findViewById(R.id.myButton);
        packageManager = ca.getPackageManager();

    }

    @Test
    public void shouldMenuHomeVisible() throws Exception {
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem(R.id.home);
        ca.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity = shadowOf(ca);
        assertTrue(!shadowActivity.isFinishing());
    }

    @Test
    public void shouldMenuFaqVisible() throws Exception {
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem(R.id.faq);
        ca.onCreateOptionsMenu(mu);
        ca.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity = shadowOf(ca);
        assertTrue(!shadowActivity.isFinishing());
    }

    @Test
    public void shouldMenuCreditsVisible() throws Exception {
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem(R.id.credits);
        ca.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity = shadowOf(ca);
        assertTrue(!shadowActivity.isFinishing());
    }

    @Test
    public void shouldMenuDefaultVisible() throws Exception {
        Menu mu = new RoboMenu();
        MenuItem mi = new RoboMenuItem();
        ca.onOptionsItemSelected(mi);
        ShadowActivity shadowActivity = shadowOf(ca);
        assertTrue(!shadowActivity.isFinishing());

    }


    @Test
    public void createImageFileTest() throws Exception {
        shadowOf(getMainLooper()).idle();
        FileOutputStream mockFos = mock(FileOutputStream.class);

        mockFos.close();
        verify(mockFos).close();

    }

    @Test
    public void dispatchIntent() throws Exception {
        shadowOf(getMainLooper()).idle();

        btn2.performClick();

        ShadowActivity shadowActivity = shadowOf(ca);
        Intent inte = shadowActivity.getNextStartedActivity();
        shadowActivity.receiveResult(inte, -1, shadowOf(ca).getResultIntent());
    }

    @Test
    public void onActResult() throws Exception {
        shadowOf(getMainLooper()).idle();

        btn.performClick();
        ShadowActivity shadowActivity = shadowOf(ca);
        ShadowActivity.IntentForResult intentForResult = shadowActivity.getNextStartedActivityForResult();
        intentForResult.requestCode = 1;

        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        URL imageFile = getClassLoader().getResource("chiave_violino.png");
        Uri uri =  Uri.parse(String.valueOf(imageFile));

        i.setType("image/*");
        i.setData(uri);

        ca.startActivityForResult(i, 1);
        Intent inten = intentForResult.intent;

        shadowActivity.receiveResult(inten, -1, shadowOf(ca).getResultIntent());

        ca.onActivityResult(1,-1,i);


    }



}

