package com.xupu.locationmap.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.xupu.locationmap.AppManager;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.tools.Utils;
import com.xupu.locationmap.R;

import org.jetbrains.annotations.NotNull;


import butterknife.ButterKnife;
import butterknife.Unbinder;



/**
 * DialogFragment 实现沉浸式的基类
 *
 * @author geyifeng
 * @date 2017 /8/26
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected Activity mActivity;
    protected View mRootView;
    protected Window mWindow;
    //private Unbinder unbinder;
    public Integer[] mWidthAndHeight;

    private DialogCallback dialogCallback;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //点击外部消失
        dialog.setCanceledOnTouchOutside(true);

        mWindow = dialog.getWindow();
        mWidthAndHeight = Utils.getWidthAndHeight(mWindow);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
        return mRootView;
    }


    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       /* unbinder = ButterKnife.bind(this, view);
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }*/
        initData();
        initView();
        setListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppManager.getInstance().hideSoftKeyBoard(mActivity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unbinder.unbind();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWidthAndHeight = Utils.getWidthAndHeight(mWindow);
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        ImmersionBar.with(this).init();
    }


    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * view与数据绑定
     */
    protected void initView() {
     /*   ImageView iv = mRootView.findViewById(R.id.mIv);
        if (iv != null) {
            Glide.with(this).asBitmap().load(Utils.getPic())
                    .apply(new RequestOptions().placeholder(R.drawable.test))
                    .into(iv);
        }*/
    }

    /**
     * 设置监听
     */
    protected void setListener() {

    }

    public DialogCallback getDialogCallback() {
        return dialogCallback;
    }

    public void setDialogCallback(DialogCallback dialogCallback) {
        this.dialogCallback = dialogCallback;
    }
}
