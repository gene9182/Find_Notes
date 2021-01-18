package com.gene.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Generico Garofano
 * @version FindNotes.0.0.1
 * @description this activity provide all methods for matching the source image with template image
 */

public class Matching extends AppCompatActivity {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private Mat result;
    private com.zolad.zoominimageview.ZoomInImageView imageView;
    private double threshold = 0.40;
    private double maxval;
    private Mat dst;
    private String picPath;
    private Bitmap bitmapTemplate;
    private Button matchButton,scarica;
    private Mat img;
    private int count=0;
    private ProgressBar progress;
    private Bitmap bmp = null;


    /**
     * @return void
     * @params Bundle
     */
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        //Keep Screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        progress=findViewById(R.id.progressBar);


        checkPermission();
        progress.setVisibility(View.INVISIBLE);

        imageView=findViewById(R.id.image2);
        matchButton=findViewById(R.id.myButton2);
        scarica=findViewById(R.id.myButton3);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            picPath = extras.getString("path");
        }

        bitmapTemplate=BitmapFactory.decodeFile(picPath);

        scarica.setClickable(false);
        Log.i("Gene", "Bottone cliccabile: "+scarica.isClickable());

        //Button not visible
        scarica.setVisibility(4);
        scarica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveImg();
                    Log.i("Gene", "Immagine salvata");
                    Toast.makeText(Matching.this, R.string.scaricato,Toast.LENGTH_LONG ).show();
                } catch (IOException e) {
                    Toast.makeText(Matching.this, R.string.non_scaricato, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        imageView.setImageBitmap(bitmapTemplate);


        matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Matching.this, R.string.elaborazione, Toast.LENGTH_LONG).show();
                progress.setVisibility(View.VISIBLE);

                matchButton.setClickable(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        multiMatching(img);
                    }
                }, 2000);
                Log.i("Gene", "Multi matching");
            }
        });

        Log.i("Gene", "ho convertito il path in bitmap"+bitmapTemplate.toString());
    }

    public BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {

            if(status== LoaderCallbackInterface.SUCCESS){
                result = new Mat();

                //Matching con immagine da galleria
                img= Imgcodecs.imread(picPath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

            }

        }
    };



    /**
     * @return void
     * @params Mat (OpenCV MAT)
     */
    @SuppressLint("WrongConstant")
    public void multiMatching(Mat img){

        try {
            //In this Try block we have all template images
            Mat tplChiave_violino = Utils.loadResource(Matching.this, R.drawable.chiave_violino, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            search(tplChiave_violino, "Chiave Violino",img);

            Mat tplChiave= Utils.loadResource(Matching.this, R.drawable.chiave, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            search(tplChiave, "Chiave",img);

            Mat tplBiscroma= Utils.loadResource(Matching.this, R.drawable.biscroma, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            search(tplChiave, "Biscroma",img);

            Mat tplCroma= Utils.loadResource(Matching.this, R.drawable.croma, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            search(tplChiave, "Croma",img);

            Mat tplMinima= Utils.loadResource(Matching.this, R.drawable.minima, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            search(tplChiave, "Minima",img);

            Mat tplSemiCroma= Utils.loadResource(Matching.this, R.drawable.semi_croma, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            search(tplChiave, "Semi Croma",img);

            try {
                bmp = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(dst, bmp);

                imageView.setImageBitmap(bmp);
                scarica.setClickable(true);

                //Button visible
                scarica.setVisibility(0);
                Log.i("Gene","Visibilità: "+scarica.getVisibility());

                Toast.makeText(Matching.this,R.string.fatto, Toast.LENGTH_LONG).show();


            }
            catch (CvException e){
                Log.d("Exception",e.getMessage());}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Dynamic String generator
    public String generateFilename(int k){
        StringBuilder builder=new StringBuilder();
        while(k-- !=0){
            int ch=(int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(ch));

        }
        return builder.toString();
    }


    //Save the result image into gallery
    public void saveImg() throws IOException {
        String filename= generateFilename(5)+".png";
        File sd= Environment.getExternalStorageDirectory();
        File dest=new File(sd,filename);
        FileOutputStream fos=new FileOutputStream(dest);
        bmp.compress(Bitmap.CompressFormat.PNG,90,fos);

    }

    public void search(Mat tpl, String text, Mat img ) throws IOException {

        //Template Matching
        Imgproc.matchTemplate(img, tpl, result, Imgproc.TM_CCOEFF_NORMED);
        Imgproc.threshold(result, result, 0.10, 1, Imgproc.THRESH_TOZERO);

        Core.MinMaxLocResult maxr = Core.minMaxLoc(result);
        Point maxp = maxr.maxLoc;
        maxval = maxr.maxVal;

        Point maxop = new Point(maxp.x + tpl.width(), maxp.y + tpl.height());
        dst = img.clone();

            if (maxval >= threshold) {

                Imgproc.rectangle(img, maxp, new Point(maxop.x + tpl.cols(),
                        maxop.y + tpl.rows()), new Scalar(0, 255, 0),2,4,0);
                Imgproc.putText
                        (img,
                                text,
                                new Point(maxp.x,
                                        maxp.y ),
                                Core.FONT_HERSHEY_SCRIPT_COMPLEX,
                                1,
                                new Scalar(0, 255, 0),1);

                Imgproc.rectangle(result, maxp, new Point(maxop.x + tpl.cols(),
                        maxop.y + tpl.rows()), new Scalar(0, 255, 0), 2,4,0);

                count++;

                Log.i("Gene", "Sto facendo " + count + " " + "matching");


            }

        progress.setVisibility(View.INVISIBLE);

        matchButton.setClickable(true);


    }

    public void onResume()
    {
        super.onResume();
        checkPermission();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


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


    public static final int MY_PERMISSIONS_REQUEST = 99;

    private boolean isPermissionGranted = false;

    public boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {


                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.permission_explain)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Matching.this,
                                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);
            }
            isPermissionGranted=false;
            return false;
        } else {
            isPermissionGranted=true;
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // check if permission_granted is equals to fine location
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        isPermissionGranted = true;

                    }

                } else {
                    Toast.makeText(Matching.this,R.string.not, Toast.LENGTH_SHORT).show();
                    // permission denied. Disable the
                    // functionality that depends on this permission.
                    isPermissionGranted = false;

                }
                return;
            }

        }
    }

}