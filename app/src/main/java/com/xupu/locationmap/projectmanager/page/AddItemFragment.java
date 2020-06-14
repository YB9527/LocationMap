package com.xupu.locationmap.projectmanager.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.projectmanager.view.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;

public class AddItemFragment extends Fragment {

    private View view;
    private ItemDataCustom itemDataCustom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(itemDataCustom.getRid(), container, false);
        init();
        return view;
    }

    public void setMyJSONObject(MyJSONObject newNF) {
        itemDataCustom.setMyJSONObject(newNF);
        init();
    }
    public void init() {
        AndroidTool.setView(view, itemDataCustom,false,0);
    }


    public AddItemFragment(ItemDataCustom itemDataCustom) {
        this.itemDataCustom = itemDataCustom;
    }


}
