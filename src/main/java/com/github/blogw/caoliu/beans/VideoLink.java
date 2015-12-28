package com.github.blogw.caoliu.beans;

/**
 * Created by blogw on 2015/09/24.
 */
public class VideoLink {
    private String poster;
    private String source;
    private String posterName;
    private String sourceName;

    public VideoLink(String poster, String source) {
        this.poster = poster;
        this.source = source;

        int p=this.poster.lastIndexOf("/");
        this.posterName=this.poster.substring(p+1);
        this.sourceName=this.source.replaceAll(".*/(.*)\\?.*","$1");
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPosterName() {
        return posterName;
    }

    public String getSourceName() {
        return sourceName;
    }
}
