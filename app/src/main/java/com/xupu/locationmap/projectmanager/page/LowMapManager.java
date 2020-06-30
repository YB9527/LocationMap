package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.tdt.TianDiTuLayerTypes;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.JSONTool;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.projectmanager.po.MapResult;
import com.xupu.locationmap.projectmanager.view.CheckBoxFieldCustom;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.po.LowImage;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.view.TableDataCustom;
import com.xupu.locationmap.projectmanager.service.ProjectService;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;


public class LowMapManager extends AppCompatActivity {

    public final static String LOWIMAGE_SelectMark = "select";
    private static String Laerys_REDIS_Mark;
    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;

    static {
        Laerys_REDIS_Mark = "Current_" + "lowlayers";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Laerys_REDIS_Mark = ProjectService.getCurrentProjectDBName() + "_" + "lowlayers";


        AndroidTool.setFullWindow(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_low_map_manager);
        initTitle();
        inintView();
    }

    private void initTitle() {

        AndroidTool.addTitleFragment(this, "底图管理", R.mipmap.topnav_icon_complete, "保存", new Callback() {
            @Override
            public void call(Object o) {
                List<LowImage> lowMapManagers = JSONTool.toObject(myItemRecyclerViewAdapter.getmValues(), LowImage.class);
                RedisTool.updateRedis(Laerys_REDIS_Mark, lowMapManagers);
                setResult(MapResult.LAYER);
                finish();
            }
        }, "确定要保存吗？");

    }

    private void inintView() {

        List<LowImage> extiLayers = getExitLowMaps();
        List<MyJSONObject> myShowLayers = JSONTool.toMyJSONObject(extiLayers);
        RecyclerView recyclerView = findViewById(R.id.recy);
        myItemRecyclerViewAdapter = getMapLayerAdapter(recyclerView,R.layout.item_low_map,myShowLayers);
        myItemRecyclerViewAdapter.addItemTouch();

    }

    public static MyItemRecyclerViewAdapter getMapLayerAdapter( RecyclerView recyclerView,Integer itemRid, List<MyJSONObject> myShowLayers) {
        List<FieldCustom> fs = new ArrayList<>();
        //项目名称
        fs.add(new FieldCustom(R.id.tv_name, "name"));
        fs.add(new CheckBoxFieldCustom(R.id.cb_isslect, LOWIMAGE_SelectMark, R.id.item));
        fs.add(new FieldCustom(R.id.tv_descrip, "size"));


        TableDataCustom tableDataCustom = new TableDataCustom(itemRid, fs, myShowLayers).setEdit(true);
        MyItemRecyclerViewAdapter myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom, recyclerView);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);

        return  myItemRecyclerViewAdapter;
    }

    /**
     * 得到显示的layers
     *
     * @return
     */
    public static List<LowImage> getVisableLowMaps() {
        try {
            List<LowImage> extiLayers = getExitLowMaps();
            List<LowImage> visableLyaers = new ArrayList<>();
            for (LowImage lowImage : extiLayers) {
                if (lowImage.isSelect()) {
                    visableLyaers.add(lowImage);
                }
            }
            return visableLyaers;
        }catch (Exception e){

        }
        return  null;
    }

    /**
     * 得到存在的layers 包括不显示的
     *
     * @return
     */
    public static List<LowImage> getExitLowMaps() {

        List<LowImage> layerStatus = RedisTool.findRedis(Laerys_REDIS_Mark, new TypeToken<List<LowImage>>() {
        }.getType());

        List<LowImage> haseLayers = new ArrayList<>();
        //添加天地图
        haseLayers.addAll(getTDTLowMaps());
        //查找本地tpk 文件
        haseLayers.addAll(getLativeTPK());
        List<LowImage> extiLayers = getExitLowMaps(layerStatus, haseLayers);
        return extiLayers;
    }

    /**
     * 界面显示图层信息
     *
     * @param layerStatus 保存在数据中的 layer 1、里面有是否被选择状态，2、还有叠加的顺序
     * @param haseLayers
     */
    @SuppressLint("NewApi")
    public static List<LowImage> getExitLowMaps(List<LowImage> layerStatus, List<LowImage> haseLayers) {
        List<LowImage> layers = new ArrayList<>();
        if (layerStatus != null) {
            for (int i = 0; i < layerStatus.size(); i++) {
                LowImage stausLayer = layerStatus.get(i);
                if (stausLayer.getType() != 100) {
                    layers.add(stausLayer);
                    haseLayers.remove(stausLayer);
                } else {
                    for (int j = 0; j < haseLayers.size(); j++) {
                        LowImage haselayer = haseLayers.get(j);

                        if (stausLayer.getName().equals(haselayer.getName())) {
                            haselayer.setSelect(stausLayer.isSelect());
                            layers.add(haselayer);
                            haseLayers.remove(haselayer);
                            break;
                        }
                    }
                }
            }
        }

        layers.addAll(haseLayers);
        return layers;
    }

    private static List<LowImage> getLativeTPK() {
        List<LowImage> list = new ArrayList<>();
        String tpkPath = AndroidTool.getRootDir() + "tpk";
        File[] files = new File(tpkPath).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                if (s.endsWith(".tpk")) {
                    return true;
                }
                return false;
            }
        });
        if (files != null) {
            for (File file : files) {
                LowImage lowImage = new LowImage(file.getName(), file.getAbsolutePath(), 100);
                lowImage.setSize(Math.round(file.length() / 1024d / 1024d) +" M");
                list.add(lowImage);
            }
        }
        return list;
    }

    private static List<LowImage> getTDTLowMaps() {
        List<LowImage> list = new ArrayList<>();
        //天地图矢量墨卡托投影地图服务
        LowImage lowImage = new LowImage("天地图矢量投影地图服务", null, TianDiTuLayerTypes.TIANDITU_VECTOR_MERCATOR);
        LowImage lowImage1 = new LowImage("天地图矢量中文标注", null, TianDiTuLayerTypes.TIANDITU_VECTOR_ANNOTATION_CHINESE_MERCATOR);
        LowImage lowImage2 = new LowImage("天地图影像投影地图服务", null, TianDiTuLayerTypes.TIANDITU_IMAGE_MERCATOR);
        LowImage lowImage3 = new LowImage("天地图影像投影中文标注", null, TianDiTuLayerTypes.TIANDITU_IMAGE_ANNOTATION_CHINESE_MERCATOR);
        list.add(lowImage);
        list.add(lowImage1);
        list.add(lowImage2);
        list.add(lowImage3);
        return list;
    }


}
