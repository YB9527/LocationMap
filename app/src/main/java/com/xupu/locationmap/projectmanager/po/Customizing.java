package com.xupu.locationmap.projectmanager.po;

import java.util.HashMap;
import java.util.Map;

public class Customizing {
    public static String PROJECT = "旭普公司测试软件";
    public static String PROJECT_name = "name";
    public static String PARENTID="parentid";

    public static String NF = "农户";
    public static String NF_name = "name";
    public static String NF_bz = "bz";

    public static String XZQY = "行政区域";
    public static String XZQY_code = "code";
    public static String XZQY_caption = "caption";

    public static String MEDIA = "多媒体";
    public static String MEDIA_path = "path";
    public static String MEDIA_type = "mediatype";
    public static String MEDIA_bz = "bz";
    public static String MEDIA_task = "task";

    public static String SFZ_Front = "身份证正面";
    public static String SFZ_back = "身份证背面";


    /**
     * 对象字段 自定义
     */
    public static Map<String, DatabaseField> PROJECT_Field;
    public static Map<String, DatabaseField> NF_Field;
    public static Map<String, DatabaseField> XZQY_Field;
    public static Map<String, DatabaseField> MEDIA_Field;


    static {
        PROJECT_Field = new HashMap<>();
        {

            PROJECT_Field.put(PROJECT_name, new DatabaseField.Builder(PROJECT_name).setIsmust(true).build());
        }
        NF_Field = new HashMap<>();
        {
            NF_Field.put(NF_name, new DatabaseField.Builder(NF_name).setIsmust(true).build());
            NF_Field.put(NF_bz, new DatabaseField.Builder(NF_bz).setIsmust(true).build());
        }
        XZQY_Field = new HashMap<>();
        {
            XZQY_Field.put(XZQY_code, new DatabaseField.Builder(XZQY_code).setIsmust(true).build());
            XZQY_Field.put(XZQY_caption, new DatabaseField.Builder(XZQY_caption).setIsmust(true).build());
        }

        MEDIA_Field = new HashMap<>();
        {
            MEDIA_Field.put(MEDIA_path, new DatabaseField.Builder(MEDIA_path).setIsmust(true).build());
            MEDIA_Field.put(MEDIA_type, new DatabaseField.Builder(MEDIA_type).setIsmust(true).build());
            MEDIA_Field.put(MEDIA_bz, new DatabaseField.Builder(MEDIA_bz).setIsmust(true).build());
            MEDIA_Field.put(MEDIA_task, new DatabaseField.Builder(MEDIA_task).setIsmust(true).build());

        }

    }
}
