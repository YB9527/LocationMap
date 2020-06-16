package com.xupu.locationmap.projectmanager.service;

import com.google.gson.reflect.TypeToken;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.projectmanager.page.LowMapManager;
import com.xupu.locationmap.projectmanager.po.LowImage;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapService {

    private static List<LowImage> layerStatus;
    /**
     * 得到图层状态
     * @return
     */
    public static List<LowImage> getLayerStatus() {
        String Laerys_REDIS_Mark= getMapLayerStausRedisMark();
        synchronized (MapService.class){
            if(layerStatus == null){
               layerStatus = RedisTool.findRedis(Laerys_REDIS_Mark, new TypeToken<List<LowImage>>() {
                }.getType());
            }
            if(layerStatus == null){
                //项目拥有的 layer
                List<LowImage>   layerTableItems= getLayerTableItems();
                //有效 的 layer 状态
               layerStatus =  LowMapManager.getExitLowMaps(layerStatus,layerTableItems);

            }
        }
        return  layerStatus;
    }

    /**
     * 得到项目图层表格，根据关系表得到
     * @return
     */
    private static List<LowImage> getLayerTableItems() {
        List<MyJSONObject>  layerTableItems= ZTService.getLayerTableItems();
        List<LowImage> lowImages = new ArrayList<>();
        for (MyJSONObject myJSONObject : layerTableItems){
            LowImage lowImage = new LowImage(ZTService.getTableName(myJSONObject),null,200);
            lowImages.add(lowImage);
        }
        return  lowImages;
    }

    private static String getMapLayerStausRedisMark(){
        String Laerys_REDIS_Mark = ProjectService.getCurrentProjectDBName() + "_" + "datalayers";
        return Laerys_REDIS_Mark;
    }

    public static void saveMapLayerStats(List<LowImage> layerStatus) {
        RedisTool.updateRedis(getMapLayerStausRedisMark(),layerStatus);
        MapService.layerStatus= layerStatus;
    }
}
