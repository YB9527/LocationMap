package com.xupu.locationmap.projectmanager.service;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.po.Customizing;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProjectService {


    public static String CURRENT_PROJECT_MARK = "CURRENT_PROJECT_MARK";
    private static MyJSONObject currentSugProject;
    public static String PROJECT_RANDOM_MARK = "my_random";

    /**
     * 得到当前项目
     *
     * @return
     */
    public static MyJSONObject getCurrentSugProject() {
        if (currentSugProject == null) {
            //在数据中查找
            currentSugProject = RedisTool.findRedis(CURRENT_PROJECT_MARK, MyJSONObject.class);
        }

        return currentSugProject;
    }

    /**
     * 得到项目随机码
     *
     * @param project
     * @return
     */
    public static String getProjectRandom(MyJSONObject project) {

        return project.getJsonobject().getString(PROJECT_RANDOM_MARK);
    }

    /**
     * 设置当前的项目
     *
     * @param currentSugProject
     */
    public static void setCurrentSugProject(MyJSONObject currentSugProject) {
        //保存到数据库中，下次重新启动有记录
        if(currentSugProject != null){
            RedisTool.updateRedis(CURRENT_PROJECT_MARK, currentSugProject);
            ProjectService.currentSugProject = currentSugProject;
            TableTool.createDB(getProjectDBName(currentSugProject));
        }else{
            RedisTool.deleteRedisByMark(CURRENT_PROJECT_MARK);
        }

    }

    public static String getProjectDBName(MyJSONObject project) {
        return getName(project) + "_" + getProjectRandom(project);
    }

    public static String getCurrentProjectDBName() {
        return getName(getCurrentSugProject()) + "_" + getProjectRandom(getCurrentSugProject());
    }
    public static ArrayList<MyJSONObject> findAll() {
        ArrayList<MyJSONObject> projects = RedisTool.findListRedis(ZTService.PROJECT_TABLE_NAME + "%", MyJSONObject.class);
        // ArrayList<MyJSONObject> projects = TableTool.findByTableName(ZTService.PROJECT_TABLE_NAME);
        //String json = JSONObject.toJSONString(projects.get(0)) ;
        return projects;
    }

    public static String getName(MyJSONObject currentSugProject) {
        return currentSugProject.getJsonobject().getString(Customizing.PROJECT_Field.get(Customizing.PROJECT_name).getName());
    }

    public static MyJSONObject newProject() {

        String uid = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", uid);
        jsonObject.put(Customizing.PROJECT_Field.get(Customizing.PROJECT_name).getName(), "");

        return new MyJSONObject(uid, Customizing.PROJECT, null, jsonObject.toJSONString());
    }

    /**
     * 删除项目
     *
     * @param deleteProject
     */
    public static void deleteProject(MyJSONObject deleteProject) {

        RedisTool.deleteRedisByMark(ZTService.PROJECT_TABLE_NAME + "_" + getProjectDBName(deleteProject));
        if(currentSugProject != null && deleteProject.getId().equals(currentSugProject.getId())){
            setCurrentSugProject(null);
        }
    }
}
