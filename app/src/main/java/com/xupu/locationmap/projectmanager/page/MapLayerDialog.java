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
import com.xupu.locationmap.projectmanager.view.TableDataCustom;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;


import java.util.ArrayList;
import java.util.List;

/**
 * map 图层弹框
 */
public class MapLayerDialog extends RightDialogFragment {

    private final static String CURRENT_LAYER = "CURRENT_LAYER";
    private static LowImage currentlowImage;
    private static LowImage oldLayer;
    static {
        currentlowImage = RedisTool.findRedis(CURRENT_LAYER, LowImage.class);
        oldLayer = currentlowImage;
    }

    public static  LowImage getCurrentLayer(){
        return  currentlowImage;
    }

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
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View view = super.onCreateView(inflater, container, savedInstanceState);


        List<LowImage> layerStatus = MapService.getLayerStatus();

        List<MyJSONObject> myShowLayers = JSONTool.toMyJSONObject(layerStatus);
        //侧滑功能

        List<FieldCustom> fs = new ArrayList<>();
        //项目名称
        fs.add(new FieldCustom(R.id.tv_name, "name"));
        fs.add(new CheckBoxFieldCustom(R.id.cb_isslect, LowMapManager.LOWIMAGE_SelectMark, R.id.cb_isslect).setImg(R.mipmap.layer_icon_eyesclose, R.mipmap.layer_icon_eyesopen));
        //点击设置为当前图层
        fs.add(new ViewFieldCustom(R.id.item) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {


                LowImage lowImage = myJSONObject.getJsonobject().toJavaObject(LowImage.class);
                if (currentlowImage == null || !currentlowImage.equals(lowImage)) {
                    //如果是当前区域，不用选中

                    AndroidTool.confirm(getContext(), "确定要选择这个图层吗？", new MyCallback() {
                        @Override
                        public void call(ResultData resultData) {
                            if (resultData.getStatus() == 0) {
                                if (currentlowImage != null) {
                                    int index = layerStatus.indexOf(currentlowImage);
                                    myItemRecyclerViewAdapter.notifyItemChanged(index);
                                }
                                myItemRecyclerViewAdapter.update(myJSONObject);
                                setCurrentLowImage(lowImage);
                            }
                        }
                    });
                }
            }
        });

        //图层缩放至
        fs.add(new ViewFieldCustom(R.id.iv_location) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                LowImage lowImage = myJSONObject.getJsonobject().toJavaObject(LowImage.class);
                getDialog().dismiss();
                getDialogCallback().call(MapFragment.SCALE_MAPLAYER, lowImage);
            }
        });
        //图层表格数据
        fs.add(new ViewFieldCustom(R.id.iv_datatable) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                LowImage lowImage = myJSONObject.getJsonobject().toJavaObject(LowImage.class);
                getDialog().dismiss();
                getDialogCallback().call(MapFragment.LOOK_DATATABEL, lowImage);
            }
        });


        RecyclerView recyclerView = view.findViewById(R.id.recy);
        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.item_layer_map, fs, myShowLayers).setEdit(true);
        // TableDataCustom tableDataCustom = new TableDataCustom(R.layout.map_item_searhhistorytext, fs, myShowLayers).setEdit(true);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom, recyclerView);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        myItemRecyclerViewAdapter.addItemTouch();
        myItemRecyclerViewAdapter.setLoadViewCallback(new ViewHolderCallback() {
            @Override
            public void call(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
                MyJSONObject myJSONObject = myItemRecyclerViewAdapter.getItem(position);
                LowImage lowImage = myJSONObject.getJsonobject().toJavaObject(LowImage.class);
                if (currentlowImage == null || !currentlowImage.equals(lowImage)) {
                    holder.mView.findViewById(R.id.item_currentxzqy).setVisibility(View.GONE);
                } else {
                    holder.mView.findViewById(R.id.item_currentxzqy).setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    private void setCurrentLowImage(LowImage currentlowImage) {
        MapLayerDialog.currentlowImage = currentlowImage;
        RedisTool.updateRedis(CURRENT_LAYER, currentlowImage);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.dialog_map_layer;
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
        //检查当前图层是否被更改
        if(currentlowImage != null && oldLayer != null && !oldLayer.equals(currentlowImage)){
            getDialogCallback().call(MapFragment.CURRENT_LAYER_CHANGE, currentlowImage);
        }
    }
}
