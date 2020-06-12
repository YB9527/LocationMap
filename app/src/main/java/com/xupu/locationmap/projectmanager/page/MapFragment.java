package com.xupu.locationmap.projectmanager.page;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.arcgisruntime.ArcGISRuntimeException;
import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.GeometryBuilder;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.PolygonBuilder;
import com.esri.arcgisruntime.geometry.PolylineBuilder;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.location.LocationDataSource;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.google.gson.JsonParser;
import com.tianditu.android.maps.overlay.PolygonOverlay;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.tdt.LayerInfoFactory;
import com.xupu.locationmap.common.tdt.TianDiTuLayer;
import com.xupu.locationmap.common.tdt.TianDiTuLayerInfo;
import com.xupu.locationmap.common.tdt.TianDiTuLayerTypes;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.JSONTool;
import com.xupu.locationmap.common.tools.MyLocationUtil;
import com.xupu.locationmap.common.tools.ReflectTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.MyGeometryType;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.service.XZQYService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapFragment extends Fragment {

    View view;

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
        initView();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLocationUtil.getMyLocation();
    }

    private MapView mMapView;

    private void initView() {
        mMapView = view.findViewById(R.id.mv_tian_di_tu);
        loadTpk();
        addTDT(mMapView); //添加天地图底图
        /*//fun(mMapView);
        //addTDT(mMapView);
        Point centralPoint = new Point(104.04, 30.67);
        //Point centralPoint = new Point(12765789.164,4033943.961);
        //Point centralPoint = new Point(1160.41,39.902);
        mMapView.setViewpointCenterAsync(centralPoint, 4000f); //设置地图中心点和初始放缩比*/
        mMapView.setAttributionTextVisible(false); //隐藏Esri logo
        initButton();

    }

    /**123
     * 初始化按钮
     */
    private void initButton() {
        //定位
        view.findViewById(R.id.btn_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapLocation();
            }
        });
        //图层
        view.findViewById(R.id.btn_layers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<MyJSONObject> list =null;
                try {
                    list = TableTool.findByParentId(XZQYService.getCurrentCode());
                }catch (Exception e){
                    return;
                }

                for (int i = 0; i < list.size(); i++) {
                    if(!list.get(i).getJsonobject().containsKey("geometry")){
                        list.remove(i);
                        i--;
                    }
                }
                Map<String, List<MyJSONObject>> map = JSONTool.getIDMap("table", list);
                addGraphicsOverlay(map);
            }
        });
    }

    /**
     * 加载天地图
     * @param mapView
     */
    public void addTDT(MapView mapView) {
        TianDiTuLayerInfo layerInfo = LayerInfoFactory.getLayerInfo(TianDiTuLayerTypes.TIANDITU_VECTOR_MERCATOR); //这个参数就是地图类型
        TileInfo info = layerInfo.getTileInfo();
        Envelope fullExtent = layerInfo.getFullExtent();
        TianDiTuLayer layer =
                new TianDiTuLayer(info, fullExtent);
        layer.setLayerInfo(layerInfo);

        TianDiTuLayerInfo layerInfo_cva =
                LayerInfoFactory.getLayerInfo(TianDiTuLayerTypes.TIANDITU_VECTOR_ANNOTATION_CHINESE_MERCATOR);
        TileInfo info_cva = layerInfo_cva.getTileInfo();
        Envelope fullExtent_cva = layerInfo_cva.getFullExtent();
        TianDiTuLayer layer_cva =
                new TianDiTuLayer(info_cva, fullExtent_cva);
        layer_cva.setLayerInfo(layerInfo_cva);


        ArcGISMap map = mapView.getMap();
        if (map == null) {
            map = new ArcGISMap();
            mapView.setMap(map);
        }
        map.getBasemap().getBaseLayers().add(layer);
        map.getBasemap().getBaseLayers().add(layer_cva);


    }

    /**
     * 加载切片
     */
    private void loadTpk() {
        ArcGISMap map = mMapView.getMap();
        if (map == null) {
            map = new ArcGISMap();
            mMapView.setMap(map);
        }
        String path = "/data/data/com.xupu.locationmap/files/无标题38_2.tpk";
        final TileCache tileCache = new TileCache(path);
        tileCache.loadAsync();
        tileCache.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                if (tileCache.getLoadStatus() == LoadStatus.LOADED) {
                    ArcGISTiledLayer arcGISTiledLayer = new ArcGISTiledLayer(tileCache);
                    arcGISTiledLayer.loadAsync();
                    if (arcGISTiledLayer.getLoadStatus() == LoadStatus.LOADED) {
                        mMapView.getMap().getOperationalLayers().add(arcGISTiledLayer);
                        mMapView.setViewpointCenterAsync(arcGISTiledLayer.getFullExtent().getCenter(), 50000);
                        //mMapView.getMap().setMinScale(200000);
                        //mMapView.getMap().setMaxScale(500);

                    } else {
                        ArcGISRuntimeException exception = arcGISTiledLayer.getLoadError();
                        System.out.println();
                    }
                } else {
                    ArcGISRuntimeException exception = tileCache.getLoadError();
                    System.out.println();
                }
            }
        });
    }

    /**
     * 地图定位
     */
    public void mapLocation() {
        LocationDisplay locationDisplay = mMapView.getLocationDisplay();
        Resources resources = getResources();
        BitmapDrawable bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeResource(resources, R.drawable.new_default));
        PictureMarkerSymbol campsiteSymbol = new PictureMarkerSymbol(bitmapDrawable);
        campsiteSymbol.loadAsync();
        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        locationDisplay.startAsync();
        campsiteSymbol.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                LocationDataSource.Location location = locationDisplay.getLocation();
                Point pt = location.getPosition();
                //locationDisplay.setDefaultSymbol(campsiteSymbol);//设置默认符号
                locationDisplay.setShowAccuracy(false);//隐藏符号的缓存区域
                locationDisplay.setAcquiringSymbol(campsiteSymbol);//设置当前位置的填充符号
                ///locationDisplay.setAccuracySymbol(campsiteSymbol);//设置最后一个已知位置的标记符号
                //locationDisplay.setPingAnimationSymbol(campsiteSymbol);//设置当前位置更新时候的动画
                //locationDisplay.setCourseSymbol(campsiteSymbol);//设置当前位置的路线符号
                //locationDisplay.setHeadingSymbol(campsiteSymbol);//设置当前位置设备面对方向的符号
            }
        });

    }
    private static  String GEOMETRY_KEY="geometry";
    private void addGraphicsOverlay(Map<String, List<MyJSONObject>> map) {
        Graphic pointGraphic ;
        SimpleFillSymbol polygonSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.YELLOW, null);
        SimpleRenderer polygonRenderer = new SimpleRenderer(polygonSymbol);
        GraphicsOverlay polygonGraphicOverlay = new GraphicsOverlay();
        polygonGraphicOverlay.setRenderer(polygonRenderer );
        for (String tablename : map.keySet()){
            //按表格名称创建 图层
            GraphicsOverlay pointGraphicOverlay = new GraphicsOverlay();
            SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.RED, 10);
            SimpleRenderer pointRenderer = new SimpleRenderer(pointSymbol);
            pointGraphicOverlay.setRenderer(pointRenderer);
            mMapView.getGraphicsOverlays().add(pointGraphicOverlay);

            for (MyJSONObject geometryMyJson: map.get(tablename)){
                String jsonGeometry = geometryMyJson.getJsonobject().getString(GEOMETRY_KEY);
                String type = jsonGeometry.substring(0,jsonGeometry.indexOf("("));
                //原始坐标的point
                List<Point> srcpoints = getPoints(jsonGeometry,SpatialReference.create(4526));
                List<Point> descpoints = pointschangeSp(srcpoints,mMapView.getSpatialReference());
                switch (type){
                    case "MULTIPOLYGON":
                        PolygonBuilder polygonGeometry = new PolygonBuilder(mMapView.getSpatialReference());
                        for (Point srcPoint : descpoints){
                            polygonGeometry.addPoint(srcPoint);
                        }
                        Polygon polygon = polygonGeometry.toGeometry();
                        polygonGraphicOverlay.getGraphics().add(new Graphic(polygon));
                        break;
                    case "MULTIPOINT":
                        for (Point srcPoint : descpoints){
                            //本图坐标系的point；
                            Point descPoint =  (Point)GeometryEngine.project(srcPoint, mMapView.getSpatialReference());
                            pointGraphic = new Graphic(descPoint);
                            pointGraphicOverlay.getGraphics().add(pointGraphic);
                        }
                        break;

                }


            }
        }



        // point graphic
        Point pointGeometry = new Point(40e5, 40e5, SpatialReferences.getWebMercator());
        // red diamond point symbol
        SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.RED, 10);
        // create graphic for point

        // create a graphic overlay for the point
        GraphicsOverlay pointGraphicOverlay = new GraphicsOverlay();
        // create simple renderer
        SimpleRenderer pointRenderer = new SimpleRenderer(pointSymbol);
        pointGraphicOverlay.setRenderer(pointRenderer);
        // add graphic to overlay
        //pointGraphicOverlay.getGraphics().add(pointGraphic);
        // add graphics overlay to the MapView
        mMapView.getGraphicsOverlays().add(pointGraphicOverlay);

        // line graphic
        PolylineBuilder lineGeometry = new PolylineBuilder(SpatialReferences.getWebMercator());
        lineGeometry.addPoint(-10e5, 40e5);
        lineGeometry.addPoint(20e5, 50e5);
        // solid blue line symbol
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 5);
        // create graphic for polyline
        Graphic lineGraphic = new Graphic(lineGeometry.toGeometry());
        // create graphic overlay for polyline
        GraphicsOverlay lineGraphicOverlay = new GraphicsOverlay();
        // create simple renderer
        SimpleRenderer lineRenderer = new SimpleRenderer(lineSymbol);
        // add graphic to overlay
        lineGraphicOverlay.setRenderer(lineRenderer);
        // add graphic to overlay
        lineGraphicOverlay.getGraphics().add(lineGraphic);
        // add graphics overlay to the MapView
        mMapView.getGraphicsOverlays().add(lineGraphicOverlay);

        //polygon graphic
        PolygonBuilder polygonGeometry = new PolygonBuilder(SpatialReferences.getWebMercator());
        polygonGeometry.addPoint(-20e5, 20e5);
        polygonGeometry.addPoint(20e5, 20e5);
        polygonGeometry.addPoint(20e5, -20e5);
        polygonGeometry.addPoint(-20e5, -20e5);
        // solid yellow polygon symbol

        // create graphic for polygon
        Graphic polygonGraphic = new Graphic(polygonGeometry.toGeometry());
        // create graphic overlay for polygon

        polygonGraphicOverlay.getGraphics().add(polygonGraphic);
        // add graphics overlay to MapView
        mMapView.getGraphicsOverlays().add(polygonGraphicOverlay);

    }

    /**
     *点集合转换坐标系
     * @param srcpoints  准备要转换的点
     * @param spatialReference 目标坐标系
     * @return
     */
    private List<Point> pointschangeSp(List<Point> srcpoints, SpatialReference spatialReference) {
        List<Point> descPoints = new ArrayList<>();
        for (Point srcPoint : srcpoints){
            Point descPoint =  (Point)GeometryEngine.project(srcPoint, mMapView.getSpatialReference());
            descPoints.add(descPoint);
        }
        return  descPoints;
    }

    private List<Point> getPoints(String jsonGeometry, SpatialReference spatialReference) {
        jsonGeometry = jsonGeometry.substring(jsonGeometry.indexOf("(")).replaceAll("\\(","").replaceAll("\\)","");
        List<Point> list = new ArrayList<>();
        String[]  pointsStr = jsonGeometry.split(",");
        for (String pointStr : pointsStr){
            String[] xyArray = pointStr.split(" ");
            Point point = new Point(Double.parseDouble(xyArray[0]) ,Double.parseDouble(xyArray[1]),0,spatialReference);
            list.add(point);
        }
        return list;
    }
}
