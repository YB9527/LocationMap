package com.xupu.locationmap.usermanager.page;

import android.app.Activity;
import android.os.Bundle;

import com.xupu.locationmap.R;

public class UserInfo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("用户信息");
        setContentView(R.layout.activity_login);
    }
}
