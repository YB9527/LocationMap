package com.xupu.locationmap.projectmanager.page;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.ReflectTool;
import com.xupu.locationmap.exceptionmanager.MapException;
import com.xupu.locationmap.projectmanager.view.BtuFieldCustom;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.view.TableDataCustom;
import com.xupu.locationmap.projectmanager.service.ZTService;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 选择项目下载页面
 */
public class SelectProjectDowload extends AppCompatActivity {

    private List<MyJSONObject> oldProjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("下载项目");
        setContentView(R.layout.activty_select_down_project);
        List tem = getIntent().getParcelableArrayListExtra("projects");
        oldProjects = tem;
        init();
    }

    private void init() {
        //拿到我的项目id
        ZTService.getTableId(ZTService.PROJECT_TABLE_NAME, new Callback<String>() {
            //项目列表id
            @Override
            public void call(String id) {
                ZTService.getTableItemList(id, new Callback<JSONArray>() {
                    @Override
                    public void call(JSONArray projects) {
                        showNewProjects(oldProjects, projects);
                        /*for (int i = 0; i < projects.size(); i++) {
                            //当个项目
                            JSONObject jsonObject = projects.getJSONObject(0);
                            //拿项目的表格
                            String appid = jsonObject.getString(ZTService.APP_ID);
                            ZTService.getTableId(ZTService.PROJECT_TABLE_LIST, new Callback<String>() {
                                @Override
                                public void call(String tableid) {
                                    ZTService.getTableItemList(tableid, appid, new Callback<JSONArray>() {
                                        @Override
                                        public void call(JSONArray objects) {

                                        }
                                    });
                                }
                            });
                        }*/
                    }
                });
                //下载任务
            }
        });


    }

    /**
     * 展示老项目中没有的项目
     *
     * @param oldProjects
     * @param projects
     */
    private void showNewProjects(List<MyJSONObject> oldProjects, JSONArray projects) {
        List<MyJSONObject> newProjects = new ArrayList<>();
        try {
            Map<String, MyJSONObject> oldProjectIdMap = ReflectTool.getIDMap("getId", oldProjects);
            for (int i = 0; i < projects.size(); i++) {
                JSONObject project = projects.getJSONObject(i);
                if (!oldProjectIdMap.keySet().contains(project.getString(ZTService.ID))) {
                    newProjects.add(new MyJSONObject(project.getString(ZTService.ID), ZTService.PROJECT_TABLE_NAME, project.getString(ZTService.APP_ID), project));
                }
            }
            List<FieldCustom> fs = new ArrayList<>();
            fs.add(new FieldCustom(R.id.name,"name"));
            //, "下载"
            fs.add(new ViewFieldCustom(R.id.item) {
                @Override
                public void OnClick(View view, MyJSONObject project) {
                    Intent intent = new Intent(SelectProjectDowload.this,ProjectDownload.class);
                    intent.putExtra("project",project);

                    SelectProjectDowload.this.startActivityForResult(intent,AndroidTool.ACTIVITY_FINISH);
                    //SelectProjectDowload.this.finish();
                }
            });

            TableDataCustom tableDataCustom = new TableDataCustom(R.layout.activty_project_dowload_item, fs, newProjects);
            ItemFragment itemFragment = new ItemFragment(tableDataCustom);
            //主页面显示
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl, itemFragment, "下载列表")   // 此处的R.id.fragment_container是要盛放fragment的父容器'
                    .commit();
        } catch (MapException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == AndroidTool.ACTIVITY_FINISH){
            setResult(AndroidTool.ACTIVITY_FINISH);
            finish();
        }
    }
}
