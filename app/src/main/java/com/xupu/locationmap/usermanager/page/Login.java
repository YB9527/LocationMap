package com.xupu.locationmap.usermanager.page;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.MainActivity;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.page.PermissionUtils;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.JSONObjectRespon;
import com.xupu.locationmap.common.tools.OkHttpClientUtils;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.view.BtuFieldCustom;
import com.xupu.locationmap.projectmanager.view.EditFieldCusom;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.ImgFieldCusom;
import com.xupu.locationmap.projectmanager.view.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;
import com.xupu.locationmap.usermanager.po.User;
import com.xupu.locationmap.usermanager.service.UserService;

import java.util.ArrayList;
import java.util.List;


public class Login extends AppCompatActivity {

    private static String USERREDISMark = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        AndroidTool.setMainActivity(this);
        init();

        PermissionUtils.requestPermissions(this,1);


    }

    private User user;

    private void init() {
        user = RedisTool.findRedis(USERREDISMark, User.class);
        if (user == null) {
            user = new User();
            user.setNickName("YanBo");
            //user.setAccount("525730167@qq.com");
            //user.setPassword("1");
        }
        List<FieldCustom> filedCustoms = new ArrayList<>();
        filedCustoms.add(new EditFieldCusom(R.id.et_account, "account", true));
        filedCustoms.add(new EditFieldCusom(R.id.et_password, "password", true));
        filedCustoms.add(new BtuFieldCustom(R.id.btn_sign_in, "登录") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                RedisTool.updateRedis(USERREDISMark, myJSONObject.getJson());
                login(myJSONObject.getJsonobject().toJavaObject(User.class));
            }
        }.setCheck(true).setReturn(true));
        filedCustoms.add(new BtuFieldCustom(R.id.btn_sign_up, "注册") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                toSignUp();
            }
        });
        filedCustoms.add(new BtuFieldCustom(R.id.btn_youke_login, "游客登录") {
            @Override
            public void OnClick(MyJSONObject myJSONObject) {
                user.setNickName("游客");
                toMainActivity();
                //AndroidTool.showAnsyTost("暂未开放，敬请期待", 2);
            }
        });
        /**
         * 清除账号
         */
        filedCustoms.add(new ViewFieldCustom(R.id.login_username_cancel) {

            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                EditText et = findViewById(R.id.et_account);
                et.setText("");
            }
        });
        /**
         * 查看密码
         */
        filedCustoms.add(new ViewFieldCustom(R.id.login_password_visible) {
            @Override
            public void OnClick(View view, MyJSONObject myJSONObject) {
                EditText et = findViewById(R.id.et_password);
                if (et.getInputType() == 129) {
                    view.findViewById(R.id.img_pasword_visible).setVisibility(View.GONE);
                    view.findViewById(R.id.img_pasword_gone).setVisibility(View.VISIBLE);
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    et.setInputType(129);
                    view.findViewById(R.id.img_pasword_visible).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.img_pasword_gone).setVisibility(View.GONE);
                }
            }
        });
        ItemDataCustom itemDataCustom = new ItemDataCustom(null, user, filedCustoms);
        boolean isEdit = true;
        int postion = 0;
        AndroidTool.setView(this.findViewById(R.id.page), itemDataCustom, isEdit, postion);
    }

    private void login(User user) {
       getServerUser(user, new Callback<User>() {
            @Override
            public void call(User user) {
                if(user != null){
                    AndroidTool.showAnsyTost("登录成功",0);
                    user.setNickName("测试账号");
                    Login.this.user = user;
                    toMainActivity();
                }else{
                    AndroidTool.showAnsyTost("账号 或者 密码 错误",1);
                }
            }
        });
    }

   private   void toMainActivity(){
       Intent intent = new Intent( Login.this, MainActivity.class);
       UserService.setUser(user);
       startActivity(intent);
       Login.this.finish();
    }

    private void getServerUser(User user, Callback<User> callback) {

        StringBuilder sbURL =new StringBuilder(Tool.getHostBasicAddress()+"/Auth/login?");
        sbURL.append("loginName="+user.getAccount()+"&");
        sbURL.append("password="+user.getPassword()+"&");
        sbURL.append("tenantId=6f2c3bde-a937-4729-bb5b-47ea4b0f4c25");

        new  OkHttpClientUtils.GetBuild(sbURL.toString()).buildToGet(new JSONObjectRespon() {
            @Override
            public void onSuccess(JSONObject httpRespon) {
                if(httpRespon != null && httpRespon.containsKey("isOK") && httpRespon.getBooleanValue("isOK")){
                    callback.call(user);
                }else{
                    callback.call(null);
                }
            }
        });

    }


    /**
     * 到注册页面
     */
    private void toSignUp() {
        Intent intent = new Intent(this, Regist.class);
        startActivity(intent);
    }
}
