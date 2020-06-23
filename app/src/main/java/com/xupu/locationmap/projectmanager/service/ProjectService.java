package com.xupu.locationmap.projectmanager.service;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.tianditu.maps.Map.Project;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.page.ProjectPage;
import com.xupu.locationmap.projectmanager.po.Customizing;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;


import java.io.File;
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
        String random = getProjectRandom(project);
        if(Tool.isEmpty(random)){
            return getName(project);
        }else{
            return getName(project) + "_" + getProjectRandom(project);
        }

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

    @SuppressLint("NewApi")
    public static void setName(MyJSONObject currentSugProject, String projectname) {
        String key  = Customizing.PROJECT_Field.get(Customizing.PROJECT_name).getName();
         currentSugProject.getJsonobject().replace(key,projectname);
        currentSugProject.toJson();
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

        String dbpath = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dbpath = AndroidTool.getDatabaseDir()+getProjectDBName(deleteProject);
            boolean bl = new File(dbpath+".db").delete();
            boolean b2 = new File(dbpath+".db-journal").delete();
        }

        RedisTool.deleteRedisByMark(ZTService.PROJECT_TABLE_NAME + "_" + getProjectDBName(deleteProject));
        if(currentSugProject != null && deleteProject.getId().equals(currentSugProject.getId())){
            setCurrentSugProject(null);
        }
       
    }


    public static void toProjectPage() {
        Intent intent = new Intent(AndroidTool.getMainActivity(), ProjectPage.class);
        AndroidTool.getMainActivity().startActivity(intent);
    }

    public static void saveProject(MyJSONObject project) {


        //设置为当前项目
        ProjectService.setCurrentSugProject(project);
        //开启事务
        RedisTool.saveRedis(ZTService.PROJECT_TABLE_NAME + "_" + ProjectService.getProjectDBName(project), project);

    }
}
