package com.xupu.locationmap.common.page;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.xupu.locationmap.R;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;

import java.util.List;

public class MyDialog extends DialogFragment {

    public MyDialog() {
    }
    private  String tip;
    private  String okStr;
    private MyCallback callback;
    public static MyDialog newInstance(String tip,String okStr, final MyCallback callback) {
        MyDialog fragment = new MyDialog();
        fragment.tip =tip;
        fragment.callback =callback;
        return fragment;
    }
    public static MyDialog newInstance(String tip, final MyCallback callback) {
        MyDialog fragment = new MyDialog();
        fragment.tip =tip;
        fragment.callback =callback;
        fragment.okStr ="确定";
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_simple, container, false);
        TextView tv_tip = view.findViewById(R.id.tv_tip);
        tv_tip.setText(tip);
        //取消按钮
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                callback.call(new ResultData(1));
            }
        });
        TextView tv_delete2 = view.findViewById(R.id.tv_delete2);
        tv_delete2.setText(okStr);
        //确认按钮
        view.findViewById(R.id.tv_delete2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                callback.call(new ResultData(0));
            }
        });
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}



