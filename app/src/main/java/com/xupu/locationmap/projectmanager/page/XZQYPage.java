package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.po.ViewHolderCallback;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.EditFiledCusom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.PositionField;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.po.XZDM;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.XZQYService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XZQYPage extends AppCompatActivity {

    Button btuAdd;
    AddItemFragment addItemFragment;
    ItemFragment itemFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activty_xzqy);
        init();
        //initAddItemFragment();
        initTitle();
    }
    private void initTitle() {
        AndroidTool.addTitleFragment(this, "行政区域", R.mipmap.topnav_icon_new, "新增", new Callback() {
            @Override
            public void call(Object o) {
                //标题 右边新增按钮事件
                addXZQY();
            }
        });
    }

    List<MyJSONObject> xzqys =null;
    MyJSONObject currentXZQY;
    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private void init() {
        xzqys = XZQYService.findByProject();

        if (!Tool.isEmpty(xzqys)) {
            findViewById(R.id.recy).setVisibility(View.VISIBLE);
            findViewById(R.id.data_no).setVisibility(View.GONE);

            //当前项目 放到第一个
            currentXZQY = XZQYService.getCurrentXZDM();
            if (currentXZQY != null) {
                String currentProjectId = currentXZQY.getId();
                currentXZQY = null;
                for (int i = 0; i < xzqys.size(); i++) {
                    if (currentProjectId.equals(xzqys.get(i).getId())) {
                        currentXZQY = xzqys.remove(i);
                        xzqys.add(0, currentXZQY);
                        break;
                    }
                }
            }
        }
        List<FiledCustom> fs = new ArrayList<>();
        //名称
        fs.add(new FiledCustom(R.id.tv_projectname, "caption"));
        //描述
        fs.add(new FiledCustom(R.id.tv_descrip, "code"));
        //区域选择
        fs.add(new BtuFiledCustom(R.id.btu_select, "选择") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                XZQYService.setCurrentXZDM(myJSONObject);
                AndroidTool.showAnsyTost("当前区域是：" + ProjectService.getName(myJSONObject), 0);
            }
        }.setConfirm(true, "确定要选择这个区域吗？"));
        //新增区域按钮
        findViewById(R.id.btn_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addXZQY();

            }
        });
       /* fs.add(new BtuFiledCustom(R.id.btu_info, "详情") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                Intent intent = new Intent(ProjectPage.this, ObjectInfoActivty.class);
                intent.putExtra("id", myJSONObject.getId());
                startActivityForResult(intent, 1);

            }
        });*/
        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.fragment_project_item, fs, xzqys);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom);
        RecyclerView recyclerView = findViewById(R.id.recy);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        myItemRecyclerViewAdapter.setLoadViewCallback(new ViewHolderCallback() {
            @Override
            public void call(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {

                TextView tv_current = holder.mView.findViewById(R.id.tv_current);
                tv_current.setText("当前区域");
                ImageView imageView = holder.mView.findViewById(R.id.iv_currentIcon);
                if (position == 0 && currentXZQY != null)  {
                    imageView.setImageResource(R.drawable.area_icon_blue);
                    holder.mView.findViewById(R.id.fl_currentproject).setVisibility(View.VISIBLE);
                }else{
                    imageView.setImageResource(R.drawable.area_icon_gray);
                    holder.mView.findViewById(R.id.fl_currentproject).setVisibility(View.GONE);
                }
            }
        });

    }

    /**
     * 添加行政区域
     */
    private void addXZQY() {
        Intent intent = new Intent(XZQYPage.this, ObjectInfoActivty.class);
        intent.putExtra("state", TableTool.STATE_INSERT);
        intent.putExtra("id", xzqys.get(0).getTableid());
        startActivityForResult(intent, 2);
    }

    private void init2() {
        btuAdd = findViewById(R.id.btu_add);
        btuAdd.setVisibility(View.VISIBLE);
        btuAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                XZQYPage.this.showMain(false);
            }
        });


        List<MyJSONObject> xzdms =XZQYService.findByProject();
        List<FiledCustom> fs = new ArrayList<>();
        FiledCustom filedCustom ;
        filedCustom = new FiledCustom(R.id.code,"code");
        fs.add(filedCustom);
        filedCustom = new FiledCustom(R.id.caption,"caption");
        fs.add(filedCustom);
        filedCustom = new BtuFiledCustom(R.id.btu_delete, "删除") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                TableTool.delete(myJSONObject);
                itemFragment.remove(myJSONObject);
            }
        }.setConfirm(true,"确认要删除行政区域吗？");
        fs.add(filedCustom);
        filedCustom = new BtuFiledCustom(R.id.btu_select, "选择") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                AndroidTool.confirm(XZQYPage.this, "确定要选择这个区域吗？", new MyCallback() {
                    @Override
                    public void call(ResultData resultData) {
                        if (resultData.getStatus() == 0) {
                            XZQYService.setCurrentXZDM(myJSONObject);
                            AndroidTool.showAnsyTost("当前区域是：" + XZQYService.getCaption(myJSONObject), 0);
                        }
                    }
                });
            }
        };
        fs.add(filedCustom);


        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.fragment_xzqy_item, fs, xzdms);
        itemFragment = new ItemFragment(tableDataCustom);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl, itemFragment, "list")   // 此处的R.id.fragment_container是要盛放fragment的父容器'
                .commit();
    }

    /**
     * 初始化 新增item窗口
     */
    private void initAddItemFragment() {
        List<MyJSONObject> xzdms =XZQYService.findByProject();
        List<FiledCustom> fs = new ArrayList<>();
        FiledCustom filedCustom ;
        filedCustom = new EditFiledCusom(R.id.code,"code", true);
        fs.add(filedCustom);
        filedCustom = new EditFiledCusom(R.id.caption,"caption", true);
        fs.add(filedCustom);
        filedCustom = new BtuFiledCustom(R.id.btu_submit, "添加") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                itemFragment.addItem(myJSONObject);
                TableTool.insert(myJSONObject,TableTool.STATE_INSERT);
                showMain(true);
                //init();
            }
        }.setCheck(true).setReturn(true);
        fs.add(filedCustom);


        filedCustom = new BtuFiledCustom(R.id.btu_cancel, "取消") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                showMain(true);
            }
        };
        fs.add(filedCustom);
        ItemDataCustom itemDataCustom = new ItemDataCustom(R.layout.fragment_add_item, XZQYService.newXZDM(), fs);
        addItemFragment = new AddItemFragment(itemDataCustom);
        getSupportFragmentManager().beginTransaction().add(R.id.fl, addItemFragment, "item").hide(addItemFragment).commit();
    }

    public void showMain(boolean ishow) {
        if (ishow) {
            getSupportFragmentManager().beginTransaction().hide(addItemFragment).show(itemFragment).commit();
            btuAdd.setVisibility(View.VISIBLE);
        } else {
            getSupportFragmentManager().beginTransaction().show(addItemFragment).hide(itemFragment).commit();
            btuAdd.setVisibility(View.GONE);
        }
    }
}
