package com.xupu.locationmap.projectmanager.service;

import android.icu.lang.UScript;

import com.google.gson.reflect.TypeToken;
import com.tianditu.maps.Map.Project;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.po.SugProject;

import java.util.ArrayList;
import java.util.List;

public class ProjectService {
    private static  String CURRENT_PROJECT_MARK="CURRENT_PROJECT_MARK";
    private  static SugProject  currentSugProject;

    /**
     * 得到当前项目
     * @return
     */
    public static SugProject getCurrentSugProject() {
        if(currentSugProject == null){
            //在数据中查找
            currentSugProject = RedisTool.findRedis(CURRENT_PROJECT_MARK,SugProject.class);
        }
        return currentSugProject;
    }
    /**
     * 设置当前的项目
     * @param currentSugProject
     */
    public static void setCurrentSugProject(SugProject currentSugProject) {
        //保存到数据库中，下次重新启动有记录
        RedisTool.updateRedis(CURRENT_PROJECT_MARK,currentSugProject);
        ProjectService.currentSugProject = currentSugProject;
    }


    public static List<SugProject> findAll() {

        List<SugProject> projects = RedisTool.findListRedis("project", SugProject.class);
        if (Tool.isEmpty(projects)) {
            projects = new ArrayList<>();
            SugProject sugProject = new SugProject();
            sugProject.setId("1");
            sugProject.setName("测试");
            projects.add(sugProject);
            RedisTool.saveRedis("project",sugProject);
        }
        return projects;
    }


}
