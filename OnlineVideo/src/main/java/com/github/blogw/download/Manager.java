package com.github.blogw.download;

import com.github.blogw.caoliu.Progress;
import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.utils.CaoliuParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.blogw.caoliu.constant.WebConstants.*;

/**
 * Created by blogw on 2015/12/22.
 */
public class Manager {
    protected static Log log = LogFactory.getLog(Manager.class);
    private static int retry = RETRY;

    public static boolean start(PageLink pl) {
        retry = RETRY;
        // http://stackoverflow.com/questions/20498831/refused-to-display-in-a-frame-because-it-set-x-frame-options-to-sameorigin
        long size = checkSize(pl.getVideoUrl(), pl.getReferer2());
        pl.setSize(size);

        // 文件<10M的情况
        if (size>10 * M_BYTES) {
            log.info(LOG_PREFIX + pl.getTxt() + ":" + size);
            Progress p = Progress.on(System.out, size);

            PosterAnt a = new PosterAnt(pl);
            new Thread(a).start();

            int count=5;
            long per = size / count;
            List<Ant> list=new ArrayList<>();
            for (int i = 0; i < count; i++) {
                Ant ant = new Ant(pl, per, i, i == count-1);
                Thread t = new Thread(ant);
                t.start();
                list.add(ant);
            }

            for (; ; ) {
                long all=0;

                for (int i = 0; i < count; i++) {
                    Ant ant=list.get(i);
                    if (ant.getCurrent() > 0 && a.isFinished()) {
                        all+=ant.getCurrent();
                    }
                }

                if(all==size){
                    return true;
                }

                try {
                    p.tick(all);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        return false;
    }

    private static long checkSize(String url, String referer) {
        if (retry <= 0) {
            return 0l;
        }

        log.info(LOG_PREFIX + "begin to get size of " + url);
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            // define http get
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("User-Agent", USER_AGENT);
            httpGet.addHeader("Referer", referer);

            // execute http get
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (200 == statusCode) {
                HttpEntity entity = httpResponse.getEntity();
                long size = entity.getContentLength();
                log.info(LOG_PREFIX + " file size is " + size);
                return size;
            } else {
                log.error(LOG_PREFIX + "status code is " + statusCode);
                return -1*statusCode;
            }
        } catch (Exception e) {
            log.error(LOG_PREFIX + e.getMessage());
            retry--;
            return checkSize(url, referer);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // ignore this error
            }
        }
    }

    public static void main(String[] args) {
        String u = "http://localhost:8080/test.mp4";
    }
}
