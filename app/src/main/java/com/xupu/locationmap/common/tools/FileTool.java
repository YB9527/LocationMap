package com.xupu.locationmap.common.tools;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;

/**
 * 文件处理工具类
 */
public class FileTool {
    /**
     * @param path
     * @return 获取文件的名字 包含文件后缀名
     */
    public static String getFileName(String path) {
        if (path == null) {
            return null;
        }
        int index = path.lastIndexOf("/");
        if (index != -1 && index != path.length() - 1) {
            return path.substring(index + 1);
        }
        return null;
       /* Path.GetFileNameWithoutExtension
        Path.GetExtension
        Path.GetFileName*/
    }

    /***
     *
     * @param path
     * @return 返回不具有后缀名的 文件名
     */
    public static String getFileNameWithoutExtension(String path) {
        String fileName = getFileName(path);
        if (fileName == null) {
            return null;
        }
        int index = fileName.indexOf(".");
        if (index != -1) {
            return fileName.substring(0, index);
        }
        return null;
    }

    /**
     * @param path
     * @return 返回后缀名
     */
    public static String getExtension(String path) {
        String fileName = getFileName(path);
        if (fileName == null) {
            return null;
        }
        int index = fileName.indexOf(".");
        if (index != -1 && index != fileName.length() - 1) {
            return fileName.substring(index + 1);
        }
        return null;
    }

    /**
     * @param path
     * @return 获取文件路径
     */
    public static String getDir(String path) {
        if (path == null) {
            return null;
        }
        int index = path.lastIndexOf("/");
        if (index > 0) {
            return path.substring(0, index);
        }
        return null;
    }

    /**
     * 文件复制
     *
     * @param source
     * @param dest
     * @return
     */
    public static boolean copyFile(String source, String dest) {
        FileChannel input = null;
        FileChannel output = null;
        try {
            File dir = new File(getDir(dest));
            if (!dir.exists()) {
                dir.mkdirs();
            }
           File newFile =  new File(dest);
            if(newFile.exists()){
               newFile.delete();
            }
            boolean bl= newFile.createNewFile();
            input = new FileInputStream(new File(source)).getChannel();
            output = new FileOutputStream(newFile).getChannel();
            output.transferFrom(input, 0, input.size());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 检查文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean exitFile(String filePath) {
        if(filePath == null){
            return  false;
        }
        boolean bl = new File(filePath).exists();
        //boolean bl = (filePath == null || filePath.trim() == "") ? false : new File(filePath).exists();
        return bl;

    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return false;
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
     * 检查文件夹是否存在
     *
     * @param dir
     * @param isCreated
     * @return
     */
    public static boolean exitsDir(String dir, boolean isCreated) {
        File f = new File(dir);
        return exitsDir(f, isCreated);
    }

    /**
     * 保存文件
     *
     * @param is       数据流
     * @param savePath 保存的位置
     */
    public static void saveFile(InputStream is, String savePath) {

        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        // 储存下载文件的目录
        try {
            fos = new FileOutputStream(new File(savePath));
            long sum = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取文件的创建日期  "yyyy-MM-dd HH-mm-ss"
     *
     * @param file
     * @return
     */
    @TargetApi(Build.VERSION_CODES.O)
    public static Date getCreateDate(File file) {
        return getCreateDate(file.getAbsolutePath());
    }

    /**
     * 获取文件的创建日期  "yyyy-MM-dd HH-mm-ss"
     *
     * @param path
     * @return
     * @TargetApi(Build.VERSION_CODES.O)
     */


    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Date getCreateDate(String path) {
        try {
            FileTime t = Files.readAttributes(Paths.get(path), BasicFileAttributes.class).creationTime();
            long millis = t.toMillis();
            return new Date(millis);


        } catch (Exception e) {
            AndroidTool.showAnsyTost(e.getMessage(), 1);
            //e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param olddir
     * @param newdir
     * @return 如果文件不存在，返回false
     */
    public static boolean reName(String olddir, String newdir) {
        File oldfile = new File(olddir);
        File newfile = new File(newdir);
        if(oldfile.exists()){
            oldfile.renameTo(newfile);
            return true;
        }
       return false;
       // file.renameTo(new File("D:\\Java\\gitworkspace\\Coding\\src\\com\\stono\\thread2\\page0"+substring));
    }

    /**
     * 将字节流写入为文件
     *
     * @param name
     * @param binaryData
     */
    public static boolean saveFile(String name, byte[] binaryData) {
        if(name == null || binaryData == null || binaryData.length == 0){
            return false;
        }
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
        return true;
    }
}
