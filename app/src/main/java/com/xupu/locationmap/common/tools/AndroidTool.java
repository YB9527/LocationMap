package com.xupu.locationmap.common.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.page.MyDialog;
import com.xupu.locationmap.common.page.SlidingDeleteView;
import com.xupu.locationmap.common.page.TitleBarFragment;
import com.xupu.locationmap.common.page.ZQImageViewRoundOval;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.projectmanager.page.AddItemFragment;
import com.xupu.locationmap.projectmanager.view.BtuFieldCustom;
import com.xupu.locationmap.projectmanager.po.Customizing;
import com.xupu.locationmap.projectmanager.view.CheckBoxFieldCustom;
import com.xupu.locationmap.projectmanager.view.EditFieldCusom;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.ImgFieldCusom;
import com.xupu.locationmap.projectmanager.view.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.view.PositionField;
import com.xupu.locationmap.projectmanager.view.ProgressFieldCusom;
import com.xupu.locationmap.projectmanager.view.SlidingFieldCustom;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
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
        MyDialog myDialog = MyDialog.newInstance(tip, callback);
        Activity activity = (Activity) context;
        myDialog.show(activity.getFragmentManager(), "123");
       /* AlertDialog.Builder builder = new AlertDialog.Builder(context).setIcon(R.mipmap.ic_launcher).setTitle("提示")
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
        builder.create().show();*/
    }

    public static void confirm(Context context, String tip, String okStr, final MyCallback callback) {
        MyDialog myDialog = MyDialog.newInstance(tip, okStr, callback);
        Activity activity = (Activity) context;
        myDialog.show(activity.getFragmentManager(), "123");
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
     * @param FieldCustoms
     * @return
     */
    private static boolean checkData(JSONObject jsonObject, Collection<FieldCustom> FieldCustoms) {
        for (FieldCustom FieldCustom : FieldCustoms) {
            if (FieldCustom instanceof EditFieldCusom) {
                EditFieldCusom editFieldCusom = (EditFieldCusom) FieldCustom;
                if (editFieldCusom.isMust()) {
                    String result = jsonObject.getString(FieldCustom.getAttribute());
                    if (Tool.isEmpty(result)) {
                        AndroidTool.showAnsyTost("数据没有填写完整", 1);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static SlidingDeleteView oldSliding;

    /**
     * @param view
     * @param itemDataCustom
     * @param isEdit         是否直接修改
     */
    @SuppressLint("NewApi")
    public static void setView(View view, ItemDataCustom itemDataCustom, boolean isEdit, int postion) {
        List<FieldCustom> fs = itemDataCustom.getFieldCustoms();
        //使用副本修改，
        final MyJSONObject myJSONObject = itemDataCustom.getMyJSONObject();
        final JSONObject jsonObject;
        if (isEdit) {
            jsonObject = myJSONObject.getJsonobject();
        } else {
            jsonObject = (JSONObject) myJSONObject.getJsonobject().clone();
        }

        for (FieldCustom fieldCustom : fs) {
            View temView = view.findViewById(fieldCustom.getId());

            if (temView instanceof TextView) {
                TextView tv = (TextView) temView;
                temView.setVisibility(fieldCustom.isVisable());
                if (fieldCustom instanceof PositionField) {
                    PositionField positionField = (PositionField) fieldCustom;
                    tv.setText(positionField.getStartIndex() + postion + 1 + "");
                } else  if(!(fieldCustom instanceof  CheckBoxFieldCustom)){
                    String attribute = fieldCustom.getAttribute();
                    if (attribute == null) {
                        tv.setText("");
                    } else {
                        String str = jsonObject.getString(attribute);
                        if (str != null || temView instanceof EditText) {
                            tv.setText(str);
                        }else if(!(fieldCustom instanceof  BtuFieldCustom || fieldCustom instanceof  ViewFieldCustom) ){
                            tv.setText("");
                        }
                    }
                }
            }
            if (fieldCustom instanceof ViewFieldCustom) {
                ViewFieldCustom viewFieldCustom = (ViewFieldCustom) fieldCustom;
                temView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (viewFieldCustom.isConfirm()) {
                            confirm(temView.getContext(), viewFieldCustom.getConfirmmessage(), new MyCallback() {
                                @Override
                                public void call(ResultData resultData) {
                                    if (resultData.getStatus() == 0) {
                                        viewFieldCustom.OnClick(view, myJSONObject);
                                    }
                                }
                            });
                        } else {
                            viewFieldCustom.OnClick(view,myJSONObject);
                        }
                    }
                });

            } else if (fieldCustom instanceof CheckBoxFieldCustom) {
                CheckBoxFieldCustom checkBoxFieldCustom = (CheckBoxFieldCustom) fieldCustom;

                boolean bl = jsonObject.getBoolean(checkBoxFieldCustom.getAttribute());
                CheckBox checkBox = view.findViewById(checkBoxFieldCustom.getId());
                checkBox.setChecked(bl);

                view.findViewById(checkBoxFieldCustom.getItemRid()).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean bl = !jsonObject.getBoolean(checkBoxFieldCustom.getAttribute());
                        CheckBox checkBox = view.findViewById(checkBoxFieldCustom.getId());
                        checkBox.setChecked(bl);
                        jsonObject.replace(checkBoxFieldCustom.getAttribute(), bl);
                        Callback callback = checkBoxFieldCustom.getCallback();
                        if (callback != null) {
                            callback.call(jsonObject);
                        }
                    }
                });


            } else if (fieldCustom instanceof SlidingFieldCustom) {
                SlidingFieldCustom slidingFieldCustom = (SlidingFieldCustom) fieldCustom;
                View containerView = view.findViewById(slidingFieldCustom.getLayoutid());
                if(slidingFieldCustom.getWidth() == 0){
                    containerView.getLayoutParams().width = ScreenUtils.getScreenWidth(AndroidTool.getMainActivity().getBaseContext());
                }else{
                    containerView.getLayoutParams().width =slidingFieldCustom.getWidth();
                }

                SlidingDeleteView slidingview = view.findViewById(slidingFieldCustom.getId());
                slidingview.setEnable(true);
                if (oldSliding != null) {
                    oldSliding.removeOld();
                }
                oldSliding = slidingview;
            } else if (temView instanceof Button) {
                Button btu = (Button) temView;
                btu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BtuFieldCustom btuFieldCustom = (BtuFieldCustom) fieldCustom;
                        //是否是检查选项按钮
                        ResultData<JSONObject> resultData = new ResultData<JSONObject>(0, jsonObject);
                        if (btuFieldCustom.isCheck()) {
                            //检查数据
                            boolean checkresult = checkData(jsonObject, fs);
                            if (!checkresult) {
                                return;
                            }
                        }
                        if (btuFieldCustom.isReturn() && !btuFieldCustom.isConfirm()) {
                            myJSONObject.setJsonobject(jsonObject);
                        }
                        //先查 对象是否被更改
                        if (btuFieldCustom.isCompare()) {
                            if (jsonObject.toJSONString().equals(myJSONObject.getJsonobject().toJSONString())) {
                                showAnsyTost(btuFieldCustom.getCompareMessage(), 2);
                                return;
                            }
                        }
                        //是否 弹出确认窗口
                        if (btuFieldCustom.isConfirm()) {
                            confirm(temView.getContext(), btuFieldCustom.getConfirmmessage(), new MyCallback() {

                                @Override
                                public void call(ResultData resultData) {
                                    if (resultData.getStatus() == 0) {
                                        //检查是否被更改

                                        myJSONObject.setJsonobject(jsonObject);
                                        // Log.v("yb",myJSONObject.toString());
                                        btuFieldCustom.OnClick(myJSONObject);

                                    }
                                }
                            });
                        } else {
                            btuFieldCustom.OnClick(myJSONObject);
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
                        if ("".equals(text) && jsonObject.getString(fieldCustom.getAttribute()) == null) {
                            return;
                        }
                        jsonObject.replace(fieldCustom.getAttribute(), text);
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
                if (img instanceof ZQImageViewRoundOval) {
                    ZQImageViewRoundOval iv_roundRect = (ZQImageViewRoundOval) img;
                    iv_roundRect.setType(ZQImageViewRoundOval.TYPE_ROUND);
                    iv_roundRect.setRoundRadius(10);//矩形凹行大小
                }
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (fieldCustom instanceof ImgFieldCusom) {
                            ImgFieldCusom imgFieldCustom = (ImgFieldCusom) fieldCustom;
                            imgFieldCustom.onClick(myJSONObject);
                        }
                    }
                });
            } else if (temView instanceof ProgressBar) {
                ProgressBar pb = (ProgressBar) temView;
                ProgressFieldCusom pbFieldCustom = (ProgressFieldCusom) fieldCustom;
                pb.setProgress(jsonObject.getIntValue(pbFieldCustom.getAttribute()), true);
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
            root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/外业调查";
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

    /**
     * 添加标题
     */
    public static TitleBarFragment addTitleFragment(Activity activity, String title) {
        TitleBarFragment titleBarFragment = new TitleBarFragment(title);
        activity.getFragmentManager().beginTransaction().replace(R.id.title, titleBarFragment).commit();
        return titleBarFragment;
    }

    public static TitleBarFragment addTitleFragment(Activity activity, String title, int leftIcRid, String rightStr, Callback callback) {
        TitleBarFragment titleBarFragment = new TitleBarFragment(title);
        activity.getFragmentManager().beginTransaction().replace(R.id.title, new TitleBarFragment(leftIcRid, title, callback, rightStr)).commit();
        return titleBarFragment;
    }


    public static TitleBarFragment addTitleFragment(Activity activity, String title, int leftIcRid, String rightStr, Callback callback, String confirmMessage) {
        TitleBarFragment titleBarFragment = new TitleBarFragment(title);
        activity.getFragmentManager().beginTransaction().replace(R.id.title, new TitleBarFragment(leftIcRid, title, callback, rightStr, confirmMessage)).commit();
        return titleBarFragment;
    }

    public static   Bitmap setTextToImg(int rid, String text, float textsize, int color) {
        BitmapDrawable icon = (BitmapDrawable) getMainActivity().getResources().getDrawable(rid);

        Bitmap bitmap = icon.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        //bitmap = getNewBitmap(bitmap,100,100);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        // 抗锯齿
        paint.setAntiAlias(true);
        // 防抖动
        paint.setDither(true);
        paint.setTextSize(textsize);
        paint.setTextAlign(Paint.Align.CENTER);

        paint.setColor(color);
        canvas.drawText(text,(bitmap.getWidth() / 2),(bitmap.getHeight() / 2), paint);
        return bitmap;
    }
    public static   Bitmap setTextToImg(Bitmap bitmap,Paint paint,String text) {

        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text,(bitmap.getWidth() / 2),(bitmap.getHeight() / 2), paint);
        return bitmap;
    }


    public  static Bitmap getNewBitmap(Bitmap bitmap, int newWidth ,int newHeight){
        // 获得图片的宽高.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBitmap;
    }
}
