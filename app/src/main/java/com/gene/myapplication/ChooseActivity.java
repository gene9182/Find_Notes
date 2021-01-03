package com.gene.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;



public class ChooseActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static int RESULT_LOAD_IMAGE = 1;
    private Button myButton, myButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);


        //Keep screen On
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Force the vertical orientation for this activity
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        checkPermission();

        myButton = findViewById(R.id.myButton);
        myButton2 = findViewById(R.id.myButton3);

        //Get image from Phone Camera
        myButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                dispatchTakePictureIntent();
            }
        });


        //Get image from Gallery
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);


            }
        });

    }

    /**
     * @return void
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Log.i("Gene", "OnActivityResult button galleria");
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Log.i("Gene", "sono nella main prima dell'intent" + picturePath);
            Intent intent = new Intent(this, Matching.class);
            intent.putExtra("path", picturePath);
            startActivity(intent);

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.i("Gene", "OnActivityResult button fotocamera");
            galleryAddPic();
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                "example",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * @return void
     * @description this method save image into gallery
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Log.i("Gene", "Path della foto da fotocamera: " + "" + mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        Log.i("Gene", "Path della foto da fotocamera: " + "" + contentUri);

        Intent intent = new Intent(this, Matching.class);
        intent.putExtra("path", mCurrentPhotoPath);
        startActivity(intent);

        this.sendBroadcast(mediaScanIntent);
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
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home:
                Intent home = new Intent(this, ChooseActivity.class);
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

    /**
     * @return boolean
     * @description with this method we can check if user have granted us permission
     */
    public boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.permission_explain)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ChooseActivity.this,
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

    /**
     * @return void
     * @description with this method we can check the results of request permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // vediamo se il permission_granted corrisponde al fine location
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        isPermissionGranted = true;

                    }

                } else {
                    Toast.makeText(ChooseActivity.this,R.string.not, Toast.LENGTH_SHORT).show();
                    Log.i("Gene", "Permesso negato");
                    // permission denied. Disable the
                    // functionality that depends on this permission.
                    isPermissionGranted = false;

                }
                return;
            }

        }
    }

}
