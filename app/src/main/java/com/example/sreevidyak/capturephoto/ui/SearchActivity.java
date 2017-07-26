package com.example.sreevidyak.capturephoto.ui;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sreevidyak.capturephoto.R;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Button  mFindBtn;
    private TextView mIdTxtVw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        initViews();
    }

    private void initViews() {
        mFindBtn = (Button)findViewById(R.id.find_btn);
        mIdTxtVw = (TextView)findViewById(R.id.id_txt_vw);
        mFindBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.find_btn:
                findId();
                break;
        }
    }

    private void findId() {
        TelephonyManager tMgr = (TelephonyManager)getSystemService(this.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        if(!TextUtils.isEmpty(mPhoneNumber)) {
            mIdTxtVw.setText(mPhoneNumber);
        }else {
           mIdTxtVw.setText(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID));

        }
    }
}
