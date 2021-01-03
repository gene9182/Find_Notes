package com.gene.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class Credits extends AppCompatActivity {
    private MediaPlayer player;
    private ImageButton play, pause;

    /**
     * @return void
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        //Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    /**
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    /**
     * @return boolean
     * @params MenuItem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home:
                Intent home= new Intent(this, ChooseActivity.class);
                startActivity(home);
                return true;
            case R.id.faq:
                Intent faq = new Intent(this, FAQ.class);
                startActivity(faq);
                return true;
            case R.id.credits:
                Intent credits = new Intent(this, Credits.class);
                startActivity(credits);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
