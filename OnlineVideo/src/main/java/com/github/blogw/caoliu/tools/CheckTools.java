package com.github.blogw.caoliu.tools;

import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.utils.CaoliuUtils;
import com.github.blogw.download.Obj2DiskUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.Stack;

/**
 * 1.remove no poster or video item from old stack and save these to new stack
 * 2.remove \ / : * ? " < > | from name
 * <p>
 * Created by blogw on 2016/05/29.
 */
public class CheckTools {
    private static Log log = LogFactory.getLog(CheckTools.class);

    public static void main(String[] args) throws Exception {
        Stack<PageLink> newStack = new Stack<>();
        String newFile = "d:/caoliu/tasks-20160528-need-analyze.obj";

        String file = "d:/caoliu/tasks-20160528.obj";
        Stack<PageLink> stack = (Stack<PageLink>) Obj2DiskUtils.deserialize(file);
        Iterator<PageLink> it = stack.iterator();

        // step 1
        while (it.hasNext()) {
            PageLink pl = it.next();
            if (pl.getVideoUrl() == null || pl.getPosterUrl() == null) {
                log.info("==>" + pl.getTxt());
                newStack.push(pl);
            }
        }

        // remove from old stack
        stack.removeAll(newStack);

        // step 2
        // TODO: handle char of �
        it = stack.iterator();
        while (it.hasNext()) {
            PageLink pl = it.next();
            String name=pl.getTxt();
            if(name.indexOf("�")>=0){
                if(name.indexOf("2个极品长腿美少女")>=0){
                    pl.setTxt("2个极品长腿美少女 在化妆室刮屄毛");
                }
                if(name.indexOf("中字 马上和绘�e香来一炮吧 第1幕")>=0){
                    pl.setTxt("中字 马上和绘裡香来一炮吧 第1幕");
                }
                if(name.indexOf("中字 马上和绘�e香来一炮吧 第2幕")>=0){
                    pl.setTxt("中字 马上和绘裡香来一炮吧 第2幕");
                }
                if(name.indexOf("�律褡懔菩〗�")>=0){
                    pl.setTxt("大学生淫娃");
                }
            }
        }

        // step 3
        it = stack.iterator();
        while (it.hasNext()) {
            PageLink pl = it.next();
            String name = pl.getTxt();
            pl.setTxt(CaoliuUtils.folderNameFilter(name));
        }

        // step 4
        // TODO: poster and video name duplicated

        // save old stack
        Obj2DiskUtils.serialize(file, stack);

        // save new stack if size > 0
        if (newStack.size() > 0) {
            Obj2DiskUtils.serialize(newFile, newStack);
        }
    }
}
