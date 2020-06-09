package com.xupu.locationmap.projectmanager.po;

public class SlidingFieldCustom extends FiledCustom {
    private  Integer layoutid;

    /**
     *
     * @param rid SlidingDeleteView 的id
     * @param layoutid  并行 布局的 id
     */
    public SlidingFieldCustom(Integer rid,Integer layoutid) {
        super(rid,"");
        this.layoutid =layoutid;
    }

    public Integer getLayoutid() {
        return layoutid;
    }

    public void setLayoutid(Integer layoutid) {
        this.layoutid = layoutid;
    }
}
