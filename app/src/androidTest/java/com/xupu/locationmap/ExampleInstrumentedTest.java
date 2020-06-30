package com.xupu.locationmap;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
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
        String fileName =  "d:/1.jpg";

        try {
            ExifInterface exifInterface = new ExifInterface(fileName);
            String TAG_APERTURE =exifInterface.getAttribute("aa");
            exifInterface.setAttribute("aa","123");

        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
