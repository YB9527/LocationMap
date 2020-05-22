package com.xupu.locationmap.common.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.projectmanager.page.AddItemFragment;
import com.xupu.locationmap.projectmanager.page.NFActivity;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.EditFiledCusom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;

import java.util.Collection;
import java.util.List;
import java.util.Map;


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

    /**
     * 显示某一个 fragment
     *
     * @param fragmentTagName
     */
    public static void showFragment(AppCompatActivity activity, String fragmentTagName) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        List<Fragment> list = fragmentManager.getFragments();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment fragment : list) {
            if (fragment.getTag().equals(fragmentTagName)) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
        }
        transaction.commit();
    }

    /**
     * 检查数据是否满足要求
     *
     * @param jsonObject
     * @param filedCustoms
     * @return
     */
    private static boolean checkData(JSONObject jsonObject, Collection<FiledCustom> filedCustoms) {
        for (FiledCustom filedCustom : filedCustoms) {
            if (filedCustom instanceof EditFiledCusom) {
                EditFiledCusom editFiledCusom = (EditFiledCusom) filedCustom;
                if (editFiledCusom.isMust()) {
                    String result = jsonObject.getString(filedCustom.getAttribute());
                    if (Tool.isEmpty(result)) {
                        AndroidTool.showAnsyTost("数据没有填写完整", 1);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void setView(View view, ItemDataCustom itemDataCustom) {
        Map<Integer, FiledCustom> map = itemDataCustom.getMap();
        //使用副本修改，
        final JSONObject oldJsonObject = itemDataCustom.getJsonObject();
        JSONObject jsonObject = (JSONObject) oldJsonObject.clone();
        for (Integer rid : map.keySet()) {
            View temView = view.findViewById(rid);
            FiledCustom filedCustom = map.get(rid);
            if (temView instanceof TextView) {
                TextView tv = (TextView) temView;
                String attribute = filedCustom.getAttribute();
                String str = jsonObject.getString(attribute);
                if (str != null) {
                    tv.setText(str);
                }
            }
            if (temView instanceof Button) {
                Button btu = (Button) temView;
                btu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BtuFiledCustom btuFiledCustom = (BtuFiledCustom) filedCustom;
                        //是否是检查选项按钮
                        ResultData<JSONObject> resultData = new ResultData<JSONObject>(0, jsonObject);
                        if (btuFiledCustom.isCheck()) {
                            //检查数据
                            boolean checkresult = checkData(jsonObject, map.values());
                            if (!checkresult) {
                                return;
                            }
                        }
                        if (btuFiledCustom.isReturn()) {
                            JSONTool.copytAttribute(jsonObject, oldJsonObject);
                            btuFiledCustom.OnClick(resultData);
                        } else {
                            btuFiledCustom.OnClick(resultData);
                        }
                    }
                });

            } else if (temView instanceof EditText) {
                EditText et = (EditText) temView;

                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        // TODO Auto-generated method stub

                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void afterTextChanged(Editable s) {
                        String text = et.getText().toString();
                        jsonObject.replace(filedCustom.getAttribute(), text);
                    }
                });

            }
        }
    }
}
