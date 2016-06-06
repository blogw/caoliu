package com.github.blogw.caoliu.utils;

import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.DbConstants;
import com.github.blogw.caoliu.constant.WebConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by blogw on 2015/09/25.
 */
public class SqliteUtils {
    Connection conn = null;
    private static volatile SqliteUtils instance;
    private static Log log = LogFactory.getLog(SqliteUtils.class);

    static {
        try {
            // load the sqlite-JDBC driver using the current class loader
            log.info("===>load sqlite jdbc driver.");
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("org.sqlite.JDBC not found.");
        }
    }

    private SqliteUtils() {
        initConnection();
        if (!checkTableExist()) {
            createTables();
        }
    }

    public static SqliteUtils getInstance() {
        if (instance == null) {
            synchronized (SqliteUtils.class) {
                if (instance == null) {
                    instance = new SqliteUtils();
                }
            }
        }
        return instance;
    }

    private void initConnection() {
        try {
            if (conn == null || !conn.isValid(5)) {
                log.info("===>get new connection");
                conn = DriverManager.getConnection(DbConstants.DB);
            }
        } catch (SQLException e) {
            throw new RuntimeException("get connection failed.");
        }
    }

    private boolean checkTableExist() {
        List<HashMap<String, String>> list = select(DbConstants.VIDEO_TABLE_EXIST_SQL);
        return list.size() > 0;
    }

    private void createTables() {
        try {
            log.info("===>create table");
            conn.createStatement().execute(DbConstants.VIDEO_TABLE_CREATE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("create table failed.");
        }
    }

    public List<HashMap<String, String>> select(String sql, String... args) {
        try {
            PreparedStatement pstm = conn.prepareStatement(sql);
            for (int i = 1; i <= args.length; i++) {
                pstm.setString(i, args[i - 1]);
            }
            ResultSet rs = pstm.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();

            List<HashMap<String, String>> list = new ArrayList<>();
            while (rs.next()) {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 1; i <= count; i++) {
                    String column = rsmd.getColumnName(i);
                    map.put(column, rs.getString(i));
                }
                list.add(map);
            }
            pstm.close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException("select error.");
        }
    }

    public int insert(PageLink p) throws SQLException {
        //url,txt,poster,postername,video,videoname,time,referer1,referer2,size,type,posterok,videook

        String posterName = p.getPoster() == null ? "99999.999" : p.getPoster();
        String videoName = p.getVideo() == null ? "99999.999" : p.getVideo();
        File poster = new File(WebConstants.SAVE_FOLDER + p.getTxt(), posterName);
        File video = new File(WebConstants.SAVE_FOLDER + p.getTxt(), videoName);
        int posterok = poster.exists() ? 1 : 0;
        int videook = video.exists() ? 1 : 0;

        return this.execute(DbConstants.VIDEO_TABLE_INSERT_SQL,
                p.getHref(),
                CaoliuUtils.folderNameFilter(p.getTxt()),
                p.getVideoUrl() == null ? "" : p.getVideoUrl().toString(),
                p.getPoster() == null ? "" : p.getPoster().toString(),
                p.getVideoUrl() == null ? "" : p.getVideoUrl().toString(),
                p.getVideo() == null ? "" : p.getVideo().toString(),
                p.getTime() == null ? "" : p.getTime().toString(),
                p.getReferer1() == null ? "" : p.getReferer1().toString(),
                p.getReferer2() == null ? "" : p.getReferer2().toString(),
                p.getSize(),
                p.getType() == null ? "" : p.getType().toString(),
                posterok,
                videook);
    }

    //UPDATE video SET poster=?,postername=?,video=?,videourl=?,referer2=?,type=? WHERE id=?
    public int update(PageLink p) throws SQLException {
        return this.execute(DbConstants.VIDEO_TALBE_UPDATE_POSTER_SQL,
                p.getPosterUrl(), p.getPoster(),
                p.getVideoUrl(), p.getVideo(),
                p.getReferer2(), p.getType().toString(), p.getId()
        );
    }

    public int finish(String ok1, String ok2, String id) throws SQLException {
        return this.execute(DbConstants.VIDEO_TALBE_UPDATE_FINISH, ok1, ok2, id);
    }

    public int execute(String sql, Object... args) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement(sql);
        for (int i = 1; i <= args.length; i++) {
            if (args[i - 1] instanceof java.lang.String) {
                pstm.setString(i, args[i - 1].toString());
            } else {
                pstm.setInt(i, Integer.parseInt(args[i - 1].toString()));
            }
        }
        int result = pstm.executeUpdate();
        pstm.close();
        return result;
    }
}
