package com.xupu.locationmap.common.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.projectmanager.page.AddItemFragment;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;


/**
 * 处理Android 工具类
 */
public class AndroidTool {

    private static AppCompatActivity activity;

    public static void setMainActivity(AppCompatActivity activity) {
        AndroidTool.activity = activity;
    }

    public static AppCompatActivity getMainActivity() {
        return activity;
    }

    /**
     * 弹出提示窗口
     *
     * @param tip
     * @param status 消息状态
     */
    public static void showToast(String tip, int status) {
        if (!Tool.isEmpty(tip)) {
            Toast.makeText(getMainActivity(), tip, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 异步弹出提示窗口
     *
     * @param tip
     * @param status 消息状态
     */
    public static void showAnsyTost(final String tip, final int status) {
        getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(tip, status);
            }
        });
    }

    /**
     * 设置全屏
     *
     * @param activity
     */
    public static void setFullWindow(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 两个按钮的 dialog
     */
    public static void confirm(Activity activity, String tip, final MyCallback callback) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity).setIcon(R.mipmap.ic_launcher).setTitle("提示")
                .setMessage(tip).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        callback.call(new ResultData(1));
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        dialogInterface.dismiss();
                        callback.call(new ResultData(0));
                    }
                });
        builder.create().show();
    }


    /**
     * 替换 fragment
     *
     * @param activity
     * @param rid
     * @param fragment
     */
    public static void replaceFragment(AppCompatActivity activity, int rid, AddItemFragment fragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(rid, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
