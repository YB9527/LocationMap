package com.xupu.locationmap.common.tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ocr.ui.camera.CameraActivity;

import com.google.gson.Gson;

import com.xupu.locationmap.projectmanager.po.Customizing;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.service.MediaService;

import java.io.File;

public class MediaTool {

    public static void to(Activity activity, int requestCode, MyJSONObject media) {
        switch (media.getJsonobject().getInteger(Customizing.MEDIA_type)) {
            case 0:
                //SFZPhotoTool.getSFZPhotoTool(activity).front(media);
                photo(activity, requestCode, media);
                break;
            case 1:
                break;
        }
    }

   /* @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void photo(Activity activity, int requestCode, MyJSONObject media) {
        Uri imageUri;
        // 创建File对象，用于存储拍照后的图片
        //存放在手机SD卡的应用关联缓存目录下
        File outputImage = new File(media.getJsonobject().getString(Customizing.MEDIA_path));

        Tool.exitsDir(outputImage.getParent(), true);
        // 从Android 6.0系统开始，读写SD卡被列为了危险权限，如果将图片存放在SD卡的任何其他目录，
        //   都要进行运行时权限处理才行，而使用应用关联 目录则可以跳过这一步
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
                  *//* 7.0系统开始，直接使用本地真实路径的Uri被认为是不安全的，会抛 出一个FileUriExposedException异常。
                   而FileProvider则是一种特殊的内容提供器，它使用了和内 容提供器类似的机制来对数据进行保护，
                   可以选择性地将封装过的Uri共享给外部，从而提高了 应用的安全性*//*

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //大于等于版本24（7.0）的场合
            imageUri = FileProvider.getUriForFile(activity, AndroidTool.FILEPROVIDER, outputImage);
        } else {
            //小于android 版本7.0（24）的场合
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//

        activity.getIntent().putExtra("media", media);

        activity.startActivityForResult(intent, requestCode);
    }*/

    //private static final int REQUEST_CODE_DRIVING_LICENSE = 103;

    public static void photo(Activity activity,int requestCode,MyJSONObject media){
        String path = MediaService.getPath(media);
        //File outputImage = new File(media.getJsonobject().getString(Customizing.MEDIA_path));
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,path
                );
       intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL);
        activity.getIntent().putExtra("media", media);
        activity.startActivityForResult(intent, requestCode);
    }
    public static void photo(Fragment fragment, int requestCode, MyJSONObject media){
        String path = MediaService.getPath(media);
        //File outputImage = new File(media.getJsonobject().getString(Customizing.MEDIA_path));
        Intent intent = new Intent(fragment.getActivity(), CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,path
        );
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL);
        fragment.getActivity().getIntent().putExtra("media", media);
        fragment.startActivityForResult(intent, requestCode);
    }
}
