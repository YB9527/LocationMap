package com.xupu.locationmap.projectmanager.page;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
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

    private final List<JSONObject> mValues;
    private final TableDataCustom tableDataCustom;
    private ViewHolder oldHolder;
    private int oldPostion;

    public MyItemRecyclerViewAdapter(TableDataCustom tableDataCustom) {
        this.mValues = tableDataCustom.getList();
        this.tableDataCustom = tableDataCustom;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(tableDataCustom.getFragmentItem(), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        JSONObject json = mValues.get(position);
        holder.itemView.setBackgroundColor(Color.WHITE);

        holder.mItem = json;
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
            }
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("yb", holder.mItem.toString());

                if (oldHolder != null) {
                    oldHolder.itemView.setBackgroundColor(Color.WHITE);
                }
                holder.itemView.setBackgroundColor(Color.GRAY);
                oldHolder = holder;


            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public JSONObject mItem;
        Map<View, FiledCustom> vieMap = new HashMap<>();

        public ViewHolder(View view) {
            super(view);
            mView = view;
            Map<Integer, FiledCustom> filedMap = tableDataCustom.getMap();
            for (Integer rid : filedMap.keySet()) {
                View temView = view.findViewById(rid);
                if (view != null) {
                    vieMap.put(temView, filedMap.get(rid));
                }
            }
        }
    }

    public void addItem(int index, JSONObject jsonObject) {
        this.mValues.add(index,jsonObject);
        this.notifyItemInserted(index);
    }

    public void addItem(JSONObject jsonObject) {
        addItem(this.mValues.size(), jsonObject);
    }

    public void remove(JSONObject jsonObject) {
        int index = this.mValues.indexOf(jsonObject);
        this.mValues.remove(index);
        this.notifyDataSetChanged();
    }
    public void update(JSONObject jsonObject) {
        this.notifyDataSetChanged();
    }

}
