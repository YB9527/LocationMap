package com.xupu.locationmap.common.dialog;

import android.content.res.Configuration;

import android.view.Gravity;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import com.gyf.immersionbar.ImmersionBar;


import butterknife.BindView;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.tools.Utils;

/**
 * 右边DialogFragment
 *
 * @author geyifeng
 * @date 2017/7/28
 */
public class RightDialogFragment extends BaseDialogFragment {

    private Integer width;
    private Integer height;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public  RightDialogFragment(Integer width,Integer height){
            this.width = width;
            this.height = height;
    }
    public  RightDialogFragment(){
        Integer[] widthAndHeight = Utils.getWidthAndHeight();
        this.width = widthAndHeight[0] / 2;
        this.height = widthAndHeight[1];
    }
    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.TOP | Gravity.END);
        mWindow.setWindowAnimations(R.style.RightAnimation);
        mWindow.setLayout(width, height);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
       /* ImmersionBar.with(this).titleBar(toolbar)
                .navigationBarColor(R.color.btn8)
                .keyboardEnable(true)
                .init();*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(mWidthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
