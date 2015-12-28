package com.github.blogw.caoliu.utils;

import com.github.blogw.caoliu.beans.PageLink;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by blogw on 2015/12/25.
 */
public class HtmlUtils {
    public static List<PageLink> parseLinks(String html,String referer){
        Document doc = Jsoup.parse(html);
        List<PageLink>links=new ArrayList<>();

        for(Element link : doc.select("a")) {
            if(link.children().size()==0 && StringUtils.isBlank(link.attr("class"))){
                String text=link.ownText();
                Pattern pattern = Pattern.compile("(.*?)\\[(.*?)\\]");
                Matcher matcher = pattern.matcher(text);

                if(matcher.find()) {
                    String txt = matcher.group(1).trim();
                    String time=matcher.group(2);
                    String href=link.attr("href");
                    PageLink pl=new PageLink(txt,href,time,referer);
                    links.add(pl);
                }
            }
        }
        return links;
    }
}
