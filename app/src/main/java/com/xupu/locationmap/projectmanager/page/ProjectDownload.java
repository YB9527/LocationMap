package com.xupu.locationmap.projectmanager.page;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.PositionField;
import com.xupu.locationmap.projectmanager.po.ProgressFiledCusom;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.ZTService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 单个项目下载
 */
public class ProjectDownload extends AppCompatActivity {

    MyJSONObject project;
    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    /**
     * 下载 的任务条目数
     */
    List<MyJSONObject> tasks = new ArrayList<>();

    /**
     * 表列表
     */
    JSONArray tableitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_download);

        //1、拿到要下载的页面
        project = (MyJSONObject) getIntent().getSerializableExtra("project");
        setTitle("一旦下载，请勿断开");
        downProject(project);
        init();
    }

    private void init() {

        Integer rid = this.getTaskId();
        MyJSONObject jsonObject = project;
        List<FiledCustom> filedCustoms = new ArrayList<>();
        //开始下载按钮
        filedCustoms.add(new BtuFiledCustom(R.id.btn_down, "下载") {
            //保存项目记录
            //RedisTool.saveRedis(ZTService.PROJECT_TABLE_NAME, project);
            //TableTool.createDB(ZTService.PROJECT_TABLE_NAME);

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void OnClick(MyJSONObject project) {
                //设置为当前项目
                ProjectService.setCurrentSugProject(project);
                RedisTool.saveRedis(ZTService.PROJECT_TABLE_NAME, project);
                TableTool.insert(project,0);

                TableTool.createDB(ProjectService.getName(project));
                for (int i = 0; i < tasks.size(); i++) {
                    setProgress(tasks.get(i).getJsonobject(), 1);
                    switch (i) {
                        case 0:
                            //开始下载表列表
                            downTableList(tableitems, i);
                            break;
                        case 1:
                            //开始下载任务列表
                            downLoadTask(project, i);
                            break;
                        case 2:
                            //开始下载行政区
                            downXZQList(project, i);
                            break;
                        case 3:
                            //开始下载表结构
                            downFiledTable(project, i);
                            break;
                        default:
                            //开始下载各种业务表格
                            downLoadTable(tasks.get(i).getJsonobject());
                            break;
                    }
                }
            }
        }.setConfirm(true, "一旦下载，请勿断开"));
        ItemDataCustom itemDataCustom = new ItemDataCustom(rid, jsonObject, filedCustoms);
        AndroidTool.setView(findViewById(R.id.page), itemDataCustom, false, 0);
    }


    private void downProject(MyJSONObject project) {
        //2、创建下载表格的碎片
        //页面显示
        List<FiledCustom> fs = new ArrayList<>();
        fs.add(new PositionField(R.id.index, "0"));
        fs.add(new FiledCustom(R.id.tablename, "my_tablename"));
        fs.add(new FiledCustom(R.id.state, "my_state"));
        fs.add(new ProgressFiledCusom(R.id.pb, "my_value"));

        int fragmentItem = R.layout.fragment_down_table_item;

        MyJSONObject listable = newDownLoadPo("项目表列表", new JSONObject());
        MyJSONObject tasktable = newDownLoadPo("任务表", new JSONObject());
        MyJSONObject xzqtable = newDownLoadPo("行政区列表", new JSONObject());
        MyJSONObject fieldtable = newDownLoadPo("表结构", new JSONObject());
        tasks.add(listable);
        tasks.add(tasktable);
        tasks.add(xzqtable);
        tasks.add(fieldtable);

        TableDataCustom tableDataCustom = new TableDataCustom(fragmentItem, fs, tasks);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom);
        RecyclerView recyclerView = findViewById(R.id.list);

        //得到要下载的表格
        getTableList(project, new Callback<JSONArray>() {
            @Override
            public void call(JSONArray tableitems) {
                //项目中表格条目，

                for (int i = 0; i < tableitems.size(); i++) {
                    JSONObject tableItem = tableitems.getJSONObject(i);
                    tasks.add(newDownLoadPo(tableItem.getString("aliasname"), tableItem));
                }
                recyclerView.setAdapter(myItemRecyclerViewAdapter);
                ProjectDownload.this.tableitems = tableitems;
                //显示下载按钮
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Button btn_down = findViewById(R.id.btn_down);
                        btn_down.setVisibility(View.VISIBLE);
                    }
                });


            }
        });
    }

    /**
     * 下载表列表
     *
     * @param tableitems
     */
    private void downTableList(JSONArray tableitems, int taskindex) {

        List<MyJSONObject> tableitemsMyJson = new ArrayList<>();
        setProgress(tasks.get(taskindex).getJsonobject(), 2);
        for (int i = 0; i < tableitems.size(); i++) {
            JSONObject tableItem = JSONObject.parseObject(JSONObject.toJSONString(tableitems.getJSONObject(i)));

            Set<String> keys = tableitems.getJSONObject(i).keySet();
            for (String key : keys) {
                if (key.startsWith("my_")) {
                    tableItem.remove(key);
                }
            }
            MyJSONObject table = new MyJSONObject(tableItem.getString("id"), ZTService.PROJECT_TABLE_LIST, project.getId(), tableItem);
            tableitemsMyJson.add(table);
        }
        //项目中表格保存
        TableTool.insertMany(tableitemsMyJson,0);
        setProgress(tasks.get(taskindex).getJsonobject(), 3);
    }

    /**
     * 下载项目任务列表
     *
     * @param newproject
     */
    private void downLoadTask(MyJSONObject newproject, int taskindex) {
        //下载表格任务列表
        ZTService.getTableId(ZTService.TASK_LIST, new Callback<String>() {
            @Override
            public void call(String tableid) {
                ZTService.getTableItemList(tableid, newproject.getParentid(), new Callback<JSONArray>() {
                    @Override
                    public void call(JSONArray tableTasks) {
                        setProgress(tasks.get(taskindex).getJsonobject(), 2);
                        //表格任务列表,任务的tableid 不是表格的id 是所属表格的id
                        List<MyJSONObject> tableitemsMyJson = new ArrayList<>();
                        //项目中表格条目，
                        for (int i = 0; i < tableTasks.size(); i++) {
                            JSONObject tableItem = tableTasks.getJSONObject(i);
                            String tasktableid = ZTService.getTableIdByItemId(tableItem.getString("tableid"));
                            tableitemsMyJson.add(new MyJSONObject(tableItem.getString("id"), ZTService.TASK_LIST, tasktableid, tableItem));
                        }
                        //项目中表格保存
                        TableTool.insertMany(tableitemsMyJson,0);
                        setProgress(tasks.get(taskindex).getJsonobject(), 3);
                    }
                });
            }
        });
    }

    /**
     * 下载行政区列表
     *
     * @param project
     */
    private void downXZQList(MyJSONObject project, int taskindex) {
        //下载行政区列表
        ZTService.getTableId(ZTService.XZQ_LIST, new Callback<String>() {
            @Override
            public void call(String tableid) {
                ZTService.getTableItemList(tableid, project.getParentid(), new Callback<JSONArray>() {
                    @Override
                    public void call(JSONArray tableTasks) {
                        setProgress(tasks.get(taskindex).getJsonobject(), 2);
                        //表格任务列表,任务的tableid 不是表格的id 是所属表格的id
                        List<MyJSONObject> tableitemsMyJson = new ArrayList<>();
                        //项目中表格条目，
                        for (int i = 0; i < tableTasks.size(); i++) {
                            JSONObject tableItem = tableTasks.getJSONObject(i);
                            tableitemsMyJson.add(new MyJSONObject(tableItem.getString("id"), ZTService.XZQ_LIST, project.getId(), tableItem));
                        }
                        //项目中表格保存
                        TableTool.insertMany(tableitemsMyJson,0);
                        setProgress(tasks.get(taskindex).getJsonobject(), 3);
                    }
                });
            }
        });
    }

    /**
     * 下载表结构
     *
     * @param project
     * @param taskindex
     */
    private void downFiledTable(MyJSONObject project, int taskindex) {
        ZTService.getTableId(ZTService.TABLE_Structure, new Callback<String>() {
            @Override
            public void call(String tableid) {
                ZTService.getTableItemList(tableid, project.getParentid(), new Callback<JSONArray>() {
                    @Override
                    public void call(JSONArray fileds) {
                        setProgress(tasks.get(taskindex).getJsonobject(), 2);
                        List<MyJSONObject> filedsMyJson = new ArrayList<>();
                        //项目中表格条目，
                        for (int i = 0; i < fileds.size(); i++) {
                            JSONObject filed = fileds.getJSONObject(i);
                            String structureid = ZTService.getTableIdByItemId(filed.getString("tableid"));
                            filedsMyJson.add(new MyJSONObject(i + "", ZTService.TABLE_Structure, structureid, filed));
                        }
                        //项目中表格保存
                        TableTool.insertMany(filedsMyJson,0);
                        setProgress(tasks.get(taskindex).getJsonobject(), 3);
                    }
                });
            }
        });
    }

    /**
     * 得到要下载的 业务表格
     *
     * @param project
     * @param callback
     */
    public void getTableList(MyJSONObject project, Callback<JSONArray> callback) {
        //保存项目表格
        ZTService.getTableId(ZTService.PROJECT_TABLE_LIST, new Callback<String>() {
            @Override
            public void call(String tableid) {
                //得到项目的所有列表
                ZTService.getTableItemList(tableid, project.getParentid(), new Callback<JSONArray>() {
                    @Override
                    public void call(JSONArray tableitems) {
                        callback.call(tableitems);
                    }
                });
            }
        });
    }

    /**
     * 执行下载 业务表格
     *
     * @param tableItem 表格条目
     */
    private void downLoadTable(JSONObject tableItem) {
        //表格信息
        //1、得到是哪张表格

        String tableid = ZTService.getTableIdByItemId(tableItem.getString("id"));

        //2、得到表格内容
        ZTService.getTableItemList(tableid, new Callback<JSONArray>() {
            @SuppressLint("NewApi")
            @Override
            public void call(JSONArray objects) {
                //3、转换成本地使用数据
                MyJSONObject task = null;
                for (int i = 0; i < tasks.size(); i++) {
                    task = tasks.get(i);
                    if (task.getJsonobject() == tableItem) {
                        setProgress(tableItem, 2);
                        break;
                    }
                }
                saveInDatabase(tableid, tableItem.getString("aliasname"), objects);
                if (task != null) {
                    setProgress(tableItem, 3);
                }
            }
        });
    }

    /**
     * 完成任务个数
     */
    int finishCount = 0;

    /**
     * 设置进度
     *
     * @param task  任务
     * @param level 执行的阶段
     */
    @SuppressLint("NewApi")
    private void setProgress(JSONObject task, int level) {
        task.replace("my_state", state[level]);
        double value = (level + 1 + 0.0) / state.length;
        task.replace("my_value", (int) (value * 100));
        for (int i = 0; i < myItemRecyclerViewAdapter.getmValues().size(); i++) {
            MyJSONObject myJSONObject = myItemRecyclerViewAdapter.getmValues().get(i);
            if (myJSONObject.getJsonobject() == task) {
                int finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myItemRecyclerViewAdapter.notifyItemChanged(finalI);
                    }
                });

                break;
            }
        }
        if (level + 1 == state.length) {
            finishCount++;
        }
        if (finishCount == tasks.size()) {
            finshAllTask();
        }
    }

    /**
     * 完成所有任务执行方法
     */
    private void finshAllTask() {
        Intent intent = new Intent(this, XZQYPage.class);
        startActivity(intent);
        AndroidTool.showAnsyTost("项目下载完成，请选择工作区域", 0);
        this.finish();
    }


    private String[] state = new String[]{
            "未开始", "请求数据", "保存到本地", "完成"
    };

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
        TableTool.insertMany(myJSONObjects,0);
        Log.v("yb", tablename + " 完成");
    }

    public MyJSONObject newDownLoadPo(String tablename, JSONObject tableitem) {

        tableitem.put("my_tablename", tablename);
        tableitem.put("my_state", state[0]);
        tableitem.put("my_value", 0);
        MyJSONObject myJSONObject = new MyJSONObject("", "", "", tableitem);
        return myJSONObject;
    }
}
