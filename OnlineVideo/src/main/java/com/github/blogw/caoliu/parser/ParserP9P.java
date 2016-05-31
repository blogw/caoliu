package com.github.blogw.caoliu.parser;

import com.github.blogw.caoliu.VedioType;
import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.utils.HttpUtils;
import com.github.blogw.caoliu.utils.ParseUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by blogw on 2016/05/28.
 */
public class ParserP9P implements DetailPageParser {
    @Override
    public void parse(String page, PageLink pl) {
        pl.setType(VedioType.P9P);
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

            Pattern p = Pattern.compile("'image': '(.*?)',", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(page);
            if (m.find()) {
                String image = m.group(1);
                pl.setPosterUrl(image);
                pl.setPoster(ParseUtils.nameFromUrl(image));
            }


            p = Pattern.compile("'file': '(.*?)'", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            m = p.matcher(page);
            if (m.find()) {
                String file = m.group(1);
                pl.setVideoUrl(file);
                pl.setVideo(ParseUtils.nameFromUrl(file));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ParserP9P p=new ParserP9P();
        PageLink pl=new PageLink("","","","");
        p.analyze("http://www.avtaobao.me/embed/3376/#iframeload",pl);
    }
}
