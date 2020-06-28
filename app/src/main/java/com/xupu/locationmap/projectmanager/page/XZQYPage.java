package com.xupu.locationmap.projectmanager.page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xupu.locationmap.MainActivity;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.po.ViewHolderCallback;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.po.MapResult;
import com.xupu.locationmap.projectmanager.view.BtuFieldCustom;
import com.xupu.locationmap.projectmanager.view.EditFieldCusom;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.view.SlidingFieldCustom;
import com.xupu.locationmap.projectmanager.view.TableDataCustom;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;
import com.xupu.locationmap.projectmanager.service.XZQYService;

import java.util.ArrayList;
import java.util.List;

public class XZQYPage extends AppCompatActivity {

    Button btuAdd;
    AddItemFragment addItemFragment;
    ItemFragment itemFragment;

    private MyJSONObject oldXZQY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activty_xzqy);
        init();
        //initAddItemFragment();
        initTitle();

    }
    private void initTitle() {
        AndroidTool.addTitleFragment(this, "行政区域", R.mipmap.topnav_icon_new, "新增", new Callback() {
            @Override
            public void call(Object o) {
                //标题 右边新增按钮事件
                addXZQY();
            }
        });
    }

    List<MyJSONObject> xzqys =null;
    MyJSONObject currentXZQY;
    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private void init() {
        xzqys = XZQYService.findByProject();

        if (!Tool.isEmpty(xzqys)) {
            findViewById(R.id.recy).setVisibility(View.VISIBLE);
            findViewById(R.id.data_no).setVisibility(View.GONE);

            //当前项目 放到第一个
            currentXZQY = XZQYService.getCurrentXZDM();
            oldXZQY = currentXZQY;
            if (currentXZQY != null) {
                String currentProjectId = currentXZQY.getId();
                currentXZQY = null;
                for (int i = 0; i < xzqys.size(); i++) {
                    if (currentProjectId.equals(xzqys.get(i).getId())) {
                        currentXZQY = xzqys.remove(i);
                        xzqys.add(0, currentXZQY);
                        break;
                    }
                }
            }
        }
        List<FieldCustom> fs = new ArrayList<>();
        //名称
        fs.add(new FieldCustom(R.id.tv_projectname, "caption"));
        //描述
        fs.add(new FieldCustom(R.id.tv_descrip, "code"));


        //侧滑功能
        fs.add(new SlidingFieldCustom(R.id.slidingview, R.id.first));
        //删除项目
        fs.add(new ViewFieldCustom(R.id.tv_delete1) {
            @Override
            public void OnClick(View view,MyJSONObject myJSONObject) {
                //以后增加此功能
                myItemRecyclerViewAdapter.remove(myJSONObject);
            }
        }.setConfirm(true, "确认要删除此区域吗？"));

        //区域选择
        fs.add(new ViewFieldCustom(R.id.item) {
            @Override
            public void OnClick(View view,MyJSONObject myJSONObject) {
                //如果是当前区域，不用选中
                if (XZQYService.getCurrentXZDM() == null || !myJSONObject.getId().equals(XZQYService.getCurrentXZDM().getId())) {
                    AndroidTool.confirm(XZQYPage.this, "确定要选择这个区域吗？", new MyCallback() {
                        @Override
                        public void call(ResultData resultData) {
                            if (resultData.getStatus() == 0) {
                                if(!myJSONObject.equals(oldXZQY)){
                                    setResult(MapResult.XZQYCHANGE);
                                }else{
                                    setResult(MapResult.NONE);
                                }
                                setCurrentXZQY(myJSONObject);
                            }
                        }
                    });
                }
            }
        });

        //新增区域按钮
        findViewById(R.id.btn_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addXZQY();
            }
        });
       /* fs.add(new BtuFieldCustom(R.id.btu_info, "详情") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                Intent intent = new Intent(ProjectPage.this, ObjectInfoActivty.class);
                intent.putExtra("id", myJSONObject.getId());
                startActivityForResult(intent, 1);

            }
        });*/
        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.fragment_project_item, fs, xzqys);
        RecyclerView recyclerView = findViewById(R.id.recy);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom,recyclerView);

        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        myItemRecyclerViewAdapter.setRecyclerView(recyclerView);

        myItemRecyclerViewAdapter.setLoadViewCallback(new ViewHolderCallback() {
            @Override
            public void call(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {

                TextView tv_current = holder.mView.findViewById(R.id.tv_current);
                tv_current.setText("当前区域");
                ImageView imageView = holder.mView.findViewById(R.id.iv_currentIcon);
                if (position == 0 && currentXZQY != null)  {
                    imageView.setImageResource(R.drawable.area_icon_blue);
                    holder.mView.findViewById(R.id.fl_currentproject).setVisibility(View.VISIBLE);
                }else{
                    imageView.setImageResource(R.drawable.area_icon_gray);
                    holder.mView.findViewById(R.id.fl_currentproject).setVisibility(View.GONE);
                }
            }
        });

    }

    /**
     * 设置为当前行政区域
     * @param xzqy
     */
    private void setCurrentXZQY(MyJSONObject xzqy) {
        this.currentXZQY =xzqy;
        XZQYService.setCurrentXZDM(xzqy);
        AndroidTool.showAnsyTost("当前区域是：" + XZQYService.getCaption(xzqy), 0);
        myItemRecyclerViewAdapter.remove(xzqy);
        myItemRecyclerViewAdapter.addItem(0,xzqy);
        if(myItemRecyclerViewAdapter.getmValues().size() > 1){
            myItemRecyclerViewAdapter.notifyItemChanged(1);
        }

    }

    /**
     * 添加行政区域
     */
    private void addXZQY() {
        Intent intent = new Intent(XZQYPage.this, ObjectInfoActivty.class);
        intent.putExtra("state", TableTool.STATE_INSERT);
        intent.putExtra("id", xzqys.get(0).getTableid());
        startActivityForResult(intent, 2);
    }

    private void init2() {
        btuAdd = findViewById(R.id.btu_add);
        btuAdd.setVisibility(View.VISIBLE);
        btuAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                XZQYPage.this.showMain(false);
            }
        });

        List<MyJSONObject> xzdms =XZQYService.findByProject();
        List<FieldCustom> fs = new ArrayList<>();
        FieldCustom filedCustom ;
        filedCustom = new FieldCustom(R.id.code,"code");
        fs.add(filedCustom);
        filedCustom = new FieldCustom(R.id.caption,"caption");
        fs.add(filedCustom);
        filedCustom = new BtuFieldCustom(R.id.btu_delete, "删除") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                TableTool.delete(myJSONObject);
                itemFragment.remove(myJSONObject);
            }
        }.setConfirm(true,"确认要删除行政区域吗？");
        fs.add(filedCustom);
        filedCustom = new BtuFieldCustom(R.id.btu_select, "选择") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                AndroidTool.confirm(XZQYPage.this, "确定要选择这个区域吗？", new MyCallback() {
                    @Override
                    public void call(ResultData resultData) {
                        if (resultData.getStatus() == 0) {
                            XZQYService.setCurrentXZDM(myJSONObject);
                            Intent intent = new Intent(XZQYPage.this, MainActivity.class);
                            startActivity(intent);
                            AndroidTool.showAnsyTost("当前区域是：" + XZQYService.getCaption(myJSONObject), 0);
                        }
                    }
                });
            }
        };
        fs.add(filedCustom);


        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.fragment_xzqy_item, fs, xzdms);
        itemFragment = new ItemFragment(tableDataCustom);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl, itemFragment, "list")   // 此处的R.id.fragment_container是要盛放fragment的父容器'
                .commit();
    }

    /**
     * 初始化 新增item窗口
     */
    private void initAddItemFragment() {
        List<MyJSONObject> xzdms =XZQYService.findByProject();
        List<FieldCustom> fs = new ArrayList<>();
        FieldCustom filedCustom ;
        filedCustom = new EditFieldCusom(R.id.code,"code", true);
        fs.add(filedCustom);
        filedCustom = new EditFieldCusom(R.id.caption,"caption", true);
        fs.add(filedCustom);
        filedCustom = new BtuFieldCustom(R.id.btu_submit, "添加") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                itemFragment.addItem(myJSONObject);
                TableTool.insert(myJSONObject,TableTool.STATE_INSERT);
                showMain(true);
                //init();
            }
        }.setCheck(true).setReturn(true);
        fs.add(filedCustom);


        filedCustom = new BtuFieldCustom(R.id.btu_cancel, "取消") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                showMain(true);
            }
        };
        fs.add(filedCustom);
        ItemDataCustom itemDataCustom = new ItemDataCustom(R.layout.fragment_add_item, XZQYService.newXZDM(), fs);
        addItemFragment = new AddItemFragment(itemDataCustom);
        getSupportFragmentManager().beginTransaction().add(R.id.fl, addItemFragment, "item").hide(addItemFragment).commit();
    }

    public void showMain(boolean ishow) {
        if (ishow) {
            getSupportFragmentManager().beginTransaction().hide(addItemFragment).show(itemFragment).commit();
            btuAdd.setVisibility(View.VISIBLE);
        } else {
            getSupportFragmentManager().beginTransaction().show(addItemFragment).hide(itemFragment).commit();
            btuAdd.setVisibility(View.GONE);
        }
    }

}
