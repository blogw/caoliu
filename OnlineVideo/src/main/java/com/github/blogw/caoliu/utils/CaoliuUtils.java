package com.github.blogw.caoliu.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by blogw on 2016/05/29.
 */
public class CaoliuUtils {
    public static String folderNameFilter(String input) {
        StringBuffer filtered = new StringBuffer(input.length());
        char c;
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            if (c == '\\') {
                continue;
            } else if (c == '/') {
                continue;
            } else if (c == ':') {
                continue;
            } else if (c == '*') {
                continue;
            } else if (c == '?') {
                continue;
            } else if (c == '"') {
                continue;
            } else if (c == '<') {
                continue;
            } else if (c == '>') {
                continue;
            } else if (c == '|') {
                continue;
            } else {
                filtered.append(c);
            }
        }
        return StringUtils.trim(filtered.toString());
    }
}
