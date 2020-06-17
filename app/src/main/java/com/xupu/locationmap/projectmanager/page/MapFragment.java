package com.xupu.locationmap.projectmanager.page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.esri.arcgisruntime.ArcGISRuntimeException;
import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PolygonBuilder;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.location.LocationDataSource;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.util.ListenableList;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.dialog.DialogCallback;
import com.xupu.locationmap.common.dialog.RightDialogFragment;
import com.xupu.locationmap.common.po.HistorySearchText;
import com.xupu.locationmap.common.po.ViewHolderCallback;
import com.xupu.locationmap.common.tdt.LayerInfoFactory;
import com.xupu.locationmap.common.tdt.TianDiTuLayer;
import com.xupu.locationmap.common.tdt.TianDiTuLayerInfo;
import com.xupu.locationmap.common.tdt.TianDiTuLayerTypes;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.JSONTool;
import com.xupu.locationmap.common.tools.MyLocationUtil;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.ReflectTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.common.tools.Utils;
import com.xupu.locationmap.exceptionmanager.MapException;
import com.xupu.locationmap.projectmanager.po.CloseLog;
import com.xupu.locationmap.projectmanager.po.LowImage;
import com.xupu.locationmap.projectmanager.po.MapResult;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.service.MapService;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.TableService;
import com.xupu.locationmap.projectmanager.service.TaskService;
import com.xupu.locationmap.projectmanager.service.XZQYService;
import com.xupu.locationmap.projectmanager.service.ZTService;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.PositionField;
import com.xupu.locationmap.projectmanager.view.TableDataCustom;
import com.xupu.locationmap.projectmanager.view.TableDataCustom_TableName;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;


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

    /**
     * 查询出来的文字大小
     */
    private static float SerachShpMarkTextSize = 40;
    /**
     * 图层文字大小
     */
    private static float LaeryTextSize = 40;
    /**
     * 表格 要显示 的字段
     */
    Map<String, TableDataCustom_TableName> tableResultMap;

    List<LowImageGraphicsLayer> lowImageGraphicsLayers = new ArrayList<>();
    View view;


    private EditText serachEditText;

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
        try {
            tableResultMap = ReflectTool.getIDMap("getTableName", TableService.getTableAll());
        } catch (MapException e) {
            e.printStackTrace();
        }

    }


    private MapView mMapView;

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        initButton();
        mMapView = view.findViewById(R.id.mv_tian_di_tu);
        try {
            addLowMaps(mMapView);
        } catch (Exception e) {
            ProjectService.toProjectPage();
            return;
        }
        mMapView.setAttributionTextVisible(false); //隐藏Esri logo


        addData();
        addMarkGraphics();
        try {
            CloseLog log = RedisTool.findRedis(getCloseMapMark(), CloseLog.class);
            Point lastClosePoint = new Point(log.getX(), log.getY());
            mMapView.setViewpointCenterAsync(lastClosePoint, log.getScale());
        } catch (Exception e) {

        }

        mMapView.setOnTouchListener(new MapTouch(getActivity(), mMapView));


    }

    private GraphicsOverlay markGrapics;

    private void addMarkGraphics() {


        SimpleFillSymbol polygonSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.RED, null);
        SimpleRenderer polygonRenderer = new SimpleRenderer(polygonSymbol);
        markGrapics = new GraphicsOverlay();
        markGrapics.setRenderer(polygonRenderer);
        mMapView.getGraphicsOverlays().add(markGrapics);
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
     * 所放止图层
     *
     * @param lowImage
     */
    private void scaleMapLayer(LowImage lowImage) {
        for (LowImageGraphicsLayer lowImageGraphicsLayer : lowImageGraphicsLayers) {
            if (lowImageGraphicsLayer.lowImage.equals(lowImage)) {
                GraphicsOverlay graphicsOverlay = lowImageGraphicsLayer.getShapeGraphics();
                ListenableList<Graphic> graphics = graphicsOverlay.getGraphics();
                Envelope extent = graphicsOverlay.getExtent();
                if (extent != null && !extent.isEmpty()) {
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
        for (LowImageGraphicsLayer lowImageGraphicsLayer : lowImageGraphicsLayers) {
            if (lowImageGraphicsLayer.getLowImage().equals(lowImage)) {

                List<MyJSONObject> myJSONObjects = new ArrayList<>();
                myJSONObjects.addAll(lowImageGraphicsLayer.getLayerdatas());
                showSearchResult(myJSONObjects);
                addSerachShpMark(myJSONObjects);
                break;
            }
        }

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

                        Tool.swap(lowImageGraphicsLayers, i, j);

                        //地图 list 交换位置
                        int mapIndex1 = mMapView.getGraphicsOverlays().indexOf(lowImageGraphicsLayer1.getShapeGraphics());
                        int mapIndex2 = mMapView.getGraphicsOverlays().indexOf(lowImageGraphicsLayer2.getShapeGraphics());

                        mMapView.getGraphicsOverlays().remove(mapIndex1);
                        mMapView.getGraphicsOverlays().remove(lowImageGraphicsLayer2.getShapeGraphics());

                        mMapView.getGraphicsOverlays().add(mapIndex1, lowImageGraphicsLayer2.getShapeGraphics());
                        mMapView.getGraphicsOverlays().add(mapIndex2, lowImageGraphicsLayer1.getShapeGraphics());


                    }
                    //设置是否显示 图层

                }

            }
            boolean show = newImage.isSelect();
            lowImageGraphicsLayers.get(i).getShapeGraphics().setVisible(show);
            lowImageGraphicsLayers.get(i).getLowImage().setSelect(show);
            lowImageGraphicsLayers.get(i).getTextGraphics().setVisible(show);
        }
    }


    /**
     * 添加数据
     */
    private void addData() {
        List<MyJSONObject> mapDatas = null;
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
        serachEditText = view.findViewById(R.id.et_search);
        searchHistoryText(serachEditText);
        //查询按钮
        view.findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                clearAll();
                EditText editText = MapFragment.this.view.findViewById(R.id.et_search);
                String serachKey = editText.getText().toString();
                searchShp(serachKey);
                //setHistorySearchTextRecyclerView(View.GONE);
            }
        });
        view.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearMark();
                clearSerachText();
                setHistorySearchTextRecyclerView(View.GONE);
            }
        });
    }

    private static Timer timer = new Timer();
    private static String searchText;

    /**
     * 查询历史记录
     *
     * @param searchEdit
     */
    private void searchHistoryText(EditText searchEdit) {
        searchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText(searchEdit.getText().toString());
            }
        });
        searchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchText(searchEdit.getText().toString());
            }
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable s) {
                String newstr = s.toString();
                if (searchText == null && newstr.equals("")) {
                    return;
                }
                if (searchText == null || !searchText.equals(newstr)) {
                    //按钮清空时发生
                    if (!newstr.equals("")) {
                        searchText = newstr;
                        //每半秒运行一次
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                searchText(searchText);
                                timer.cancel();
                                timer = new Timer();
                            }
                        };
                        timer.schedule(task, 500);
                    }
                }
                searchText = newstr;
            }
        });
    }

    /**
     * 查询历史记录并显示
     *
     * @param searchText
     */
    private void searchText(String searchText) {
        List<HistorySearchText> historySearchTexts = HistorySearchText.searchTexts(searchText);
        showHistorySearchText(historySearchTexts);
        setHistorySearchTextRecyclerView(View.VISIBLE);
    }

    MyItemRecyclerViewAdapter historySearchTextAdapter;
    MyItemRecyclerViewAdapter searchResultAdapter;

    private final static String my_tablename = "my_tablename";
    private final static String my_title = "my_title";

    /**
     * 显示查询结果
     *
     * @param serachResult
     */
    private void showSearchResult(List<MyJSONObject> serachResult) {

        if (serachResult.size() == 0) {
            AndroidTool.showAnsyTost("没有查询结果", 0);
            //隐藏查询结果
            setShowSearchResultRecyclerView(View.GONE);
        }

        for (MyJSONObject myJSONObject : serachResult) {


            replaceSearchObj(myJSONObject);
        }

        if (searchResultAdapter == null) {
            List<FieldCustom> fs = new ArrayList<>();

            fs.add(new FieldCustom(R.id.title, "my_title"));
            fs.add(new FieldCustom(R.id.info, "my_tablename"));
            fs.add(new PositionField(R.id.index, ""));
            //定位
            fs.add(new ViewFieldCustom(R.id.item_location) {
                @Override
                public void OnClick(View view, MyJSONObject myJSONObject) {
                    scaleData(myJSONObject);
                }
            });
            //详情
            fs.add(new ViewFieldCustom(R.id.item_info) {
                @Override
                public void OnClick(View view, MyJSONObject myJSONObject) {
                    TableItemListFragment.toInfoPage(MapFragment.this, myJSONObject);
                }
            });

            //多媒体
            fs.add(new ViewFieldCustom(R.id.item_media) {
                @Override
                public void OnClick(View view, MyJSONObject myJSONObject) {
                    TableItemListFragment.toMediaPage(getActivity(), myJSONObject);
                }
            });
            TableDataCustom tableDataCustom = new TableDataCustom(R.layout.map_item_searchresult, fs, serachResult).setEdit(false);
            RecyclerView recyclerView = view.findViewById(R.id.recy_search_result);
            searchResultAdapter = new MyItemRecyclerViewAdapter(tableDataCustom, recyclerView);
            searchResultAdapter.setAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(searchResultAdapter);

            searchResultAdapter.setLoadViewCallback(new ViewHolderCallback() {
                @Override
                public void call(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
                    //检查多媒体是否有任务
                    List<MyJSONObject> tasks = TaskService.findTaskByTableid(searchResultAdapter.getItem(position).getTableid());
                    View mediaView = holder.mView.findViewById(R.id.item_media);
                    if (tasks.size() > 0) {
                        mediaView.setVisibility(View.VISIBLE);
                    } else {
                        mediaView.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            setShowSearchResultRecyclerView(View.VISIBLE);
            searchResultAdapter.setDatas(serachResult);
        }

    }

    /**
     * 更改 为搜索结果
     *
     * @param shpdata
     */
    private void replaceSearchObj(MyJSONObject shpdata) {
        String tablename = shpdata.getTablename();
        TableDataCustom_TableName tableDataCustom_tableName = tableResultMap.get(shpdata.getTablename());
        String title = shpdata.getJsonobject().getString(tableDataCustom_tableName.getFieldCustoms().get(0).getAttribute());
        shpdata.getJsonobject().put(my_tablename, tablename);
        shpdata.getJsonobject().put(my_title, title);
    }

    /**
     * 显示历史窗口
     *
     * @param historySearchTexts
     */
    private void showHistorySearchText(List<HistorySearchText> historySearchTexts) {
        List<MyJSONObject> historys = JSONTool.toMyJSONObject(historySearchTexts);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (historySearchTextAdapter == null) {
                    List<FieldCustom> fs = new ArrayList<>();
                    fs.add(new FieldCustom(R.id.tv_searchhistroytext, HistorySearchText.JSON_TEXT));
                    //item 点击事件
                    fs.add(new ViewFieldCustom(R.id.item) {
                        @Override
                        public void OnClick(View view, MyJSONObject myJSONObject) {
                            String text = JSONTool.getString(myJSONObject, HistorySearchText.JSON_TEXT);
                            serachEditText.setText(text);
                            serachEditText.setSelection(text.length());
                            setHistorySearchTextRecyclerView(View.GONE);
                        }
                    });
                    TableDataCustom tableDataCustom = new TableDataCustom(R.layout.map_item_searhhistorytext, fs, historys).setEdit(true);
                    RecyclerView recyclerView = view.findViewById(R.id.recy_search_history);
                    historySearchTextAdapter = new MyItemRecyclerViewAdapter(tableDataCustom, recyclerView);
                    historySearchTextAdapter.setAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(historySearchTextAdapter);
                } else {
                    historySearchTextAdapter.setDatas(historys);
                }
            }
        });
    }

    /**
     * 清除搜索框文字
     */
    private void clearSerachText() {
        EditText editText = MapFragment.this.view.findViewById(R.id.et_search);
        editText.setText("");
    }

    /**
     * 查询地块
     *
     * @param serachKey
     */
    private void searchShp(String serachKey) {
        clearMark();
        if (!"".equals(serachKey.trim())) {

            List<MyJSONObject> serachResult = new ArrayList<>();
            for (LowImageGraphicsLayer lowImageGraphicsLayer : lowImageGraphicsLayers) {
                if (lowImageGraphicsLayer.getLowImage().isSelect()) {
                    List<MyJSONObject> datas = lowImageGraphicsLayer.getLayerdatas();
                    for (MyJSONObject data : datas) {
                        String json = data.getJson();
                        int start = json.indexOf("geometry");
                        int end = json.indexOf(")\"", start + 5);
                        //除去坐标
                        String dataJson = json.replace(json.substring(start, end + 1), "");
                        if (dataJson.contains(serachKey)) {
                            serachResult.add(data);
                        }
                    }
                }
            }

            //显示查询结果
            showSearchResult(serachResult);

            addSerachShpMark(serachResult);
            //保存查询的记录
            HistorySearchText.saveHistory(searchText, serachResult.size());


        }
    }

    /**
     * 设置是否显示 查询结果床狗
     *
     * @param visbility
     */
    private void setShowSearchResultRecyclerView(int visbility) {
        if (searchResultAdapter != null) {
            searchResultAdapter.getRecyclerView().setVisibility(visbility);
        }

    }


    /**
     * 设置历史查询文本的状态
     *
     * @param visibility
     */
    private void setHistorySearchTextRecyclerView(int visibility) {
        if (historySearchTextAdapter != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    historySearchTextAdapter.getRecyclerView().setVisibility(visibility);
                }
            });
        }

    }

    /**
     * 清除 标记图层
     */
    private void clearMark() {
        markGrapics.getGraphics().clear();
    }


    /**
     * 查询出来的结果添加 标记， 方便再地图上分辨出来查询结果
     *
     * @param serachResult
     */
    private void addSerachShpMark(List<MyJSONObject> serachResult) {

        for (int i = 0; i < serachResult.size(); i++) {
            addSerachShpMark(serachResult.get(i), i + 1 + "");
        }
    }

    private void addSerachShpMark(MyJSONObject serachResult, String text) {


        Geometry geometry = getGeometry(serachResult);
        if (geometry != null) {
            Point core = geometry.getExtent().getCenter();
            addPictureGraphic(markGrapics, core, text, SerachShpMarkTextSize, R.drawable.new_default);
        }
    }

    private void addMapLayerText(GraphicsOverlay graphicsOverlay, MyJSONObject data) {
        Geometry geometry = getGeometry(data);
        addMapLayerText(graphicsOverlay, data, geometry);
    }

    /**
     * 添加地图标注
     *
     * @param graphicsOverlay 图层
     * @param data            数据
     */
    private void addMapLayerText(GraphicsOverlay graphicsOverlay, MyJSONObject data, Geometry geometry) {

        String text = data.getJsonobject().getString(tableResultMap.get(data.getTablename()).getFieldCustoms().get(0).getAttribute());
        Paint mPaint = new TextPaint();
        mPaint.setTextSize(40);
        mPaint.setAntiAlias(true);
        Rect mRect = new Rect();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.getTextBounds(text, 0, text.length(), mRect);
        float strwid = mRect.width() * 1.2f;
        float strhei = mRect.height() * 1.5f;

        BitmapDrawable icon = (BitmapDrawable) getResources().getDrawable(R.drawable.white);
        Bitmap bitmap = icon.getBitmap();
        bitmap = AndroidTool.getNewBitmap(bitmap, (int) strwid, (int) strhei);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, (bitmap.getWidth() / 2), (bitmap.getHeight() / 1.3f), mPaint);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(bitmapDrawable);
        Point center = geometry.getExtent().getCenter();
        center = new Point(center.getX(), center.getY() - 30);
        Graphic graphic = new Graphic(center, pictureMarkerSymbol);
        graphicsOverlay.getGraphics().add(graphic);
    }


    /**
     * 添加图片
     *
     * @param layer       图层
     * @param geometry    图形
     * @param text        文字
     * @param textsize    文字大小
     * @param drawablerid 图片 id
     */
    private void addPictureGraphic(GraphicsOverlay layer, Geometry geometry, String text, float textsize, int drawablerid) {
        Bitmap bitmap = AndroidTool.setTextToImg(drawablerid, text, textsize, Color.WHITE);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(bitmapDrawable);
        Graphic graphic = new Graphic(geometry, pictureMarkerSymbol);
        layer.getGraphics().add(graphic);

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


            GraphicsOverlay textGraphicOverlay = new GraphicsOverlay();
            SimpleRenderer textRenderer = new SimpleRenderer(pointSymbol);
            textGraphicOverlay.setRenderer(textRenderer);

            mMapView.getGraphicsOverlays().add(textGraphicOverlay);

            polygonGraphicOverlay.setVisible(lowImage.isSelect());
            textGraphicOverlay.setVisible(lowImage.isSelect());

            //添加到记录中，方便控制图层
            lowImageGraphicsLayers.add(new LowImageGraphicsLayer(lowImage, polygonGraphicOverlay, textGraphicOverlay, map.get(lowImage)));


            for (MyJSONObject geometryMyJson : map.get(lowImage)) {
                Geometry geometry = getGeometry(geometryMyJson);
                polygonGraphicOverlay.getGraphics().add(new Graphic(geometry));
                addMapLayerText(textGraphicOverlay, geometryMyJson, geometry);
            }
        }

    }


    /**
     * 转换为 图形数据
     *
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
            //记录图层顺序记录
            String mark = getCloseMapMark();
            RedisTool.updateRedis(getCloseMapMark(), log);
            //保存搜索的文本
            HistorySearchText.saveToRedis();
        } catch (Exception e) {
            Log.e("yb", "出错了，你找啊！！！");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MapResult.layer && resultCode == MapResult.layer) {
            List<LowImage> list = LowMapManager.getVisableLowMaps();
            addLowMaps(mMapView, list);
        } else if (resultCode == MapResult.datalocation) {
            MyJSONObject shpdata = (MyJSONObject) data.getSerializableExtra("data");
            scaleData(shpdata);
            addSerachShpMark(shpdata,"");
        }else if (resultCode == MapResult.datachange){
            AndroidTool.showAnsyTost("数据被改变了",0);
            //刷新数据
            reflushPage();

        }
        else {
            if (data == null) {
                return;
            }
            //对于地块的操作
            MyJSONObject obj = null;
            if (requestCode == 1 || requestCode == 2) {
                obj = (MyJSONObject) data.getSerializableExtra("obj");
            }
            switch (requestCode) {
                case 1:
                    //详情页面返回
                    switch (resultCode) {
                        case TableTool.STATE_UPDATE:
                            //修改
                            updateItem(obj);
                            break;
                        case TableTool.STATE_DELETE:
                            //删除
                            deleteItem(obj);
                            break;
                        case TableTool.STATE_INSERT:
                            //增加
                            break;
                    }
                    break;
                case 2:
                    switch (resultCode) {
                        case TableTool.STATE_UPDATE:
                            //这是增加，用的是修改按钮而已
                            //TableTool.insert(obj, TableTool.STATE_INSERT);
                            //addItem(newobj);
                            //checkHasDataAndShow();
                            break;
                    }
                    break;
            }
        }

    }

    /**
     * 刷新页面
     */
    private void reflushPage() {

    }

    /**
     * 修改后 返回 obj
     *
     * @param obj
     */
    public void updateItem(MyJSONObject obj) {

        /**
         * 更改图层数据
         */
        for (LowImageGraphicsLayer lowImageGraphicsLayer : lowImageGraphicsLayers) {
            if (obj.getTablename().equals(lowImageGraphicsLayer.lowImage.getName())) {
                List<MyJSONObject> layerdatas = lowImageGraphicsLayer.getLayerdatas();
                for (int i = 0; i < layerdatas.size(); i++) {
                    if (layerdatas.get(i).getId().equals(obj.getId())) {
                        layerdatas.set(i, obj);
                        lowImageGraphicsLayer.getTextGraphics().getGraphics().remove(i);
                        addMapLayerText(lowImageGraphicsLayer.getTextGraphics(), obj);
                        break;
                    }
                }
            }
        }
        replaceSearchObj(obj);
        searchResultAdapter.update(obj);
    }

    /**
     * 删除后 返回的 obj
     *
     * @param obj
     */
    public void deleteItem(MyJSONObject obj) {

        /**
         * 删除标注
         */
        for (LowImageGraphicsLayer lowImageGraphicsLayer : lowImageGraphicsLayers) {
            if (obj.getTablename().equals(lowImageGraphicsLayer.lowImage.getName())) {
                List<MyJSONObject> layerdatas = lowImageGraphicsLayer.getLayerdatas();
                for (int i = 0; i < layerdatas.size(); i++) {
                    if (layerdatas.get(i).getId().equals(obj.getId())) {
                        layerdatas.remove(i);
                        lowImageGraphicsLayer.getTextGraphics().getGraphics().remove(i);
                        lowImageGraphicsLayer.getShapeGraphics().getGraphics().remove(i);
                        break;
                    }
                }
            }
        }

        //1、删除搜索的结果
        searchResultAdapter.remove(obj);
    }

    /**
     * 新增后 返回的 o
     *
     * @param obj
     */
    public void addItem(MyJSONObject obj) {

    }


    public void scaleData(MyJSONObject shpdata) {
        Geometry geometry = getGeometry(shpdata);
        mMapView.setViewpointGeometryAsync(geometry);
    }

    class LowImageGraphicsLayer {
        private LowImage lowImage;
        private GraphicsOverlay shapeGraphics;
        private GraphicsOverlay textGraphics;
        private List<MyJSONObject> layerdatas;

        public LowImageGraphicsLayer(LowImage lowImage, GraphicsOverlay shapeGraphics, GraphicsOverlay textGraphics, List<MyJSONObject> layerdatas) {
            this.lowImage = lowImage;
            this.shapeGraphics = shapeGraphics;
            this.textGraphics = textGraphics;
            this.layerdatas = layerdatas;
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

        public List<MyJSONObject> getLayerdatas() {
            return layerdatas;
        }

        public void setLayerdatas(List<MyJSONObject> layerdatas) {
            this.layerdatas = layerdatas;
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

    private class MapTouch extends DefaultMapViewOnTouchListener {

        public MapTouch(Context context, MapView mapView) {
            super(context, mapView);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            boolean result = super.onSingleTapUp(e);
            clearAll();
            return result;
        }
    }

    public void clearAll() {
        setHistorySearchTextRecyclerView(View.GONE);
        setShowSearchResultRecyclerView(View.GONE);
        clearMark();
    }
}
