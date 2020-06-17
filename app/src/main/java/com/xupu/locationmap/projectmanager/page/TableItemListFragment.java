package com.xupu.locationmap.projectmanager.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.xupu.locationmap.R;

import com.xupu.locationmap.common.po.ViewHolderCallback;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.po.MapResult;
import com.xupu.locationmap.projectmanager.po.TableItem;
import com.xupu.locationmap.projectmanager.po.TableType;
import com.xupu.locationmap.projectmanager.service.ZTService;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.view.PositionField;
import com.xupu.locationmap.projectmanager.view.TableDataCustom_TableName;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;
import com.xupu.locationmap.projectmanager.service.TableService;
import com.xupu.locationmap.projectmanager.service.TaskService;
import com.xupu.locationmap.projectmanager.service.XZQYService;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据表格名称显示数据
 * <p/>
 * interface.
 */
public class TableItemListFragment extends Fragment {

    public static List<MyItemRecyclerViewAdapter> recyclerViewAdapters = new ArrayList<>();
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private TableDataCustom_TableName tableDataCustom;
    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    /**
     * 这张表格有任务吗？
     */
    private boolean haseTask;

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
    private String tablename;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle aa = getArguments();
        if (getArguments() != null) {
            //根据表格名称来查找对象
            tablename = getArguments().getString("tablename");
            tableid = getArguments().getString("tableid");

            //表格任务
            List<MyJSONObject> tasks = TaskService.findTaskByTableid(tableid);
            if (tasks.size() > 0) {
                haseTask = true;
            }
            tableDataCustom = TableService.getTable(tablename);
            //int a = R.id.title;
            //a = R.id.info1;
            //a = R.id.info2;
            if(tableDataCustom == null){
                AndroidTool.showAnsyTost("表格xml配置中，没有添加这个表格："+tablename,1);
                return;
            }
            tableDataCustom.setFragmentItem(R.layout.fragment_item);
            List<FieldCustom> fs = tableDataCustom.getFieldCustoms();
            fs.add(new PositionField(R.id.index, "PositionField"));


            FieldCustom filedCustom;
            //按钮 点击 的详情
            filedCustom = new ViewFieldCustom(R.id.v_toinfo) {
                @Override
                public void OnClick(View view, MyJSONObject myJSONObject) {
                    toInfoPage(TableItemListFragment.this, myJSONObject);
                }
            };
            fs.add(filedCustom);
            //item 点击的 详情
            fs.add(new ViewFieldCustom(R.id.item_info) {
                @Override
                public void OnClick(View view, MyJSONObject myJSONObject) {
                    toInfoPage(TableItemListFragment.this,myJSONObject);
                }
            });
            //定位
            filedCustom = new ViewFieldCustom(R.id.v_location) {
                @Override
                public void OnClick(View view, MyJSONObject myJSONObject) {
                    Intent intent = new Intent();
                    intent.putExtra("data",myJSONObject);
                    getActivity().setResult(MapResult.datalocation,intent);
                    getActivity().finish();
                }
            };
            //检查类型，如果是图形才有地位
            MyJSONObject tableItem = ZTService.getTableItemByTablename(tablename);
            if(! tableItem.getJsonobject().getString(TableItem.type).equals(TableType.LAYER_TYPE) ){
                filedCustom.setVisable(View.GONE);
            }
            fs.add(filedCustom);



            if (haseTask) {
                //到多媒体
                filedCustom = new ViewFieldCustom(R.id.v_tomedia) {
                    @Override
                    public void OnClick(View view,MyJSONObject myJSONObject) {
                        //跳到任务界面
                        toMediaPage(TableItemListFragment.this,myJSONObject);

                    }
                };
                fs.add(filedCustom);
            }/*else{
                View v =  view.findViewById(R.id.v_tomedia);
                v.setVisibility(View.GONE);
            }*/

            String code = XZQYService.getCurrentCode();
            List<MyJSONObject> items = TableTool.findByTableNameAndParentId(tablename, code);
            tableDataCustom.setList(items);
            mColumnCount = tableDataCustom.getList().size();
        }
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }


    public static void toMediaPage(Activity activity, MyJSONObject myJSONObject) {
        Intent intent = new Intent(activity, TaskActivty.class);
        intent.putExtra("parent", myJSONObject);
        activity.startActivity(intent);
    }

    public static void toInfoPage(Activity activity, MyJSONObject obj){
        //AndroidTool.showAnsyTost("详请", 1);
        Intent intent = new Intent(activity, ObjectInfoActivty.class);
        intent.putExtra("id", obj.getId());
        intent.putExtra("tablename", obj.getTablename());
        activity.startActivityForResult(intent, 1);
    }

    public static void toMediaPage(Fragment fragment, MyJSONObject myJSONObject) {
        Intent intent = new Intent(fragment.getActivity(), TaskActivty.class);
        intent.putExtra("parent", myJSONObject);
        fragment.startActivity(intent);
    }

    public static void toInfoPage(Fragment fragment, MyJSONObject obj){
        //AndroidTool.showAnsyTost("详请", 1);
        Intent intent = new Intent(fragment.getActivity(), ObjectInfoActivty.class);
        intent.putExtra("id", obj.getId());
        intent.putExtra("tablename", obj.getTablename());
        fragment.startActivityForResult(intent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        myOnActivityResult(requestCode,resultCode,data);
    }


    public  void myOnActivityResult(int requestCode, int resultCode, @Nullable Intent data){
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
            case 2:
                switch (resultCode) {
                    case TableTool.STATE_UPDATE:
                        //这是增加，用的是修改按钮而已
                        MyJSONObject newobj = (MyJSONObject) data.getSerializableExtra("obj");
                        TableTool.insert(newobj, TableTool.STATE_INSERT);

                        addItem(newobj);
                        checkHasDataAndShow();
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
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom,recyclerView);
        boolean flag = false;
        /**
         * 因为缓存问题，替换到当前 myItemRecyclerViewAdapter
         */
        for (MyItemRecyclerViewAdapter tem : recyclerViewAdapters) {
            TableDataCustom_TableName tb = (TableDataCustom_TableName) tem.tableDataCustom;
            if (tb.getTableName().equals(tableDataCustom.getTableName())) {
                recyclerViewAdapters.set(recyclerViewAdapters.indexOf(tem), myItemRecyclerViewAdapter);
                flag = true;
            }
        }
        if (!flag) {
            recyclerViewAdapters.add(myItemRecyclerViewAdapter);
        }
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        //如果没有数据，显示缺省照片
        checkHasDataAndShow();

        //添加数据按钮
        view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加数据按钮
                toAddDataPage();
            }
        });
        /**
         * 每条数据加载完成，的返回函数
         */
        myItemRecyclerViewAdapter.setLoadViewCallback(new ViewHolderCallback() {
            @Override
            public void call(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
                //如果没有任务，隐藏多媒体图标
                if (!haseTask) {
                    View v =  holder.mView.findViewById(R.id.v_tomedia);
                    v.setVisibility(View.GONE);
                }
                initTitelLinstener();
            }
        });

        return view;
    }

    /**
     * 如果没有数据，显示缺省照片
     */
    private void checkHasDataAndShow() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!Tool.isEmpty(tableDataCustom.getList())) {
                    view.findViewById(R.id.data_no).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.data_no).setVisibility(View.VISIBLE);
                }
            }
        });

    }


    public void addItem(MyJSONObject jsonObject) {
        TableListPage parent = (TableListPage) getActivity();
        MyItemRecyclerViewAdapter myItemRecyclerViewAdapter = recyclerViewAdapters.get(parent.position);
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
        checkHasDataAndShow();
        getActivity().setResult(MapResult.datachange,null);
    }

    public  void update(MyJSONObject jsonObject) {
        myItemRecyclerViewAdapter.update(jsonObject);
        getActivity().setResult(MapResult.datachange,null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initTitelLinstener() {

        getActivity().findViewById(R.id.title_right_ib).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                toAddDataPage();
            }
        });

        getActivity().findViewById(R.id.title_right_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddDataPage();
            }
        });
    }

    //跳到添加对象也米娜
    public void toAddDataPage() {
        SmartTabLayout smartTabLayout = null;
        //添加对象
        TableListPage parent = (TableListPage) getActivity();
        Intent intent = new Intent(this.getActivity(), ObjectInfoActivty.class);
        intent.putExtra("state", TableTool.STATE_INSERT);
        tableid = parent.tableids.get(parent.position);
        intent.putExtra("id", tableid);

        startActivityForResult(intent, 2);
    }

}
