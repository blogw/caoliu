package com.github.blogw.download;

import java.io.Serializable;

/**
 * Created by blogw on 2015/12/24.
 */
public class BlockBean implements Serializable{
    private long start;
    private long end;

    public BlockBean(){
        this.start=0;
        this.end=0;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
