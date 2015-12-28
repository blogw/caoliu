package com.github.blogw.caoliu.utils;

import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.DbConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
        if(!checkTableExist()){
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

    private void initConnection(){
        try {
            if(conn==null ||  !conn.isValid(5)){
                log.info("===>get new connection");
                conn = DriverManager.getConnection(DbConstants.DB);
            }
        } catch (SQLException e) {
            throw new RuntimeException("get connection failed.");
        }
    }

    private boolean checkTableExist() {
        List<HashMap<String,String>>list=select(DbConstants.VIDEO_TABLE_EXIST_SQL);
        return list.size()>0;
    }

    private void createTables(){
        try {
            log.info("===>create table");
            conn.createStatement().execute(DbConstants.VIDEO_TABLE_CREATE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("create table failed.");
        }
    }

    public List<HashMap<String,String>> select(String sql, String... args) {
        try {
            PreparedStatement pstm =conn.prepareStatement(sql);
            for(int i=1;i<=args.length;i++) {
                pstm.setString(i,args[i-1]);
            }
            ResultSet rs =pstm.executeQuery();
            ResultSetMetaData rsmd=rs.getMetaData();
            int count = rsmd.getColumnCount();

            List<HashMap<String,String>>list=new ArrayList<>();
            while(rs.next()){
                for(int i=1;i<=count;i++){
                    HashMap<String,String> map=new HashMap<>();
                    String column=rsmd.getColumnName(i);
                    map.put(column,rs.getString(i));
                    list.add(map);
                }
            }
            pstm.close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException("select error.");
        }
    }

    public int insert(PageLink p){
        //url,txt,poster,video,time
        return this.execute(DbConstants.VIDEO_TABLE_INSERT_SQL,p.getHref(),p.getTxt(),p.getPoster(),p.getVideoUrl(),p.getTime());
    }

    public int update(PageLink p){
        return this.execute(DbConstants.VIDEO_TALBE_UPDATE_POSTER_SQL,p.getPoster());
    }

    public int execute(String sql, String... args) {
        try {
            PreparedStatement pstm =conn.prepareStatement(sql);
            for(int i=1;i<=args.length;i++) {
                pstm.setString(i,args[i-1]);
            }
            int result=pstm.executeUpdate();
            pstm.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("select error.");
        }
    }
}
