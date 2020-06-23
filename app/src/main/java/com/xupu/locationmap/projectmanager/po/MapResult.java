package com.xupu.locationmap.projectmanager.po;

public interface MapResult {

    /**
     * 图层改变
     */
    final int LAYER = 20;


    /**
     * 定位
     */
    final int DATALOCATION = 21;
    /**
     * 数据被改变
     */
    final int DATACHANGE = 22;
    /**
     * 行政区域被改变
     */
    final int XZQYCHANGE = 23;
    /**
     * 删除数据
     */
    final  int DELETEDATA = 1;
    /**
     * 修改数据
     */
    final  int UPDATEDATA = 2;

    final int NONE=-100;


}
