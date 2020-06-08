package com.xupu.locationmap.projectmanager.page;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.tools.AndroidTool;

import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.common.tools.Tool;
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


    List<MyJSONObject> tables;
   public List<String> tableids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        getSupportActionBar().hide();
        initTitle();
        setContentView(R.layout.activity_table_list);
        List<TableViewCustom> tableViewCustomList = new ArrayList<>();
        //得到要详细的表格
        //找到所有表
        //找到表的 对应的表id
        tables = TableTool.findByTableName(ZTService.PROJECT_TABLE_LIST);
        for (MyJSONObject table : tables) {
            //TableDataCustom tableDataCustom2 = new TableDataCustom_TableName(R.layout.fragment_item, null, table);
            TableViewCustom tc = new TableViewCustom(table.getJsonobject().getString("aliasname"), TableItemListFragment.class, TableDataCustom.class);
            //tc.setTableDataCustom(tableDataCustom2);
            tc.setTableid(ZTService.getItemIdByTableId(table.getId()));
            tableids.add(ZTService.getItemIdByTableId(table.getId()));
            tableViewCustomList.add(tc);
        }
        //init2();
        init(tableViewCustomList);
    }
    private void initTitle() {
        AndroidTool.addTitleFragment(this, "调查数据", R.mipmap.topnav_icon_new, "添加", new Callback() {
            @Override
            public void call(Object o) {
                //跳到添加对象也米娜
                //toAddDataPage(null);
            }
        });
    }

   public int position = 0;

    private void init(List<TableViewCustom> tableViewCustomList) {

        FragmentPagerItems.Creator Creator = FragmentPagerItems.with(this);

        for (TableViewCustom tableViewCustom : tableViewCustomList) {
            String tableName = tableViewCustom.getTableName();
            Class clazz = tableViewCustom.getItemFragMentClass();
            Bundle bundle = new Bundle();
            bundle.putString("tablename", tableName);
            bundle.putString("tableid", tableViewCustom.getTableid());
            //bundle.pu
            Creator.add(tableName, clazz, bundle);
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
                TableListPage.this.position = position;
            }
        });
        viewPagerTab.setViewPager(viewPager);

        /*FloatingActionButton btn = findViewById(R.id.btn_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TableListPage.this, ObjectInfoActivty.class);
                intent.putExtra("state", TableTool.STATE_INSERT);
                intent.putExtra("id", tableids.get(position));
                RecyclerView recyclerView = findViewById(R.id.recy);
                myItemRecyclerViewAdapter = (MyItemRecyclerViewAdapter) recyclerView.getAdapter();
                startActivityForResult(intent, 2);


            }
        });*/

    }

    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                switch (resultCode) {
                    case TableTool.STATE_UPDATE:
                        //这是增加，用的是修改按钮而已
                        MyJSONObject newobj = (MyJSONObject) data.getSerializableExtra("obj");
                        TableTool.insert(newobj, TableTool.STATE_INSERT);
                        myItemRecyclerViewAdapter.addItem(newobj);
                        findViewById(R.id.data_no).setVisibility(View.GONE);
                        break;
                }
                break;
        }
    }

}
