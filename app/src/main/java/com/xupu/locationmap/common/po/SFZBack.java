package com.xupu.locationmap.common.po;

import com.baidu.ocr.sdk.model.IDCardResult;

import java.io.Serializable;

/**
 * 身份证背面
 */
public class SFZBack implements Serializable {

    public void setBack(IDCardResult result) {
        Object obj = result.getSignDate();
        if (obj != null) {
            this.signDate = obj.toString();
        }else{
            this.signDate ="";
        }
        obj = result.getExpiryDate();
        if (obj != null) {
            this.expiryDate = obj.toString();
        }else{
            this.expiryDate ="";
        }
        obj = result.getIssueAuthority();
        if (obj != null) {
            this.issueAuthority = obj.toString();
        }else{
            this.issueAuthority ="";
        }
    }

    /**
     * 身份证起始日期
     */
    private String signDate;
    /**
     * 身份证终止日期
     */
    private String expiryDate;
    /**
     * 发证地址
     */
    private String issueAuthority;

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getIssueAuthority() {
        return issueAuthority;
    }

    public void setIssueAuthority(String issueAuthority) {
        this.issueAuthority = issueAuthority;
    }
}
