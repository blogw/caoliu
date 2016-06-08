package com.github.blogw.caoliu;

import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.WebConstants;
import com.github.blogw.caoliu.utils.HtmlUtils;
import com.github.blogw.caoliu.utils.HttpUtils;
import com.github.blogw.caoliu.utils.SqliteUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

/**
 * Created by blogw on 2015/09/24.
 */
public class Caoliu {
    private static Log log = LogFactory.getLog(Caoliu.class);
    private String url;
    private Stack<PageLink> stack = new Stack<>();
    String ymd = DateFormatUtils.format(Calendar.getInstance(), "yyyyMMdd");

    public Caoliu(String url) {
        this.url = url;
    }

    public void parse() throws Exception {
        //Runtime.getRuntime().addShutdownHook(xxx);

        // get referer from url
        String referer = url.replaceAll("(.*?)://(.*?)/.*", "$1://$2");

        // parse list page
        String html = HttpUtils.readUrl(url, referer, WebConstants.CAOLIU_ENCODE);

        List<PageLink> links = HtmlUtils.parseLinks(html, referer);
        links = Lists.reverse(links);

        for (PageLink pl : links) {
            try {
                SqliteUtils.getInstance().insert(pl);
                log.info("insert " + pl.getTxt());
            } catch (SQLException e) {
                log.info("insert " + pl.getTxt() + " error:" + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // analyze all link of page, got poster and video url from this links and save to disk
        String url = "http://og.90cl.org/thread0806.php?fid=22";
        Caoliu cl = new Caoliu(url);
        cl.parse();
    }
}
