package com.xupu.locationmap.projectmanager.page;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xupu.locationmap.R;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.MediaTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.Customizing;
import com.xupu.locationmap.projectmanager.po.EditFiledCusom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.service.MediaService;
import com.xupu.locationmap.projectmanager.service.NFService;
import com.xupu.locationmap.projectmanager.service.XZQYService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NFActivity extends AppCompatActivity {
    private static String lookinfoTagName = "LookInfoFragment";
    private static String listTagName = "list";
    private static String additemTagName = "item";
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
        MyJSONObject currentXZDM = XZQYService.getCurrentXZDM();
        if (currentXZDM != null) {
            title = "当前任务：" + currentXZDM.getJsonobject().getString(Customizing.XZQY_caption);
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
                addItemFragment.setMyJSONObject(NFService.newNF());
                showFragment(additemTagName);
            }
        });

        List<MyJSONObject> nfs = NFService.findByXZDM();
        Map<Integer, FiledCustom> map = new HashMap<>();
        map.put(R.id.name, new FiledCustom("name"));
        map.put(R.id.bz, new FiledCustom("bz"));
        map.put(R.id.btu_delete, new BtuFiledCustom<MyJSONObject>("删除") {
            @Override
            public void OnClick(MyJSONObject nf) {
                if(TableTool.delete(nf)){
                    itemFragment.remove(nf);
                }

            }
        }.setConfirm(true,"确定要删除农户吗？"));
        map.put(R.id.btu_lookinfo, new BtuFiledCustom<MyJSONObject>("查看") {
            @Override
            public void OnClick(MyJSONObject nf) {
                //跳到详细信息页面
                toLookInfoFragment(nf);
            }
        });

        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.fragment_nf_item, map, nfs);
        itemFragment = new ItemFragment(tableDataCustom);
        //主页面显示
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl, itemFragment, listTagName)   // 此处的R.id.fragment_container是要盛放fragment的父容器'
                .commit();
    }

    LookInfoFragment lookInfoFragment;

    /**
     * 到详细信息页面
     *
     * @param myJSONObject
     */
    private void toLookInfoFragment(MyJSONObject myJSONObject) {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(lookinfoTagName);
        if (fragment == null) {
            ItemDataCustom itemDataCustom = getLookInfoFragmentItemDataCustom();

            lookInfoFragment = new LookInfoFragment(itemDataCustom);

            getSupportFragmentManager().beginTransaction().add(R.id.fl, lookInfoFragment, lookinfoTagName).commit();

        } else {
            lookInfoFragment = (LookInfoFragment) fragment;
        }

        lookInfoFragment.setJSONbject(myJSONObject);
        showFragment(lookinfoTagName);


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
        filedCustomMap.put(R.id.btu_submit, new BtuFiledCustom<MyJSONObject>("修改") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                //更新listview

                itemFragment.update(myJSONObject);
                TableTool.updateById(myJSONObject);
                TableTool.updateMany(lookInfoFragment.medias);
                //修改多媒体文件
                //找到多媒体文件
                showFragment(listTagName);


            }
        }.setCheck(true).setReturn(true).setConfirm(true,"确认要修改吗？"));
        filedCustomMap.put(R.id.btu_cancel, new BtuFiledCustom("取消") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {

                showFragment(listTagName);

            }
        });
        //RecyclerViewFiledCustom recyclerViewFiledCustom = new  RecyclerViewFiledCustom.Builder(R.id.fl_photos).addFiledCustom(R.id.img,new FiledCustom())


        //添加照片的按钮
        filedCustomMap.put(R.id.btu_add_sfz_photo, new BtuFiledCustom<MyJSONObject>("添加身份证照片") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                //添加照片
                MyJSONObject media = MediaService.getMedia(myJSONObject, 0, "身份证");
                MediaTool.to(NFActivity.this, 101, media);
            }
        });
        filedCustomMap.put(R.id.btu_add_hkb_photo, new BtuFiledCustom("添加户口本照片") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                MyJSONObject media = MediaService.getMedia(myJSONObject, 0, "户口本");
                MediaTool.to(NFActivity.this, 101, media);
            }
        });
        filedCustomMap.put(R.id.btu_add_fw_photo, new BtuFiledCustom("添加房屋照片") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                MyJSONObject media = MediaService.getMedia(myJSONObject, 0, "房屋");
                MediaTool.to(NFActivity.this, 101, media);
            }
        });

        ItemDataCustom itemDataCustom = new ItemDataCustom(R.layout.fragment_nf_item_info, NFService.newNF(), filedCustomMap);

        return itemDataCustom;
    }

    /**
     * 拍照
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        switch (requestCode) {
            case 101:
                if (resultCode == RESULT_OK) {
                    // 将拍摄的照片显示出来
                    MyJSONObject media = (MyJSONObject) this.getIntent().getSerializableExtra("media");
                    TableTool.insert(media);
                    lookInfoFragment.setJSONbject(TableTool.findById(media.getParentid()));
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

        filedCustomMap.put(R.id.btu_submit, new BtuFiledCustom<MyJSONObject>("添加") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {

                TableTool.insert(myJSONObject);
                itemFragment.addItem(myJSONObject);
                showFragment(listTagName);
                //init();
            }
        }.setCheck(true).setReturn(true));
        filedCustomMap.put(R.id.btu_cancel, new BtuFiledCustom("取消") {
            @Override
            public void OnClick(MyJSONObject resultData) {
                showFragment(listTagName);
            }
        });
        ItemDataCustom itemDataCustom = new ItemDataCustom(R.layout.fragment_nf_item_add, NFService.newNF(), filedCustomMap);
        addItemFragment = new AddItemFragment(itemDataCustom);
        getSupportFragmentManager().beginTransaction().add(R.id.fl, addItemFragment, additemTagName).hide(addItemFragment).commit();

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
