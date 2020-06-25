package com.xupu.locationmap.projectmanager.page;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.dialog.RightDialogFragment;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.po.ViewHolderCallback;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.JSONTool;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.projectmanager.po.LowImage;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.service.MapService;
import com.xupu.locationmap.projectmanager.view.CheckBoxFieldCustom;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.ItemDataCustom;
import com.xupu.locationmap.projectmanager.view.TableDataCustom;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;

import java.util.ArrayList;
import java.util.List;

/**
 * map 轨迹弹框
 */
public class MapTrajectoryDialog extends RightDialogFragment {



    public MapTrajectoryDialog() {
        super();
    }

    public MapTrajectoryDialog(Integer width, Integer height) {
        super(width, height);
    }


    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
         view = super.onCreateView(inflater, container, savedInstanceState);

        init();

        return view;
    }

    private void init() {
        MyJSONObject jsonObject = new MyJSONObject();
        jsonObject.setJsonobject(new JSONObject());
        List<FieldCustom> filedCustoms = new ArrayList<>();
        filedCustoms.add(new ViewFieldCustom(R.id.start) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                    switchOpen (R.id.start);
            }
        }.setConfirm(true,"确定要开启吗？"));
        filedCustoms.add(new ViewFieldCustom(R.id.paush) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                    switchOpen (R.id.paush);
            }
        }.setConfirm(true,"确定要暂停录制吗？"));

        ItemDataCustom itemDataCustom = new ItemDataCustom(null,  jsonObject,  filedCustoms);
        AndroidTool.setView(view,itemDataCustom,true,0);
    }

    /**
     * 切换打开 暂停
     * @param rid
     */
    private void switchOpen(int rid) {
       View open = view.findViewById(R.id.start);
       View paush = view.findViewById(R.id.paush);
        switch (rid){
            case  R.id.start:
                open.setVisibility(View.GONE);
                paush.setVisibility(View.VISIBLE);
                paush.setAnimation(AndroidTool.moveToViewLocation(1, 0, 0, 0));

                openStart();
                break;
            case  R.id.paush:
                paush.setVisibility(View.GONE);
                open.setVisibility(View.VISIBLE);
                open.setAnimation(AndroidTool.moveToViewLocation(1, 0, 0, 0));
                openPuash();
                break;
        }
    }

    /**
     * 暂停录制
     */
    private void openPuash() {
        AndroidTool.showAnsyTost("暂停录制",0);
    }

    /**
     * 开启录制
     */
    private void openStart() {
        AndroidTool.showAnsyTost("开启录制",0);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.dialog_map_trajectory;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
