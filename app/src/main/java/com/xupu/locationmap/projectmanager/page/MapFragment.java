package com.xupu.locationmap.projectmanager.page;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.esri.arcgisruntime.ArcGISRuntimeException;
import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.PolygonBuilder;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.location.LocationDataSource;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.util.ListenableList;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.tianditu.android.maps.overlay.MarkerOverlay;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.dialog.DialogCallback;
import com.xupu.locationmap.common.dialog.RightDialogFragment;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.tdt.LayerInfoFactory;
import com.xupu.locationmap.common.tdt.TianDiTuLayer;
import com.xupu.locationmap.common.tdt.TianDiTuLayerInfo;
import com.xupu.locationmap.common.tdt.TianDiTuLayerTypes;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.JSONTool;
import com.xupu.locationmap.common.tools.MyLocationUtil;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.common.tools.Utils;
import com.xupu.locationmap.projectmanager.po.CloseLog;
import com.xupu.locationmap.projectmanager.po.LowImage;
import com.xupu.locationmap.projectmanager.po.MapResult;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.service.MapService;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.XZQYService;
import com.xupu.locationmap.projectmanager.service.ZTService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MapFragment extends Fragment {
    /**
     * 底图被改变
     */
    public final static int LOW_MAPLAYER_ChANGE = 1;
    /**
     * 图层被改变
     */
    public final static int MAPLAYER_ChANGE = 2;
    /**
     * 查看图层表格数据
     */
    public final static int LOOK_DATATABEL = 3;
    /**
     * 缩放至图层
     */
    public final static int SCALE_MAPLAYER = 4;

    List<LowImageGraphicsLayer> lowImageGraphicsLayers = new ArrayList<>();
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
        try {
            addLowMaps(mMapView);
        } catch (Exception e) {
            ProjectService.toProjectPage();
            return;
        }
        mMapView.setAttributionTextVisible(false); //隐藏Esri logo


        initButton();
        addData();
        addTextGraphics();
        try {
            CloseLog log = RedisTool.findRedis(getCloseMapMark(), CloseLog.class);
            Point lastClosePoint = new Point(log.getX(), log.getY());
            mMapView.setViewpointCenterAsync(lastClosePoint, log.getScale());
        } catch (Exception e) {

        }


    }

    private GraphicsOverlay textGraphics;

    private void addTextGraphics() {


        SimpleFillSymbol polygonSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.RED, null);
        SimpleRenderer polygonRenderer = new SimpleRenderer(polygonSymbol);
        textGraphics = new GraphicsOverlay();
        textGraphics.setRenderer(polygonRenderer);
        mMapView.getGraphicsOverlays().add(textGraphics);
    }

    /**
     * 显示 图层 dialog
     */
    private void showLaerys() {

        Integer[] widthAndHeight = Utils.getWidthAndHeight();
        MapLayerDialog mapLayerDialog = new MapLayerDialog(widthAndHeight[0] / 3 * 2, widthAndHeight[1]);
        mapLayerDialog.setDialogCallback(new DialogCallback<Object>() {
            @Override
            public void call(int code, Object obj) {
                LowImage lowImage;
                switch (code) {
                    case MAPLAYER_ChANGE:
                        List<LowImage> layers = (List<LowImage>) obj;
                        changeMapLayerShow(layers);
                        break;
                    case LOOK_DATATABEL:
                        lowImage = (LowImage) obj;
                        lookLayerDataTable(lowImage);
                        break;
                    case SCALE_MAPLAYER:
                        lowImage = (LowImage) obj;
                        scaleMapLayer(lowImage);
                        break;


                }
            }
        });
        //MyItemRecyclerViewAdapter layerAdapter = LowMapManager.getMapLayerAdapter()
        //layerAdapter.addItemTouch();
        mapLayerDialog.show(getActivity().getSupportFragmentManager(), RightDialogFragment.class.getSimpleName());


    }

    /**
     * 所防止图层
     *
     * @param lowImage
     */
    private void scaleMapLayer(LowImage lowImage) {
        for (LowImageGraphicsLayer lowImageGraphicsLayer : lowImageGraphicsLayers) {
            if (lowImageGraphicsLayer.lowImage.equals(lowImage)) {
                GraphicsOverlay graphicsOverlay =lowImageGraphicsLayer.getShapeGraphics();
                ListenableList<Graphic> graphics = graphicsOverlay.getGraphics();
                Envelope extent = graphicsOverlay.getExtent();
                if (!extent.isEmpty()) {
                    mMapView.setViewpointGeometryAsync(extent);
                } else {
                    AndroidTool.showAnsyTost(lowImage.getName() + " 没有数据", 1);
                }

            }
        }
    }

    /**
     * 查看表格数据
     *
     * @param lowImage
     */
    private void lookLayerDataTable(LowImage lowImage) {

    }

    /**
     * 更新 MapLayer 1、是否显示，2显示顺序
     *
     * @param newlayers
     */
    private void changeMapLayerShow(List<LowImage> newlayers) {
        for (int i = 0; i < newlayers.size(); i++) {
            LowImage newImage = newlayers.get(i);
            //找到以前的位置，现在的位置，进行交换
            for (int j = 0; j < lowImageGraphicsLayers.size(); j++) {
                if (newImage.equals(lowImageGraphicsLayers.get(j).getLowImage())) {
                    if (i != j) {
                        //list 交换位置
                        LowImageGraphicsLayer lowImageGraphicsLayer1 = lowImageGraphicsLayers.get(i);
                        LowImageGraphicsLayer lowImageGraphicsLayer2 = lowImageGraphicsLayers.get(j);

                        Tool.swap(lowImageGraphicsLayers,i,j);

                        //地图 list 交换位置
                        int mapIndex1 = mMapView.getGraphicsOverlays().indexOf(lowImageGraphicsLayer1.getShapeGraphics());
                        int mapIndex2 = mMapView.getGraphicsOverlays().indexOf(lowImageGraphicsLayer2.getShapeGraphics());

                        mMapView.getGraphicsOverlays().remove(mapIndex1);
                        mMapView.getGraphicsOverlays().remove(lowImageGraphicsLayer2.getShapeGraphics());

                        mMapView.getGraphicsOverlays().add(mapIndex1,lowImageGraphicsLayer2.getShapeGraphics());
                        mMapView.getGraphicsOverlays().add(mapIndex2,lowImageGraphicsLayer1.getShapeGraphics());


                    }
                    //设置是否显示 图层

                }

            }
            lowImageGraphicsLayers.get(i).getShapeGraphics().setVisible(newImage.isSelect());
        }
    }

    private List<MyJSONObject> mapDatas;

    /**
     * 添加数据
     */
    private void addData() {
        mapDatas = null;
        try {
            mapDatas = TableTool.findByParentId(XZQYService.getCurrentCode());
        } catch (Exception e) {
            return;
        }

        for (int i = 0; i < mapDatas.size(); i++) {
            if (!mapDatas.get(i).getJsonobject().containsKey("geometry")) {
                mapDatas.remove(i);
                i--;
            }
        }
        Map<String, List<MyJSONObject>> map = JSONTool.getIDMap("table", mapDatas);
        //考虑图层没有数据的问题
        List<LowImage> layerStatus = MapService.getLayerStatus();
        Map<LowImage, List<MyJSONObject>> lowImageListMap = new LinkedHashMap<>();
        for (LowImage lowImage : layerStatus) {
            List<MyJSONObject> myJSONObjects = map.get(lowImage.getName());
            if (myJSONObjects == null) {
                lowImageListMap.put(lowImage, new ArrayList<>());
            } else {
                lowImageListMap.put(lowImage, myJSONObjects);
            }
        }
        addGraphicsOverlay(lowImageListMap);
    }

    /**
     * 添加底图
     *
     * @param mMapView
     */
    private void addLowMaps(MapView mMapView) {
        ArcGISMap map = new ArcGISMap();
        mMapView.setMap(map);
        List<LowImage> layers = LowMapManager.getVisableLowMaps();
        addLowMaps(mMapView, layers);

        // mMapView.setViewpointCenterAsync(map.getBasemap().getBaseLayers().get().getFullExtent().getCenter(), 50000);
    }

    private void addLowMaps(MapView mMapView, List<LowImage> lowImages) {
        ArcGISMap map = mMapView.getMap();


        map.getBasemap().getBaseLayers().clear();
        for (LowImage lowImage : lowImages) {
            if (lowImage.getType() != 100) {
                TianDiTuLayerInfo layerInfo = LayerInfoFactory.getLayerInfo(lowImage.getType()); //这个参数就是地图类型
                TileInfo info = layerInfo.getTileInfo();
                Envelope fullExtent = layerInfo.getFullExtent();
                TianDiTuLayer layer =
                        new TianDiTuLayer(info, fullExtent);
                layer.setLayerInfo(layerInfo);
                map.getBasemap().getBaseLayers().add(layer);
            } else {
                final TileCache tileCache = new TileCache(lowImage.getPath());
                tileCache.loadAsync();
                tileCache.addDoneLoadingListener(new Runnable() {
                    @Override
                    public void run() {
                        if (tileCache.getLoadStatus() == LoadStatus.LOADED) {
                            ArcGISTiledLayer arcGISTiledLayer = new ArcGISTiledLayer(tileCache);
                            arcGISTiledLayer.loadAsync();
                            if (arcGISTiledLayer.getLoadStatus() == LoadStatus.LOADED) {
                                mMapView.getMap().getOperationalLayers().add(arcGISTiledLayer);

                                //Point pt = arcGISTiledLayer.getFullExtent().getCenter();
                                //mMapView.setViewpointCenterAsync(pt, 500);
                                //mMapView.setViewpointCenterAsync(arcGISTiledLayer.getFullExtent().getCenter(), 50000);
                                //mMapView.getMap().setMinScale(200000);
                                //mMapView.getMap().setMaxScale(500);

                            } else {
                                ArcGISRuntimeException exception = arcGISTiledLayer.getLoadError();
                                System.out.println(exception.getMessage());
                            }
                        } else {
                            ArcGISRuntimeException exception = tileCache.getLoadError();
                            System.out.println(exception.getMessage());
                        }
                    }
                });
            }
        }
    }

    /**
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
        //图层管理
        view.findViewById(R.id.btn_layers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLaerys();

            }
        });

        //查询按钮
        view.findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText editText = MapFragment.this.view.findViewById(R.id.et_search);
                String serachKey = editText.getText().toString();
                if (!"".equals(serachKey.trim())) {
                    List<MyJSONObject> serachResult = new ArrayList<>();
                    for (MyJSONObject data : mapDatas) {
                        if (data.getJson().contains(serachKey)) {
                            serachResult.add(data);
                        }
                    }
                    addSerachDataMark(serachResult);
                }
            }
        });
    }

    /**
     * 查询出来的结果添加 标记， 方便再地图上分辨出来查询结果
     *
     * @param serachResult
     */
    private void addSerachDataMark(List<MyJSONObject> serachResult) {
        textGraphics.getGraphics().clear();
        TextSymbol textSymbol = new TextSymbol(10, "wo shi shui ", Color.RED, TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.MIDDLE);

        for (MyJSONObject myJSONObject : serachResult) {
            Geometry geometry = getGeometry(myJSONObject);
            if(geometry != null){
                Graphic graphic = new Graphic(geometry.getExtent().getCenter(), textSymbol);
                textGraphics.getGraphics().add(graphic);
            }
        }
        //TextSymbol(float size, String text, int color, TextSymbol.
        // com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment hAlign, TextSymbol.VerticalAlignment vAlign)
    }



    /**
     * 加载天地图
     *
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

    private static String GEOMETRY_KEY = "geometry";

    /**
     * 添加data数据到地图
     *
     * @param map
     */
    private void addGraphicsOverlay(Map<LowImage, List<MyJSONObject>> map) {
        Graphic pointGraphic;


        for (LowImage lowImage : map.keySet()) {
            //按表格名称创建 图层
            //点图层
            GraphicsOverlay pointGraphicOverlay = new GraphicsOverlay();
            SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.RED, 10);
            SimpleRenderer pointRenderer = new SimpleRenderer(pointSymbol);
            pointGraphicOverlay.setRenderer(pointRenderer);
            //mMapView.getGraphicsOverlays().add(pointGraphicOverlay);

            //面图层
            SimpleFillSymbol polygonSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.YELLOW, null);
            SimpleRenderer polygonRenderer = new SimpleRenderer(polygonSymbol);
            GraphicsOverlay polygonGraphicOverlay = new GraphicsOverlay();
            polygonGraphicOverlay.setRenderer(polygonRenderer);
            mMapView.getGraphicsOverlays().add(polygonGraphicOverlay);

            polygonGraphicOverlay.setVisible(lowImage.isSelect());


            GraphicsOverlay textGraphicOverlay = new GraphicsOverlay();
            SimpleRenderer textRenderer = new SimpleRenderer(pointSymbol);
            textGraphicOverlay.setRenderer(textRenderer);
            mMapView.getGraphicsOverlays().add(textGraphicOverlay);


            //添加到记录中，方便控制图层
            lowImageGraphicsLayers.add(new LowImageGraphicsLayer(lowImage, polygonGraphicOverlay,textGraphicOverlay));


            for (MyJSONObject geometryMyJson : map.get(lowImage)) {

                Geometry geometry = getGeometry(geometryMyJson);
                polygonGraphicOverlay.getGraphics().add(new Graphic(geometry));
            }
        }

    }

    /**
     *  转换为 图形数据
     * @param geometryMyJson
     * @return
     */
    private Geometry getGeometry(MyJSONObject geometryMyJson) {

        Geometry geometry = null;
        String jsonGeometry = geometryMyJson.getJsonobject().getString(GEOMETRY_KEY);
        String type = jsonGeometry.substring(0, jsonGeometry.indexOf("("));
        //原始坐标的point
        List<Point> srcpoints = getPoints(jsonGeometry, SpatialReference.create(4526));
        List<Point> descpoints = pointschangeSp(srcpoints, getSpatialReference());
        switch (type) {
            case "MULTIPOLYGON":
                PolygonBuilder polygonGeometry = new PolygonBuilder(getSpatialReference());
                for (Point descPoint : descpoints) {
                    polygonGeometry.addPoint(descPoint);
                }
                Point start = descpoints.get(0);
                Point end = descpoints.get(descpoints.size() - 1);
                //必须要首尾相连才能形成面
                //polygonGeometry.addPoint(descpoints.get(0));
                geometry = polygonGeometry.toGeometry();

                break;
            case "MULTIPOINT":
                for (Point srcPoint : descpoints) {
                    //本图坐标系的point；
                    geometry = GeometryEngine.project(srcPoint, getSpatialReference());

                }
                break;
        }
        return geometry;
    }

    private static SpatialReference spatialReference;

    private SpatialReference getSpatialReference() {
        if (spatialReference == null) {
            spatialReference = mMapView.getSpatialReference();
            if (spatialReference == null) {

                TianDiTuLayerInfo layerInfo = LayerInfoFactory.getLayerInfo(0); //这个参数就是地图类型
                TileInfo info = layerInfo.getTileInfo();
                Envelope fullExtent = layerInfo.getFullExtent();
                TianDiTuLayer layer =
                        new TianDiTuLayer(info, fullExtent);
                layer.setLayerInfo(layerInfo);
                mMapView.getMap().getBasemap().getBaseLayers().add(layer);
                new Runnable() {
                    @Override
                    public void run() {

                        mMapView.getMap().getBasemap().getBaseLayers().clear();
                    }
                }.run();
            }

        }
        return spatialReference;
    }

    /**
     * 点集合转换坐标系
     *
     * @param srcpoints        准备要转换的点
     * @param spatialReference 目标坐标系
     * @return
     */
    private List<Point> pointschangeSp(List<Point> srcpoints, SpatialReference spatialReference) {
        List<Point> descPoints = new ArrayList<>();
        for (Point srcPoint : srcpoints) {
            Point descPoint = (Point) GeometryEngine.project(srcPoint, getSpatialReference());

            descPoints.add(descPoint);
        }
        return descPoints;
    }

    private List<Point> getPoints(String jsonGeometry, SpatialReference spatialReference) {
        jsonGeometry = jsonGeometry.substring(jsonGeometry.indexOf("(")).replaceAll("\\(", "").replaceAll("\\)", "");
        List<Point> list = new ArrayList<>();
        String[] pointsStr = jsonGeometry.split(",");
        for (String pointStr : pointsStr) {
            String[] xyArray = pointStr.split(" ");
            Point point = new Point(Double.parseDouble(xyArray[0]), Double.parseDouble(xyArray[1]), 0, spatialReference);
            list.add(point);
        }
        return list;
    }


    private static String getCloseMapMark() {
        String CLOSE_MAP_MARK = "CLOSE_MAP_MARK";
        return ProjectService.getCurrentProjectDBName() + CLOSE_MAP_MARK;
    }

    /**
     * 地图关闭时的操作
     */
    public void close() {
        double scale = mMapView.getMapScale();
        Point point = mMapView.screenToLocation(new android.graphics.Point((int) mMapView.getPivotX(), (int) mMapView.getPivotY()));
        double xx = point.getX();
        double yy = point.getY();
        CloseLog log = new CloseLog();
        log.setScale(scale);
        log.setX(xx);
        log.setY(yy);
        try {
            String mark = getCloseMapMark();
            RedisTool.updateRedis(getCloseMapMark(), log);
        } catch (Exception e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MapResult.layer && resultCode == MapResult.layer) {
            List<LowImage> list = LowMapManager.getVisableLowMaps();
            addLowMaps(mMapView, list);
        }
    }

    class LowImageGraphicsLayer {
        private LowImage lowImage;
        private GraphicsOverlay shapeGraphics;
        private GraphicsOverlay textGraphics;

        public LowImageGraphicsLayer(LowImage lowImage, GraphicsOverlay shapeGraphics,GraphicsOverlay textGraphics) {
            this.lowImage = lowImage;
            this.shapeGraphics = shapeGraphics;
            this.textGraphics = textGraphics;

        }

        public LowImage getLowImage() {
            return lowImage;
        }

        public void setLowImage(LowImage lowImage) {
            this.lowImage = lowImage;
        }

        public GraphicsOverlay getShapeGraphics() {
            return shapeGraphics;
        }

        public void setShapeGraphics(GraphicsOverlay shapeGraphics) {
            this.shapeGraphics = shapeGraphics;
        }

        public GraphicsOverlay getTextGraphics() {
            return textGraphics;
        }

        public void setTextGraphics(GraphicsOverlay textGraphics) {
            this.textGraphics = textGraphics;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LowImageGraphicsLayer that = (LowImageGraphicsLayer) o;
            return Objects.equals(lowImage, that.lowImage);
        }

        @Override
        public int hashCode() {
            return Objects.hash(lowImage);
        }
    }
}
