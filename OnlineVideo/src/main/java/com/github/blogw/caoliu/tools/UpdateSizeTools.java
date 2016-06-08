package com.github.blogw.caoliu.tools;

import com.github.blogw.caoliu.VedioType;
import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.WebConstants;
import com.github.blogw.caoliu.utils.SqliteUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * download all poster
 * <p>
 * Created by blogw on 2016/05/29.
 */
public class UpdateSizeTools {
    private static Log log = LogFactory.getLog(UpdateSizeTools.class);

    public static void main(String[] args) throws Exception {
        List<HashMap<String, String>> list = SqliteUtils.getInstance().select("select * from video where videook='1' and size=0");

        for (HashMap<String, String> map : list) {
            PageLink pl = convert(map);
            File f = new File(WebConstants.SAVE_FOLDER + pl.getTxt(), pl.getVideo());
            int size = Integer.parseInt(Long.toString(f.length()));
            pl.setSize(size);
            SqliteUtils.getInstance().updateSize(pl);
            log.info(pl.getTxt() + " " + size);
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
