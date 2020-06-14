package com.xupu.locationmap.projectmanager.page;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.animation.BaseAnimation;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;

import java.util.List;

public class Tt  extends BaseQuickAdapter<MyJSONObject,MyItemRecyclerViewAdapter.ViewHolder> {


    public Tt(@Nullable List<MyJSONObject> data) {
        super(data);
    }

    @Override
    protected void convert(MyItemRecyclerViewAdapter.ViewHolder helper, MyJSONObject item) {

    }
}
