package com.github.blogw.caoliu.tools;

import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.download.Obj2DiskUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.Iterator;
import java.util.Stack;

/**
 * download all poster
 * <p>
 * Created by blogw on 2016/05/29.
 */
public class DownloadPosterTools {
    private static Log log = LogFactory.getLog(DownloadPosterTools.class);

    public static void main(String[] args) throws Exception {
        String file = "d:/caoliu/tasks-20160528.obj";
        Stack<PageLink> stack = (Stack<PageLink>) Obj2DiskUtils.deserialize(file);
        Iterator<PageLink> it = stack.iterator();

        // dir of poster
        File dir = new File("d:/caoliu");
        String[] list = dir.list();

        while (it.hasNext()) {
            PageLink pl = it.next();
//            if (!ArrayUtils.contains(list, pl.getPoster())) {
//                HttpUtils.downloadPoster(pl);
//            }
            FileUtils.forceMkdir(new File("d:/caoliu/" + pl.getTxt()));
        }
    }
}
