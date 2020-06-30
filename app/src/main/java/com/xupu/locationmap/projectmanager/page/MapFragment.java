package com.xupu.locationmap.projectmanager.page;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.graphics.drawable.Drawable;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.esri.arcgisruntime.ArcGISRuntimeException;
import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.ImmutablePart;
import com.esri.arcgisruntime.geometry.ImmutablePartCollection;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.BackgroundGrid;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.util.ListenableList;
import com.xupu.locationmap.R;
import com.xupu.locationmap.arcruntime.WKT;
import com.xupu.locationmap.common.dialog.DialogCallback;
import com.xupu.locationmap.common.dialog.RightDialogFragment;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.po.HistorySearchText;
import com.xupu.locationmap.common.po.ViewHolderCallback;
import com.xupu.locationmap.common.tdt.LayerInfoFactory;
import com.xupu.locationmap.common.tdt.TianDiTuLayer;
import com.xupu.locationmap.common.tdt.TianDiTuLayerInfo;
import com.xupu.locationmap.common.tdt.TianDiTuLayerTypes;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.JSONTool;
import com.xupu.locationmap.common.tools.MediaTool;
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
import com.xupu.locationmap.projectmanager.service.MediaService;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.TableService;
import com.xupu.locationmap.projectmanager.service.TaskService;
import com.xupu.locationmap.projectmanager.service.XZQYService;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.ItemDataCustom;
import com.xupu.locationmap.projectmanager.view.PositionField;
import com.xupu.locationmap.projectmanager.view.TableDataCustom;
import com.xupu.locationmap.projectmanager.view.TableDataCustom_TableName;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;


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
     * 设置未当前图层，更换地图页面标题
     */
    public final static int CURRENT_LAYER_CHANGE = 5;

    /**
     * 查询出来的文字大小
     */
    private static float SerachShpMarkTextSize = 40;
    /**
     * 图层文字大小
     */
    private static float LaeryTextSize = 40;
    /**
     * 随手拍
     */
    public final static int NVER_TIME = 50;
    /**
     * 表格 要显示 的字段
     */
    Map<String, TableDataCustom_TableName> tableResultMap;

    List<LowImageGraphicsLayer> lowImageGraphicsLayers = new ArrayList<>();

    private View navView;
    private View backView;
    private View nverTimeView;
    private View rl_xy;
    View view;


    private EditText serachEditText;
    public TextView tv_title;
    private static LowImage currentLayer;

    static {
        currentLayer = MapLayerDialog.getCurrentLayer();
    }

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
        init();
        return view;
    }

    private void init() {
        mMapView = view.findViewById(R.id.mv_tian_di_tu);

        mMapView.setAttributionTextVisible(false); //隐藏Esri logo
        //mMapView.setBackgroundColor(Color.WHITE);
        // mMapView.setBackground(AndroidTool.getMainActivity().getResources().getDrawable(R.drawable.white1));
        mMapView.setBackgroundGrid(new BackgroundGrid(0xffffff, 0xffffff, 1.0f, 100.0f));




        MyJSONObject myJSONObject = new MyJSONObject();
        myJSONObject.setJsonobject(new JSONObject());
        List<FieldCustom> filedCustoms = new ArrayList<>();
        /*导航栏*/
        filedCustoms.add(new ViewFieldCustom(R.id.nav_tool) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                NavDialog navDialog = new NavDialog();
                navDialog.show(getActivity().getSupportFragmentManager(), NavDialog.class.getSimpleName());
            }
        });
        /*弹出 查询按钮*/
        filedCustoms.add(new ViewFieldCustom(R.id.title_search) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                changeSate(R.id.state2);
            }
        });

        /*绘制 按钮*/
        filedCustoms.add(new ViewFieldCustom(R.id.btn_state4) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                if(haseProject()){
                    changeSate(R.id.state4_1);
                }

            }
        });
        /*绘制 时用的返回按钮 按钮*/
        filedCustoms.add(new ViewFieldCustom(R.id.back) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                changeSate(R.id.state1);
            }
        }.setVisable(View.GONE));


        ItemDataCustom itemDataCustom = new ItemDataCustom(null, myJSONObject, filedCustoms);
        AndroidTool.setView(view, itemDataCustom, true, 0);

        initView();
    }

    /**
     * 改变页面状态
     *
     * @param stateRid
     */
    public void changeSate(Integer stateRid) {

        CURRENT_STATE = stateRid;
        View currentState = MapFragment.this.view.findViewById(stateRid);
        if (currentState.getVisibility() == View.VISIBLE) {
            return;
        }
        /*状态切换时，清空图上标记*/
        clearMark();

        View state1 = MapFragment.this.view.findViewById(R.id.state1);
        View state2 = MapFragment.this.view.findViewById(R.id.state2);
        /*右侧的绘制点击按钮*/
        /*绘制 线 面 两个按钮*/
        View state4_1 = MapFragment.this.view.findViewById(R.id.state4_1);
        /*下面绘制工具*/
        View state4_2 = MapFragment.this.view.findViewById(R.id.state4_2);
        /*绘制信息  周长 、 面积*/
        View state4_3 = MapFragment.this.view.findViewById(R.id.state4_3);


        state1.setVisibility(View.GONE);
        state2.setVisibility(View.GONE);
        state4_1.setVisibility(View.GONE);
        state4_2.setVisibility(View.GONE);
        state4_3.setVisibility(View.GONE);
        switch (stateRid) {
            case R.id.state1:
                state1.setVisibility(View.VISIBLE);
                state1.setAnimation(AndroidTool.moveToViewLocation(0, 0, -1, 0));
                break;
            case R.id.state2:
                state2.setVisibility(View.VISIBLE);
                state2.setAnimation(AndroidTool.moveToViewLocation(0, 0, -1, 0));
                break;
            case R.id.state4_1:
                state4_1.setVisibility(View.VISIBLE);
                state4_1.setAnimation(AndroidTool.moveToViewLocation(0, 0, -1, 0f));
                state4_2.setVisibility(View.VISIBLE);
                state4_2.setAnimation(AndroidTool.moveToViewLocation(0, 0, 1, 0));
                state4_3.setVisibility(View.VISIBLE);
                state4_3.setAnimation(AndroidTool.moveToViewLocation(-1, 0, 0, 0));

                break;
        }
        if (stateRid == R.id.state4_1) {
            //隐藏导航栏、显示返回按钮
            navView.setVisibility(View.GONE);
            backView.setVisibility(View.VISIBLE);
            nverTimeView.setVisibility(View.GONE);
            //清除搜索内容
            //clearSerachTextResutl();
            //clearSerachText();
            clearAll();
            rl_xy.setVisibility(View.GONE);
            //隐藏 下面xy描述

        } else {
            navView.setVisibility(View.VISIBLE);
            backView.setVisibility(View.GONE);
            if (nverTimeView.getVisibility() == View.GONE) {
                nverTimeView.setVisibility(View.VISIBLE);
                nverTimeView.setAnimation(AndroidTool.moveToViewLocation(0, 0, 1, 0));
            }
            if (rl_xy.getVisibility() == View.GONE) {
                rl_xy.setVisibility(View.VISIBLE);
                rl_xy.setAnimation(AndroidTool.moveToViewLocation(0, 0, 1, 0));
            }
        }

        /**
         * 如果不是状态 2 隐藏搜索结果
         */
        if (stateRid != R.id.state2) {
            clearSerachTextResutl();

        }
    }

    /**
     * 隐藏 搜索的历史记录
     */
    private void clearSerachTextResutl() {
        if (historySearchTextRecy != null) {
            historySearchTextRecy.setVisibility(View.GONE);
        }
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




        //如果时第一次启动软件会没有值
        try {
            CloseLog log = RedisTool.findRedis(getCloseMapMark(), CloseLog.class);
            Point lastClosePoint = new Point(log.getX(), log.getY());
            mMapView.setViewpointCenterAsync(lastClosePoint, log.getScale());
        } catch (Exception e) {

        }

        mMapView.setOnTouchListener(new MapTouch(getActivity(), mMapView));
        autoComputeXY();

        navView = view.findViewById(R.id.nav_tool);
        backView = view.findViewById(R.id.back);
        nverTimeView = view.findViewById(R.id.nevertime);
        rl_xy = view.findViewById(R.id.rl_xy);
        tv_title = view.findViewById(R.id.tv_title);
        setTitle(currentLayer);


        try {
            addLowMaps(mMapView);
        } catch (Exception e) {
            ProjectService.toProjectPage();
            return;
        }
        try {
            addData();
            addMarkGraphics();
        } catch (Exception e) {
            ProjectService.toProjectPage();
            AndroidTool.showAnsyTost("项目有问题，请删除后下载！！！", 1);
            e.printStackTrace();
            return;
        }
    }

    /**
     * 设置标题  就是当前图层
     *
     * @param currentLayer
     */
    private void setTitle(LowImage currentLayer) {
        if (currentLayer != null) {
            tv_title.setText(currentLayer.getName());
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
                //先检查时候又项目
                if(haseProject()){
                    showLaerys();
                }


            }
        });
        /**
         * 轨迹导航
         */
        view.findViewById(R.id.btn_trajectory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTrajectory();
            }
        });

        serachEditText = view.findViewById(R.id.et_search);
        searchHistoryText(serachEditText);
        //查询按钮
        view.findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                searchShp();

                //setHistorySearchTextRecyclerView(View.GONE);
            }
        });


        view.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果没有文字,改为状态1
                if (serachEditText.getText().toString().equals("")) {
                    changeSate(R.id.state1);
                }
                clearMark();
                clearSerachText();
                setHistorySearchTextRecyclerView(View.GONE);
            }
        });
        /**
         * 随手拍照
         */
        view.findViewById(R.id.nevertime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = MediaService.getNverTimePhoto();
                MediaTool.photo(MapFragment.this, NVER_TIME, path);
            }
        });
        /**
         * 随手拍照 被长按，跳转到 随手拍所有的图片
         */
        view.findViewById(R.id.nevertime).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getActivity(), NverTimeActivty.class);
                MapFragment.this.startActivity(intent);
                return false;
            }
        });
        btnDrawLine = view.findViewById(R.id.btn_draw_line);
        btnDrawLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDrawState(btnDrawLine);
            }
        });
        //初始状态绘制线
        currentDrawState = btnDrawLine;
        tv_draw = view.findViewById(R.id.tv_draw);
        tv_draw.setText(lineStr);
        tv_unit = view.findViewById(R.id.tv_unit);
        tv_unit.setText(lineUnit);
        btnDrawPlygon = view.findViewById(R.id.btn_draw_plygon);
        btnDrawPlygon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                changeDrawState(btnDrawPlygon);
            }
        });
        Resources resources = getResources();
        bg_white_round_btn_left_select = resources.getDrawable(R.drawable.bg_white_round_btn_left_select);
        bg_white_round_btn_left = resources.getDrawable(R.drawable.bg_white_round_btn_left);
        bg_white_round_btn_right_select = resources.getDrawable(R.drawable.bg_white_round_btn_right_select);
        bg_white_round_btn_right = resources.getDrawable(R.drawable.bg_white_round_btn_right);


    }

    /**
     * 检查是否有项目
     * @return
     */
    private boolean haseProject() {
        if(ProjectService.getCurrentSugProject() == null){
            AndroidTool.showAnsyTost("请先设置当前项目",1);
            return  false;
        }
        return true;
    }

    private static String lineStr = "绘制线";
    private static String lineUnit = "长度：";
    private static String plygonStr = "绘制面";
    private static String plygonUnit = "面积：";
    private TextView tv_draw;
    private TextView tv_unit;
    private Drawable bg_white_round_btn_left_select;
    private Drawable bg_white_round_btn_left;
    private Drawable bg_white_round_btn_right_select;
    private Drawable bg_white_round_btn_right;

    private View btnDrawLine;
    private View btnDrawPlygon;
    /**
     * 画 线 或者 面的状态
     */
    private View currentDrawState;

    private void changeDrawState(View drawState) {
        if (currentDrawState != drawState) {
            //更改为 当前状态图
            currentDrawState = drawState;
            if (currentDrawState.getId() == R.id.btn_draw_line) {
                btnDrawLine.setBackground(bg_white_round_btn_left_select);
                btnDrawPlygon.setBackground(bg_white_round_btn_right);
                tv_draw.setText(lineStr);
                tv_unit.setText(lineUnit);
            } else if (currentDrawState.getId() == R.id.btn_draw_plygon) {
                btnDrawLine.setBackground(bg_white_round_btn_left);
                btnDrawPlygon.setBackground(bg_white_round_btn_right_select);
                tv_draw.setText(plygonStr);
                tv_unit.setText(plygonUnit);
            }
        }
    }

    private GraphicsOverlay markGrapics= new GraphicsOverlay();

    private void addMarkGraphics() {


        SimpleFillSymbol polygonSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.RED, null);
        SimpleRenderer polygonRenderer = new SimpleRenderer(polygonSymbol);
        markGrapics = new GraphicsOverlay();
        markGrapics.setRenderer(polygonRenderer);
        mMapView.getGraphicsOverlays().add(markGrapics);
    }

    /**
     * 显示 轨迹 dialog
     */
    private void showTrajectory() {
        Integer[] widthAndHeight = Utils.getWidthAndHeight();
        MapTrajectoryDialog mapTrajectoryDialog = new MapTrajectoryDialog(widthAndHeight[0] / 3 * 2, widthAndHeight[1]);
        mapTrajectoryDialog.show(getActivity().getSupportFragmentManager(), MapTrajectoryDialog.class.getSimpleName());

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
                    case CURRENT_LAYER_CHANGE:
                        lowImage = (LowImage) obj;
                        setTitle(lowImage);
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
    private boolean scaleMapLayer(LowImage lowImage) {
        for (LowImageGraphicsLayer lowImageGraphicsLayer : lowImageGraphicsLayers) {
            if (lowImageGraphicsLayer.lowImage.equals(lowImage) && lowImage.isIsload()) {
                GraphicsOverlay graphicsOverlay = lowImageGraphicsLayer.getShapeGraphics();
                ListenableList<Graphic> graphics = graphicsOverlay.getGraphics();
                Envelope extent = graphicsOverlay.getExtent();
                if (extent != null && !extent.isEmpty()) {
                    mMapView.setViewpointGeometryAsync(extent);
                    return true;
                } else {
                    //AndroidTool.showAnsyTost(lowImage.getName() + " 没有数据,或者没有开启图层", 1);
                }
            }
        }
        return false;
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
            if (show) {
                if (!newImage.isIsload()) {
                    //如果没有加载，要加载图形
                    addLayerShp(lowImageGraphicsLayers.get(i));
                    MapService.setLayerLoad(newImage);
                }
            }
            lowImageGraphicsLayers.get(i).getShapeGraphics().setVisible(show);
            lowImageGraphicsLayers.get(i).getLowImage().setSelect(show);
            lowImageGraphicsLayers.get(i).getTextGraphics().setVisible(show);
        }

    }

    /**
     * @param lowImageGraphicsLayer
     */
    private void addLayerShp(LowImageGraphicsLayer lowImageGraphicsLayer) {
        LowImage lowImage = lowImageGraphicsLayer.getLowImage();
        lowImage.setIsload(true);
        GraphicsOverlay shapeGraphics = lowImageGraphicsLayer.getShapeGraphics();
        GraphicsOverlay textGraphics = lowImageGraphicsLayer.getTextGraphics();
        List<MyJSONObject> myJSONObjects = lowImageGraphicsLayer.getLayerdatas();
        for (MyJSONObject myJSONObject : myJSONObjects) {
            addShap(shapeGraphics, textGraphics, myJSONObject);
        }
    }

    /**
     * 重新再数据中查找 是shp的数据
     *
     * @return
     */
    private List<MyJSONObject> getAllShapDatas() {
        List<MyJSONObject> mapDatas = null;
        try {
            mapDatas = XZQYService.findDatasByXZQYCode(XZQYService.getCurrentCode());

        } catch (Exception e) {
            return mapDatas;
        }
        for (int i = 0; i < mapDatas.size(); i++) {
            if (!mapDatas.get(i).getJsonobject().containsKey("geometry")) {
                mapDatas.remove(i);
                i--;
            }
        }
        return mapDatas;
    }

    /**
     * shp 数据放到对应的图层
     *
     * @param shpdatas
     */
    public Map<LowImage, List<MyJSONObject>> getLowImageListMap(List<MyJSONObject> shpdatas) {

        Map<String, List<MyJSONObject>> map = ReflectTool.getListIDMap("getTablename", shpdatas);
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
        return lowImageListMap;
    }

    /**
     * 添加数据
     */
    private void addData() {
        List<MyJSONObject> shpdatas = getAllShapDatas();
        Map<LowImage, List<MyJSONObject>> lowImageListMap = getLowImageListMap(shpdatas);
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
     * 执行查询
     */
    private void searchShp() {
        clearAll();
        EditText editText = MapFragment.this.view.findViewById(R.id.et_search);
        String serachKey = editText.getText().toString();
        searchShp(serachKey);
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

                        synchronized (MapFragment.class) {
                            timer.schedule(task, 500);
                        }


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
            setShowSearchResultRecyclerView(View.VISIBLE);
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

    RecyclerView historySearchTextRecy;

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
                    fs.add(new ViewFieldCustom(R.id.iv_search) {
                        @Override
                        public void OnClick(View view, MyJSONObject myJSONObject) {
                            String text = JSONTool.getString(myJSONObject, HistorySearchText.JSON_TEXT);
                            serachEditText.setText(text);
                            serachEditText.setSelection(text.length());
                            searchShp();
                        }
                    });

                    //item 点击事件
                    fs.add(new ViewFieldCustom(R.id.item) {
                        @Override
                        public void OnClick(View view, MyJSONObject myJSONObject) {
                            String text = JSONTool.getString(myJSONObject, HistorySearchText.JSON_TEXT);
                            serachEditText.setText(text);
                            serachEditText.setSelection(text.length());
                            //setHistorySearchTextRecyclerView(View.GONE);
                        }
                    });
                    TableDataCustom tableDataCustom = new TableDataCustom(R.layout.map_item_searhhistorytext, fs, historys).setEdit(true);
                    historySearchTextRecy = view.findViewById(R.id.recy_search_history);
                    historySearchTextAdapter = new MyItemRecyclerViewAdapter(tableDataCustom, historySearchTextRecy);
                    historySearchTextAdapter.setAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
                    historySearchTextRecy.setLayoutManager(new LinearLayoutManager(getContext()));
                    historySearchTextRecy.setAdapter(historySearchTextAdapter);
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

            if (serachResult.size() == 0) {
                AndroidTool.showAnsyTost("没有查询结果", 0);
            } else {
                HistorySearchText.saveHistory(searchText, serachResult.size());
            }

            //显示查询结果
            showSearchResult(serachResult);

            addSerachShpMark(serachResult);
            //保存查询的记录


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

        clearMark();
        for (int i = 0; i < serachResult.size(); i++) {
            addSerachShpMark(serachResult.get(i), i + 1 + "");
        }


    }

    private void addSerachShpMark(MyJSONObject serachResult, String text) {


        Geometry geometry = getGeometry(serachResult);
        if (geometry != null) {
            Point core = geometry.getExtent().getCenter();
            addPictureGraphic(markGrapics, core, text, SerachShpMarkTextSize, R.mipmap.icon_position_red);
        }
    }


    /**
     * 得到地图标注
     *
     * @param data
     * @param geometry
     */
    private Graphic getMapTextMark(MyJSONObject data, Geometry geometry) {

        String text = data.getJsonobject().getString(tableResultMap.get(data.getTablename()).getFieldCustoms().get(0).getAttribute());
        Paint mPaint = new TextPaint();
        mPaint.setTextSize(40);
        mPaint.setAntiAlias(true);
        Rect mRect = new Rect();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.getTextBounds(text, 0, text.length(), mRect);
        float strwid = mRect.width() * 1.2f;
        float strhei = mRect.height() * 1.5f;

        BitmapDrawable icon = (BitmapDrawable) getResources().getDrawable(R.drawable.whiteimg);
        Bitmap bitmap = icon.getBitmap();
        bitmap = AndroidTool.getNewBitmap(bitmap, (int) strwid, (int) strhei);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, (bitmap.getWidth() / 2), (bitmap.getHeight() / 1.3f), mPaint);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(bitmapDrawable);
        Point center = geometry.getExtent().getCenter();
        center = new Point(center.getX(), center.getY() - 8);
        Graphic graphic = new Graphic(center, pictureMarkerSymbol);
        return graphic;
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

        BitmapDrawable bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeResource(resources, R.mipmap.icon_position));
        PictureMarkerSymbol campsiteSymbol = new PictureMarkerSymbol(bitmapDrawable);
        campsiteSymbol.loadAsync();
        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        locationDisplay.startAsync();
        campsiteSymbol.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                //LocationDataSource.Location location = locationDisplay.getLocation();
                //Point pt = location.getPosition();
                locationDisplay.setDefaultSymbol(campsiteSymbol);//设置默认符号
                locationDisplay.setShowAccuracy(false);//隐藏符号的缓存区域
                //locationDisplay.setAcquiringSymbol(campsiteSymbol);//设置当前位置的填充符号
                //locationDisplay.setAccuracySymbol(campsiteSymbol);//设置最后一个已知位置的标记符号
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
            new Runnable() {
                @Override
                public void run() {
                    //按表格名称创建 图层
                    //点图层

                    //添加到记录中，方便控制图层
                    LowImageGraphicsLayer lowImageGraphicsLayer = null;
                    for (int i = 0; i < lowImageGraphicsLayers.size(); i++) {
                        if (lowImageGraphicsLayers.get(i).getLowImage().equals(lowImage)) {
                            lowImageGraphicsLayer = lowImageGraphicsLayers.get(i);
                            lowImageGraphicsLayer.getLayerdatas().addAll(map.get(lowImage));
                            break;
                        }
                    }
                    if (lowImageGraphicsLayer == null) {
                        GraphicsOverlay pointGraphicOverlay = new GraphicsOverlay();
                        SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.RED, 10);
                        SimpleRenderer pointRenderer = new SimpleRenderer(pointSymbol);
                        pointGraphicOverlay.setRenderer(pointRenderer);
                        //mMapView.getGraphicsOverlays().add(pointGraphicOverlay);

                        //面图层
                        SimpleLineSymbol polygonSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 1);
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
                        lowImageGraphicsLayer = new LowImageGraphicsLayer(lowImage, polygonGraphicOverlay, textGraphicOverlay, map.get(lowImage));
                        lowImageGraphicsLayers.add(lowImageGraphicsLayer);
                    }

                    /**
                     * 图层被选中了，才渲染图层
                     */
                    if (lowImage.isSelect()) {
                        addLayerShp(lowImageGraphicsLayer);
                    } else {
                        lowImage.setIsload(false);
                    }
                }
            }.run();
        }
    }

    /**
     * 添加单个shp 到图层
     *
     * @param shpGraphics
     * @param textGraphics
     * @param myJSONObject
     */
    private void addShap(GraphicsOverlay shpGraphics, GraphicsOverlay textGraphics, MyJSONObject myJSONObject) {
        Geometry geometry = getGeometry(myJSONObject);
        shpGraphics.getGraphics().add(new Graphic(geometry));
        Graphic textMark = getMapTextMark(myJSONObject, geometry);
        textGraphics.getGraphics().add(textMark);
    }
    private WKT wkt = new WKT();
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
        //List<Point> srcpoints = getPoints(jsonGeometry, SpatialReference.create(4526));
        //List<Point> descpoints = pointschangeSp(srcpoints, getSpatialReference());
        switch (type) {
            case "MULTIPOLYGON":
                String json = wkt.getMULTIPOLYGONWktToJson(jsonGeometry, 4526);
                Geometry geometry1 = Geometry.fromJson(json);
                geometry1 =  GeometryEngine.project(geometry1, getSpatialReference());
                if(geometry1 != null){
                   /* ImmutablePartCollection collection =  ((Polygon)geometry1).getParts();
                    for (int i = 0; i < collection.size(); i++) {
                        ImmutablePart segments = collection.get(i);

                    }*/
                    return  geometry1;
                }


                break;
            case "MULTIPOINT":

                break;
        }
        String json = geometry.toJson();


        return geometry;
    }

    private void openShp1() {
        // 构建ShapefileFeatureTable，引入本地存储的shapefile文件
        ShapefileFeatureTable shapefileFeatureTable = new ShapefileFeatureTable(
                getActivity().getFilesDir()+"/test.shp");
        shapefileFeatureTable.loadAsync();
        // 构建featureLayerr
        FeatureLayer featureLayer = new FeatureLayer(shapefileFeatureTable);
        // 设置Shapefile文件的渲染方式
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 1.0f);
        SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.YELLOW, lineSymbol);
        SimpleRenderer renderer = new SimpleRenderer(fillSymbol);
        featureLayer.setRenderer(renderer);
        // 添加到地图的业务图层组中
        ArcGISMap arcGISMap = new ArcGISMap(Basemap.createTopographic());
        arcGISMap.getOperationalLayers().add(featureLayer);
        // set the map to the map view

    }

    // 加载shapefile
    private void featureLayerShapefile1() {
        // 获取本地shapefile文件
        String path = getActivity().getFilesDir()+"/test.shp";
        boolean b = new File(path).exists();
        ShapefileFeatureTable  mShapefileFeatureTable = new ShapefileFeatureTable(path);
        mShapefileFeatureTable.loadAsync();
        mShapefileFeatureTable.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                GeometryType gt = mShapefileFeatureTable.getGeometryType();
                String name = mShapefileFeatureTable.getTableName();
                FeatureLayer mFeatureLayer = new FeatureLayer(mShapefileFeatureTable);
                if (mFeatureLayer.getFullExtent() != null) {
                    mMapView.setViewpointGeometryAsync(mFeatureLayer.getFullExtent());
                } else {
                    mFeatureLayer.addDoneLoadingListener(new Runnable() {
                        @Override
                        public void run() {
                            Geometry geometry =mFeatureLayer.getFullExtent();
                            mMapView.setViewpointGeometryAsync(mFeatureLayer.getFullExtent());
                        }
                    });
                }

            }
        });


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

    public Point getScreenPoint() {
        Point point = mMapView.screenToLocation(new android.graphics.Point((int) mMapView.getPivotX(), (int) mMapView.getPivotY()));
        return point;
    }

    /**
     * 地图关闭时的操作
     */
    public void close() {
        double scale = mMapView.getMapScale();
        Point point = getScreenPoint();
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

    static double oldx = 0;

    /**
     * 自动计算xy 坐标
     */
    private void autoComputeXY() {
        TextView xTextView = view.findViewById(R.id.tv_x);
        TextView yTextView = view.findViewById(R.id.tv_y);
        Activity activity = getActivity();


        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                //得到屏幕中心
                //mMapView.setBackgroundColor(Color.WHITE);
                Point screenPoint = getScreenPoint();
                if (screenPoint == null) {
                    return;
                }

                Geometry geometry = GeometryEngine.project(screenPoint, SpatialReference.create(4326));

                Point jinweiduPoint = (Point) geometry;
                double x = jinweiduPoint.getX();
                //可见状态 且 坐标被移动才变化
                if (x != oldx && rl_xy != null && rl_xy.getVisibility() == View.VISIBLE) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            oldx = x;
                            xTextView.setText(String.format("%.5f", x));
                            yTextView.setText(String.format("%.5f", jinweiduPoint.getY()));
                        }
                    });
                }
            }
        }, 0, 60);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MapResult.LAYER && resultCode == MapResult.LAYER) {
            List<LowImage> list = LowMapManager.getVisableLowMaps();
            addLowMaps(mMapView, list);

        } else if (resultCode == MapResult.DATALOCATION) {
            //定位
            clearAll();
            MyJSONObject shpdata = (MyJSONObject) data.getSerializableExtra("data");
            scaleData(shpdata);
            addSerachShpMark(shpdata, "");
        } else if (resultCode == MapResult.DATACHANGE) {
            // AndroidTool.showAnsyTost("数据被改变了", 0);
            //刷新数据
            reflushPage();

        } else if (requestCode == NVER_TIME && resultCode == RESULT_OK) {
            //随手拍的返回值
            String path = getActivity().getIntent().getStringExtra("data");
        } else if (requestCode == MapResult.XZQYCHANGE && resultCode == MapResult.XZQYCHANGE) {
            reloadPage();
        } else {
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
     * 重新加载页面
     */
    private void reloadPage() {
        clearAll();

        for (LowImageGraphicsLayer lowImageGraphicsLayer : lowImageGraphicsLayers) {
            lowImageGraphicsLayer.getShapeGraphics().getGraphics().clear();
            lowImageGraphicsLayer.getLayerdatas().clear();
            lowImageGraphicsLayer.getTextGraphics().getGraphics().clear();
        }

        addData();
        addMarkGraphics();
        //缩放在该区区域， 如果图层是开启状态
        for (LowImageGraphicsLayer lowImageGraphicsLayer : lowImageGraphicsLayers) {
            if (scaleMapLayer(lowImageGraphicsLayer.getLowImage())) {
                break;
            }
        }
    }

    /**
     * 刷新页面 ，跟以前的老数据对比看看哪些时被删除 、修改、还没有增加
     */
    private void reflushPage() {
        clearAll();
        //1、重新获取数据库数据
        List<MyJSONObject> shpdatas = getAllShapDatas();
        Map<String, MyJSONObject> dataMap = new HashMap<>();
        for (MyJSONObject data : shpdatas) {
            dataMap.put(data.getId() + data.getTablename(), data);
        }
        //Map<LowImage,List<MyJSONObject>> lowImageListMap = getLowImageListMap( shpdatas );
        //2、找到现在数据
        for (LowImageGraphicsLayer lowImageGraphicsLayer : lowImageGraphicsLayers) {
            //3、进行对比
            List<MyJSONObject> olddatas = lowImageGraphicsLayer.getLayerdatas();
            for (int i = 0; i < olddatas.size(); i++) {
                MyJSONObject olddata = olddatas.get(i);
                MyJSONObject newdata = dataMap.get(olddata.getId() + olddata.getTablename());
                if (newdata == null) {
                    //表示data数据被删除
                    //删除对象
                    lowImageGraphicsLayer.getLayerdatas().remove(i);

                    //如果图形还没有加载就不用删除了
                    if (lowImageGraphicsLayer.getLowImage().isIsload()) {
                        //删除文字标注
                        lowImageGraphicsLayer.getTextGraphics().getGraphics().remove(i);
                        //删除图形
                        lowImageGraphicsLayer.getShapeGraphics().getGraphics().remove(i);
                    }
                } else if (!olddata.getJson().equals(newdata.getJson())) {
                    //跟新对象
                    lowImageGraphicsLayer.getLayerdatas().set(i, newdata);
                    //删除以前标注
                    //跟新文字标注
                    Graphic textMark = getMapTextMark(newdata, getGeometry(newdata));
                    lowImageGraphicsLayer.getTextGraphics().getGraphics().set(i, textMark);

                }

            }
            //4、、 更新 、删除 图形、文字标注
        }


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
                        //跟新对象
                        layerdatas.set(i, obj);
                        //删除以前标注
                        //增加最新标注
                        //lowImageGraphicsLayer.getTextGraphics().getGraphics().remove(i);
                        Graphic textMark = getMapTextMark(obj, getGeometry(obj));
                        lowImageGraphicsLayer.getTextGraphics().getGraphics().set(i, textMark);

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

    /**
     * 初始 时 默认状态
     */
    private static Integer CURRENT_STATE = R.id.state1;

    private class MapTouch extends DefaultMapViewOnTouchListener {

        public MapTouch(Context context, MapView mapView) {
            super(context, mapView);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            boolean result = super.onSingleTapUp(e);
            if (CURRENT_STATE == R.id.state1 || CURRENT_STATE == R.id.state2) {
                clickSearchShp(e, new Callback<List<Graphic>>() {
                    @Override
                    public void call(List<Graphic> results) {

                        List<MyJSONObject> myJSONObjects = getShowClickSearchShp(results);

                        showSearchResult(myJSONObjects);
                        addSerachShpMark(myJSONObjects);
                        setHistorySearchTextRecyclerView(View.GONE);
                    }
                });
            }

            return result;
        }
    }

    /**
     * 得到 点击地图的 图形
     *
     * @param graphics
     */
    private List<MyJSONObject> getShowClickSearchShp(List<Graphic> graphics) {
        List<MyJSONObject> myJSONObjects = new ArrayList<>();
        if (Tool.isEmpty(graphics)) {
            return myJSONObjects;
        }
        for (LowImageGraphicsLayer lowImageGraphicsLayer : lowImageGraphicsLayers) {
            List<Graphic> databaseGraphic = lowImageGraphicsLayer.getShapeGraphics().getGraphics();
            List<Graphic> textGraphic = lowImageGraphicsLayer.getTextGraphics().getGraphics();
            for (int i = 0; i < graphics.size(); i++) {
                Graphic graphic = graphics.get(i);

                int index = databaseGraphic.indexOf(graphic);
                if (index == -1) {
                    index = textGraphic.indexOf(graphic);
                }
                if (index != -1) {
                    MyJSONObject myJSONObject = lowImageGraphicsLayer.getLayerdatas().get(index);
                    if (!myJSONObjects.contains(myJSONObject)) {
                        myJSONObjects.add(myJSONObject);
                    }
                    if (myJSONObjects.size() == graphics.size()) {
                        return myJSONObjects;
                    }
                }
            }

        }
        return myJSONObjects;
    }

    /**
     * e
     * 点击地图返回 的结果 返回的是 List
     * 后期要优化时的频率，避免进入死机状态
     *
     * @param e
     * @param callback
     */
    private void clickSearchShp(MotionEvent e, Callback<List<Graphic>> callback) {
        android.graphics.Point screenPoint = new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY()));
        List<Graphic> results = new ArrayList<>();

        final ListenableFuture<List<IdentifyGraphicsOverlayResult>> identifyGraphic = mMapView.identifyGraphicsOverlaysAsync(
                screenPoint, 10.0, false, 10);
        identifyGraphic.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    List<IdentifyGraphicsOverlayResult> grOverlayResult = identifyGraphic.get();
                    // get the list of graphics returned by identify graphic overlay
                    for (IdentifyGraphicsOverlayResult identifyGraphicsOverlayResult : grOverlayResult) {
                        List<Graphic> graphics = identifyGraphicsOverlayResult.getGraphics();
                        if (!Tool.isEmpty(graphics)) {
                            results.addAll(graphics);
                        }
                    }
                    callback.call(results);
                } catch (InterruptedException | ExecutionException ie) {
                    ie.printStackTrace();
                }
            }
        });
    }

    public void clearAll() {
        setHistorySearchTextRecyclerView(View.GONE);
        setShowSearchResultRecyclerView(View.GONE);
        clearMark();
    }
}
