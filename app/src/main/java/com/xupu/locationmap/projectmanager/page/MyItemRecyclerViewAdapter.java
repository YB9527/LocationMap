package com.xupu.locationmap.projectmanager.page;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.ViewHolderCallback;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.ItemDataCustom;

import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.view.ItemTouchHelperAdapter;
import com.xupu.locationmap.projectmanager.view.SimpleItemTouchHelperCallback;
import com.xupu.locationmap.projectmanager.view.TableDataCustom;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
//public class MyItemRecyclerViewAdapter  extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {
public class MyItemRecyclerViewAdapter extends BaseQuickAdapter<MyJSONObject, MyItemRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private final List<MyJSONObject> mValues;
    public final TableDataCustom tableDataCustom;
    private ViewHolder oldHolder;
    private int oldPostion;
    List<List<MyJSONObject>> childs;
    List<List<FieldCustom>> childRidMap;
    private ViewHolderCallback callback;
    private int mBannerHeight;
    private int mBannerPosition = -1;

    /**
     * 设置每条数据加载完成的回调函数
     *
     * @param callback
     */
    public void setLoadViewCallback(ViewHolderCallback callback) {
        this.callback = callback;
    }

    public List<MyJSONObject> getmValues() {
        return mValues;
    }

    /**
     * 添加条目移动
     */
    public void addItemTouch() {

        //创建SimpleItemTouchHelperCallback
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this);
        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void convert(MyItemRecyclerViewAdapter.ViewHolder helper, MyJSONObject item) {

    }

    @SuppressLint("WrongConstant")
    public MyItemRecyclerViewAdapter(TableDataCustom tableDataCustom, RecyclerView recyclerView) {
        super(tableDataCustom.getList());
        this.mValues = tableDataCustom.getList();
        this.tableDataCustom = tableDataCustom;
        this.childRidMap = tableDataCustom.getChildRidList();
        this.recyclerView = recyclerView;
        openLoadAnimation(BaseQuickAdapter.SCALEIN);

        isFirstOnly(false);

        setNewData( this.mValues);

    }

    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    public void setAnimation(int animation) {
        openLoadAnimation(animation);
    }

    public void setChilds(List<List<MyJSONObject>> childs) {
        this.childs = childs;
        notifyDataSetChanged();
    }

    @Override
    public MyItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(tableDataCustom.getFragmentItem(), parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
        MyJSONObject json = mValues.get(position);

        ItemDataCustom itemDataCustom = new ItemDataCustom(null, json, tableDataCustom.getFieldCustoms());
        AndroidTool.setView(holder.mView, itemDataCustom, tableDataCustom.isEdit(), position);
        holder.mView.setBackgroundColor(Color.WHITE);
        /**
         * 写入子对象的属性
         */
        if (!Tool.isEmpty(childRidMap)) {
            for (int i = 0; i < childRidMap.size(); i++) {
                if (childs.size() > i && childs.get(i).size() > position) {
                    ItemDataCustom itemDataCustom2 = new ItemDataCustom(null, childs.get(i).get(position), childRidMap.get(i));
                    AndroidTool.setView(holder.mView, itemDataCustom2, tableDataCustom.isEdit(), position);
                }
            }
        }
        if (callback != null) {
            callback.call(holder, position);
        }
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setDatas(List<MyJSONObject> medias) {
        boolean isequals = true;
        //检查是否是一样的信息
        if (medias.size() == this.mValues.size()) {
            for (int i = 0; i < medias.size(); i++) {
                if (!medias.get(i).getJson().equals(this.mValues.get(i).getJson())) {
                    isequals = false;
                    break;
                }
            }
        }else{
            isequals =false;
        }

        if(!isequals){
            this.mValues.clear();
            this.mValues.addAll(medias);
            this.notifyDataSetChanged();
        }

    }

    RecyclerView recyclerView;

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void changeItem(int oldindex, int newindex) {
        MyJSONObject myJSONObject = this.getmValues().get(oldindex);
        this.remove(myJSONObject);
        this.addItem(newindex, myJSONObject);
        this.notifyItemChanged(oldindex);
        this.notifyItemChanged(newindex);
    }


    public class ViewHolder extends BaseViewHolder {
        public final View mView;
        public JSONObject mItem;
        Map<View, FieldCustom> vieMap = new HashMap<>();

        public ViewHolder(View view) {
            super(view);
            mView = view;
            List<FieldCustom> filedMap = tableDataCustom.getFieldCustoms();
            for (FieldCustom filedCustom : filedMap) {
                View temView = view.findViewById(filedCustom.getId());
                if (view != null) {
                    vieMap.put(temView, filedCustom);
                }
            }

        }
    }

    public void addItem(int index, MyJSONObject jsonObject) {
        if (index < 0) {
            index = 0;
        }
        int size = this.mValues.size();
        if (index >= this.mValues.size()) {
            index = size;
        }
        this.mValues.add(index, jsonObject);
        if (recyclerView != null) {
            recyclerView.scrollToPosition(index);
        }
        this.notifyItemInserted(index);

    }

    public void addItem(MyJSONObject myJSONObject) {
        addItem(this.mValues.size(), myJSONObject);
    }

    public int remove(MyJSONObject jsonObject) {
        int index = this.mValues.indexOf(jsonObject);
        if (index == -1) {
            for (int i = 0; i < this.mValues.size(); i++) {
                if (this.mValues.get(i).getId().equals(jsonObject.getId())) {
                    index = i;
                    break;
                }
            }
        }
        this.mValues.remove(index);
        this.notifyItemRemoved(index);
        return index;
    }

    public void update(MyJSONObject myJSONObject) {
        for (int i = 0; i < this.mValues.size(); i++) {
            if (myJSONObject.getId().equals(this.mValues.get(i).getId())) {
                this.mValues.set(i, myJSONObject);
                this.notifyItemChanged(i);
            }
        }

    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder source,
                           RecyclerView.ViewHolder target) {
        int fromPosition = source.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < mValues.size() && toPosition < mValues.size()) {
            //交换数据位置
            Collections.swap(mValues, fromPosition, toPosition);
            //刷新位置交换
            notifyItemMoved(fromPosition, toPosition);
        }
        //移动过程中移除view的放大效果
        onItemClear(source);
    }

    @Override
    public void onItemDissmiss(RecyclerView.ViewHolder source) {

        int position = source.getAdapterPosition();
        mValues.remove(position); //移除数据
        notifyItemRemoved(position);//刷新数据移除
    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder viewHolder) {

        //当拖拽选中时放大选中的view
        viewHolder.itemView.setScaleX(1.2f);
        viewHolder.itemView.setScaleY(1.2f);
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder viewHolder) {

        //拖拽结束后恢复view的状态
        viewHolder.itemView.setScaleX(1.0f);
        viewHolder.itemView.setScaleY(1.0f);
    }



}
