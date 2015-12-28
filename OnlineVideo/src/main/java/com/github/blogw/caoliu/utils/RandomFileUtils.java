package com.github.blogw.caoliu.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.management.openmbean.SimpleType;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by blogw on 2015/12/23.
 */
public class RandomFileUtils {
    public static void delFileHeader(File f) throws Exception {
        RandomAccessFile raf=new RandomAccessFile(f,"r");
        long a=raf.readLong();
        long b=raf.readLong();
        long c=raf.readLong();
        raf.close();

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
    }

    public static void alloc(String name,long size)throws Exception {
        RandomAccessFile raf = new RandomAccessFile(name, "rw");
        raf.setLength(size);
        raf.close();
    }

    public static void copy()throws Exception {
        String in="c:/caoliu/test.mp4";
        String out="c:/caoliu/test.mp4!";

        FileChannel srcChannel = new FileInputStream(in).getChannel();
        FileChannel dstChannel = new FileOutputStream(out).getChannel();
        dstChannel.position(24);
        srcChannel.transferTo(0, srcChannel.size(), dstChannel);
        srcChannel.close();
        dstChannel.close();
    }

    public static void copy2()throws Exception {
        String in="c:/caoliu/test.mp4!";
        String out="c:/caoliu/test2.mp4";

        FileChannel srcChannel = new FileInputStream(in).getChannel();
        FileChannel dstChannel = new FileOutputStream(out).getChannel();
        srcChannel.transferTo(24, srcChannel.size(), dstChannel);
        srcChannel.close();
        dstChannel.close();

        FileUtils.forceDeleteOnExit(new File(in));
    }

    public static void test()throws Exception {
        String out="c:/caoliu/test.mp4!";
        RandomAccessFile raf=new RandomAccessFile(out,"rw");
        raf.writeLong(1000l);
        raf.writeLong(2000l);
        raf.writeLong(3000l);
        raf.close();
    }

    public static void main(String[] args) throws Exception {
        copy2();
    }
}
