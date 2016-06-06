package com.github.blogw.caoliu.tools;

import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.WebConstants;
import com.github.blogw.caoliu.utils.SqliteUtils;
import com.github.blogw.download.Obj2DiskUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.Stack;

/**
 * Created by blogw on 2016/06/04.
 */
public class ImportTools {
    private static Log log = LogFactory.getLog(ImportTools.class);

    public static void main(String[] args) throws Exception {
        String file = WebConstants.SAVE_FOLDER + "tasks-20160603.obj";
        Stack<PageLink> stack = null;
        stack = (Stack<PageLink>) Obj2DiskUtils.deserialize(file);
        Iterator<PageLink> it = stack.iterator();

        while (it.hasNext()) {
            PageLink pl = it.next();
            SqliteUtils.getInstance().insert(pl);
            log.info("insert " + pl.getTxt());
        }
    }
}
