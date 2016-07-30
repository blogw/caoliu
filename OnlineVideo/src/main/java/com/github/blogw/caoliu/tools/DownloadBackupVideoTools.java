package com.github.blogw.caoliu.tools;

import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.WebConstants;
import com.github.blogw.caoliu.utils.CaoliuParser;
import com.github.blogw.caoliu.utils.HttpUtils;
import com.github.blogw.caoliu.utils.SqliteUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * https://github.com/sannies/mp4parser
 * <p>
 * Created by blogw on 2016/05/29.
 */
public class DownloadBackupVideoTools {
    private static Log log = LogFactory.getLog(DownloadBackupVideoTools.class);

    public static void main(String[] args) throws Exception {
        List<HashMap<String, String>> list = SqliteUtils.getInstance().select("select * from backup where posterok!='1' or videook!='1' order by time");

        for (HashMap<String, String> map : list) {
            String posterok = map.get("posterok");
            String videook = map.get("videook");
            PageLink pl = SqliteUtils.convert(map);

            log.info(pl.getTxt());
            File dir = new File(WebConstants.SAVE_FOLDER + pl.getTxt());

            if (StringUtils.isEmpty(pl.getPosterUrl()) || StringUtils.isEmpty(pl.getVideoUrl())) {
                log.info("parse poster and video true url");
                CaoliuParser.parse(pl);
                if (StringUtils.isEmpty(pl.getPosterUrl()) || StringUtils.isEmpty(pl.getVideoUrl())) {
                    // url still empty after parse
                    SqliteUtils.getInstance().deleteNotBackup(pl.getId());
                    FileUtils.deleteDirectory(dir);
                    log.info("can not parse this url,remove it");
                    continue;
                } else {
                    SqliteUtils.getInstance().update(pl);
                }
            }

            if (!"1".equals(posterok)) {
                if (pl.getPosterUrl() == null) continue;
                int b = HttpUtils.downloadPoster(dir, pl);
                if (b == 1) posterok = "1";
            }

            Thread.sleep(500);

            if (!"1".equals(videook)) {
                if (pl.getVideoUrl() == null) continue;
                int b = HttpUtils.downloadVideo(dir, pl);
                if (b == 1) {
                    videook = "1";
                } else if (b > 1) {
                    log.info("parse poster and video true url");
                    CaoliuParser.parse(pl);
                    SqliteUtils.getInstance().update(pl);
                    int bb = HttpUtils.downloadVideo(dir, pl);
                    if (bb == 1) videook = "1";
                } else {
                    // download error
                    videook = "0";
                    if (b < 0) {
                        SqliteUtils.getInstance().delete(pl.getId());
                        FileUtils.deleteDirectory(dir);
                        log.info("can not download http://goo.gl,remove it");
                    }
                }

            }

            SqliteUtils.getInstance().updateStatus(posterok, videook, pl.getId());
        }
    }
}
//TODO: move downloaded url from backup to video