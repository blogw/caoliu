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
public class ParserVideoShare implements DetailPageParser {
    @Override
    public void parse(String page, PageLink pl) {
        pl.setType(VedioType.NINEP91);
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
            Pattern p = Pattern.compile("<video.*src='(.*?)'.*poster='(.*?)'", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(page);
            if (m.find()) {
                String file = m.group(1);
                pl.setVideoUrl(file);
                pl.setVideo(ParseUtils.nameFromUrl(file));

                String image = m.group(2);
                pl.setPosterUrl(image);
                pl.setPoster(ParseUtils.nameFromUrl(image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ParserVideoShare p=new ParserVideoShare();
        PageLink pl=new PageLink("","","","");
        p.analyze("http://media.embed.player.videoshare.space/html/aWRfczh2MV8yMjAzNA.html#iframeload",pl);
        System.out.println(pl);
    }
}
