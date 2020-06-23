package com.xupu.locationmap;

import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.google.android.material.navigation.NavigationView;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.projectmanager.page.HelpActivty;
import com.xupu.locationmap.projectmanager.page.LowMapManager;
import com.xupu.locationmap.projectmanager.page.MapFragment;
import com.xupu.locationmap.projectmanager.page.NFActivity;
import com.xupu.locationmap.projectmanager.page.ProjectPage;
import com.xupu.locationmap.projectmanager.page.TableListPage;
import com.xupu.locationmap.projectmanager.page.ToolActivty;
import com.xupu.locationmap.projectmanager.page.XZQYPage;
import com.xupu.locationmap.projectmanager.po.MapResult;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.XZQYService;
import com.xupu.locationmap.usermanager.page.UserInfo;
import com.xupu.locationmap.usermanager.service.UserService;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        AndroidTool.setFullWindow(this);
        setContentView(R.layout.activity_main);

       /* Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);*/

        init();


    }

    private MapView mMapView;
    MapFragment mapFragment;

    /**
     * 检查用户是否登录
     * 设置 Android tool 的active
     */
    private void init() {
        AndroidTool.setMainActivity(this);

        mMapView = findViewById(R.id.mv_tian_di_tu);

        //消除水印
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud4449636536,none,NKMFA0PL4S0DRJE15166");
        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.page, mapFragment, MapFragment.class.getSimpleName()).commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * 点击头像，到用户设置
     */
    private void setHeadOnClick() {
        ImageView imageView = this.findViewById(R.id.headImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserInfo.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    //第一次点击事件发生的时间
    private long mExitTime;

    /**
     * 点击两次返回退出app
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出APP", Toast.LENGTH_SHORT).show();
                //System.currentTimeMillis()系统当前时间
                mExitTime = System.currentTimeMillis();
            } else {
                mapFragment.close();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == MapResult.XZQYCHANGE && resultCode == MapResult.XZQYCHANGE) {
            *//*现将该fragment从fragmentList移除*//*
            getSupportFragmentManager().beginTransaction().replace(R.id.page, mapFragment, MapFragment.class.getSimpleName()).commit();
        }*/
    }
}
