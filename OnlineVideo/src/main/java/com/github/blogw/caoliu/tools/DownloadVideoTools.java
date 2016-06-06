package com.github.blogw.caoliu.tools;

import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.WebConstants;
import com.github.blogw.download.DLFile;
import com.github.blogw.download.DLTask;
import com.github.blogw.download.Obj2DiskUtils;
import com.github.blogw.download.ThreadInfo;
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
public class DownloadVideoTools {
    private static Log log = LogFactory.getLog(DownloadVideoTools.class);

    public static void main(String[] args) throws Exception {
        String file = "d:/caoliu/tasks-20160603.obj";
        Stack<PageLink> stack = (Stack<PageLink>) Obj2DiskUtils.deserialize(file);
        Iterator<PageLink> it = stack.iterator();

        while (it.hasNext()) {
            PageLink pl = it.next();
            if (pl.getVideoUrl() == null) continue;
            File dir = new File(WebConstants.SAVE_FOLDER + pl.getTxt());
            File f = new File(dir, pl.getVideo());
            if (f.exists()) continue;
            DLFile dlFile = DLTask.download(pl);
            log.info("start to download " + pl.getTxt());
            while (!isOver(dlFile)) {
                Thread.sleep(1000);
            }
        }
    }

    private static boolean isOver(DLFile file) {
        for (ThreadInfo info : file.getThreads()) {
            if (info.getCurrent() < info.getEnd()) {
                return false;
            }
        }
        return true;
    }
}
