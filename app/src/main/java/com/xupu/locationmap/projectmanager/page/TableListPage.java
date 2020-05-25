package com.xupu.locationmap.projectmanager.page;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.tools.AndroidTool;

import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.po.TableViewCustom;
import com.xupu.locationmap.projectmanager.po.XZDM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 普通表格数据装载
 */
public class TableListPage extends AppCompatActivity {


    public TableListPage(){


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  /*      AndroidTool.setFullWindow(this);
        setTitle("数据");
        setContentView(R.layout.activity_table_list);

        List<TableViewCustom> tableViewCustomList = new ArrayList<>();
        List<XZDM> xzdms = new ArrayList<>();
        xzdms.add(new XZDM("aa","123"));
        xzdms.add(new XZDM("bb","345"));
        xzdms.add(new XZDM("bb","345"));
        xzdms.add(new XZDM("bb","345"));
        xzdms.add(new XZDM("bb","345"));
        Map<Integer, FiledCustom> map = new HashMap<>();
        map.put(R.id.item_number, new FiledCustom("code"));
        map.put(R.id.content, new FiledCustom("caption"));

        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.fragment_item, map, xzdms);
        TableViewCustom tableViewCustom = new TableViewCustom("aa", ItemFragment.class, TableDataCustom.class, tableDataCustom);
        tableViewCustomList.add(tableViewCustom);
        tableViewCustom = new TableViewCustom("bb", ItemFragment.class, TableDataCustom.class, tableDataCustom);
        tableViewCustomList.add(tableViewCustom);

        //init2();
        init(tableViewCustomList);*/
    }

    private void init(List<TableViewCustom> tableViewCustomList) {

        FragmentPagerItems.Creator Creator = FragmentPagerItems.with(this);

        for (TableViewCustom tableViewCustom : tableViewCustomList) {
            String tableName = tableViewCustom.getTableName();
            Class clazz = tableViewCustom.getItemFragMentClass();
            Bundle bundle = new Bundle();
            bundle.putString("TableDataCustom",new Gson().toJson(tableViewCustom.getTableDataCustom()));
            //bundle.pu
            Creator.add(tableName, clazz,bundle);
        }

        //1、表格名称，2、fragment，3、要显示的字段，4、信息按钮
        //viewPagerTab.addView(viewPager);
        //viewPagerTab.addView(viewPager);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), Creator
                .create());

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        //viewPager.setCurrentItem(5);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("yb", "你点击了：");
            }
        });
        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
            @Override
            public void onTabClicked(int position) {
                Log.v("yb", "你点击了：" + position);
            }
        });
        viewPagerTab.setViewPager(viewPager);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }




}
