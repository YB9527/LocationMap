package com.xupu.locationmap;

import android.graphics.Color;
import android.media.ExifInterface;
import android.telephony.mbms.FileInfo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryBuilder;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.MultipartBuilder;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.xupu.locationmap.arcruntime.WKT;
import com.xupu.locationmap.common.tools.OkHttpClientUtils;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import kotlin.Metadata;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect()  {







        String attionFlag = "234";
    }

    private String shpName = "d:\test.shp";



    class Person {
        private String name;
        private String sex;

        public Person(String name, String sex) {
            this.name = name;
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }

    class CommUtil {

        private Properties getAttionReplyPro() {
            try {
                InputStream in = new FileInputStream("d:/1.jpg");
                Properties p = new Properties();
                p.load(in);
                in.close();
                return p;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        //获取关注的标示
        public String getAttionFlag() {
            Properties p = getAttionReplyPro();
            String attionFlag = p.getProperty("attionFlag");
            return attionFlag;
        }

        //设置attionFlag的值
        public void setAttionFlag(String flag) {
            try {
                Properties p = getAttionReplyPro();
                p.setProperty("attionFlag", flag);
                OutputStream out = new FileOutputStream(new File(CommUtil.class.getResource("attionReply.properties").toURI()));
                p.store(out, "");
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}