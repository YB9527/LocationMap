package com.xupu.locationmap.projectmanager.service;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.FileTool;
import com.xupu.locationmap.projectmanager.po.Customizing;

import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.NF;
import com.xupu.locationmap.projectmanager.po.XZDM;


import java.io.File;
import java.io.IOException;
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
                + NFService.getName(parent) + "_" + parent.getId() + "/" + task.replace("正面", "").replace("反面", "") + "/" + uuid;
        FileTool.exitsDir(FileTool.getDir(path), true);

        switch (mediaType) {
            case 0:
                path = path + ".jpg";
                break;
            case 1:
                path = path + ".mp4";
                break;
        }

        MyJSONObject media = newMedia(parent, mediaType, path, task);

        return media;
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
         media.getJsonobject().replace(Customizing.MEDIA_path,path);
    }
}
