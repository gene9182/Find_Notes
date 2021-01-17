package com.gene.myapplication;


import android.content.Context;
import android.os.Build;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.io.FileOutputStream;

import static android.os.Looper.getMainLooper;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

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
    public void setUp() throws Exception {
        shadowOf(getMainLooper()).idle();
        matching = Robolectric.buildActivity(Matching.class).create().start().resume().get();
        chooseActivity = Robolectric.buildActivity(ChooseActivity.class).create().start().resume().postResume().get();
        ct = matching.getApplicationContext();
        matchButton = matching.findViewById(R.id.myButton2);
        scarica = matching.findViewById(R.id.myButton3);
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