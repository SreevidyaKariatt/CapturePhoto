package com.example.sreevidyak.capturephoto.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sreevidyak.capturephoto.R;
import com.example.sreevidyak.capturephoto.utils.RuntimePermissionUtils;

public class SmsActivity extends AppCompatActivity {

    private String mSenderExtra;
    private String mMessageExtra;

    private TextView mSenderTxtVw;
    private TextView mMessageTxtVw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RuntimePermissionUtils.requestPermission(this, Manifest.permission.READ_SMS,
                RuntimePermissionUtils.PermissionKeys.READ_SMS, null, new RuntimePermissionUtils.PermissionCallBack() {
                    @Override
                    public void onPermissionGranted() {
                        setViews();
                    }
                });
    }

    private void setViews() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mSenderExtra = extras.getString("Sender");
            mMessageExtra = extras.getString("Message");

        }
        setContentView(R.layout.sms_acivity);
        initViews();
        setData();

    }

    private void setData() {
        mSenderTxtVw.setText(mSenderExtra);
        mMessageTxtVw.setText(mMessageExtra);
    }

    private void initViews() {
        mSenderTxtVw = (TextView) findViewById(R.id.sender_val_text);
        mMessageTxtVw = (TextView) findViewById(R.id.message_val_text);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RuntimePermissionUtils.PermissionKeys.READ_SMS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setViews();

                } else {
                    Toast.makeText(getBaseContext(), "You must allow permission to read sms", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

}
