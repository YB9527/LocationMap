package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.po.SFZFront;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.FileTool;
import com.xupu.locationmap.common.tools.SFZPhotoTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.service.MediaService;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.SFZService;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.ItemDataCustom;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

    private BufferedWriter out = null;


    /**
     * 初始化页面
     */
    private void initView() {
        List<FieldCustom> fieldCustom = new ArrayList<>();
        fieldCustom.add(new ViewFieldCustom(R.id.btn_copy_database) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                if(ProjectService.haseCurrentProject()){
                    boolean success = copyDBToSDcrad(ProjectService.getCurrentProjectDBName());
                    if (success) {
                        AndroidTool.showAnsyTost("复制数据成功:" + ProjectService.getName(ProjectService.getCurrentSugProject()), 0);
                    } else {
                        AndroidTool.showAnsyTost("复制数据 失败:", 1);
                    }
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

        fieldCustom.add(new ViewFieldCustom(R.id.btn_ocr_sfz) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                ocr_sfz();
            }
        });
        MyJSONObject myJSONObject = new MyJSONObject();
        myJSONObject.setJsonobject(new JSONObject());
        ItemDataCustom itemDataCustom = new ItemDataCustom(null, myJSONObject, fieldCustom);
        AndroidTool.setView(findViewById(R.id.page), itemDataCustom, true, 0);
    }

    private void ocr_sfz() {
        String dir = AndroidTool.getRootDir() + "身份证";
        FileTool.exitsDir(dir, true);

        File[] files = new File(dir).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                if(MediaService.IMG_TYPE.contains(FileTool.getExtension(s))){
                    return true;
                }
                return false;
            }
        });

        if (files == null || files.length == 0) {
             AndroidTool.showAnsyTost("此文件夹下，还没有可识别的身份证::"+dir,1);
        }else {
            String path = dir + "/识别结果.txt";
            File text = new File(path);
            if (!text.exists()) {
                try {
                    text.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            if (out == null) {
                try {
                    out = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(path, true)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            int[] writeCount = new int[]{0};
            for (File file : files) {

                SFZPhotoTool.getSFZPhotoTool(ToolActivty.this).recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, file.getAbsolutePath(), new MyCallback<SFZFront>() {
                    @Override
                    public void call(ResultData<SFZFront> resultData) {
                        SFZFront sfz = resultData.getT();
                        String str = JSONObject.toJSONString(sfz);
                        try {
                            writeCount[0]++;
                            out.write(file.getAbsolutePath() + "::" + str + "\r\n");
                            out.flush();
                            AndroidTool.showAnsyTost("识别：" + writeCount[0] + "  张图片", 0);
                            if (writeCount[0] == files.length) {
                                out.close();
                                finish();
                                AndroidTool.showAnsyTost("识别完成,请在文件夹，旭普公司/身份证/识别结果  中查看", 0);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }

        }
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
