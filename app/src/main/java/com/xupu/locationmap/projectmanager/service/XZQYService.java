package com.xupu.locationmap.projectmanager.service;

import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.projectmanager.po.Redis;
import com.xupu.locationmap.projectmanager.po.SugProject;
import com.xupu.locationmap.projectmanager.po.XZDM;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class XZQYService {

    private static String CURRENT_XZDM_MARK = "CURRENT_PROJECT_MARK";
    private static String XZDM_MARK = "XZDM_MARK";

    /**
     * 得到所有的行政区域
     *
     * @return
     */
    public static List<XZDM> findAll() {
        List<XZDM> xzdms = RedisTool.findListRedis(XZDM_MARK + "%", XZDM.class);
      /*  for (int i = 0; i < 2; i++) {
            String own = UUID.randomUUID().toString();
            XZDM xzdm = new XZDM(i+"", "1组");
            xzdm.setId(own);
            xzdms.add(xzdm);

        }*/

        return xzdms;
    }

    public static void addXZDM(XZDM xzdm) {
        String own = UUID.randomUUID().toString();
        xzdm.setId(own);
        String mark = getXZDMMark(xzdm);
        RedisTool.saveRedis(mark, xzdm);
    }

    private static String getXZDMMark(XZDM xzdm) {
        return XZDM_MARK + "_" + xzdm.getId();
    }

    public static void deleteItem(XZDM xzdm) {
        String mark = getXZDMMark(xzdm);
        RedisTool.deleteRedisByMark(mark);
    }


    private static XZDM currentXZDM;

    /**
     * 得到当前区域
     *
     * @return
     */
    public static XZDM getCurrentXZDM() {
        if (currentXZDM == null) {
            //在数据中查找
            currentXZDM = RedisTool.findRedis(CURRENT_XZDM_MARK, XZDM.class);
        }
        return currentXZDM;
    }

    /**
     * 设置当前的区域
     *
     * @param currentXZDM
     */
    public static void setCurrentXZDM(XZDM currentXZDM) {
        //保存到数据库中，下次重新启动有记录
        RedisTool.updateRedis(CURRENT_XZDM_MARK, currentXZDM);
        XZQYService.currentXZDM = currentXZDM;
    }

}
