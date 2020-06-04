package com.xupu.locationmap.usermanager.page;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xupu.locationmap.MainActivity;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.page.AskUser;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.usermanager.po.User;
import com.xupu.locationmap.usermanager.service.UserService;

import java.io.File;
import java.io.IOException;

public class Login extends AppCompatActivity implements View.OnClickListener {


    ImageView imageView;
    TextView textView;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        init();
        AskUser.ask(this);

    }

    private void init() {
        //图片增加事件
        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                count++;
                if (count % 2 == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");

                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                }
            }

            public void onSwipeLeft() {
                if (count % 2 == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");

                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                }
            }

            public void onSwipeBottom() {
            }

        });
        findViewById(R.id.btu_sign_in).setOnClickListener(this);
        findViewById(R.id.btu_sign_up).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btu_sign_in:
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                //this.finish();
                Intent intent = new Intent(this, MainActivity.class);
                User test = new User();
                UserService.setUser(test);
                startActivity(intent);
                this.finish();
                break;
            case R.id.btu_sign_up:
                toSignUp();
                break;
            default:
                break;

        }
    }

    /**
     * 到注册页面
     */
    private void toSignUp() {
        Intent intent = new Intent(this, Regist.class);
        startActivity(intent);
    }
}
