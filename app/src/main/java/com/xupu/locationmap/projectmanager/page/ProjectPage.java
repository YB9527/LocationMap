package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.po.ViewHolderCallback;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.Tool;
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
    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        getSupportActionBar().hide();
        initTitle();
        setContentView(R.layout.activity_project);
        init();
    }

    private void initTitle() {
        AndroidTool.addTitleFragment(this, "项目管理", R.mipmap.topnav_icon_new, "下载", new Callback() {
            @Override
            public void call(Object o) {
                toDownLoadProject();
                //标题 右边下载按钮事件
            }
        });
    }

    /**
     * 跳到下载项目页面
     */
    private void toDownLoadProject() {
        Intent intent = new Intent(ProjectPage.this, SelectProjectDowload.class);
        intent.putExtra("projects", projects);
        startActivity(intent);
        ProjectPage.this.finish();
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
    ArrayList<MyJSONObject> projects;
    MyJSONObject currentProject;

    private void init() {
        projects = ProjectService.findAll();
        if (!Tool.isEmpty(projects)) {
            findViewById(R.id.recy).setVisibility(View.VISIBLE);
            findViewById(R.id.data_no).setVisibility(View.GONE);
            projects.add(projects.get(0));
            projects.add(projects.get(0));
            projects.add(projects.get(0));
            //当前项目 放到第一个
            currentProject = ProjectService.getCurrentSugProject();
            if (currentProject != null) {
                String currentProjectId = currentProject.getId();
                currentProject = null;
                for (int i = 0; i < projects.size(); i++) {
                    if (currentProjectId.equals(projects.get(i).getId())) {
                        currentProject = projects.remove(i);
                        projects.add(0, currentProject);
                        break;
                    }
                }
            }
        }
        List<FiledCustom> fs = new ArrayList<>();
        //项目名称
        fs.add(new FiledCustom(R.id.tv_projectname, "name"));
        //项目描述
        fs.add(new FiledCustom(R.id.tv_descrip, "name"));
        //项目选择
        fs.add(new BtuFiledCustom(R.id.btu_select, "选择") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                ProjectService.setCurrentSugProject(myJSONObject);
                AndroidTool.showAnsyTost("当前项目是：" + ProjectService.getName(myJSONObject), 0);
            }
        }.setConfirm(true, "确定要选择这个项目吗？"));
        //下载按钮
        findViewById(R.id.btn_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDownLoadProject();
            }
        });
       /* fs.add(new BtuFiledCustom(R.id.btu_info, "详情") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                Intent intent = new Intent(ProjectPage.this, ObjectInfoActivty.class);
                intent.putExtra("id", myJSONObject.getId());
                startActivityForResult(intent, 1);

            }
        });*/
        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.fragment_project_item, fs, projects);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom);
        RecyclerView recyclerView = findViewById(R.id.recy);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        myItemRecyclerViewAdapter.setLoadViewCallback(new ViewHolderCallback() {
            @Override
            public void call(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
                ImageView imageView = holder.mView.findViewById(R.id.iv_currentIcon);
                if (position == 0 && currentProject != null)  {
                    imageView.setImageResource(R.drawable.project_icon_blue);
                    holder.mView.findViewById(R.id.fl_currentproject).setVisibility(View.VISIBLE);
                }else{
                    imageView.setImageResource(R.drawable.project_icon_gray);
                    holder.mView.findViewById(R.id.fl_currentproject).setVisibility(View.GONE);
                }
            }
        });
        //itemFragment = new ItemFragment(tableDataCustom);


      /*  getSupportFragmentManager().beginTransaction()
                .add(R.id.fl, itemFragment, "list")   // 此处的R.id.fragment_container是要盛放fragment的父容器'
                .commit();*/
        //initAddItemFragment();

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
