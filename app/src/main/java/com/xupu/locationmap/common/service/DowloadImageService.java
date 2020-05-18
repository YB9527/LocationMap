package com.xupu.locationmap.common.service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;


import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;


@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class DowloadImageService extends IntentService {


    //默认的构造方法一定要实现
    public DowloadImageService() {
        super("DowloadImageService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {


    }


    public void downloadImage(String url, String MD5_name) {
        try {
            URL uri = new URL(url);
            URLConnection urlConnection = uri.openConnection();
//            //拿到图片的边间,大小
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;


            //拿到一张图片
            Bitmap bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
            //往sd卡上写入图片
            saveToSD(bitmap, MD5_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 保存到sd
    public void saveToSD(Bitmap bitmap, String MD5_name) {
        if (null == bitmap) {
            return;
        }
        //判断手机Sd卡是否装载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File SD = AndroidTool.getMainActivity().getCacheDir();
            File cacheFile = new File(SD, Constant.CACHE);
            if (!cacheFile.exists()) {
                cacheFile.mkdirs();
            }

            File image = new File(cacheFile, MD5_name + ".jpg");
            //图片存在
            if (image.exists()) {
                return;
            }
            try {
                FileOutputStream image_out = new FileOutputStream(image);

                //bitmap压缩到sd卡
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, image_out);
                image_out.flush();
                image_out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
