package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xupu.locationmap.R;
import com.xupu.locationmap.common.tools.AndroidTool;

public class HelpActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_help);
        AndroidTool.addTitleFragment(this, "文档");
    }
}
