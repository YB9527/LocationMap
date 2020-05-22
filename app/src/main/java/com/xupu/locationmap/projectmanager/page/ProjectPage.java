package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.SugProject;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.service.ProjectService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        setMyTitle();
        setContentView(R.layout.activity_project);
        init();

    }

    private void setMyTitle() {
        String title ;
        SugProject currentSugProject = ProjectService.getCurrentSugProject();
        if(currentSugProject != null){
            title ="当前项目："+currentSugProject.getName();
        }else{
            title ="还没有设置当前项目";
        }
        setTitle(title);
    }

    private void init() {
        List<SugProject> projects = ProjectService.findAll();
        Map<Integer, FiledCustom> map = new HashMap<>();
        map.put(R.id.item_number, new FiledCustom("id"));
        map.put(R.id.content, new FiledCustom("name"));
        map.put(R.id.btu_select, getBtuSelect());

        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.fragment_project_item, map, projects);

        ItemFragment itemFragment = new ItemFragment(tableDataCustom);
        getSupportFragmentManager()    //
                .beginTransaction()
                .replace(R.id.fl, itemFragment)   // 此处的R.id.fragment_container是要盛放fragment的父容器
                .commit();
    }

    /**
     * 选择项目按钮
     *
     * @return
     */
    private FiledCustom getBtuSelect() {
       return new BtuFiledCustom() {
            @Override
            public void OnClick(ResultData resultData) {
                //确定选择此项目
                //保存到数据库
                JSONObject jsonObject =(JSONObject)resultData.getT();
                SugProject project =jsonObject.toJavaObject(SugProject.class);
                AndroidTool.confirm(ProjectPage.this,"确定要选择这个项目吗？", new MyCallback() {
                    @Override
                    public void call(ResultData resultData) {
                        if (resultData.getStatus() == 0) {
                            ProjectService.setCurrentSugProject(project);
                            setMyTitle();
                            AndroidTool.showAnsyTost("当前项目是："+project.getName(),0);
                        }
                    }
                });
            }
        };
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
