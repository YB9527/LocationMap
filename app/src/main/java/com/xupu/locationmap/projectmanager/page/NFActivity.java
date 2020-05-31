package com.xupu.locationmap.projectmanager.page;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.po.SFZBack;
import com.xupu.locationmap.common.po.SFZFront;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.FileTool;
import com.xupu.locationmap.common.tools.MediaTool;
import com.xupu.locationmap.common.tools.SFZPhotoTool;
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
import com.xupu.locationmap.projectmanager.service.SFZService;
import com.xupu.locationmap.projectmanager.service.XZQYService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


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

        List<FiledCustom> fs = new ArrayList<>();
        fs.add(new FiledCustom(R.id.name, "name"));
        fs.add(new FiledCustom(R.id.bz, "bz"));
        fs.add(new BtuFiledCustom(R.id.btu_delete, "删除") {
            @Override
            public void OnClick(MyJSONObject nf) {
                if (TableTool.delete(nf)) {
                    itemFragment.remove(nf);
                }
            }
        }.setConfirm(true, "确定要删除农户吗？"));

        fs.add(new BtuFiledCustom(R.id.btu_lookinfo, "查看") {
            @Override
            public void OnClick(MyJSONObject nf) {
                //跳到详细信息页面
                toLookInfoFragment(nf);
            }
        });


        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.fragment_nf_item, fs, nfs);
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
        List<FiledCustom> fs = new ArrayList<>();
        fs.add(new EditFiledCusom(R.id.name, "name", true));
        fs.add(new EditFiledCusom(R.id.bz, "bz", false));
        fs.add(new BtuFiledCustom(R.id.btu_submit, "修改") {
            @Override
            public void OnClick(MyJSONObject nf) {
                //更新listview
                //找到以前，看名字是否被更改，如果被更改，文件名字也要被改
                MyJSONObject oldnf = TableTool.findById(nf.getId());
                if (!NFService.getName(oldnf).equals(NFService.getName(nf))) {
                    String olddir = MediaService.getMediaDir(oldnf);
                    String newdir = MediaService.getMediaDir(nf);
                    //修改media对象
                    boolean isupdate = FileTool.reName(olddir, newdir);
                    if (isupdate) {
                        for (MyJSONObject media : lookInfoFragment.medias) {
                            MediaService.setPath(media, MediaService.getPath(media).replace(olddir, newdir));
                            media.toJson();
                        }
                    }
                }
                itemFragment.update(nf);
                TableTool.updateById(nf);
                TableTool.updateMany(lookInfoFragment.medias);

                TableTool.updateMany(lookInfoFragment.sfzFronts);
                TableTool.updateMany(lookInfoFragment.sfzBacks);
                //修改多媒体文件
                //找到多媒体文件
                showFragment(listTagName);
            }
        }.setCheck(true).setReturn(true).setConfirm(true, "确认要修改吗？"));
        fs.add(new BtuFiledCustom(R.id.btu_cancel, "取消") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                showFragment(listTagName);
            }
        });


        //RecyclerViewFiledCustom recyclerViewFiledCustom = new  RecyclerViewFiledCustom.Builder(R.id.fl_photos).addFiledCustom(R.id.img,new FiledCustom())
        //添加照片的按钮
        fs.add(new BtuFiledCustom(R.id.btu_add_sfz_photo_front, "添加身份证正面照片") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                //添加照片
                MyJSONObject media = MediaService.getMedia(myJSONObject, 0, Customizing.SFZ_Front);
                SFZPhotoTool.getSFZPhotoTool(NFActivity.this).front(media);
            }
        });
        fs.add(new BtuFiledCustom(R.id.btu_add_sfz_photo_back, "添加身份证反面照片") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                //添加照片
                MyJSONObject media = MediaService.getMedia(myJSONObject, 0, Customizing.SFZ_back);
                SFZPhotoTool.getSFZPhotoTool(NFActivity.this).back(media);
            }
        });
        fs.add(new BtuFiledCustom(R.id.btu_add_hkb_photo, "添加户口本照片") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                MyJSONObject media = MediaService.getMedia(myJSONObject, 0, "户口本");
                MediaTool.to(NFActivity.this, 101, media);
            }
        });
        fs.add(new BtuFiledCustom(R.id.btu_add_fw_photo, "添加房屋照片") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                MyJSONObject media = MediaService.getMedia(myJSONObject, 0, "房屋");
                MediaTool.to(NFActivity.this, 101, media);
            }
        });


        ItemDataCustom itemDataCustom = new ItemDataCustom(R.layout.fragment_nf_item_info, NFService.newNF(), fs);

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
            case SFZPhotoTool.REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    // 将拍摄的照片显示出来
                    if (data != null) {
                        String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                        final MyJSONObject media = (MyJSONObject) this.getIntent().getSerializableExtra("media");
                        //保存多媒体
                        TableTool.insert(media);
                        //保存身份证

                        //检查是否联网
                        if (SFZPhotoTool.INTERNET) {
                            if (!TextUtils.isEmpty(contentType)) {
                                if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                                    //身份证正面
                                    //1、检查是否通过了
                                    //2、保存多媒体照片
                                    SFZPhotoTool.getSFZPhotoTool(NFActivity.this).recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, MediaService.getPath(media), new MyCallback<SFZFront>() {
                                        @Override
                                        public void call(ResultData<SFZFront> resultData) {
                                            SFZFront sfz = resultData.getT();
                                            //保存身份证
                                            TableTool.insert(SFZService.frontToMyJSONObject(media, sfz));
                                            lookInfoFragment.setJSONbject(TableTool.findById(media.getParentid()));
                                        }
                                    });
                                } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                                    //身份证背面
                                    SFZPhotoTool.getSFZPhotoTool(NFActivity.this).recIDCard(IDCardParams.ID_CARD_SIDE_BACK, MediaService.getPath(media), new MyCallback<SFZBack>() {
                                        @Override
                                        public void call(ResultData<SFZBack> resultData) {
                                            SFZBack sfz = resultData.getT();
                                            TableTool.insert(SFZService.backToMyJSONObject(media, sfz));
                                            lookInfoFragment.setJSONbject(TableTool.findById(media.getParentid()));
                                        }
                                    });
                                }
                            }
                        } else {
                            lookInfoFragment.setJSONbject(TableTool.findById(media.getParentid()));
                        }

                    }
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

        List<FiledCustom> fs = new ArrayList<>();
        fs.add(new EditFiledCusom(R.id.name, "name", true));
        fs.add(new EditFiledCusom(R.id.bz, "bz", false));
        fs.add(new BtuFiledCustom(R.id.btu_submit, "添加") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                TableTool.insert(myJSONObject);
                itemFragment.addItem(myJSONObject);
                showFragment(listTagName);
                //init();
            }
        }.setCheck(true).setReturn(true));

        fs.add(new BtuFiledCustom(R.id.btu_cancel, "取消") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                showFragment(listTagName);
            }
        });

        ItemDataCustom itemDataCustom = new ItemDataCustom(R.layout.fragment_nf_item_add, NFService.newNF(), fs);
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
