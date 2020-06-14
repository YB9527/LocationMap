package com.xupu.locationmap.projectmanager.view;

public class SlidingFieldCustom extends FieldCustom {
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
