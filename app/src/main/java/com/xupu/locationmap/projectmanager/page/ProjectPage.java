package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.gyf.immersionbar.ImmersionBar;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.tools.AndroidTool;

public class ProjectPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        setTitle("请选择项目");
       /* ImmersionBar.with(this)
                .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                .transparentBar()  .navigationBarDarkIcon(true) .init();*/
        setContentView(R.layout.activity_project);

    }
}
