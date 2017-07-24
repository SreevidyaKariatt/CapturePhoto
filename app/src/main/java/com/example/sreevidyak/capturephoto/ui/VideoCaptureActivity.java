package com.example.sreevidyak.capturephoto.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sreevidyak.capturephoto.R;
import com.example.sreevidyak.capturephoto.utils.RuntimePermissionUtils;

import java.io.IOException;

public class VideoCaptureActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView mCaptureVideoView;
    private Button mTakeVideoBtn;

    static final int REQUEST_VIDEO_CAPTURE = 1;


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

    private void setUpViews() {
        setContentView(R.layout.video_capture_activity);
        initViews();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RuntimePermissionUtils.PermissionKeys.CAMERA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpViews();

                } else {
                    Toast.makeText(getBaseContext(), "You must allow permission record video to your mobile device.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private void initViews() {
        mCaptureVideoView = (VideoView) findViewById(R.id.capture_video_view);
        mTakeVideoBtn = (Button)findViewById(R.id.take_video_btn);
        mTakeVideoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.take_video_btn:
                dispatchTakeVideoIntent();
                break;
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            mCaptureVideoView.setVideoURI(videoUri);
            mCaptureVideoView.start();
        }

    }
}
