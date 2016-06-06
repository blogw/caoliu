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
        String newFile = "d:/caoliu/tasks2-20160603-need-analyze.obj";

        String file = "d:/caoliu/tasks2-20160603.obj";
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
        it = stack.iterator();
        while (it.hasNext()) {
            PageLink pl = it.next();
            String name = pl.getTxt();
            pl.setTxt(CaoliuUtils.folderNameFilter(name));
        }

        // save old stack
        Obj2DiskUtils.serialize(file, stack);

        // save new stack if size > 0
        if (newStack.size() > 0) {
            Obj2DiskUtils.serialize(newFile, newStack);
        }
    }
}
