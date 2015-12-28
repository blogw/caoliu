package com.github.blogw.download;

import com.github.blogw.caoliu.beans.PageLink;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by blogw on 2015/12/24.
 */
public class Obj2DiskUtils {

    // 序列化对象到文件
    public static void serialize(String path, Object obj) throws Exception {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
        out.writeObject(obj);
        out.close();
    }

    // 从文件反序列化到对象
    public static Object deserialize(String path) throws Exception {
        // 创建一个对象输入流，从文件读取对象
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
        Object obj=in.readObject();
        in.close();
        return obj;
    }

    public static void main(String[] args) throws Exception {
        Stack<PageLink> stack=(Stack<PageLink>)Obj2DiskUtils.deserialize("c:/caoliu/tasks.obj");
        Iterator<PageLink> it=stack.iterator();
        while(it.hasNext()){
            PageLink t=it.next();
            if(t.getType()!=null) {
                System.out.println(t.getTxt() + "==>" + t.getType() + "==>" + t.getVideoUrl());
            }
        }
    }
}
