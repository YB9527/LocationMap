package com.xupu.locationmap.projectmanager.page;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xupu.locationmap.R;

import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.PositionField;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.po.TableDataCustom_TableName;
import com.xupu.locationmap.projectmanager.po.TableViewCustom;
import com.xupu.locationmap.projectmanager.po.XZDM;
import com.xupu.locationmap.projectmanager.service.TableService;
import com.xupu.locationmap.projectmanager.service.XZQYService;
import com.xupu.locationmap.projectmanager.service.ZTService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据表格名称显示数据
 * <p/>
 * interface.
 */
public class TableItemListPage extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private TableDataCustom_TableName tableDataCustom;
    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TableItemListPage() {

    }


    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle aa = getArguments();
        if (getArguments() != null) {
            //根据表格名称来查找对象
            String tablename = getArguments().getString("tablename");
            tableDataCustom = TableService.getTable(tablename);
            //int a = R.id.title;
            //a = R.id.info1;
            //a = R.id.info2;
            tableDataCustom.setFragmentItem(R.layout.fragment_item);
            List<FiledCustom> fs = tableDataCustom.getFiledCustoms();
            fs.add(new PositionField(R.id.index, "PositionField"));


            FiledCustom filedCustom;
            filedCustom = new BtuFiledCustom(R.id.btn_info, "详请") {
                @Override
                public void OnClick(MyJSONObject MyJSONObject) {
                    AndroidTool.showAnsyTost("详请", 1);
                }
            };
            fs.add(filedCustom);
            filedCustom = new BtuFiledCustom(R.id.btn_tomedia, "多媒体") {
                @Override
                public void OnClick(MyJSONObject MyJSONObject) {
                    AndroidTool.showAnsyTost("多媒体", 1);
                }
            };
            fs.add(filedCustom);

            String code = XZQYService.getCurrentCode();
            List<MyJSONObject> items = TableTool.findByTableNameAndParentId(tablename, code);
            tableDataCustom.setList(items);
            mColumnCount = tableDataCustom.getList().size();
        }
    }

    /**
     * 使用java反射机制
     * 设置Activity不用findViewbyid
     */


    //这是我项目中封装在BaseActivity的，可以直接在BaseActivity中放在onCreate就行了，具体的看自己BaseActivity怎么封装了0.0
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if (mColumnCount <= 1) {
                //recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                //recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom);
            recyclerView.setAdapter(myItemRecyclerViewAdapter);
        }
        Resources res = getResources();
        int viewId = AndroidTool.getCompentID("id", "index");

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnListFragmentInteractionListener) {
            //mListener = (OnListFragmentInteractionListener) context;
            //TableListPage tableListPage = (TableListPage)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void addItem(MyJSONObject jsonObject) {

        myItemRecyclerViewAdapter.addItem(jsonObject);

    }

    public void remove(MyJSONObject jsonObject) {
        myItemRecyclerViewAdapter.remove(jsonObject);
    }

    public void update(MyJSONObject jsonObject) {
        myItemRecyclerViewAdapter.update(jsonObject);
    }

}
