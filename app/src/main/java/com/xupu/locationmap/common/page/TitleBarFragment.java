package com.xupu.locationmap.common.page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.page.MyItemRecyclerViewAdapter;

@SuppressLint("ValidFragment")
public class TitleBarFragment extends android.app.Fragment {

    private String title;
    private Callback callback;
    private Integer icRid;
    private String rightTv;

    private boolean confirm;
    private String confirmMessage;
    public TitleBarFragment(){

    }
    @SuppressLint("ValidFragment")
    public TitleBarFragment(int leftIcRid, String title, Callback callback, String rightStr, String confirmMessage) {
        this(leftIcRid,title,callback,rightStr);
        if(!Tool.isEmpty(confirmMessage)){
            this.confirm =true;
            this.confirmMessage =confirmMessage;
        }
    }

    public void setConfirm(boolean confirm, String confirmMessage) {
        this.confirm = confirm;
        this.confirmMessage = confirmMessage;
    }

    @SuppressLint("ValidFragment")
    public TitleBarFragment(Integer icRid, String title, Callback callback, String rightTv) {
        this(title);
        this.icRid = icRid;
        this.callback = callback;
        this.rightTv = rightTv;
    }

    @SuppressLint("ValidFragment")
    public TitleBarFragment(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.app_bar_title, container, false);
        //设置标题
        TextView tv_title = view.findViewById(R.id.title_tv_title);
        tv_title.setText(title);
        //设置右边按钮
        if (icRid != null) {
            ImageButton ib = view.findViewById(R.id.title_right_ib);
            ib.setImageResource(icRid);
            ib.setVisibility(View.VISIBLE);
            view.findViewById(R.id.title_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(confirm){
                        AndroidTool.confirm(getActivity(), confirmMessage, new MyCallback() {
                            @Override
                            public void call(ResultData resultData) {
                                if(resultData.getStatus() == 0){
                                    callback.call(view);
                                }
                            }
                        });
                    }else{
                        callback.call(view);
                    }
                }
            });
            TextView tv_right = view.findViewById(R.id.title_right_tv);
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText(rightTv);
            //点击右边文字也触犯事件
        } else {
            view.findViewById(R.id.title_right_ib).setVisibility(View.GONE);
            view.findViewById(R.id.title_right_tv).setVisibility(View.GONE);

        }
        //设置返回键
        view.findViewById(R.id.title_ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return view;
    }
}
