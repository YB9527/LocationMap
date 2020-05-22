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
import com.xupu.locationmap.projectmanager.page.dummy.DummyContent;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.EditFiledCusom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.po.XZDM;
import com.xupu.locationmap.projectmanager.service.XZQYService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NFActivity extends AppCompatActivity {

    Button btuAdd;
    AddItemFragment addItemFragment;
    ItemFragment itemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        setTitle("农户数据");
        setContentView(R.layout.activity_nf);
        init();
        initAddItemFragment();
        //itemFragment2 = new ItemFragment2();
        //getSupportFragmentManager().beginTransaction().replace(R.id.page,itemFragment2).commit();
        setMyTitle();

    }

    private void setMyTitle() {
        String title;
        XZDM currentXZDM = XZQYService.getCurrentXZDM();
        if (currentXZDM != null) {
            title = "当前任务：" + currentXZDM.getCaption();
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
                showMain(false);
            }
        });


        List<XZDM> xzdms = XZQYService.findAll();
        Map<Integer, FiledCustom> map = new HashMap<>();
        map.put(R.id.code, new FiledCustom("code"));
        map.put(R.id.caption, new FiledCustom("caption"));
        map.put(R.id.btu_delete, new BtuFiledCustom<JSONObject>("删除") {
            @Override
            public void OnClick(ResultData<JSONObject> resultData) {
                JSONObject jsonObject = resultData.getT();
                XZDM xzdm = jsonObject.toJavaObject(XZDM.class);
                XZQYService.deleteItem(xzdm);
                itemFragment.remove(resultData.getT());
            }
        });
        map.put(R.id.btu_select, new BtuFiledCustom<JSONObject>("选择") {
            @Override
            public void OnClick(ResultData<JSONObject> resultData) {
                JSONObject jsonObject = resultData.getT();
                XZDM xzdm = jsonObject.toJavaObject(XZDM.class);
                AndroidTool.confirm(NFActivity.this, "确定要选择这个区域吗？", new MyCallback() {
                    @Override
                    public void call(ResultData resultData) {
                        if (resultData.getStatus() == 0) {
                            XZQYService.setCurrentXZDM(xzdm);
                            setMyTitle();
                            AndroidTool.showAnsyTost("当前区域是：" + xzdm.getCaption(), 0);
                        }
                    }
                });
            }
        });

        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.fragment_xzqy_item, map, xzdms);
        itemFragment = new ItemFragment(tableDataCustom);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl, itemFragment, "list")   // 此处的R.id.fragment_container是要盛放fragment的父容器'
                .commit();
    }

    /**
     * 初始化 新增item窗口
     */
    private void initAddItemFragment() {

        Map<Integer, FiledCustom> filedCustomMap = new HashMap<>();
        filedCustomMap.put(R.id.code, new EditFiledCusom("code", true));
        filedCustomMap.put(R.id.caption, new EditFiledCusom("caption", true));

        filedCustomMap.put(R.id.btu_submit, new BtuFiledCustom<JSONObject>("确定") {
            @Override
            public void OnClick(ResultData<JSONObject> resultData) {
                JSONObject jsonObject = resultData.getT();
                XZDM xzdm = jsonObject.toJavaObject(XZDM.class);
                XZQYService.addXZDM(xzdm);
                itemFragment.addItem(jsonObject);
                showMain(true);
                //init();
            }
        }.setCheck(true));
        filedCustomMap.put(R.id.btu_cancel, new BtuFiledCustom("取消") {
            @Override
            public void OnClick(ResultData resultData) {
                showMain(true);
            }
        });
        ItemDataCustom itemDataCustom = new ItemDataCustom((JSONObject) JSONObject.toJSON(new XZDM()), filedCustomMap);
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
