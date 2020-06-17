package com.xupu.locationmap.projectmanager.po;

public interface MapResult {

    /**
     * 图层改变
     */
    final int layer = 20;
    /**
     * 定位
     */
    final int datalocation = 21;
    /**
     * 数据被改变
     */
    final int datachange = 22;

    /**
     * 删除数据
     */
    final  int deletedata = 1;
    /**
     * 修改数据
     */
    final  int updatedata = 2;

}
