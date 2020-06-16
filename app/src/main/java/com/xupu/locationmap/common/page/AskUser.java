package com.xupu.locationmap.common.page;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.ocr.ui.camera.CameraActivity;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.tools.AndroidTool;

public class AskUser {
    private static final int PERMISSIONS_EXTERNAL_STORAGE = 801;
    // 要申请的权限
    private static String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};


    public static void ask(AppCompatActivity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_PRIVILEGED},
                    2);
        }else {

        }
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_PRIVILEGED},
                    2);
        }

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},
                    3);
        }

        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(activity,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_EXTERNAL_STORAGE);
                return;
            }
        }


    }


}