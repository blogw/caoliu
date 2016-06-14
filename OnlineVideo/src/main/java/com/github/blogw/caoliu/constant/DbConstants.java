package com.github.blogw.caoliu.constant;

/**
 * Created by blogw on 2015/09/25.
 */
public class DbConstants {
    public static final String DB = "jdbc:sqlite://d:/caoliu/sample.db";
    public static final String VIDEO_TABLE_EXIST_SQL = "SELECT name FROM sqlite_master WHERE type='table'";

    public static final String VIDEO_TABLE_CREATE_SQL = "CREATE TABLE video(id integer primary key autoincrement,url text,txt text,poster txt,postername txt,video txt,videoname txt,time txt)";

    public static final String VIDEO_TABLE_INSERT_SQL = "INSERT INTO video(url,txt,poster,postername,video,videoname,time,referer1,referer2,size,type,posterok,videook)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String BACKUP_TABLE_INSERT_SQL = "INSERT INTO backup(url,txt,poster,postername,video,videoname,time,referer1,referer2,size,type,posterok,videook)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public static final String VIDEO_TALBE_UPDATE_POSTER_SQL = "UPDATE video SET poster=?,postername=?,video=?,videoname=?,referer2=?,type=? WHERE id=?";
    public static final String VIDEO_TALBE_UPDATE_SIZE_SQL = "UPDATE video SET size=? WHERE id=?";

    public static final String VIDEO_TALBE_DELETE_SQL = "DELETE FROM video WHERE id=?";

    public static final String VIDEO_TALBE_UPDATE_FINISH = "UPDATE video SET posterok=?,videook=? WHERE id=?";

}
