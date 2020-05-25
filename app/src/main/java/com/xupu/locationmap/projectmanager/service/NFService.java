package com.xupu.locationmap.projectmanager.service;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.Customizing;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;

import java.util.List;
import java.util.UUID;

public class NFService {
    public static String getName(MyJSONObject nf) {
        return nf.getJsonobject().getString(Customizing.NF_Field.get(Customizing.NF_name).getName());
    }
    public static String getBZ(MyJSONObject nf) {
        return nf.getJsonobject().getString(Customizing.NF_Field.get(Customizing.NF_bz).getName());
    }

    public static MyJSONObject newNF() {

        String uid = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", uid);
        jsonObject.put(Customizing.NF_Field.get(Customizing.NF_name).getName(), "");
        jsonObject.put(Customizing.NF_Field.get(Customizing.NF_bz).getName(), "");
        MyJSONObject xzdm = XZQYService.getCurrentXZDM();
        if (xzdm == null) {
            AndroidTool.showAnsyTost("请先设置行政区域", 1);
            return null;
        }
        return new MyJSONObject(uid, Customizing.NF, xzdm.getId(), jsonObject.toJSONString());
    }

    public static List<MyJSONObject> findByXZDM() {
        List<MyJSONObject> list = TableTool.findByTableNameAndParentId(Customizing.NF, XZQYService.getCurrentXZDM().getId());
        return list;
    }
}
