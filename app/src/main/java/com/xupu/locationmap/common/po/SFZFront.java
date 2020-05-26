package com.xupu.locationmap.common.po;

import com.baidu.ocr.sdk.model.IDCardResult;

/**
 * 身份证正面
 */
public class SFZFront {
    private String name;
    private String address;
    /**
     * 身份证号码
     */
    private String idNumber;
    private String sex;
    /**
     * 民族
     */
    private String nation;

    /**
     * 生日
     */
    private String  birthday;

    public void SetFont(String name,String sex,String nation,String address){
        this.name =name;
        this.sex = sex;
        this.nation = nation;
        this.address = address;
    }
    public void setFont(IDCardResult result){
        this.name =result.getName().toString();
        this.sex = result.getGender().toString();
        this.nation = result.getEthnic().toString();
        this.address = result.getAddress().toString();
        this.idNumber = result.getIdNumber().toString();
        this.birthday =result.getBirthday().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
