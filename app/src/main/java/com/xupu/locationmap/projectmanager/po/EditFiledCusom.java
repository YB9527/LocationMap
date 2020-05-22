package com.xupu.locationmap.projectmanager.po;

public class EditFiledCusom extends FiledCustom {


    public EditFiledCusom() {

    }

    public EditFiledCusom(String attribute, boolean isMust) {
        super(attribute);
        this.isMust = isMust;
    }

    /**
     * 是必须的吗
     */
    private boolean isMust;

    public boolean isMust() {
        return isMust;
    }

    public void setMust(boolean must) {
        isMust = must;
    }


}
