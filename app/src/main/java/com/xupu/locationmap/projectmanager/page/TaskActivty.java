package com.xupu.locationmap.projectmanager.page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.WindowManager;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.Customizing;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.ZTService;

import java.util.ArrayList;
import java.util.List;

public class TaskActivty extends AppCompatActivity {
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        getSupportActionBar().hide();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_task);
        MyJSONObject parent = (MyJSONObject) getIntent().getSerializableExtra("parent");
        //找到这个对象的表格任务
        String tableid = parent.getTableid();
        //表格所有的任务
        List<MyJSONObject> tableTasks = TableTool.findByTableNameAndParentId(ZTService.TASK_LIST, tableid);
        //得到改对象所有的的多媒体
        List<MyJSONObject> medias = TableTool.findByTableNameAndParentId(Customizing.MEDIA, parent.getId());
        init(tableTasks,parent, medias);
        initTitle();
    }

    private void initTitle() {
        AndroidTool.addTitleFragment(this, "多媒体数据" );
    }

    /**
     * @param tableTasks 表格任务
     * @param medias     当前对象的所有 多媒体
     */
    private void init(List<MyJSONObject> tableTasks, MyJSONObject parent,List<MyJSONObject> medias) {
       /* RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        List<TaskFragment> taskFragments = new ArrayList<>();
        for (MyJSONObject task : tableTasks) {
            TaskFragment taskFragment = new TaskFragment(task,parent,medias);
            taskFragments.add(taskFragment);
            transaction.add(R.id.ll,taskFragment);
        }
      /*  TaskFragment taskFragment = new TaskFragment(tableTasks.get(0),parent,medias);
        taskFragments.add(taskFragment);
        transaction.add(R.id.ll,taskFragment);*/
        transaction.commit();
    }
}
