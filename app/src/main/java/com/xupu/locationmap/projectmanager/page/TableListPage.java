package com.xupu.locationmap.projectmanager.page;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.tools.AndroidTool;

import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.TableItem;
import com.xupu.locationmap.projectmanager.po.TableType;
import com.xupu.locationmap.projectmanager.service.TableService;
import com.xupu.locationmap.projectmanager.view.TableDataCustom;
import com.xupu.locationmap.projectmanager.view.TableDataCustom_TableName;
import com.xupu.locationmap.projectmanager.view.TableViewCustom;
import com.xupu.locationmap.projectmanager.service.ZTService;

import java.util.ArrayList;
import java.util.List;

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
        tables = ZTService.getTableItemAll();
        List<TableDataCustom_TableName> xmlTableAll = TableService.getTableAll();
        List<MyJSONObject> orderTables = new ArrayList<>();
        for (TableDataCustom_TableName tableDataCustom_tableName : xmlTableAll) {
            for (int i = 0; i < tables.size(); i++) {
                MyJSONObject table = tables.get(i);
                if (tableDataCustom_tableName.getTableName().equals(table.getJsonobject().getString(TableItem.aliasname))) {
                    orderTables.add(table);
                    tables.remove(i);
                    break;
                }
            }
        }
        tables.addAll(0, orderTables);

        for (
                MyJSONObject table : tables) {
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
        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TableListPage.this.position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
