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
        view = inflater.inflate(R.layout.fragment_add_item, container, false);
        init();
        return view;
    }


    private void init() {
        Map<Integer, FiledCustom> map = itemDataCustom.getMap();
        final JSONObject jsonObject = itemDataCustom.getJsonObject();
        for (Integer rid : map.keySet()) {
            View temView = view.findViewById(rid);
            FiledCustom filedCustom = map.get(rid);
            if (temView instanceof TextView) {
                TextView tv = (TextView) temView;
                String attribute = filedCustom.getAttribute();
                String str = jsonObject.getString(attribute);
                if (str != null) {
                    tv.setText(str);
                }
            }
            if (temView instanceof Button) {
                Button btu = (Button) temView;
                btu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BtuFiledCustom btuFiledCustom = (BtuFiledCustom) filedCustom;
                        //是否是检查选项按钮
                        ResultData<JSONObject> resultData = new ResultData<JSONObject>(0, jsonObject);
                        if (btuFiledCustom.isCheck()) {
                            //检查数据
                            if (checkData(jsonObject, map.values())) {
                                btuFiledCustom.OnClick(resultData);
                            }
                        } else {
                            btuFiledCustom.OnClick(resultData);
                        }


                    }
                });

            } else if (temView instanceof EditText) {
                EditText et = (EditText) temView;

                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        // TODO Auto-generated method stub

                    }
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void afterTextChanged(Editable s) {
                        String text = et.getText().toString();
                        jsonObject.replace(filedCustom.getAttribute(), text);

                    }
                });





            }
        }
    }

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

    public AddItemFragment(ItemDataCustom itemDataCustom) {
        this.itemDataCustom = itemDataCustom;
    }

}
