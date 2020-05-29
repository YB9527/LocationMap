package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.HttpRespon;
import com.xupu.locationmap.common.tools.JSONObjectRespon;
import com.xupu.locationmap.common.tools.OkHttpClientUtils;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.ReflectTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.common.tools.ZTRespon;
import com.xupu.locationmap.exceptionmanager.MapException;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.Redis;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.service.ZTService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 项目下载页面
 */
public class ProjectDowload extends AppCompatActivity {

    private List<MyJSONObject> oldProjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("下载项目");
        setContentView(R.layout.activity_project_dowload);
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
            Map<Integer, FiledCustom> map = new HashMap<>();
            map.put(R.id.name, new FiledCustom("name"));
            map.put(R.id.btu_dowload, new BtuFiledCustom<MyJSONObject>("下载") {
                @Override
                public void OnClick(MyJSONObject newproject) {
                    downLoadTask(newproject);
                }
            }.setConfirm(true, "确定要下载吗？"));
            TableDataCustom tableDataCustom = new TableDataCustom(R.layout.activty_project_dowload_item, map, newProjects);
            ItemFragment itemFragment = new ItemFragment(tableDataCustom);
            //主页面显示
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl, itemFragment, "下载列表")   // 此处的R.id.fragment_container是要盛放fragment的父容器'
                    .commit();

        } catch (MapException e) {
            e.printStackTrace();
        }

    }

    /**
     * 下载项目
     *
     * @param newproject
     */
    private void downLoadTask(MyJSONObject newproject) {

        //保存项目
        //TableTool.insert(newproject);
        RedisTool.saveRedis(ZTService.PROJECT_TABLE_NAME,newproject);
        TableTool.createDB(ZTService.PROJECT_TABLE_NAME);
        //下载行政区列表
        ZTService.getTableId(ZTService.XZQ_LIST, new Callback<String>() {
            @Override
            public void call(String tableid) {
                ZTService.getTableItemList(tableid, newproject.getParentid(), new Callback<JSONArray>() {
                    @Override
                    public void call(JSONArray tableTasks) {
                        //表格任务列表,任务的tableid 不是表格的id 是所属表格的id
                        List<MyJSONObject> tableitemsMyJson = new ArrayList<>();
                        //项目中表格条目，
                        for (int i = 0; i < tableTasks.size(); i++) {
                            JSONObject tableItem = tableTasks.getJSONObject(i);
                            tableitemsMyJson.add(new MyJSONObject(tableItem.getString("id"), ZTService.XZQ_LIST, newproject.getId(), tableItem));
                        }
                        //项目中表格保存
                        TableTool.insertMany(tableitemsMyJson);
                    }
                });
            }
        });

        //下载表格任务列表
        ZTService.getTableId(ZTService.TASK_LIST, new Callback<String>() {
            @Override
            public void call(String tableid) {
                ZTService.getTableItemList(tableid, newproject.getParentid(), new Callback<JSONArray>() {
                    @Override
                    public void call(JSONArray tableTasks) {
                        //表格任务列表,任务的tableid 不是表格的id 是所属表格的id
                        List<MyJSONObject> tableitemsMyJson = new ArrayList<>();
                        //项目中表格条目，
                        for (int i = 0; i < tableTasks.size(); i++) {
                            JSONObject tableItem = tableTasks.getJSONObject(i);
                            tableitemsMyJson.add(new MyJSONObject(tableItem.getString("id"), ZTService.TASK_LIST, tableItem.getString("tableid"), tableItem));
                        }
                        //项目中表格保存
                        TableTool.insertMany(tableitemsMyJson);
                    }
                });
            }
        });
        //保存项目表格
        ZTService.getTableId(ZTService.PROJECT_TABLE_LIST, new Callback<String>() {
            @Override
            public void call(String tableid) {
                //得到项目的所有列表
                ZTService.getTableItemList(tableid, newproject.getParentid(), new Callback<JSONArray>() {
                    @Override
                    public void call(JSONArray tableitems) {
                        List<MyJSONObject> tableitemsMyJson = new ArrayList<>();
                        //项目中表格条目，
                        for (int i = 0; i < tableitems.size(); i++) {
                            JSONObject tableItem = tableitems.getJSONObject(i);
                            downLoadTable(tableItem);
                            String tableid = ZTService.getTableIdByItemId(tableItem.getString("id"));
                            MyJSONObject table = new MyJSONObject(tableItem.getString("id"), ZTService.PROJECT_TABLE_LIST, newproject.getId(), tableItem);
                            //table.setTableid(tableid);
                            tableitemsMyJson.add(table);
                        }
                        //项目中表格保存
                        TableTool.insertMany(tableitemsMyJson);
                        //下载主体表
                        //下载地块表
                    }
                });
            }
        });

    }

    /**
     * 下载表格
     *
     * @param tableItem 表格条目
     */
    private void downLoadTable(JSONObject tableItem) {
        //表格信息
        //1、得到是哪张表格
        String tableid = ZTService.getTableIdByItemId(tableItem.getString("id"));
        //2、得到表格内容
        ZTService.getTableItemList(tableid, new Callback<JSONArray>() {
            @Override
            public void call(JSONArray objects) {
                //3、转换成本地使用数据
                saveInDatabase(tableid, tableItem.getString("aliasname"), objects);
            }
        });

        //4、保存到本地数据库
    }

    /**
     * 保存进数据库
     * id 是gid parentid shi gdomain
     *
     * @param tableid
     * @param tablename
     * @param objects
     */
    private void saveInDatabase(String tableid, String tablename, JSONArray objects) {
        List<MyJSONObject> myJSONObjects = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            JSONObject jsonObject = objects.getJSONObject(i);
            MyJSONObject myJSONObject = new MyJSONObject(jsonObject.getString("gid"), tablename, jsonObject.getString("gdomain"), jsonObject);
            myJSONObject.setTableid(tableid);
            myJSONObjects.add(myJSONObject);
        }
        TableTool.insertMany(myJSONObjects);
        Log.v("yb", tablename + " 完成");
    }
}
