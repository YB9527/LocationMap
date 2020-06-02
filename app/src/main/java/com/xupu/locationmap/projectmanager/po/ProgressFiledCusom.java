package com.xupu.locationmap.projectmanager.po;

import com.xupu.locationmap.common.tools.AndroidTool;

public class ProgressFiledCusom extends FiledCustom {

    public ProgressFiledCusom(String idText, String attribute) {
        super(idText, attribute);
    }

    public ProgressFiledCusom(Integer id, String attribute) {
        super(id, attribute);
    }

  /*  private Integer id;
    private String idText;
    private String attribute;

    public ProgressFiledCusom(String idText, String attribute) {
        this.idText = idText;
        this.id = AndroidTool.getCompentID("id", idText);
        this.attribute = attribute;
    }

    public ProgressFiledCusom(Integer id, String attribute) {
        this.id = id;
        this.attribute = attribute;
    }*/

}
