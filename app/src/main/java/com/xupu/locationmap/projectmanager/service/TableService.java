package com.xupu.locationmap.projectmanager.service;

import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.XMLTool;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.view.TableDataCustom_TableName;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class TableService {
    private static final String aliasname = "aliasname";

    public static String getAliasname(MyJSONObject myJSONObject) {
        return myJSONObject.getJsonobject().getString(aliasname);
    }



    /**
     * 根据表格名称 查找对应的 TableDataCustom_TableName
     *
     * @param tableName
     * @return
     */
    public static TableDataCustom_TableName getTable(String tableName) {
        //String tableXmlPath = AndroidTool.getRootDir()+"表格.xml";
        //String tableXmlPath = AndroidTool.getMainActivity().getFilesDir()+"/"+"表格.xml";
        List<TableDataCustom_TableName> tts = getPages("表格.xml");
        for (TableDataCustom_TableName tt : tts) {
            if (tt.getTableName().equals(tableName)) {
                return tt;
            }
        }
        return null;
    }

    private static Map<String, List<TableDataCustom_TableName>> tableMap = new HashMap<>();

    /**
     * 根据xml 路径解析成 表格
     *
     * @param path xml路径
     * @return
     */
    private static List<TableDataCustom_TableName> getPages(String path) {
        List<TableDataCustom_TableName> tts = tableMap.get(path);
        if (tts != null) {
            return tts;
        }
        InputStream inputStream = null;
        try {
            inputStream = AndroidTool.getMainActivity().getAssets().open(path);
            //inputStream = new FileInputStream(path);
            tts = readXmlBySAX(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tts;

    }

    /**
     * 【SAX解析XML文件】
     **/
    private static List<TableDataCustom_TableName> readXmlBySAX(InputStream inputStream) {
        try {
            /**【创建解析器】**/
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();
            XMLTool handler = new XMLTool();
            saxParser.parse(inputStream, handler);
            inputStream.close();
            return handler.getPages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
