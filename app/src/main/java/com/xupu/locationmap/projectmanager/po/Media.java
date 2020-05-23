package com.xupu.locationmap.projectmanager.po;

import com.xupu.locationmap.common.tools.FileTool;

import java.io.Serializable;

public class Media implements Serializable {
    private String id;
    private String name;
    private String path;
    private MediaType mediaType;
    private String bz;
    private String dir;
    public Media() {

    }
    public Media( MediaType mediaType,String path) {
        this.name = FileTool.getFileNameWithoutExtension(path);
        this.dir =FileTool.getDir(path);
        this.path = path;
        this.mediaType = mediaType;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
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

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
}
