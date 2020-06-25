package com.xupu.locationmap.usermanager.page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.tools.AndroidTool;

public class Regist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidTool.setFullWindow(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_regist);

        initTitle();
    }

    private void initTitle() {

        AndroidTool.addTitleFragment(this, "注册");

    }
}
