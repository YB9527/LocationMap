package com.xupu.locationmap.projectmanager.service;

import android.os.Environment;

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


    /**
     * @param parent
     * @param mediaType
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
        String uuid = UUID.randomUUID().toString();
        String state = Environment.getExternalStorageState();
        String root;
        if (state.equals("mounted")) {
            root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/旭普公司";
            if (!new File(root).exists()) {
                boolean bl = new File(root).mkdirs();
                if (!bl) {
                    root = AndroidTool.getMainActivity().getFilesDir().getAbsolutePath();
                }
            } else {
                if (!new File(root).canWrite()) {
                    root = AndroidTool.getMainActivity().getFilesDir().getAbsolutePath();
                }
            }
        } else {
            root = AndroidTool.getMainActivity().getFilesDir().getAbsolutePath();
        }


        String path = root + "/" + ProjectService.getName(project) + "/" + XZQYService.getCode(xzdm) + "_" + XZQYService.getCaption(xzdm) + "/"
                + NFService.getName(parent) + "_" + parent.getId() + "/" + task.replace("正面","").replace("反面","") + "/" + uuid;
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
}
