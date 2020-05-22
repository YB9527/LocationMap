package com.xupu.locationmap.projectmanager.page;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.EditFiledCusom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;

import java.util.Collection;
import java.util.Map;

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


    private void init() {
        AndroidTool.setView(view, itemDataCustom);
    }


    public AddItemFragment(ItemDataCustom itemDataCustom) {
        this.itemDataCustom = itemDataCustom;
    }

}
