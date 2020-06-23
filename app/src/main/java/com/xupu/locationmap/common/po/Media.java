package com.xupu.locationmap.common.po;

import java.util.Date;

/**
 * 多媒体对象
 */
public class Media {
    /**
     * 拍照
     */
    public final static int PHOTO = 0;
    /**
     * 录像
     */
    public final static int VIDEO = 1;
    public static final String ADD_BUTTON = "-1";
    private String id;
    /**
     * 表id
     */
    private String tableid;
    /**
     * 所属对象id
     */
    private String oid;
    /**
     * 附件路径
     */
    private String path;
    /**
     * 任务的 id
     */
    private String milepost;
    /**
     * 顺序号
     */
    private int seq;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建时间
     */
    private String createtime;
    /**
     * 描述
     */
    private String desciption;
    /**
     * 备注
     */
    private String bz;
    /**
     * 多媒体类型
     */
    private int mtype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMilepost() {
        return milepost;
    }

    public void setMilepost(String milepost) {
        this.milepost = milepost;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public int getMtype() {
        return mtype;
    }

    public void setMtype(int mtype) {
        this.mtype = mtype;
    }

    @Override
    public String toString() {
        return "Media{" +
                "id='" + id + '\'' +
                ", tableid='" + tableid + '\'' +
                ", oid='" + oid + '\'' +
                ", path='" + path + '\'' +
                ", milepost='" + milepost + '\'' +
                ", seq=" + seq +
                ", creator='" + creator + '\'' +
                ", createtime=" + createtime +
                ", desciption='" + desciption + '\'' +
                ", bz='" + bz + '\'' +
                ", mtype=" + mtype +
                '}';
    }
}
