package com.github.blogw.caoliu.parser;

import com.github.blogw.caoliu.VedioType;
import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.utils.HttpUtils;
import com.github.blogw.caoliu.utils.ParseUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by blogw on 2016/05/28.
 */
public class ParserQingYuLe implements DetailPageParser {
    @Override
    public void parse(String page, PageLink pl) {
        pl.setType(VedioType.QINGYULE);
        Pattern p = Pattern.compile("getElementById\\('iframe1'\\)\\.src='(.*?)'", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(page);
        if (m.find()) {
            analyze(m.group(1), pl);
        }
    }

    public void analyze(String url, PageLink pl) {
        try {
            pl.setReferer2(url);
            String page = HttpUtils.readUrl(url);
            //qingyule.me
            Pattern p = Pattern.compile("<video.*poster=\"(.*?)\".*src=\"(.*?)\"", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(page);
            if (m.find()) {
                String image = m.group(1);
                pl.setPosterUrl(image);
                pl.setPoster(ParseUtils.nameFromUrl(image));

                String file = m.group(2);
                pl.setVideoUrl(file);
                pl.setVideo(ParseUtils.nameFromUrl(file));
            }

            //qyule8.com
            if(StringUtils.isEmpty(pl.getVideoUrl())){
                p = Pattern.compile("<source.*src=\"(.*?)\"", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
                m = p.matcher(page);
                if (m.find()) {
                    String file = m.group(1);
                    pl.setVideoUrl(file);
                    pl.setVideo(ParseUtils.nameFromUrl(file));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ParserQingYuLe p=new ParserQingYuLe();
        PageLink pl=new PageLink("","","","");
        p.analyze("http://www.qingyule.me/embed/3671/#iframeload",pl);
    }
}
