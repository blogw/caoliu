package com.github.blogw.caoliu.utils;

import com.github.blogw.caoliu.VedioType;
import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.WebConstants;
import com.github.blogw.download.Manager;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by blogw on 2015/12/21.
 */
public class CaoliuParser {
    public static void parse(PageLink pl) {
        try {
            String page1 = HttpUtils.readUrl(pl.getUrl());

            Pattern pattern = Pattern.compile("<embed src=\"(.*?)\"", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(page1);
            if (matcher.find()) {
                pl.setType(VedioType.DEFAULT);
                up2stream(matcher.group(1), pl);
            } else {
                if (page1.indexOf("up2stream.com") > 0) {
                    pl.setType(VedioType.UP2STREAM);
                    Pattern p = Pattern.compile("getElementById\\('iframe1'\\)\\.src='(.*?)'", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(page1);
                    if (m.find()) {
                        up2stream(m.group(1), pl);
                    }
                } else if (page1.indexOf("ppt.cc") > 0) {
                    pl.setType(VedioType.PPT);
                    Pattern p = Pattern.compile("getElementById\\('iframe1'\\)\\.src='(.*?)'", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(page1);
                    if (m.find()) {
                        pptcc(m.group(1), pl);
                    }
                } else {
                    //TODO: 其他未处理的站点
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void up2stream(String url, PageLink pl) throws Exception {
        pl.setReferer2(url);
        String page = HttpUtils.readUrl(url);

        // poster url
        Pattern p1 = Pattern.compile("<video.*poster=\"(.*?)\"", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher m1 = p1.matcher(page);
        if (m1.find()) {
            String u = m1.group(1);
            pl.setPosterUrl(u);
            pl.setPoster(nameFromUrl(u));
        }

        //video url
        Pattern p2 = Pattern.compile("(eval.*?\\{\\}\\)\\))", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher m2 = p2.matcher(page);
        if (m2.find()) {
            String u = m2.group(1);
            u = u.replaceAll("return p", "return sb.append(p)");
            u = evalScript(u);
            u = u.replaceAll(".*,\"(.*)\"\\);", "$1");
            pl.setVideoUrl(u);
            pl.setVideo(nameFromUrl(u));
        }
    }

    private static void pptcc(String url, PageLink pl) throws Exception {
        pl.setReferer2(url);
        String page = HttpUtils.readUrl(url);

        Pattern p = Pattern.compile("(eval.*?)</script>", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(page);
        if (m.find()) {
            String u = m.group(1);
            u = u.replaceAll("return p", "return sb.append(p)");
            u = evalScript(u);

            String image = evalScript(u + "sb.append(config.image);");
            pl.setPosterUrl(image);
            pl.setPoster(nameFromUrl(image));

            String file = evalScript(u + "sb.append(config.file);");
            pl.setVideoUrl(file);
            pl.setVideo(nameFromUrl(file));
        }
    }

    /**
     * 从URL中取得文件名
     *
     * @param url URL
     * @return 文件名
     */
    private static String nameFromUrl(String url) {
        int p1 = url.lastIndexOf("/") + 1;
        int p2 = url.indexOf("?");
        if (p2 > 0) {
            return url.substring(p1, p2).trim();
        } else {
            return url.substring(p1).trim();
        }
    }

    private static String evalScript(String js) throws ScriptException {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("javascript");
        StringBuffer sb = new StringBuffer();
        se.put("sb", sb);
        se.eval(js);
        return sb.toString();
    }


}
