package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xupu.locationmap.R;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.EditFiledCusom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.ZTService;

import java.util.ArrayList;
import java.util.List;

public class ProjectPage extends AppCompatActivity {

    Button btuAdd;
    AddItemFragment addItemFragment;
    ItemFragment itemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        setMyTitle();
        setContentView(R.layout.activity_project);
        init();
    }

    private void setMyTitle() {
        String title;
        MyJSONObject currentSugProject = ProjectService.getCurrentSugProject();
        if (currentSugProject != null) {
            title = "当前项目：" + ProjectService.getName(currentSugProject);
        } else {
            title = "还没有设置当前项目";
        }
        setTitle(title);
    }


    /**
     * 选择项目按钮
     *
     * @return
     */
    /*private FiledCustom getBtuSelect() {
        return new BtuFiledCustom() {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                //确定选择此项目
                //保存到数据库
                AndroidTool.confirm(ProjectPage.this, "确定要选择这个项目吗？", new MyCallback() {
                    @Override
                    public void call(ResultData resultData) {
                        if (resultData.getStatus() == 0) {
                            ProjectService.setCurrentSugProject(myJSONObject);
                            setMyTitle();
                            AndroidTool.showAnsyTost("当前项目是：" + ProjectService.getName(myJSONObject), 0);
                        }
                    }
                });
            }
        };
    }*/
    private void init() {
        btuAdd = findViewById(R.id.btu_add);
        //btuAdd.setVisibility(View.VISIBLE);
        btuAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProjectPage.this.showMain(false);
            }
        });

        ArrayList<MyJSONObject> projects = ProjectService.findAll();
        List<FiledCustom> fs = new ArrayList<>();
        fs.add(new FiledCustom(R.id.name, "name"));
        fs.add(new BtuFiledCustom(R.id.btu_select, "选择") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                ProjectService.setCurrentSugProject(myJSONObject);
                setMyTitle();
                AndroidTool.showAnsyTost("当前项目是：" + ProjectService.getName(myJSONObject), 0);
            }
        }.setConfirm(true, "确定要选择这个项目吗？"));

       /* fs.add(new BtuFiledCustom(R.id.btu_info, "详情") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                Intent intent = new Intent(ProjectPage.this, ObjectInfoActivty.class);
                intent.putExtra("id", myJSONObject.getId());
                startActivityForResult(intent, 1);

            }
        });*/
        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.fragment_project_item, fs, projects);

        itemFragment = new ItemFragment(tableDataCustom);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl, itemFragment, "list")   // 此处的R.id.fragment_container是要盛放fragment的父容器'
                .commit();
        initAddItemFragment();

        findViewById(R.id.btu_project_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectPage.this, SelectProjectDowload.class);
                intent.putExtra("projects", projects);
                startActivity(intent);
                ProjectPage.this.finish();
            }
        });
    }

    /**
     * 初始化 新增item窗口
     */
    private void initAddItemFragment() {

        List<FiledCustom> fs = new ArrayList<>();
        fs.add(new EditFiledCusom(R.id.name, "name", true));
        fs.add(new BtuFiledCustom(R.id.btu_submit, "添加") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                itemFragment.addItem(myJSONObject);
                RedisTool.saveRedis(ZTService.PROJECT_TABLE_NAME, myJSONObject);
                //TableTool.insert(myJSONObject);
                showMain(true);
            }
        }.setCheck(true).setReturn(true));
        fs.add(new BtuFiledCustom(R.id.btu_cancel, "取消") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                showMain(true);
            }
        });

        ItemDataCustom itemDataCustom = new ItemDataCustom(R.layout.fragment_project_item_add, ProjectService.newProject(), fs);
        addItemFragment = new AddItemFragment(itemDataCustom);
        getSupportFragmentManager().beginTransaction().add(R.id.fl, addItemFragment, "item").hide(addItemFragment).commit();

    }

    public void showMain(boolean ishow) {
        if (ishow) {
            getSupportFragmentManager().beginTransaction().hide(addItemFragment).show(itemFragment).commit();
            //btuAdd.setVisibility(View.VISIBLE);
        } else {
            getSupportFragmentManager().beginTransaction().show(addItemFragment).hide(itemFragment).commit();
            //btuAdd.setVisibility(View.GONE);
        }

    }


}
