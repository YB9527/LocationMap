package com.xupu.locationmap.projectmanager.service;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.page.XZQYPage;
import com.xupu.locationmap.projectmanager.po.Customizing;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.XZDM;

import java.util.List;
import java.util.UUID;

public class XZQYService {

    private static String CURRENT_XZDM_MARK = "CURRENT_XZDM_MARK";
    private static String XZDM_MARK = "XZDM_MARK";


    private static MyJSONObject currentXZDM;

    /**
     * 得到当前区域
     *
     * @return
     */
    public static MyJSONObject getCurrentXZDM() {
        if (currentXZDM == null) {
            //在数据中查找
            currentXZDM = RedisTool.findRedis(CURRENT_XZDM_MARK, MyJSONObject.class);
        }
        return currentXZDM;
    }

    /**
     * 设置当前的区域
     *
     * @param currentXZDM
     */
    public static void setCurrentXZDM(MyJSONObject currentXZDM) {
        //保存到数据库中，下次重新启动有记录
        RedisTool.updateRedis(CURRENT_XZDM_MARK, currentXZDM);
        XZQYService.currentXZDM = currentXZDM;
    }

    public static MyJSONObject newXZDM() {
        String uid = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", uid);
        jsonObject.put(Customizing.XZQY_Field.get(Customizing.XZQY_code).getName(), "");
        jsonObject.put(Customizing.XZQY_Field.get(Customizing.XZQY_caption).getName(), "");
        MyJSONObject project = ProjectService.getCurrentSugProject();
        if (project == null) {
            AndroidTool.showAnsyTost("请先设置项目", 1);
            return null;
        }
        return new MyJSONObject(uid, Customizing.XZQY, project.getId(), jsonObject.toJSONString());
    }

    public static String getCode(MyJSONObject xzdm) {
        return xzdm.getJsonobject().getString(Customizing.XZQY_code);
    }

    public static String getCaption(MyJSONObject xzdm) {
        return xzdm.getJsonobject().getString(Customizing.XZQY_caption);
    }

    public static List<MyJSONObject> findByProject() {
        List<MyJSONObject> list = TableTool.findByTableNameAndParentId(ZTService.XZQ_LIST, ProjectService.getCurrentSugProject().getId());
        return list;
    }

    public static String getCurrentCode() {
        return "411627102205";
       // return  getCode(getCurrentXZDM());
    }
}
