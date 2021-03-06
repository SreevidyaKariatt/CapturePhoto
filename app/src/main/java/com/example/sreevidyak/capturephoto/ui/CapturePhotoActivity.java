package com.example.sreevidyak.capturephoto.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sreevidyak.capturephoto.R;
import com.example.sreevidyak.capturephoto.helper.FileHelper;
import com.example.sreevidyak.capturephoto.utils.RuntimePermissionUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * class for capturing an image and display as thumbnail. The image is saved to apps internal memory area
 * (Android/data/com.example.sreevidyak.capturephoto/files/Pictures)
 */

public class CapturePhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mPhtotImgVw;
    private Button mTakePhotoBtn;
    private Bitmap mPhotoBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RuntimePermissionUtils.requestPermission(this, Manifest.permission.CAMERA,
                RuntimePermissionUtils.PermissionKeys.CAMERA, null, new RuntimePermissionUtils.PermissionCallBack() {
                    @Override
                    public void onPermissionGranted() {
                        setUpViews();
                    }
                });

    }

    private void setUpViews(){
        setContentView(R.layout.photo_capture_activity);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RuntimePermissionUtils.PermissionKeys.CAMERA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpViews();

                } else {
                    finish();
                }
            }
        }
    }
}
