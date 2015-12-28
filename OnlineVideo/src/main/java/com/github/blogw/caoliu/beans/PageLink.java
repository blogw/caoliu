package com.github.blogw.caoliu.beans;

import com.github.blogw.caoliu.VedioType;

import java.io.Serializable;

/**
 * Created by blogw on 2015/09/24.
 */
public class PageLink implements Serializable{
    private VedioType type;

    private String id;
    private String txt;
    private String href;
    private String time;
    private String referer1;
    private String referer2;
    private String poster;
    private String posterUrl;
    private String video;
    private String videoUrl;
    private long size;

    public PageLink(String txt,String href,String time,String referer){
        this.txt=txt;
        this.href=href;
        this.time=time;
        this.referer1=referer;
    }

    public String getUrl(){
        return getReferer1() + "/" + getHref();
    }

    public VedioType getType() {
        return type;
    }

    public void setType(VedioType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReferer1() {
        return referer1;
    }

    public void setReferer1(String referer1) {
        this.referer1 = referer1;
    }

    public String getReferer2() {
        return referer2;
    }

    public void setReferer2(String referer2) {
        this.referer2 = referer2;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
