package com.xupu.locationmap.projectmanager.page;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.Visibility;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.dialog.LeftDialogFragment;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.Utils;
import com.xupu.locationmap.projectmanager.po.MapResult;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.XZQYService;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.ItemDataCustom;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;
import com.xupu.locationmap.usermanager.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class NavDialog extends LeftDialogFragment {

    static Integer[] widthAndHeight;

    static {
        widthAndHeight = Utils.getWidthAndHeight();
    }

    public NavDialog() {
        super(widthAndHeight[0] / 3 * 2, widthAndHeight[1]);
    }

    private static MapFragment mapFragment;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        setUser();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapFragment = (MapFragment) getActivity().getSupportFragmentManager().findFragmentByTag(MapFragment.class.getSimpleName());
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_nav;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    public void initView() {

        MyJSONObject jsonObject = new MyJSONObject();
        jsonObject.setJsonobject(new JSONObject());
        List<FieldCustom> filedCustoms = new ArrayList<>();
        ItemDataCustom itemDataCustom = new ItemDataCustom(null, jsonObject, filedCustoms);
        /**
         * 跳到项目选择
         */
        filedCustoms.add(new ViewFieldCustom(R.id.m_myproject) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                toPrjectPage();
            }
        });
        /**
         * 跳到行政区域
         */
        filedCustoms.add(new ViewFieldCustom(R.id.m_xzqy) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                if (ProjectService.getCurrentSugProject() == null) {
                    toPrjectPage();
                    AndroidTool.showAnsyTost("请先选择项目", 1);
                } else {
                    Intent intent = new Intent(getActivity(), XZQYPage.class);
                    mapFragment.startActivityForResult(intent,MapResult.XZQYCHANGE);
                }
            }
        });
        /**
         * 跳到底图管理
         */
        filedCustoms.add(new ViewFieldCustom(R.id.m_lowmapmanager) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                Intent intent = new Intent(getActivity(), LowMapManager.class);
                //startActivity(intent);
                mapFragment.startActivityForResult(intent, MapResult.LAYER);
            }
        });

        filedCustoms.add(new ViewFieldCustom(R.id.m_nfmanager) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                Intent intent = new Intent(getActivity(), NFActivity.class);
                mapFragment.startActivity(intent);
            }
        }.setVisable(View.GONE));
        /**
         * 跳到调查数据
         */
        filedCustoms.add(new ViewFieldCustom(R.id.m_tablelist) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                if (ProjectService.getCurrentSugProject() == null) {
                    toPrjectPage();
                    AndroidTool.showAnsyTost("请先选择项目，在选择区域", 1);
                } else if (XZQYService.getCurrentXZDM() == null) {

                    Intent intent = new Intent(getActivity(), XZQYPage.class);
                    startActivity(intent);
                    AndroidTool.showAnsyTost("请先选择区域", 1);
                } else {
                    Intent intent = new Intent(getActivity(), TableListPage.class);
                    mapFragment.startActivityForResult(intent, MapResult.DATALOCATION);
                }
            }
        });

        filedCustoms.add(new ViewFieldCustom(R.id.m_help) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                Intent intent = new Intent(getActivity(), HelpActivty.class);
                startActivity(intent);
            }
        });
        filedCustoms.add(new ViewFieldCustom(R.id.m_tool) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                Intent intent = new Intent(getActivity(), ToolActivty.class);
                startActivity(intent);
            }
        });
        filedCustoms.add(new ViewFieldCustom(R.id.m_setting) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {

            }
        });
        AndroidTool.setView(view, itemDataCustom, true, 0);

    }

    public void toPrjectPage() {
        Intent intent = new Intent(getActivity(), ProjectPage.class);
        //intent = new Intent(this, Ph.class);
        startActivity(intent);
    }

    public boolean setUser() {
        //导航栏设置用户、账号
        TextView tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_nickname.setText(UserService.getUser().getNickName());
        TextView tv_account = view.findViewById(R.id.tv_account);
        tv_account.setText(UserService.getUser().getAccount());

        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        this.dismiss();
    }
}
