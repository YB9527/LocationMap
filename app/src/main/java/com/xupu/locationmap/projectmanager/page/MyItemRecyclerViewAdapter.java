package com.xupu.locationmap.projectmanager.page;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.po.ViewHolderCallback;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ItemDataChildCustom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;

import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.po.XZDM;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<MyJSONObject> mValues;
    private final TableDataCustom tableDataCustom;
    private ViewHolder oldHolder;
    private int oldPostion;
    List<List<MyJSONObject>> childs;
    List<List<FiledCustom>> childRidMap;
    private ViewHolderCallback callback;
    public void setLoadViewCallback(ViewHolderCallback callback){
        this.callback =callback;
    }

    public List<MyJSONObject> getmValues() {
        return mValues;
    }

    public MyItemRecyclerViewAdapter(TableDataCustom tableDataCustom) {
        this.mValues = tableDataCustom.getList();
        this.tableDataCustom = tableDataCustom;
        this.childRidMap = tableDataCustom.getChildRidList();
    }

    public void setChilds(List<List<MyJSONObject>> childs) {
        this.childs = childs;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(tableDataCustom.getFragmentItem(), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        MyJSONObject json = mValues.get(position);
        holder.itemView.setBackgroundColor(Color.WHITE);
        ItemDataCustom itemDataCustom = new ItemDataCustom(null, json, tableDataCustom.getFiledCustoms());
        AndroidTool.setView(holder.mView, itemDataCustom, tableDataCustom.isEdit(), position);

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

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldHolder != null) {
                    oldHolder.itemView.setBackgroundColor(Color.WHITE);
                }
                holder.itemView.setBackgroundColor(Color.GRAY);
                oldHolder = holder;
            }
        });
        if(callback != null){
            callback.call(holder,position);
        }
      /*  holder.mItem = json;
        for (View view : holder.vieMap.keySet()) {

            if (view instanceof Button) {
                Button btu = (Button) view;
                btu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BtuFiledCustom btuFiledCustom = (BtuFiledCustom) holder.vieMap.get(view);
                        btuFiledCustom.OnClick(new ResultData<JSONObject>(0, json));
                    }
                });
            } else if (view instanceof TextView) {
                TextView tv = (TextView) view;
                tv.setText(json.getString(holder.vieMap.get(tv).getAttribute()));
            } else if (view instanceof ImageView) {
                //ImageView img = (ImageView) view;
                //Bitmap bm = BitmapFactory.decodeFile(json.getString("path"));
                //img.setImageBitmap(bm);//不会变形
                //img.setImageResource(R.drawable.good_morning_img);//不会变形
            }
        }
        */

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setDatas(List<MyJSONObject> medias) {
        this.mValues.clear();
        this.mValues.addAll(medias);
        this.notifyDataSetChanged();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public JSONObject mItem;
        Map<View, FiledCustom> vieMap = new HashMap<>();

        public ViewHolder(View view) {
            super(view);
            mView = view;
            List<FiledCustom> filedMap = tableDataCustom.getFiledCustoms();
            for (FiledCustom filedCustom : filedMap) {
                Integer id = filedCustom.getId();

                View temView = view.findViewById(filedCustom.getId());
                if (view != null) {
                    vieMap.put(temView, filedCustom);
                }
            }

        }
    }

    public void addItem(int index, MyJSONObject jsonObject) {
        this.mValues.add(index, jsonObject);
        this.notifyItemInserted(index);
    }

    public void addItem(MyJSONObject myJSONObject) {
        addItem(this.mValues.size(), myJSONObject);
    }

    public void remove(MyJSONObject jsonObject) {
        int index = this.mValues.indexOf(jsonObject);
        this.mValues.remove(index);
        this.notifyItemRemoved(index);
    }

    public void update(MyJSONObject myJSONObject) {
        for (int i = 0; i < this.mValues.size(); i++) {
            if (myJSONObject.getId().equals(this.mValues.get(i).getId())) {
                this.mValues.set(i, myJSONObject);
                this.notifyItemChanged(i);
            }
        }

    }

}
