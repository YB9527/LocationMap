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

import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.PositionField;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.po.TableDataCustom_TableName;
import com.xupu.locationmap.projectmanager.po.TableViewCustom;
import com.xupu.locationmap.projectmanager.po.XZDM;
import com.xupu.locationmap.projectmanager.service.ZTService;

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


        AndroidTool.setFullWindow(this);
        setTitle("数据");
        setContentView(R.layout.activity_table_list);

        List<TableViewCustom> tableViewCustomList = new ArrayList<>();

        //得到要详细的表格
        //找到所有表
        //找到表的 对应的表id
        List<MyJSONObject> tables = TableTool.findByTableName(ZTService.PROJECT_TABLE_LIST);
        for (MyJSONObject table : tables){
            //TableDataCustom tableDataCustom2 = new TableDataCustom_TableName(R.layout.fragment_item, null, table);
            TableViewCustom  tc = new TableViewCustom(table.getJsonobject().getString("aliasname"), TableItemListFragment.class, TableDataCustom.class);
            //tc.setTableDataCustom(tableDataCustom2);
            tableViewCustomList.add(tc);
        }
        //init2();
        init(tableViewCustomList);
    }

    private void init(List<TableViewCustom> tableViewCustomList) {

        FragmentPagerItems.Creator Creator = FragmentPagerItems.with(this);

        for (TableViewCustom tableViewCustom : tableViewCustomList) {
            String tableName = tableViewCustom.getTableName();
            Class clazz = tableViewCustom.getItemFragMentClass();
            Bundle bundle = new Bundle();
            bundle.putString("tablename",tableName);
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
