package com.xupu.locationmap.common.tools;

import android.text.TextUtils;

import com.google.gson.Gson;


public abstract class HttpRespon<T> {
    //http返回的类型的泛型
    Class<T> t;
    Gson gson;
    public HttpRespon(Class<T> t,Gson gson){
        this.t = t;
        this.gson = gson;
    }
    public HttpRespon(Class<T> t){
        this.t = t;
        this.gson = new Gson();
    }
    //失败->调用者->失败的原因
    public  void  onError(String msg){
        AndroidTool.showAnsyTost(msg,1);
    }
    //成功->返回我需要的类型
    public abstract void  onSuccess(T t);

    public void parse(String json){
       if(TextUtils.isEmpty(json)){
           //请求失败
           onError("网络失败");
           return;
       }
        //根据我需要的类型,进行转换json
        //如果我只需要获取json对象,老老实实把json
        if(t == String.class){
            onSuccess((T)json);
            return;
        }
        // ADS.class
        //尝试转化json->需要的类型
        T result =gson.fromJson(json,t);
        //转化成功
        if(result!=null){
            onSuccess(result);
        }else{
            onError("json解析失败");
        }
    }
}
