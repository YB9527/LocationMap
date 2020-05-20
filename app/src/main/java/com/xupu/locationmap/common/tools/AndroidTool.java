package com.xupu.locationmap.common.tools;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xupu.locationmap.usermanager.page.Login;


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

    public static void setFullWindow(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
