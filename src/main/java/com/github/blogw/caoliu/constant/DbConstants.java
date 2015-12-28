package com.github.blogw.caoliu.constant;

/**
 * Created by blogw on 2015/09/25.
 */
public class DbConstants {
    public static final String DB = "jdbc:sqlite:sample.db";
    public static final String VIDEO_TABLE_EXIST_SQL = "SELECT name FROM sqlite_master WHERE type='table'";

    public static final String VIDEO_TABLE_CREATE_SQL = "CREATE TABLE video(id integer primary key autoincrement,url text,txt text,poster txt,postername txt,video txt,videoname txt,time txt)";

    public static final String VIDEO_TABLE_INSERT_SQL = "INSERT INTO video(url,txt,poster,video,time)VALUED(?,?,?,?,?)";

    public static final String VIDEO_TALBE_UPDATE_POSTER_SQL="UPDATE video SET postername=? WHERE id=?";

    public static final String VIDEO_TALBE_UPDATE_VIDEO_SQL="UPDATE video SET videoname=? WHERE id=?";

}
