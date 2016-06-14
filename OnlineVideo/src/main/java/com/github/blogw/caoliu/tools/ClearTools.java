package com.github.blogw.caoliu.tools;

import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.WebConstants;
import com.github.blogw.caoliu.utils.SqliteUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * clear db and dir
 * <p>
 * Created by blogw on 2016/05/29.
 */
public class ClearTools {
    private static Log log = LogFactory.getLog(ClearTools.class);

    public static void main(String[] args) throws Exception {
        clearDir();
        deletePPT();
        deleteFakeVideo();
        deleteNoVideoName();
    }

    /**
     * delete type=PPT from db and disk,
     * because this type can not download.
     *
     * @throws Exception
     */
    private static void deleteNoVideoName() throws Exception {
        List<HashMap<String, String>> list = SqliteUtils.getInstance().select("select * from video where videoname ='' and video !=''");

        for (HashMap<String, String> map : list) {
            PageLink pl = SqliteUtils.convert(map);
            File dir = new File(WebConstants.SAVE_FOLDER + pl.getTxt());
            FileUtils.deleteDirectory(dir);
            SqliteUtils.getInstance().delete(pl.getId());
            log.info(pl.getTxt() + " removed");
        }
    }

    /**
     * delete type=PPT from db and disk,
     * because this type can not download.
     *
     * @throws Exception
     */
    private static void deletePPT() throws Exception {
        List<HashMap<String, String>> list = SqliteUtils.getInstance().select("select * from video where type='PPT'");

        for (HashMap<String, String> map : list) {
            PageLink pl = SqliteUtils.convert(map);
            File dir = new File(WebConstants.SAVE_FOLDER + pl.getTxt());
            FileUtils.deleteDirectory(dir);
            SqliteUtils.getInstance().delete(pl.getId());
            log.info(pl.getTxt() + " removed");
        }
    }

    /**
     * delete type=TADPOLES and size=46241 from db and disk,
     * because this video is fake.
     *
     * @throws Exception
     */
    private static void deleteFakeVideo() throws Exception {
        List<HashMap<String, String>> list = SqliteUtils.getInstance().select("SELECT * from video where size=46241 and type='TADPOLES'");
        for (HashMap<String, String> map : list) {
            PageLink pl = SqliteUtils.convert(map);
            File dir = new File(WebConstants.SAVE_FOLDER + pl.getTxt());
            FileUtils.deleteDirectory(dir);
            SqliteUtils.getInstance().delete(pl.getId());
            log.info(pl.getTxt() + " removed");
        }
    }

    /**
     * remove dir that not in db
     *
     * @throws Exception
     */
    private static void clearDir() throws Exception {
        File dir = new File(WebConstants.SAVE_FOLDER);
        for (File d : dir.listFiles()) {
            if (d.isDirectory()) {
                String txt = d.getName();
                List<HashMap<String, String>> list = SqliteUtils.getInstance().select("select * from video where txt='" + txt + "'");
                if (list.size() == 0) {
                    FileUtils.deleteDirectory(d);
                }
            }
        }
    }
}
