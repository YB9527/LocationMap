package com.xupu.locationmap.projectmanager.view;

public class EditFieldCusom extends FieldCustom {


    public EditFieldCusom() {

    }

    public EditFieldCusom(String idText, String attribute, boolean isMust) {
        super(idText,attribute);
        this.isMust = isMust;
    }
    public EditFieldCusom(Integer id, String attribute, boolean isMust) {
        super(id,attribute);
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
