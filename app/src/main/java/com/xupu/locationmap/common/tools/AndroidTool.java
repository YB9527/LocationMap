package com.xupu.locationmap.common.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.xupu.locationmap.projectmanager.po.Customizing;
import com.xupu.locationmap.projectmanager.po.EditFiledCusom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ImgFiledCusom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.PositionField;
import com.xupu.locationmap.projectmanager.po.ProgressFiledCusom;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


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


    public static String FILEPROVIDER = "com.xupu.locationmap.fileprovider";

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
    public static boolean setFullWindow(Activity activity) {
        boolean bl = activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return bl;
        //activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 两个按钮的 dialog
     */
    public static void confirm(Context context, String tip, final MyCallback callback) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context).setIcon(R.mipmap.ic_launcher).setTitle("提示")
                .setMessage(tip)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
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

    /**
     * @param view
     * @param itemDataCustom
     * @param isEdit         是否直接修改
     */

    @SuppressLint("NewApi")
    public static void setView(View view, ItemDataCustom itemDataCustom, boolean isEdit, int postion) {
        List<FiledCustom> fs = itemDataCustom.getFiledCustoms();
        //使用副本修改，
        final MyJSONObject myJSONObject = itemDataCustom.getMyJSONObject();
        final JSONObject jsonObject;
        if (isEdit) {
            jsonObject = myJSONObject.getJsonobject();
        } else {
            jsonObject = (JSONObject) myJSONObject.getJsonobject().clone();
        }

        for (FiledCustom filedCustom : fs) {
            View temView = view.findViewById(filedCustom.getId());
            if (temView instanceof TextView) {
                TextView tv = (TextView) temView;
                if (filedCustom instanceof PositionField) {
                    PositionField positionField = (PositionField) filedCustom;
                    tv.setText(positionField.getStartIndex() + postion + 1 + "");
                } else {
                    String attribute = filedCustom.getAttribute();
                    if (attribute == null) {
                        tv.setText("");
                    } else {
                        String str = jsonObject.getString(attribute);
                        if (str != null || temView instanceof EditText) {
                            tv.setText(str);
                        }
                        //tv.setText(str);

                    }
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
                            boolean checkresult = checkData(jsonObject, fs);
                            if (!checkresult) {
                                return;
                            }
                        }
                        if (btuFiledCustom.isReturn() && !btuFiledCustom.isConfirm()) {
                            myJSONObject.setJsonobject(jsonObject);
                        }
                        //先查 对象是否被更改
                        if (btuFiledCustom.isCompare()) {
                            if (jsonObject.toJSONString().equals(myJSONObject.getJsonobject().toJSONString())) {
                                showAnsyTost(btuFiledCustom.getCompareMessage(), 2);
                                return;
                            }
                        }
                        //是否 弹出确认窗口
                        if (btuFiledCustom.isConfirm()) {
                            confirm(temView.getContext(), btuFiledCustom.getConfirmmessage(), new MyCallback() {

                                @Override
                                public void call(ResultData resultData) {
                                    if (resultData.getStatus() == 0) {
                                        //检查是否被更改

                                        myJSONObject.setJsonobject(jsonObject);
                                        // Log.v("yb",myJSONObject.toString());
                                        btuFiledCustom.OnClick(myJSONObject);

                                    }
                                }
                            });
                        } else {
                            btuFiledCustom.OnClick(myJSONObject);
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
                        if("".equals(text) && jsonObject.getString(filedCustom.getAttribute()) == null){
                            return;
                        }
                        jsonObject.replace(filedCustom.getAttribute(), text);
                        // String aa ="123";1
                        //jsonObject.replace("a","123");

                    }
                });
            } else if (temView instanceof ImageView) {
                ImageView img = (ImageView) temView;
               /* View tem = view.findViewById(R.id.SFZ_Front);
                if (tem != null) {
                    tem.setVisibility(View.GONE);
                }
                tem = view.findViewById(R.id.SFZ_back);
                if (tem != null) {
                    tem.setVisibility(View.GONE);
                }*/
                String task = jsonObject.getString("task");
                if (task != null) {
                    if (task.equals(Customizing.SFZ_Front)) {
                        //显示身份证正面
                        view.findViewById(R.id.SFZ_Front).setVisibility(View.VISIBLE);
                    }
                    if (task.equals(Customizing.SFZ_back)) {
                        //显示身份证背面
                        view.findViewById(R.id.SFZ_back).setVisibility(View.VISIBLE);
                    }

                }
                String path = jsonObject.getString("path");
                if (FileTool.exitFile(path)) {
                    img.setImageBitmap(BitmapFactory.decodeFile(jsonObject.getString("path")));
                }
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImgFiledCusom imgFiledCustom = (ImgFiledCusom) filedCustom;
                        imgFiledCustom.onClick(myJSONObject);
                    }
                });
            } else if (temView instanceof ProgressBar) {
                ProgressBar pb = (ProgressBar) temView;
                ProgressFiledCusom pbFiledCustom = (ProgressFiledCusom) filedCustom;
                pb.setProgress(jsonObject.getIntValue(pbFiledCustom.getAttribute()), true);
            }
        }
    }

    private static Map<String, Class<?>> clsMap = new HashMap<>();


    /**
     * @param className 包名 layout,string,drawable,style,id,color,array
     * @param idName    唯一文件名
     * @return
     */
    public static int getCompentID(String className, String idName) {
        int id = 0;
        try {
            Class<?> cls = clsMap.get(className);
            if (cls == null) {
                cls = Class.forName("com.xupu.locationmap" + ".R$" + className);
                clsMap.put(className, cls);
            }

            id = cls.getField(idName).getInt(cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static String getRootDir() {
        String root;

        String state = Environment.getExternalStorageState();
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
        return root + "/";
    }
}
