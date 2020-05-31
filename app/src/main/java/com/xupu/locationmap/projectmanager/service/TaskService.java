package com.xupu.locationmap.projectmanager.service;

import com.xupu.locationmap.projectmanager.po.MyJSONObject;

public class TaskService {
    /**
     * 得到任务名字
     * @param task
     * @return
     */
    public static String getTaskName(MyJSONObject task) {
        return task.getJsonobject().getString("taskname");
    }
}
