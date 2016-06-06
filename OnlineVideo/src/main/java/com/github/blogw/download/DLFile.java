package com.github.blogw.download;

import com.github.blogw.caoliu.beans.PageLink;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static com.github.blogw.caoliu.constant.WebConstants.SAVE_FOLDER;

/**
 * Created by blogw on 2016/06/02.
 */
public class DLFile {
    private static Log log = LogFactory.getLog(DLFile.class);

    private String url;
    private String referer;
    private String path;
    private int size;
    private int count;
    private int retry;
    private List<ThreadInfo> threads;

    public DLFile(PageLink pageLink) throws IOException {
        this(pageLink, 1);
    }

    public DLFile(PageLink pageLink, int count) throws IOException {
        init(pageLink, count);
        initThread();
        RandomAccessFile raf = null;
        try {
            // init raf
            File f = new File(this.path + "!");
            raf = new RandomAccessFile(f, "rw");
            raf.writeInt(this.size);
            raf.writeShort(this.count);

            for (ThreadInfo info : this.threads) {
                raf.writeInt(info.getStart());
                raf.writeInt(info.getEnd());
                raf.writeInt(info.getCurrent());
            }

            raf.setLength(4 + 2 + 12 * this.count + this.size);
        } finally {
            IOUtils.closeQuietly(raf);
        }
    }

    public DLFile(PageLink pageLink, File f) throws IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(f, "r");
            pageLink.setSize(raf.readInt());
            count = raf.readShort();
            init(pageLink, count);
            for (int i = 0; i < count; i++) {
                int start = raf.readInt();
                int end = raf.readInt();
                int current = raf.readInt();
                ThreadInfo info = new ThreadInfo(start, end, current);
                threads.add(info);
            }
        } finally {
            IOUtils.closeQuietly(raf);
        }
    }

    private void init(PageLink pageLink, int count) {
        this.size = pageLink.getSize();
        this.url = pageLink.getVideoUrl();
        this.referer = pageLink.getReferer2();
        this.path = SAVE_FOLDER + pageLink.getTxt() + File.separator + pageLink.getVideo();
        this.count = count;
        this.threads = new ArrayList<>();
    }

    private void initThread() {
        int block = this.size / this.count;
        int lastend = 0;

        for (int i = 0; i < count; i++) {
            int start;
            int end;
            if (lastend == 0) {
                start = 0;
            } else {
                start = lastend + 1;
            }
            end = start + block;
            if (end > this.size) {
                end = this.size;
            }
            lastend = end;

            ThreadInfo info = new ThreadInfo(start, end);
            this.threads.add(info);
        }
    }

    public String getUrl() {
        return url;
    }

    public String getReferer() {
        return referer;
    }

    public String getPath() {
        return path;
    }

    public int getSize() {
        return size;
    }

    public int getCount() {
        return count;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public List<ThreadInfo> getThreads() {
        return threads;
    }

}
