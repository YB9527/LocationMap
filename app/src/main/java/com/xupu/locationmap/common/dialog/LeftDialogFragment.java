package com.xupu.locationmap.common.dialog;

import android.content.res.Configuration;
import com.xupu.locationmap.R;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import com.gyf.immersionbar.ImmersionBar;
import com.xupu.locationmap.common.tools.Utils;


import butterknife.BindView;

/**
 * 左边DialogFragment
 *
 * @author geyifeng
 * @date 2017/7/28
 */
public class LeftDialogFragment extends BaseDialogFragment {

    private Integer width;
    private Integer height;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public  LeftDialogFragment(Integer width,Integer height){
        this.width = width;
        this.height = height;
    }
    public  LeftDialogFragment(){
        Integer[] widthAndHeight = Utils.getWidthAndHeight();
        this.width = widthAndHeight[0] / 2;
        this.height = widthAndHeight[1];
    }


    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.TOP | Gravity.START);
        mWindow.setWindowAnimations(R.style.LeftAnimation);
        mWindow.setLayout(width, height);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(toolbar)
                .navigationBarColor(R.color.btn11)
                .keyboardEnable(true)
                .navigationBarWithKitkatEnable(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                .init();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(mWidthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
        ImmersionBar.with(this)
                .navigationBarWithKitkatEnable(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
                .init();
    }
}
