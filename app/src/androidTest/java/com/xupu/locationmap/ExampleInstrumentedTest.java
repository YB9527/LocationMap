package com.xupu.locationmap;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.tools.OkHttpClientUtils;
import com.xupu.locationmap.projectmanager.service.ZTService;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        String url = "http://192.168.30.18:8012/demo//%E5%A4%AA%E5%BA%B7%E5%8E%BF%E7%BB%BC%E5%90%88%E6%95%B4%E6%B2%BB/%E9%80%8A%E6%AF%8D%E5%8F%A3%E9%95%87/%E9%AB%98%E5%BA%84%E6%9D%91/xm074/Screenshot_20190415_161038_embed.jpg";
        OkHttpClient client = new OkHttpClient();

        client = new OkHttpClient();
        Request request = new Request.Builder().get().url(url).build();

        //这个请求方法是直接返回字节数组的,还有一个返回字符串的指向方法有点不一样
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new Throwable();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                   byte[] bs = response.body().bytes();

                }else{
                    new Throwable();
                }

            }
        });


    }

}
