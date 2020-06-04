package com.xupu.locationmap;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.tools.XMLTool;
import com.xupu.locationmap.projectmanager.po.TableDataCustom_TableName;
import com.xupu.locationmap.projectmanager.service.ZTService;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        //TableDataCustom_TableName table = getTable("地块");
        //table = getTable("地块");
        Person person = new Person(null,"nan");
        String aa= JSONObject.toJSONString(person, SerializerFeature.WriteMapNullValue);
        aa ="123";
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


}