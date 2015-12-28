package com.github.blogw.download;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by blogw on 2015/12/24.
 */
public class TaskBean implements Serializable{
    // running
    private TaskStatus status;
    // download link
    private String url;
    // refer link
    private String referer;
    // file name
    private String name;
    // file size
    private long size;
    // thread info
    private List<BlockBean> threads;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<BlockBean> getThreads() {
        return threads;
    }

    public void setThreads(List<BlockBean> threads) {
        this.threads = threads;
    }

    public void initThread(int count){
        threads=new ArrayList<>();

        for(int i=0;i<count;i++){
            threads.add(new BlockBean());
        }
    }
}
