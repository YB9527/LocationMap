package com.xupu.locationmap.usermanager.service;

import android.content.Intent;

import com.xupu.locationmap.R;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.usermanager.page.Login;
import com.xupu.locationmap.usermanager.po.User;

public class UserService {
    public static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserService.user = user;
    }

    /**
     * 如果没有登录就跳转导登录界面
     */
    public static void Login() {
        if(getUser() == null){
            toLoginPage();
        }
    }

    /**
     * 跳转到登录揭秘那
     */
    public static void toLoginPage() {
        Intent intent = new Intent(AndroidTool.getMainActivity(), Login.class);
        AndroidTool.getMainActivity().startActivity(intent);
    }

}
