package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.TableTool;
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
        setTitle("请选择任务区域");
        setContentView(R.layout.activity_table_one);
        init();
        initAddItemFragment();
        //itemFragment2 = new ItemFragment2();
        //getSupportFragmentManager().beginTransaction().replace(R.id.page,itemFragment2).commit();
        setMyTitle();
    }

    private void setMyTitle() {
        String title;
        MyJSONObject currentXZDM = XZQYService.getCurrentXZDM();
        if (currentXZDM != null) {
            title = "当前任务：" + XZQYService.getCaption(currentXZDM);
        } else {
            title = "还没有设置当前区域";
        }
        setTitle(title);
    }

    private void init() {
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
                            setMyTitle();
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
