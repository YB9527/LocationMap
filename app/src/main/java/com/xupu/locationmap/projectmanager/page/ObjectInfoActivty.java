package com.xupu.locationmap.projectmanager.page;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.EditFiledCusom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.PositionField;
import com.xupu.locationmap.projectmanager.po.ProgressFiledCusom;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.XZQYService;
import com.xupu.locationmap.projectmanager.service.ZTService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ObjectInfoActivty extends FragmentActivity {

    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    MyJSONObject obj;
    List<MyJSONObject> fileds;
    int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        setContentView(R.layout.activity_object_info);

        state = getIntent().getIntExtra("state", -1);
        String id = getIntent().getStringExtra("id");
        if (state == TableTool.STATE_INSERT) {
            //新增一个对象
            fileds = TableTool.findByTableNameAndParentId(ZTService.TABLE_Structure,id);
            JSONObject jsonObject = new JSONObject();
            for (MyJSONObject filed : fileds) {
                String name = filed.getJsonobject().getString("fieldname").toLowerCase();
                jsonObject.put(name, null);
            }
            obj = new MyJSONObject(UUID.randomUUID().toString(), fileds.get(0).getJsonobject().getString("tablealiasname"), XZQYService.getCurrentCode(), jsonObject);
            obj.setState(state);
            obj.setTableid(id);
            Button btn_add=  findViewById(R.id.btn_update);
            btn_add.setText("增加");
            findViewById(R.id.btn_delete).setVisibility(View.GONE);
        } else {

            obj = TableTool.findById(id);
            //根据tableid 查询 表格字段
            fileds = TableTool.findByTableNameAndParentId(ZTService.TABLE_Structure, obj.getTableid());

        }
        init(obj, fileds);
        initPage();
    }

    private void initPage() {
        Integer rid = this.getTaskId();
        MyJSONObject jsonObject = obj;
        List<FiledCustom> filedCustoms = new ArrayList<>();
        //开始下载按钮
        filedCustoms.add(new BtuFiledCustom(R.id.btn_update, "修改") {
            public void OnClick(MyJSONObject project) {
                //1、拿到最新的对象
                MyJSONObject newobj = getCopyObj(project, fileds);
                //2、和老对象对比
                String newjson = newobj.toJson().getJson();//json的更新
                String oldjson = project.getJson();
                if (newjson.equals(oldjson)) {
                    //没有被修改

                    AndroidTool.showAnsyTost("没有被修改", 2);
                    return;
                }
                String message ="确定要修改吗？";
                if(state == TableTool.STATE_INSERT){
                    message="确定要增加吗？";
                }
                //3、对象传给父组件
                AndroidTool.confirm(ObjectInfoActivty.this, message, new MyCallback() {
                    @Override
                    public void call(ResultData resultData) {
                        if (resultData.getStatus() == 0) {
                            newobj.toJson();
                            //更新数据库
                            TableTool.updateById(newobj);
                            //4、退出
                            Intent intent = new Intent();
                            intent.putExtra("obj", newobj);
                            setResult(TableTool.STATE_UPDATE, intent);
                            ObjectInfoActivty.this.finish();
                        }
                    }
                });
            }
        });

        filedCustoms.add(new BtuFiledCustom(R.id.btn_back, "返回") {
            public void OnClick(MyJSONObject project) {
                finish();
            }
        });

        filedCustoms.add(new BtuFiledCustom(R.id.btn_delete, "删除") {
            public void OnClick(MyJSONObject project) {

                Intent intent = new Intent();
                intent.putExtra("obj", obj);
                setResult(TableTool.STATE_DELETE, intent);
                TableTool.delete(obj);
                finish();
            }
        }.setConfirm(true, "确认要删除吗？"));
        ItemDataCustom itemDataCustom = new ItemDataCustom(rid, jsonObject, filedCustoms);
        AndroidTool.setView(findViewById(R.id.page), itemDataCustom, false, 0);
    }

    @SuppressLint("NewApi")
    private MyJSONObject getCopyObj(MyJSONObject obj, List<MyJSONObject> fileds) {
        MyJSONObject newobj = obj.copy();
        boolean bl = newobj == obj;
        for (MyJSONObject filed : fileds) {
            String name = filed.getJsonobject().getString("fieldname").toLowerCase();
            String value = filed.getJsonobject().getString("my_value");
            newobj.getJsonobject().replace(name, value);
        }
        return newobj;
    }

    private void init(MyJSONObject obj, List<MyJSONObject> fileds) {
        //页面显示
        List<FiledCustom> fs = new ArrayList<>();

        for (MyJSONObject filed : fileds) {
            String name = filed.getJsonobject().getString("fieldname").toLowerCase();
            String value = obj.getJsonobject().getString(name);
            if (value == null) {
                value = "";
            }
            filed.getJsonobject().put("my_value", value);
        }
        fs.add(new FiledCustom(R.id.key, "fieldaliasname"));
        fs.add(new EditFiledCusom(R.id.value, "my_value", false));

        int fragmentItem = R.layout.fragment_key_value_item;
        TableDataCustom tableDataCustom = new TableDataCustom(fragmentItem, fs, fileds).setEdit(true);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom);
        RecyclerView recyclerView = findViewById(R.id.recy);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);


    }

}
