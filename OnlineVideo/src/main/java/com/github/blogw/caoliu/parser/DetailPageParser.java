package com.github.blogw.caoliu.parser;

import com.github.blogw.caoliu.beans.PageLink;

/**
 * Created by blogw on 2016/05/28.
 */
public interface DetailPageParser {
    void parse(String page, PageLink link);
}
