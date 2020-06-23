package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.xupu.locationmap.R;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.FileTool;
import com.xupu.locationmap.common.tools.JSONTool;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.LowImage;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.ZTService;
import com.xupu.locationmap.projectmanager.view.CheckBoxFieldCustom;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.TableDataCustom;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 删除数据库页面
 */
public class UploadDatabaseActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_database);
        initView();
    }


    private void initView() {
        //1、查询准备要上传的数据库
        String dbDir = ToolActivty.getDBSDcardRoot();
        File[] paths = new File(dbDir).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                if (s.endsWith(".db")) {
                    return true;
                }
                return false;
            }
        });
        List<MyJSONObject> myJSONObjects = new ArrayList<>();
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i].getPath();
            LowImage lowImage = new LowImage(FileTool.getFileName(path), path, 100);
            myJSONObjects.add(JSONTool.toMyJSONObject(lowImage));
        }
        List<FieldCustom> fs = new ArrayList<>();
        //项目名称
        fs.add(new FieldCustom(R.id.tv_databasename, "name"));
        fs.add(new ViewFieldCustom(R.id.item) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                LowImage lowImage = myJSONObject.getJsonobject().toJavaObject(LowImage.class);
                uploadDB(lowImage);
            }
        }.setConfirm(true, "确认要上传吗？"));
        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.item_uploaddatabase, fs, myJSONObjects).setEdit(true);
        RecyclerView recyclerView = findViewById(R.id.recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyItemRecyclerViewAdapter myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom, recyclerView);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);

    }

    /**
     * 数据库的上传
     *
     * @param lowImage
     */
    @SuppressLint("NewApi")
    private void uploadDB(LowImage lowImage) {
        String name1 = lowImage.getName();
        String path1 = lowImage.getPath();
        String random = System.currentTimeMillis()+"";
        String name2 = name1.replace(".db", "_" + random + ".db-journal");
        String path2 = path1.replace(".db", "_" + random + ".db-journal");
        String descPath1 = (AndroidTool.getDatabaseDir() + name1).replace(".db", "_" + random + ".db");
        String descPath2 = AndroidTool.getDatabaseDir() + name2;

        FileTool.copyFile(path1, descPath1);
        if (new File(path2).exists()) {
            FileTool.copyFile(path2, descPath2);
        }


        TableTool.createDB(name1.replace(".db", "_" + random));
        List<MyJSONObject> myJSONObjects = TableTool.findByTableName(ZTService.PROJECT_TABLE_NAME);
        myJSONObjects.get(0).getJsonobject().replace(ProjectService.PROJECT_RANDOM_MARK, random);
        ProjectService.setName(myJSONObjects.get(0), name1.replace(".db", ""));
        ProjectService.saveProject(myJSONObjects.get(0));
        AndroidTool.showAnsyTost("下载完成", 0);
        this.finish();
    }
}
