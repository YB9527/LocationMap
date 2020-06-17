package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.xupu.locationmap.R;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.Customizing;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
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
        //根据seq调整顺序
        orderBySeq(tableTasks);

        //得到改对象所有的的多媒体
        List<MyJSONObject> medias = TableTool.findByTableNameAndParentId(Customizing.MEDIA, parent.getId());
        init(tableTasks, parent, medias);
        initTitle();
    }

    /**
     * 任务根据 seq 排序
     *
     * @param tableTasks
     */
    private void orderBySeq(List<MyJSONObject> tableTasks) {
        for (int i = 0; i < tableTasks.size() - 1; i++) {
            for (int j = 0; j < tableTasks.size() - 1 - i; j++) {
                if (tableTasks.get(j).getJsonobject().getIntValue("taskseq") > tableTasks.get(j + 1).getJsonobject().getIntValue("taskseq")) {
                    MyJSONObject temp = tableTasks.get(j);
                    tableTasks.set(j, tableTasks.get(j + 1));
                    tableTasks.set(j + 1, temp);
                }
            }
        }

    }

    private void initTitle() {
        AndroidTool.addTitleFragment(this, "附件");
    }

    /**
     * @param tableTasks 表格任务
     * @param medias     当前对象的所有 多媒体
     */
    private void init(List<MyJSONObject> tableTasks, MyJSONObject parent, List<MyJSONObject> medias) {
       /* RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        List<TaskFragment> taskFragments = new ArrayList<>();
        for (MyJSONObject task : tableTasks) {
            TaskFragment taskFragment = new TaskFragment(task, parent, medias);
            taskFragments.add(taskFragment);
            transaction.add(R.id.ll, taskFragment);
        }
      /*  TaskFragment taskFragment = new TaskFragment(tableTasks.get(0),parent,medias);
        taskFragments.add(taskFragment);
        transaction.add(R.id.ll,taskFragment);*/
        transaction.commit();
    }
}
