package com.xupu.locationmap;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuInflater;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.projectmanager.page.ItemFragment;
import com.xupu.locationmap.projectmanager.page.LowMapManager;
import com.xupu.locationmap.projectmanager.page.NFActivity;
import com.xupu.locationmap.projectmanager.page.ProjectPage;
import com.xupu.locationmap.projectmanager.page.TableListPage;
import com.xupu.locationmap.projectmanager.page.XZQYPage;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.po.TableViewCustom;
import com.xupu.locationmap.projectmanager.po.XZDM;
import com.xupu.locationmap.usermanager.page.UserInfo;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
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

    /**
     * 检查用户是否登录
     *设置 Android tool 的active
     */
    private void init() {
        AndroidTool.setMainActivity(this);
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
        switch (id){
            case R.id.m_myproject:

                intent = new Intent(this, ProjectPage.class);
                startActivity(intent);
                break;
            case R.id.m_xzqy:
                intent = new Intent(this, XZQYPage.class);
                startActivity(intent);
                break;
            case R.id.m_lowmapmanager:
                 intent = new Intent(this, LowMapManager.class);
                startActivity(intent);
                break;
            case R.id.m_nfmanager:
                intent = new Intent(this, NFActivity.class);
                startActivity(intent);
                break;
            case R.id.m_tablelist:
                intent = new Intent(this, TableListPage.class);
                startActivity(intent);
                break;
            case R.id.m_help:
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.activity_main_drawer, menu);
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
}
