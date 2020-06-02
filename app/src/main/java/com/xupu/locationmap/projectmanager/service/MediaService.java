package com.xupu.locationmap.projectmanager.service;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xupu.locationmap.common.po.Media;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.DateTool;
import com.xupu.locationmap.common.tools.FileTool;
import com.xupu.locationmap.projectmanager.po.Customizing;

import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.NF;
import com.xupu.locationmap.projectmanager.po.XZDM;
import com.xupu.locationmap.usermanager.service.UserService;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MediaService {

    private static String MARK = "media";


    public static String getMediaDir(MyJSONObject parent) {
        MyJSONObject project = ProjectService.getCurrentSugProject();
        MyJSONObject xzdm = XZQYService.getCurrentXZDM();
        String root = AndroidTool.getRootDir();
        String dir = root + ProjectService.getName(project) + "/" + XZQYService.getCode(xzdm) + "_" + XZQYService.getCaption(xzdm) + "/"
                + NFService.getName(parent) + "_" + parent.getId() + "/";
        return dir;
    }

    /**
     * @param parent
     * @param mediaType 0 :照片，1：视频
     * @param task
     * @return
     */
    public static MyJSONObject getMedia(MyJSONObject parent, int mediaType, String task) {

        String path = newMediaPath(parent, task, mediaType);
        if (parent == null) {
            return null;
        }
        MyJSONObject media = newMedia(parent, mediaType, path, task);
        return media;
    }

    private static String newMediaPath(MyJSONObject parent, String taskname, int mediaType) {
        MyJSONObject project = ProjectService.getCurrentSugProject();
        if (project == null) {
            AndroidTool.showAnsyTost("请先选择项目", 1);
            return null;
        }
        MyJSONObject xzdm = XZQYService.getCurrentXZDM();
        if (xzdm == null) {
            AndroidTool.showAnsyTost("请先选择项目", 1);
            return null;
        }

        String root = AndroidTool.getRootDir();
        String uuid = UUID.randomUUID().toString();
        String path = root + ProjectService.getName(project) + "/" + XZQYService.getCode(xzdm) + "_" + XZQYService.getCaption(xzdm) + "/"
                  + parent.getId() + "/" + taskname.replace("正面", "").replace("反面", "") + "/" + uuid;

        /*String path = root + ProjectService.getName(project) + "/" + XZQYService.getCode(xzdm) + "_" + XZQYService.getCaption(xzdm) + "/"
                + NFService.getName(parent) + "_" + parent.getId() + "/" + taskname.replace("正面", "").replace("反面", "") + "/" + uuid;*/
        FileTool.exitsDir(FileTool.getDir(path), true);

        switch (mediaType) {
            case 0:
                path = path + ".jpg";
                break;
            case 1:
                path = path + ".mp4";
                break;
        }
        return path;
    }

    private static MyJSONObject newMedia(MyJSONObject parent, int mediatype, String path, String task) {

        String uid = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", uid);
        jsonObject.put(Customizing.MEDIA_Field.get(Customizing.MEDIA_type).getName(), mediatype);
        jsonObject.put(Customizing.MEDIA_Field.get(Customizing.MEDIA_path).getName(), path);
        jsonObject.put(Customizing.MEDIA_Field.get(Customizing.MEDIA_bz).getName(), "");
        jsonObject.put(Customizing.MEDIA_Field.get(Customizing.MEDIA_task).getName(), task);
        return new MyJSONObject(uid, Customizing.MEDIA, parent.getId(), jsonObject.toJSONString());
    }

    public static String getPath(MyJSONObject media) {
        return media.getJsonobject().getString(Customizing.MEDIA_path);
    }

    @SuppressLint("NewApi")
    public static void setPath(MyJSONObject media, String path) {
        media.getJsonobject().replace(Customizing.MEDIA_path, path);
    }

    /**
     * meida 转 MyJSONObject
     *
     * @param media
     * @return
     */
    public static MyJSONObject mediaToMyJSONObject(Media media) {
        MyJSONObject myJSONObject = new MyJSONObject(media.getId(), Customizing.MEDIA, media.getOid(), new Gson().toJson(media));
        return myJSONObject;
    }


    /**
     * @param oid
     * @param tableid
     * @param milepost 任务id
     * @param mtype
     * @param path
     * @return
     */
    private static Media newMedia(String oid, String tableid, String milepost, int mtype, String path) {
        Media media = new Media();
        media.setId(UUID.randomUUID().toString());
        media.setCreatetime(DateTool.dataFormat(new Date()));
        media.setOid(oid);
        media.setTableid(tableid);
        media.setCreator(UserService.getUser().getNickName());
        media.setMtype(mtype);
        media.setPath(path);
        media.setMilepost(milepost);
        return media;
    }

    private static MyJSONObject newMediaJSONObject(String oid, String tableid, String milepost, int mtype, String path) {
        Media media = newMedia(oid, tableid, milepost, mtype, path);
        MyJSONObject mediaMyJSONObject = mediaToMyJSONObject(media);
        return mediaMyJSONObject;
    }

    public static Media newMedia(MyJSONObject parent, MyJSONObject task, int type) {
        String path = newMediaPath(parent, TaskService.getTaskName(task), type);
        return newMedia(parent.getId(), parent.getTableid(), task.getId(), type, path);
    }

    public static MyJSONObject newMediaJSONObject(MyJSONObject parent, MyJSONObject task, int mtype) {
        Media media = newMedia(parent, task, mtype);
        return mediaToMyJSONObject(media);
    }
}
