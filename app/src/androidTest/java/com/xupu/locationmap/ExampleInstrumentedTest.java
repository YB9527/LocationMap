package com.xupu.locationmap;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.projectmanager.service.ZTService;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

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
        // Context of the app under test.
        //Context appContext = InstrumentationRegistry.getTargetContext();

        //assertEquals("com.xupu.locationmap", appContext.getPackageName());

       /* ZTService.getProjectTableId(new MyCallback() {
            @Override
            public void call(ResultData resultData) {

            }
        });*/

    }

}
