package com.xupu.locationmap;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xupu.locationmap.common.tools.OkHttpClientUtils;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Logger;

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
    public void addition_isCorrect() {

        CommUtil commUtil = new CommUtil();
        Properties properties = commUtil.getAttionReplyPro();
        String attionFlag = commUtil.getAttionFlag();
        String s = "123";
    }

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