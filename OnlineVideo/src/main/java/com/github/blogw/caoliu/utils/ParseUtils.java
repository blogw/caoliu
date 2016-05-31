package com.github.blogw.caoliu.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by blogw on 2016/05/28.
 */
public class ParseUtils {
    /**
     * 从URL中取得文件名
     *
     * @param url URL
     * @return 文件名
     */
    public static String nameFromUrl(String url) {
        int p1 = url.lastIndexOf("/") + 1;
        int p2 = url.indexOf("?");
        if (p2 > 0) {
            return url.substring(p1, p2).trim();
        } else {
            return url.substring(p1).trim();
        }
    }

    public static String evalScript(String js) throws ScriptException {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("javascript");
        StringBuffer sb = new StringBuffer();
        se.put("sb", sb);
        se.eval(js);
        return sb.toString();
    }
}
