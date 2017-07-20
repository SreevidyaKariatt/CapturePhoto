package com.example.sreevidyak.capturephoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class for capturing an image and display as thumbnail. The image is saved to apps internal memory area
 * (Android/data/com.example.sreevidyak.capturephoto/files/Pictures)
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mPhtotImgVw;
    private Button mTakePhotoBtn;
    private Bitmap mPhotoBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mPhtotImgVw = (ImageView) findViewById(R.id.photo_img_vw);
        mTakePhotoBtn = (Button) findViewById(R.id.take_photo_btn);
        mTakePhotoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo_btn:
                dispatchTakePictureIntent();
                break;
        }
    }

    /**
     * intent for capturing photo
     */

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mPhotoBitmap = (Bitmap) extras.get("data");
            mPhtotImgVw.setImageBitmap(mPhotoBitmap);
            writeToFile();

        }
    }





    /**
     * method to reduce the size of the image and save in apps private storage area
     */
    public void writeToFile() {
        mPhotoBitmap = Bitmap.createScaledBitmap(mPhotoBitmap, 150, 150, false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        try {
            File photoFile = FileHelper.createImageFile(this);
            FileOutputStream fo = new FileOutputStream(photoFile);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
