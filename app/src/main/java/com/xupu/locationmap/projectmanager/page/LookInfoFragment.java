package com.xupu.locationmap.projectmanager.page;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.EditFiledCusom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ImgFiledCusom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.service.ItemListService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 查看详细信息
 */
public class LookInfoFragment extends Fragment {

    private View view;
    private ItemDataCustom itemDataCustom;
    private String photoTag = "photos";
    private  MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    /**
     * 检查数据是否满足要求
     *
     * @param jsonObject
     * @param filedCustoms
     * @return
     */
    private boolean checkData(JSONObject jsonObject, Collection<FiledCustom> filedCustoms) {
        for (FiledCustom filedCustom : filedCustoms) {
            if (filedCustom instanceof EditFiledCusom) {
                EditFiledCusom editFiledCusom = (EditFiledCusom) filedCustom;
                if (editFiledCusom.isMust()) {
                    String result = jsonObject.getString(filedCustom.getAttribute());
                    if (Tool.isEmpty(result)) {
                        AndroidTool.showAnsyTost("数据没有填写完整", 1);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public LookInfoFragment(ItemDataCustom itemDataCustom) {
        this.itemDataCustom = itemDataCustom;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(itemDataCustom.getRid(), container, false);
        init();
        return view;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Map<Integer, FiledCustom> map = new HashMap<>();
        map.put(R.id.img, new ImgFiledCusom("path"));
        map.put(R.id.bz, new EditFiledCusom("bz",false));
        map.put(R.id.btu_delete, new BtuFiledCustom<JSONObject>("删除") {
            @Override
            public void OnClick(ResultData<JSONObject> resultData) {

            }
        });

        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.photo, map, itemDataCustom.getJsonObject().getJSONArray("medias"));
        View recy = view.findViewById(R.id.fl);
        RecyclerView recyclerView = (RecyclerView) recy;
         myItemRecyclerViewAdapter =new MyItemRecyclerViewAdapter(tableDataCustom);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        myItemRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void init() {
        AndroidTool.setView(view, itemDataCustom);
        //查看所有的照片

            /*Context context = recy.getContext();
            RecyclerView recyclerView = (RecyclerView) recy;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            MyItemRecyclerViewAdapter myItemRecyclerViewAdapter =new MyItemRecyclerViewAdapter(tableDataCustom);
            recyclerView.setAdapter(myItemRecyclerViewAdapter);
*/


    }

    /**
     * 初始化里面的内容
     *
     * @param jsonObject
     */
    public void setJSONbject(JSONObject jsonObject) {
        itemDataCustom.setJsonObject(jsonObject);

        if (view != null) {
            AndroidTool.setView(view, itemDataCustom);

            myItemRecyclerViewAdapter.setDatas(itemDataCustom.getJsonObject().getJSONArray("medias"));
        }

    }
}
