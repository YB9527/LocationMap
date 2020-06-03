package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.PositionField;
import com.xupu.locationmap.projectmanager.po.ProgressFiledCusom;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.service.ZTService;

import java.util.ArrayList;
import java.util.List;

public class ObjectInfoActivty extends AppCompatActivity {

    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_info);
        MyJSONObject obj = (MyJSONObject) getIntent().getSerializableExtra("obj");
        //根据tableid 查询 表格字段
        List<MyJSONObject>  fileds = TableTool.findByTableNameAndParentId(ZTService.TABLE_Structure,obj.getTableid());
        init(obj,fileds);
    }
    private void init(MyJSONObject obj, List<MyJSONObject> fileds) {
        //页面显示
        List<FiledCustom> fs = new ArrayList<>();

        for(MyJSONObject filed : fileds){
            String name = filed.getJsonobject().getString("fieldname").toLowerCase();
            String value = obj.getJsonobject().getString(name);
            filed.getJsonobject().put("my_value",value);
        }
        fs.add(new FiledCustom(R.id.key, "fieldaliasname"));
        fs.add(new FiledCustom(R.id.value, "my_value"));

        int fragmentItem = R.layout.fragment_key_value_item;
        TableDataCustom tableDataCustom = new TableDataCustom(fragmentItem, fs, fileds);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom);
        RecyclerView recyclerView = findViewById(R.id.recy);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
    }

}
