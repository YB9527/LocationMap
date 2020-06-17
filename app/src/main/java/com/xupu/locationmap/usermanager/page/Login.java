package com.xupu.locationmap.usermanager.page;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.xupu.locationmap.MainActivity;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.page.PermissionUtils;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.projectmanager.view.BtuFieldCustom;
import com.xupu.locationmap.projectmanager.view.EditFieldCusom;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.ImgFieldCusom;
import com.xupu.locationmap.projectmanager.view.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
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
        String[] permis = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                //定位权限
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        PermissionUtils.requestPermissions(this,1,permis);


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
                AndroidTool.showAnsyTost("还没有开发功能", 2);
            }
        });
        /**
         * 清除账号
         */
        filedCustoms.add(new ImgFieldCusom(R.id.login_username_cancel) {
            @SuppressLint("NewApi")
            @Override
            public void onClick(MyJSONObject myJSONObject) {
                EditText et = findViewById(R.id.et_account);
                et.setText("");
            }
        });
        /**
         * 查看密码
         */
        filedCustoms.add(new ImgFieldCusom(R.id.login_password_visible) {
            @Override
            public void onClick(MyJSONObject myJSONObject) {
                EditText et = findViewById(R.id.et_password);
                if (et.getInputType() == 129) {
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    et.setInputType(129);
                }
            }
        });
        ItemDataCustom itemDataCustom = new ItemDataCustom(null, user, filedCustoms);
        boolean isEdit = true;
        int postion = 0;
        AndroidTool.setView(this.findViewById(R.id.page), itemDataCustom, isEdit, postion);
    }

    private void login(User user) {
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        UserService.setUser(user);
        startActivity(intent);
        this.finish();
    }


    /**
     * 到注册页面
     */
    private void toSignUp() {
        Intent intent = new Intent(this, Regist.class);
        startActivity(intent);
    }
}
