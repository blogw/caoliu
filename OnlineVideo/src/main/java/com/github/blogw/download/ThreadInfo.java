package com.github.blogw.download;

/**
 * Created by blogw on 2016/06/02.
 */
public class ThreadInfo {
    private int start;
    private int end;
    private int current;
    private int retry;

    public ThreadInfo(int start, int end) {
        this.start = start;
        this.end = end;
        this.current = this.start;
    }

    public ThreadInfo(int start, int end, int current) {
        this.start = start;
        this.end = end;
        this.current = current;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }
}
