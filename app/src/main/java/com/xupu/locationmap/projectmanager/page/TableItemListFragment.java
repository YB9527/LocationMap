package com.xupu.locationmap.projectmanager.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xupu.locationmap.R;

import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.PositionField;
import com.xupu.locationmap.projectmanager.po.TableDataCustom_TableName;
import com.xupu.locationmap.projectmanager.service.TableService;
import com.xupu.locationmap.projectmanager.service.XZQYService;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 根据表格名称显示数据
 * <p/>
 * interface.
 */
public class TableItemListFragment extends Fragment {

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
    public TableItemListFragment() {

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

    private String tableid;
    private String tablename ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle aa = getArguments();
        if (getArguments() != null) {
            //根据表格名称来查找对象
            tablename = getArguments().getString("tablename");
            tableid=  getArguments().getString("tableid");
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
                public void OnClick(MyJSONObject myJSONObject) {
                    //AndroidTool.showAnsyTost("详请", 1);
                    Intent intent = new Intent(getActivity(), ObjectInfoActivty.class);
                    intent.putExtra("id", myJSONObject.getId());
                    startActivityForResult(intent, 1);
                }
            };
            fs.add(filedCustom);
            filedCustom = new BtuFiledCustom(R.id.btn_tomedia, "多媒体") {
                @Override
                public void OnClick(MyJSONObject myJSONObject) {
                    //跳到任务界面
                    Intent intent = new Intent(getActivity(), TaskActivty.class);
                    intent.putExtra("parent", myJSONObject);
                    startActivity(intent);

                }
            };
            fs.add(filedCustom);

            String code = XZQYService.getCurrentCode();
            List<MyJSONObject> items = TableTool.findByTableNameAndParentId(tablename, code);
            tableDataCustom.setList(items);
            mColumnCount = tableDataCustom.getList().size();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                //详情页面返回
                switch (resultCode) {
                    case TableTool.STATE_UPDATE:
                        //修改
                        MyJSONObject newObj = (MyJSONObject) data.getSerializableExtra("obj");
                        update(newObj);
                        break;
                    case TableTool.STATE_DELETE:
                        //删除
                        MyJSONObject Obj = (MyJSONObject) data.getSerializableExtra("obj");
                        remove(Obj);
                        break;
                    case TableTool.STATE_INSERT:
                        //增加
                        break;
                }
                break;
            case  2:
                switch (resultCode) {
                    case TableTool.STATE_UPDATE:
                        //这是增加，用的是修改按钮而已
                        MyJSONObject newobj = (MyJSONObject)data.getSerializableExtra("obj");
                        TableTool.insert(newobj,TableTool.STATE_INSERT);
                        addItem(newobj);
                        break;
                }
                break;
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
        view = inflater.inflate(R.layout.fragment_table_item_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recy);
        // Set the adapter
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if (mColumnCount <= 1) {
            //recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            //recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);


        FloatingActionButton btn = view.findViewById(R.id.btn_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ObjectInfoActivty.class);
                intent.putExtra("state", TableTool.STATE_INSERT);
                intent.putExtra("id", tableid);
                startActivityForResult(intent, 2);
            }
        });

        return view;
    }




    public void addItem(MyJSONObject jsonObject) {
        myItemRecyclerViewAdapter.addItem(jsonObject);
    }

    /**
     * 显示fragment
     *
     * @param tagName
     */
    private void showFragment(String tagName) {
        Intent intent = new Intent(getActivity(), LowMapManager.class);
        startActivity(intent);
    }

    public void remove(MyJSONObject jsonObject) {
        myItemRecyclerViewAdapter.remove(jsonObject);
    }

    public void update(MyJSONObject jsonObject) {
        myItemRecyclerViewAdapter.update(jsonObject);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
