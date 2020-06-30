package com.xupu.locationmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PushbackReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Date;
import java.util.SortedMap;

public class TestFile {
    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        Path testPath = Paths.get("F:/download/test.txt");
        BasicFileAttributeView basicView = Files.getFileAttributeView(testPath, BasicFileAttributeView.class);
        BasicFileAttributes basicFileAttributes = basicView.readAttributes();
        System.out.println("创建时间：" + new Date(basicFileAttributes.creationTime().toMillis()));
        System.out.println("最后访问时间：" + new Date(basicFileAttributes.lastAccessTime().toMillis()));
        System.out.println("最后修改时间：" + new Date(basicFileAttributes.lastModifiedTime().toMillis()));
        System.out.println("文件大小：" + basicFileAttributes.size());
        FileOwnerAttributeView ownerView = Files.getFileAttributeView(testPath, FileOwnerAttributeView.class);
        System.out.println("文件所有者：" + ownerView.getOwner());
    }
}