package com.xupu.locationmap.common.tools;

import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpClientUtils {
    static OkHttpClientUtils util;
    public static OkHttpClient client;


    private OkHttpClientUtils() {
        client = new OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .build();
    }

    //单例的方法
    public static OkHttpClientUtils getInstance() {
        if (util == null) {
            synchronized (OkHttpClientUtils.class) {
                if (util == null) {
                    util = new OkHttpClientUtils();
                }
            }
        }
        return util;
    }

    private void getHttpRespon(Request request, final JSONObjectRespon respon) {
        client.newCall(request).enqueue(new Callback() {
            //请求失败
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                respon.onError("连接服务器失败");
            }

            //有响应
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    //请求失败
                    respon.onError("连接服务器失败");
                    return;
                }
                String data = response.body().string();
                respon.parse(data);
            }
        });
    }


    private void getHttpRespon(PostBuild postBuild, JSONObjectRespon respon) {
        Gson gson = Tool.getGson();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (String key : postBuild.paramterMap.keySet()) {
            Object obj = postBuild.paramterMap.get(key);
            formBodyBuilder.add(key, gson.toJson(obj));
        }
        FormBody formBody = formBodyBuilder.build();
        Request request = new Request
                .Builder()
                .post(formBody)
                .url(postBuild.url)
                .build();
        //Response response = null;
        getHttpRespon(request, respon);
    }

    private void get(Request request, JSONObjectRespon respon) {
        client.newCall(request).enqueue(new Callback() {
            //请求失败
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                respon.onError("连接服务器失败");

            }

            //有响应
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    //请求失败
                    respon.onError("连接服务器失败");
                    return;
                }
                String data = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(data);
                respon.parse(data);
            }
        });
    }

    /**
     * post 的构建模式
     * post 的构建模式
     * 如果 gson 为空， 是再 tool里面拿的gson
     */
    public static class PostBuild {

        String url;
        Map<String, Object> paramterMap = new HashMap<>();
        Map<String, File> fileMap = new HashMap<>();
        //对象转换的规则
        Gson gson;

        public PostBuild(String url) {
            this.url = url;
        }

        public PostBuild setGson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public PostBuild addParameter(String mark, Object obj) {
            paramterMap.put(mark, obj);
            return this;
        }

        public PostBuild addFile(String fileName, File file) {
            fileMap.put(fileName, file);
            return this;
        }

        Request request;

        public Request build() {
            if (fileMap.size() == 0) {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                formBodyBuilderAdd(formBodyBuilder);
                FormBody formBody = formBodyBuilder.build();
                request = new Request
                        .Builder()
                        .post(formBody)
                        .url(url)
                        .build();
                return request;
            } else {
                MultipartBody.Builder requestBody = new MultipartBody.Builder();
                formBodyBuilderAdd(requestBody);
                request = new Request.Builder()
                        .url(url)
                        .post(requestBody.build())
                        .build();
            }
            return request;
        }

        public void post(JSONObjectRespon respon) {
            OkHttpClientUtils.getInstance().getHttpRespon(request, respon);
        }

        /**
         * 装载数据
         *
         * @param build
         */
        private void formBodyBuilderAdd(FormBody.Builder build) {
            if (gson == null) {
                gson = Tool.getGson();
            }
            for (String key : paramterMap.keySet()) {
                Object obj = paramterMap.get(key);
                build.add(key, gson.toJson(obj));
            }
        }

        /**
         * 装载数据和文件
         *
         * @param build
         */
        private void formBodyBuilderAdd(MultipartBody.Builder build) {
            if (gson == null) {
                gson = Tool.getGson();
            }
            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
            for (String key : paramterMap.keySet()) {
                Object obj = paramterMap.get(key);
                build.addFormDataPart(key, gson.toJson(obj));
            }
            for (String key : fileMap.keySet()) {
                File file = fileMap.get(key);
                build.addFormDataPart("path", file.getAbsolutePath(), RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
            }
        }
    }

    public static class GetBuild {

        Map<String, String> paramterMap;
        String basicUrl = "";

        public GetBuild(String basicUrl) {
            this.basicUrl = basicUrl;
            paramterMap = new HashMap<>();
        }

        public GetBuild() {
            this.basicUrl = Tool.getZTHostAddress();
            paramterMap = new HashMap<>();
        }

        /**
         * @param mark 标记
         * @param str  对象将执行 toString 方法
         * @return
         */
        public GetBuild addParamter(String mark, Object str) {
            paramterMap.put(mark, str.toString());
            return this;
        }

        private String lastUrl = "";

        public GetBuild addLastUrl(String lastUrl) {
            this.lastUrl = lastUrl;
            return this;
        }

        public GetBuild addUrl(String url) {
            this.basicUrl = this.basicUrl + "/" + url;
            return this;
        }

        public void buildToGet(JSONObjectRespon respon) {
            request = build(basicUrl);
            OkHttpClientUtils.getInstance().get(request, respon);
        }

        public void ztBuildToGet(JSONObjectRespon respon) {
            request = ztBuild(basicUrl);
            OkHttpClientUtils.getInstance().get(request, respon);
        }

        Request request;

        public Request build(String basicUrl) {
            if (paramterMap.size() > 0) {
                basicUrl = basicUrl + "?";
                for (String mark : paramterMap.keySet()) {
                    basicUrl = basicUrl + mark + "=" + paramterMap.get(mark) + "&";
                }
                basicUrl = basicUrl.substring(0, basicUrl.length() - 1);
            }
            request = new Request.Builder().get().url(basicUrl).build();
            return request;
        }

        public Request ztBuild(String basicUrl) {
            if (paramterMap.size() > 0) {
                basicUrl=basicUrl+"/";
                for (String mark : paramterMap.keySet()) {
                    basicUrl = basicUrl + mark + "=" + paramterMap.get(mark) + ",";
                }
                basicUrl = basicUrl.substring(0, basicUrl.length() - 1);
            }
            request = new Request.Builder().get().url(basicUrl + lastUrl).build();
            return request;
        }
    }
}

