package com.xupu.locationmap.projectmanager.page;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.dialog.RightDialogFragment;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.tools.JSONTool;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.Utils;
import com.xupu.locationmap.projectmanager.po.LowImage;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.service.MapService;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.ZTService;
import com.xupu.locationmap.projectmanager.view.CheckBoxFieldCustom;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.SlidingFieldCustom;
import com.xupu.locationmap.projectmanager.view.TableDataCustom;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * map 图层弹框
 */
public class MapLayerDialog extends RightDialogFragment {


    public MapLayerDialog() {
        super();
    }

    public MapLayerDialog(Integer width, Integer height) {
        super(width, height);

    }

    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);


        List<LowImage> layerStatus = MapService.getLayerStatus();

        List<MyJSONObject> myShowLayers = JSONTool.toMyJSONObject(layerStatus);
        //侧滑功能

        List<FieldCustom> fs = new ArrayList<>();
        //项目名称
        fs.add(new FieldCustom(R.id.tv_name, "name"));
        fs.add(new CheckBoxFieldCustom(R.id.cb_isslect, LowMapManager.LOWIMAGE_SelectMark, R.id.item));
        //图层缩放至
        fs.add(new ViewFieldCustom(R.id.tv_location) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                LowImage lowImage = myJSONObject.getJsonobject().toJavaObject(LowImage.class);
                getDialog().dismiss();
                getDialogCallback().call(MapFragment.SCALE_MAPLAYER, lowImage);
            }
        });
        //图层表格数据
        fs.add(new ViewFieldCustom(R.id.tv_datatable) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                LowImage lowImage = myJSONObject.getJsonobject().toJavaObject(LowImage.class);
                getDialog().dismiss();
                getDialogCallback().call(MapFragment.LOOK_DATATABEL, lowImage);
            }
        });
        //侧滑功能
        fs.add(new SlidingFieldCustom(R.id.slidingview, R.id.item).setWidth(getWidth()));

        RecyclerView recyclerView = view.findViewById(R.id.recy);
        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.item_layer_map, fs, myShowLayers).setEdit(true);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom, recyclerView);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        myItemRecyclerViewAdapter.addItemTouch();

        return view;
    }


    @Override
    protected int setLayoutId() {
        return R.layout.map_layer;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        List<LowImage> layerStatus = JSONTool.toObject(myItemRecyclerViewAdapter.getmValues(), LowImage.class);
        if (!JSONObject.toJSONString(layerStatus).equals(JSONObject.toJSONString(MapService.getLayerStatus()))) {
            //修改过图层数据
            MapService.saveMapLayerStats(layerStatus);
            getDialogCallback().call(MapFragment.MAPLAYER_ChANGE, layerStatus);
        }
    }
}
