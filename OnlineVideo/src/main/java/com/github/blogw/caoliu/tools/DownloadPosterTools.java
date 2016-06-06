package com.github.blogw.caoliu.tools;

import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.WebConstants;
import com.github.blogw.caoliu.utils.HttpUtils;
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
        String file = "d:/caoliu/tasks-20160603.obj";
        Stack<PageLink> stack = (Stack<PageLink>) Obj2DiskUtils.deserialize(file);
        Iterator<PageLink> it = stack.iterator();

        while (it.hasNext()) {
            PageLink pl = it.next();
            if (pl.getPosterUrl() == null) continue;
            File dir = new File(WebConstants.SAVE_FOLDER + pl.getTxt());
            FileUtils.forceMkdir(dir);
            HttpUtils.downloadPoster(dir,pl);
        }
    }
}
