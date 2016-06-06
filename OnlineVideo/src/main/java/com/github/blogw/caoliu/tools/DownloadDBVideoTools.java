package com.github.blogw.caoliu.tools;

import com.github.blogw.caoliu.VedioType;
import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.WebConstants;
import com.github.blogw.caoliu.utils.CaoliuParser;
import com.github.blogw.caoliu.utils.HttpUtils;
import com.github.blogw.caoliu.utils.SqliteUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * https://github.com/sannies/mp4parser
 * <p>
 * Created by blogw on 2016/05/29.
 */
public class DownloadDBVideoTools {
    private static Log log = LogFactory.getLog(DownloadDBVideoTools.class);

    public static void main(String[] args) throws Exception {
        List<HashMap<String, String>> list = SqliteUtils.getInstance().select("select * from video where videook!='1'");
        List<PageLink> pllist = new ArrayList<>();

        for (HashMap<String, String> map : list) {
            String posterok = map.get("posterok");
            String videook = map.get("videook");
            PageLink pl = convert(map);

            log.info(pl.getTxt());

            if (StringUtils.isEmpty(pl.getPosterUrl()) || StringUtils.isEmpty(pl.getVideoUrl())) {
                log.info("parse poster and video true url");
                CaoliuParser.parse(pl);
                SqliteUtils.getInstance().update(pl);
            }

            if (!"1".equals(posterok)) {
                if (pl.getPosterUrl() == null) continue;
                File dir = new File(WebConstants.SAVE_FOLDER + pl.getTxt());
                int b = HttpUtils.downloadPoster(dir, pl);
                if (b == 1) posterok = "1";
            }

            if (!"1".equals(videook)) {
                if (pl.getVideoUrl() == null) continue;
                File dir = new File(WebConstants.SAVE_FOLDER + pl.getTxt());
                int b = HttpUtils.downloadVideo(dir, pl);
                if (b == 1) videook = "1";
                if (b > 1) {
                    log.info("parse poster and video true url");
                    CaoliuParser.parse(pl);
                    SqliteUtils.getInstance().update(pl);

                    int bb = HttpUtils.downloadVideo(dir, pl);
                    if (bb == 1) videook = "1";
                }
            }

            SqliteUtils.getInstance().finish(posterok, videook, pl.getId());
            pllist.add(pl);
        }


    }

    private static PageLink convert(HashMap<String, String> map) {
        PageLink pl = new PageLink();
        pl.setId(map.get("id"));
        pl.setTxt(map.get("txt"));
        pl.setVideo(map.get("videoname"));
        pl.setVideoUrl(map.get("video"));
        pl.setPoster(map.get("postername"));
        pl.setPosterUrl(map.get("poster"));
        pl.setHref(map.get("url"));
        pl.setReferer1(map.get("referer1"));
        pl.setReferer2(map.get("referer2"));
        pl.setSize(Integer.parseInt(map.get("size")));
        pl.setTime(map.get("time"));
        pl.setType(VedioType.getByName(map.get("type")));
        return pl;
    }
}
