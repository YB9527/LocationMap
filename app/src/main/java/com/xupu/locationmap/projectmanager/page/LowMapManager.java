package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;

import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.tools.AndroidTool;

public class LowMapManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle();
        setContentView(R.layout.activity_low_map_manager);

    }

    private void initTitle() {
        AndroidTool.setFullWindow(this);
        getSupportActionBar().hide();
        AndroidTool.addTitleFragment(this, "底图管理");
    }
}
