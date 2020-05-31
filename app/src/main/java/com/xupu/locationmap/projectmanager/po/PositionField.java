package com.xupu.locationmap.projectmanager.po;

public class PositionField extends FiledCustom {


    public PositionField(String idText, String attribute) {
        super(idText, attribute);
    }

    public PositionField(Integer id, String attribute) {
        super(id, attribute);
    }

    private int startIndex;

    public int getStartIndex() {
        return startIndex;
    }

    public PositionField setStartIndex(int startIndex) {
        this.startIndex = startIndex;
        return this;
    }
}
