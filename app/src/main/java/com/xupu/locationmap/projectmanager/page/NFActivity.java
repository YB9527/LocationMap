package com.xupu.locationmap.projectmanager.page;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.MediaTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.EditFiledCusom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.Media;
import com.xupu.locationmap.projectmanager.po.MediaType;
import com.xupu.locationmap.projectmanager.po.NF;
import com.xupu.locationmap.projectmanager.po.RecyclerViewFiledCustom;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.po.XZDM;
import com.xupu.locationmap.projectmanager.service.ItemListService;
import com.xupu.locationmap.projectmanager.service.MediaService;
import com.xupu.locationmap.projectmanager.service.XZQYService;

import java.io.File;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NFActivity extends AppCompatActivity {
    private static String lookinfoTagName = "LookInfoFragment";
    private static String listTagName = "list";
    private static String itemTagName = "item";
    private static String Table_Name = "NF";
    Button btuAdd;
    AddItemFragment addItemFragment;
    ItemFragment itemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        setTitle("农户数据");
        setContentView(R.layout.activity_table_one);
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
                showFragment(itemTagName);
            }
        });

        List<NF> nfs = ItemListService.findAll(Table_Name + "_", NF.class);
        Map<Integer, FiledCustom> map = new HashMap<>();
        map.put(R.id.name, new FiledCustom("name"));
        map.put(R.id.bz, new FiledCustom("bz"));
        map.put(R.id.btu_delete, new BtuFiledCustom<JSONObject>("删除") {
            @Override
            public void OnClick(ResultData<JSONObject> resultData) {
                JSONObject jsonObject = resultData.getT();

              ItemListService.deleteItem(Table_Name+ "_"+jsonObject.get("id"));
                itemFragment.remove(resultData.getT());
            }
        });
        map.put(R.id.btu_lookinfo, new BtuFiledCustom<JSONObject>("查看") {
            @Override
            public void OnClick(ResultData<JSONObject> resultData) {
                JSONObject jsonObject = resultData.getT();
                //跳到详细信息页面
                toLookInfoFragment(jsonObject);
            }
        });

        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.fragment_nf_item, map, nfs);
        itemFragment = new ItemFragment(tableDataCustom);
        //主页面显示
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl, itemFragment, listTagName)   // 此处的R.id.fragment_container是要盛放fragment的父容器'
                .commit();
    }

    /**
     * 到详细信息页面
     *
     * @param jsonObject
     */
    private void toLookInfoFragment(JSONObject jsonObject) {

        LookInfoFragment lookInfoFragment;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(lookinfoTagName);
        if (fragment == null) {
            ItemDataCustom itemDataCustom = getLookInfoFragmentItemDataCustom();
            lookInfoFragment = new LookInfoFragment(itemDataCustom);
            getSupportFragmentManager().beginTransaction().add(R.id.fl, lookInfoFragment, lookinfoTagName).commit();

        } else {
            lookInfoFragment = (LookInfoFragment) fragment;
        }
        lookInfoFragment.setJSONbject(jsonObject);

        showFragment("LookInfoFragment");
    }

    /**
     * 详细信息页面中对象的封装
     *
     * @return
     */
    private ItemDataCustom getLookInfoFragmentItemDataCustom() {
        Map<Integer, FiledCustom> filedCustomMap = new HashMap<>();
        filedCustomMap.put(R.id.name, new EditFiledCusom("name", true));
        filedCustomMap.put(R.id.bz, new EditFiledCusom("bz", false));
        filedCustomMap.put(R.id.btu_submit, new BtuFiledCustom<JSONObject>("修改") {
            @Override
            public void OnClick(ResultData<JSONObject> resultData) {
                JSONObject jsonObject = resultData.getT();
                //更新listview
                itemFragment.update(jsonObject);
                NF nf = jsonObject.toJavaObject(NF.class);
                ItemListService.updateByMark(Table_Name + "_" + nf.getId(), nf);
                showFragment(listTagName);
            }
        }.setCheck(true).setReturn(true));
        filedCustomMap.put(R.id.btu_cancel, new BtuFiledCustom("取消") {
            @Override
            public void OnClick(ResultData resultData) {
                showFragment(listTagName);
            }
        });
        //RecyclerViewFiledCustom recyclerViewFiledCustom = new  RecyclerViewFiledCustom.Builder(R.id.fl_photos).addFiledCustom(R.id.img,new FiledCustom())


        //添加照片的按钮
        filedCustomMap.put(R.id.btu_add_sfz_photo, new BtuFiledCustom<JSONObject>("添加身份证照片") {
            @Override
            public void OnClick(ResultData<JSONObject> resultData) {
                //添加照片
                NF nf = resultData.getT().toJavaObject(NF.class);
                Media media = MediaService.getMedia(nf, MediaType.Photo, "身份证");

                MediaTool.to(NFActivity.this, 101, resultData.getT(), media);

                //输入照片名字
                //拍照
                //保存到本地
                //更新到数据库
            }
        });
        filedCustomMap.put(R.id.btu_add_hkb_photo, new BtuFiledCustom("添加户口本照片") {
            @Override
            public void OnClick(ResultData resultData) {
                showFragment(listTagName);
            }
        });
        filedCustomMap.put(R.id.btu_add_fw_photo, new BtuFiledCustom("添加房屋照片") {
            @Override
            public void OnClick(ResultData resultData) {
                showFragment(listTagName);
            }
        });

        ItemDataCustom itemDataCustom = new ItemDataCustom(R.layout.fragment_nf_item_info, (JSONObject) JSONObject.toJSON(new NF()), filedCustomMap);
        return itemDataCustom;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        switch (requestCode) {
            case 101:
                if (resultCode == RESULT_OK) {
                    // 将拍摄的照片显示出来
                    JSONObject json = (JSONObject)this.getIntent().getSerializableExtra("json");
                    Media media = (Media)this.getIntent().getSerializableExtra("media");
                    json.getJSONArray("medias").add(media);
                    ItemListService.updateByMark(Table_Name + "_" + json.get("id"), json);
                    itemFragment.update(json);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化 添加item 面
     */
    private void initAddItemFragment() {

        Map<Integer, FiledCustom> filedCustomMap = new HashMap<>();
        filedCustomMap.put(R.id.name, new EditFiledCusom("name", true));
        filedCustomMap.put(R.id.bz, new EditFiledCusom("bz", false));

        filedCustomMap.put(R.id.btu_submit, new BtuFiledCustom<JSONObject>("添加") {
            @Override
            public void OnClick(ResultData<JSONObject> resultData) {
                JSONObject jsonObject = resultData.getT();
                NF nf = jsonObject.toJavaObject(NF.class);
                ItemListService.addItem(Table_Name, jsonObject);
                itemFragment.addItem(jsonObject);
                showFragment(listTagName);
                //init();
            }
        }.setCheck(true));
        filedCustomMap.put(R.id.btu_cancel, new BtuFiledCustom("取消") {
            @Override
            public void OnClick(ResultData resultData) {
                showFragment(listTagName);
            }
        });
        ItemDataCustom itemDataCustom = new ItemDataCustom(R.layout.fragment_nf_item_add, (JSONObject) JSONObject.toJSON(new NF()), filedCustomMap);
        addItemFragment = new AddItemFragment(itemDataCustom);
        getSupportFragmentManager().beginTransaction().add(R.id.fl, addItemFragment, itemTagName).hide(addItemFragment).commit();

    }

    /**
     * 显示fragment
     *
     * @param tagName
     */
    private void showFragment(String tagName) {
        AndroidTool.showFragment(this, tagName);
        if (tagName.equals(listTagName)) {
            showMain(true);
        } else {
            showMain(false);
        }

    }

    /**
     * 是否显示主页面
     *
     * @param isShow
     */
    public void showMain(boolean isShow) {
        if (isShow) {
            btuAdd.setVisibility(View.VISIBLE);
        } else {
            btuAdd.setVisibility(View.GONE);
        }
    }
}
