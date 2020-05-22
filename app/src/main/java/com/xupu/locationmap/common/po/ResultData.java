package com.xupu.locationmap.common.po;

import com.google.gson.annotations.Expose;

import okhttp3.Response;

public class ResultData<T> {
    /**
     *
     * @param status 响应状态
     * @param message 响应信息
     * @param json 响应数据
     */

    public ResultData(int status, String message, String json) {
        this.status = status;
        this.message = message;
        this.json = json;
    }
    /**
     * 结果状态
     */
    private int status;
    /**
     * 提示
     */
    private String message;
    /**
     * 返回的json 数据
     */
    private String json;

    /**
     * string data 转换 成 具体对象
     */

    private T t;

    private Response response;

    public ResultData(int status) {
        this.status = status;
    }

    public ResultData(int status, T t) {
        this.status = status;
        this.t = t;
    }

    public ResultData(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResultData(int status, Response response) {
        this.status = status;
        this.response = response;
    }

    public ResultData(int status, String message, Response response) {
        this.status = status;
        this.response = response;
        this.message = message;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
