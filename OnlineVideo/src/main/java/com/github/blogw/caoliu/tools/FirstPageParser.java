package com.github.blogw.caoliu.tools;

import com.github.blogw.caoliu.Caoliu;

/**
 * Created by blogw on 2016/05/30.
 */
public class FirstPageParser {
    public static void main(String[] args) throws Exception {
        String url = "http://og.90cl.org/thread0806.php?fid=22";
        Caoliu cl=new Caoliu(url);
        cl.parse();
    }
}
