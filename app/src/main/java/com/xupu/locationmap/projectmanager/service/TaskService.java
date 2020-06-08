package com.xupu.locationmap.projectmanager.service;

import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;

import java.util.List;

public class TaskService {
    /**
     * 得到任务名字
     * @param task
     * @return
     */
    public static String getTaskName(MyJSONObject task) {
        return task.getJsonobject().getString("taskname");
    }
    public static List<MyJSONObject> findTaskByTableid(String tableid){
        List<MyJSONObject> tableTasks = TableTool.findByTableNameAndParentId(ZTService.TASK_LIST, tableid);
        return  tableTasks;
    }
}
