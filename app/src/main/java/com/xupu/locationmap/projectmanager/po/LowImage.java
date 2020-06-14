package com.xupu.locationmap.projectmanager.po;

import com.xupu.locationmap.common.tdt.TianDiTuLayerTypes;

import java.util.Objects;

public class LowImage {
    private String name;
    private String path;
    private int type;
    private  boolean select;
    private  String  size;
    public LowImage() {

    }
    public LowImage(String name, String path, int type) {
        this.name = name;
        this.path = path;
        this.type = type;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LowImage lowImage = (LowImage) o;
        return name.equals(lowImage.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}

