package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.FileTool;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.service.MediaService;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.ItemDataCustom;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ToolActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tool);
        initView();
        initTitle();
    }

    private void initTitle() {
        AndroidTool.addTitleFragment(this, "工具箱");
    }
    /**
     * 初始化页面
     */
    private void initView() {
        List<FieldCustom> fieldCustom = new ArrayList<>();
        fieldCustom.add(new ViewFieldCustom(R.id.btn_copy_database) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                boolean success = copyDBToSDcrad(ProjectService.getCurrentProjectDBName());
                if (success) {
                    AndroidTool.showAnsyTost("复制数据成功:" + ProjectService.getName(ProjectService.getCurrentSugProject()), 0);
                } else {
                    AndroidTool.showAnsyTost("复制数据 失败:", 1);
                }
            }
        });
        fieldCustom.add(new ViewFieldCustom(R.id.btn_upload_database) {

            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                ItemDataCustom itemDataCustom = new ItemDataCustom(null, new MyJSONObject(), fieldCustom);
                AndroidTool.setView(findViewById(R.id.page), itemDataCustom, true, 0);
                Intent intent = new Intent(ToolActivty.this, UploadDatabaseActivty.class);
                startActivity(intent);
                ToolActivty.this.finish();
            }
        });
        MyJSONObject myJSONObject = new MyJSONObject();
        myJSONObject.setJsonobject(new JSONObject());
        ItemDataCustom itemDataCustom = new ItemDataCustom(null, myJSONObject, fieldCustom);
        AndroidTool.setView(findViewById(R.id.page), itemDataCustom, true, 0);
    }

    /**
     * 得到sdk db数据库路径
     *
     * @return
     */
    public static String getDBSDcardRoot() {
        //return AndroidTool.getRootDir() +"拷贝数据库" + File.separator;
        return AndroidTool.getRootDir();
    }

    @SuppressLint("NewApi")
    public static boolean copyDBToSDcrad(String databaseName) {


        String oldPath = AndroidTool.getDatabaseDir() + databaseName;
        //去除uuid
        String replaceStr = databaseName.substring(databaseName.lastIndexOf("_"));

        String newPath = getDBSDcardRoot() + databaseName;
        newPath = newPath.replace(replaceStr, "");

        boolean bl = FileTool.copyFile(oldPath + ".db", newPath + ".db");
        boolean bl2 = FileTool.copyFile(oldPath + ".db-journal", newPath + ".db-journal");

        return bl && bl2;
    }
}
