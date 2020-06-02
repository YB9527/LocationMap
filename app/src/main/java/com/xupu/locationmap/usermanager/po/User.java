package com.xupu.locationmap.usermanager.po;

import com.google.gson.annotations.Expose;

public class User {
    @Expose
    private Long id;
    /**
     * 账号
     */
    @Expose
    private String account;
    /**
     * 昵称
     */
    @Expose
    private String nickName;
    /**
     * 密码
     */
    @Expose
    private String password;
    /**
     * 注册日期
     */
    @Expose
    private String registDate;

    /**
     * 用户邮箱，用于找回密码
     */
    @Expose
    private String email;


    public User(){

    }

    public User(String account, String nickName, String password, String registDate, String email) {
        this.account = account;
        this.nickName = nickName;
        this.password = password;
        this.registDate = registDate;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegistDate() {
        return registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                ", registDate='" + registDate + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
