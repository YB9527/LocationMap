package com.xupu.locationmap;

import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        AndroidTool.setFullWindow(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        init();


    }
    private MapView mMapView;
    MapFragment mapFragment ;
    /**
     * 检查用户是否登录
     * 设置 Android tool 的active
     */
    private void init() {
        AndroidTool.setMainActivity(this);

        mMapView =findViewById(R.id.mv_tian_di_tu);

        //消除水印
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud4449636536,none,NKMFA0PL4S0DRJE15166");
        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.page, mapFragment,"tinaditu").commit();

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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.m_myproject:
                toPrjectPage();
                break;
            case R.id.m_xzqy:
                if(ProjectService.getCurrentSugProject() == null){
                    toPrjectPage();
                    AndroidTool.showAnsyTost("请先选择项目",1);
                }else{
                    intent = new Intent(this, XZQYPage.class);
                    startActivity(intent);
                }
                break;
            case R.id.m_lowmapmanager:
                intent = new Intent(this, LowMapManager.class);
                //startActivity(intent);
                mapFragment.startActivityForResult(intent, MapResult.layer);
                break;
            case R.id.m_nfmanager:
                intent = new Intent(this, NFActivity.class);
                startActivity(intent);
                break;
            case R.id.m_tablelist:
                if(ProjectService.getCurrentSugProject() == null){

                    toPrjectPage();
                    AndroidTool.showAnsyTost("请先选择项目，在选择区域",1);
                }else if(XZQYService.getCurrentXZDM() == null){

                    intent = new Intent(this, XZQYPage.class);
                    startActivity(intent);
                    AndroidTool.showAnsyTost("请先选择区域",1);
                }else{
                    intent = new Intent(this, TableListPage.class);
                    mapFragment.startActivityForResult(intent, MapResult.datalocation);
                }


                break;
            case R.id.m_help:
                intent = new Intent(this, HelpActivty.class);
                startActivity(intent);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void toPrjectPage(){
        Intent intent = new Intent(this, ProjectPage.class);
        //intent = new Intent(this, Ph.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //导航栏设置用户、账号
        TextView tv_nickname = findViewById(R.id.tv_nickname);
        tv_nickname.setText(UserService.getUser().getNickName());
        TextView tv_account = findViewById(R.id.tv_account);
        tv_account.setText(UserService.getUser().getAccount());
        MenuInflater inflater = getMenuInflater();
        setHeadOnClick();
        return true;
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



}
