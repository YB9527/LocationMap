package com.xupu.locationmap.common.tools;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 一般工具类
 */

public class Tool {

    /**
     * 检查集合是否为空
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        return list == null ? true : list.isEmpty();
    }

    /**
     * @param flag null 返回false
     * @return
     */
    public static boolean isEmpty(Boolean flag) {
        return flag == null ? false : flag;
    }

    /**
     * 检查 字符串 是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null ? true : str.trim().isEmpty();
    }


    public static String getHostBasicAddress() {

        String hostAddress = "http://192.168.30.105:9999/api/";
        return hostAddress;
    }
    /**
     * 获取本机ip地址
     *
     * @return
     */
    public static String getHostAddress() {
        String hostAddress = getHostBasicAddress()+"apidata/";
        return hostAddress;
    }
    public static String getPhotoHostAddress() {

        String hostAddress = "http://192.168.30.18:9091/admin";
        return hostAddress;
    }


    public static String getZTHostAddress() {
        return getHostAddress()+"data/";
    }
    /**
     * 检查文件夹是否存在
     *
     * @param dirPath   文件路径
     * @param isCreated 如果没有是否创建
     */
    public static boolean exitsDir(String dirPath, boolean isCreated) {
        File file = new File(dirPath);
        return exitsDir(file, isCreated);
    }

    /**
     * 检查文件夹是否存在
     *
     * @param dir
     * @param isCreated
     * @return
     */
    public static boolean exitsDir(File dir, boolean isCreated) {
        if (dir.exists()) {
            return true;
        } else {
            if (isCreated) {
                return dir.mkdirs();
            }
        }
        return false;
    }

    /**
     * 将字节流写入为文件
     *
     * @param name
     * @param binaryData
     */
    public static void saveFile(String name, byte[] binaryData) {
        File file = new File(name);    //1、建立连接
        Tool.exitsDir(file.getParent(), true);
        OutputStream os = null;
        try {
            //2、选择输出流,以追加形式(在原有内容上追加) 写出文件 必须为true 否则为覆盖
            os = new FileOutputStream(file, false);
//            //和上一句功能一样，BufferedInputStream是增强流，加上之后能提高输出效率，建议
//            os = new BufferedOutputStream(new FileOutputStream(file,true));
            os.write(binaryData, 0, binaryData.length);    //3、写入文件
            os.flush();    //将存储在管道中的数据强制刷新出去
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }

    /**
     * 只复制集合，对象并没有复制
     *
     * @param list
     * @return
     */
    public static <T> List<T> copyList(List<T> list) {
        List<T> results = new ArrayList<>();
        for (T t : list
        ) {
            results.add(t);
        }
        return results;
    }

    private static Gson gson;

    /**
     * 得到 只处理 @Expose json对象
     *
     * @return
     */
    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").excludeFieldsWithoutExposeAnnotation()
                    .create();
        }
        return gson;
    }

    /**
     * 使用json功能复制对象
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> T copyObject(T obj) {
        Gson gson = getGson();
        String json = gson.toJson(obj);
        return (T) gson.fromJson(json, obj.getClass());
    }

    /**
     * 检查 bool 是否为 true ，null是返回 false
     *
     * @param bool
     * @return
     */
    public static boolean isTrue(Boolean bool) {
        return bool == null ? false : bool;
    }

    /**
     * json 转为 对象
     *
     * @param json
     * @param type
     * @return
     */
    public static <T> T JsonToObject(String json, Type type) {
        if (isEmpty(json)) {
            return null;
        }
        T t = new Gson().fromJson(json, type);
        return t;
    }

    /**
     * 对象转换为json
     *
     * @param obj
     * @return
     */
    public static String objectToJson(Object obj) {
        return new Gson().toJson(obj);
    }


    public static <T> void swap(List<T> list, int oldPosition, int newPosition){
        if(null == list){
            throw new IllegalStateException("The list can not be empty...");
        }

        // 向前移动，前面的元素需要向后移动
        if(oldPosition < newPosition){
            for(int i = oldPosition; i < newPosition; i++){
                Collections.swap(list, i, i + 1);
            }
        }

        // 向后移动，后面的元素需要向前移动
        if(oldPosition > newPosition){
            for(int i = oldPosition; i > newPosition; i--){
                Collections.swap(list, i, i - 1);
            }
        }
    }

}
