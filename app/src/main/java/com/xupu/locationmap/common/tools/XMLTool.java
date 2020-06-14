package com.xupu.locationmap.common.tools;

import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.TableDataCustom_TableName;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XMLTool extends DefaultHandler {

    private List<TableDataCustom_TableName> pages = null;
    private TableDataCustom_TableName tt;
    private FieldCustom fc = null;
    private List<FieldCustom> fs;
    private String tagName = null;//当前解析的元素标签

    public List<TableDataCustom_TableName> getPages() {
        return pages;
    }

    @Override
/**【文档开始时，调用此方法】**/
    public void startDocument() throws SAXException {
        fs = new ArrayList<>();
    }

    @Override
/**【标签开始时，调用此方法】**/
    /**【uri是命名空间|localName是不带命名空间前缀的标签名|qName是带命名空间前缀的标签名|attributes可以得到所有的属性名和对应的值】**/
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName){
            case "pages":
                pages =new ArrayList<>();
                break;
            case  "page":
                tt = new TableDataCustom_TableName();
                fs = new ArrayList<>();
                tt.setTableName(attributes.getValue("tablename"));
                tt.setFieldCustoms(fs);
                pages.add(tt);
                break;
        }
        this.tagName = qName;
    }

    @Override
/**【接收标签中字符数据时，调用此方法】**/
    /**【ch存放标签中的内容，start是起始位置，length是内容长度】**/
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (tagName != null) {
            String data = new String(ch, start, length);
            switch (tagName) {

                case "fieldCustom":
                    fc = new FieldCustom();

                    break;
                case "id":
                    fc.setIdText(data);
                    break;
                case "attribute":
                    fc.setAttribute(data);
                    break;
            }
        }
    }

    @Override
/**【标签结束时，调用此方法】**/
    /**【localName表示元素本地名称（不带前缀），qName表示元素的限定名（带前缀）】**/
    public void endElement(String uri, String localName, String qName) throws SAXException {

        switch (localName) {
            case "fieldCustom":
                fs.add(fc);
                break;
            case "page":
                break;
            default:
                break;
        }
        this.tagName = null;
    }
}
