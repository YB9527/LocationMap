package com.xupu.locationmap.usermanager.page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xupu.locationmap.R;

public class UserInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("用户信息");
        setContentView(R.layout.activity_user_info);
    }
}
