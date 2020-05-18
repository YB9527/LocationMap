package com.xupu.locationmap.common.service;

import android.app.Application;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

/**
 * @author 小码哥Android学院(520it.com)
 * @time 2016/10/15  11:03
 * @desc ${TODD}
 */
public class NetEaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //ImageLoaderConfiguration imageLoder的配置类,createDefault 创建一个默认的显示配置
        //ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        File sd = Environment.getExternalStorageDirectory();
        File image_loader_cache= new File(sd,"xmg4");
        if(!image_loader_cache.exists()){
            image_loader_cache.mkdirs();
        }

        //全局显示的设置类
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).discCache(new UnlimitedDiskCache(image_loader_cache)).diskCacheFileNameGenerator(new Md5FileNameGenerator()).build();

        ImageLoader.getInstance().init(config);
    }
}
