package com.github.blogw.download;

import com.github.blogw.caoliu.beans.PageLink;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;

import static com.github.blogw.caoliu.constant.WebConstants.*;

/**
 * Created by blogw on 2016/06/02.
 */
public class DLTask {
    private static Log log = LogFactory.getLog(DLTask.class);

    public static DLFile download(PageLink pageLink) throws IOException {
        return download(pageLink, 1);
    }

    public static DLFile download(PageLink pageLink, int count) throws IOException {
        // define DLFile
        DLFile file = null;

        File f = new File(SAVE_FOLDER + pageLink.getTxt() + File.separator + pageLink.getVideo());
        if (!f.exists()) {
            // check resume
            File ff = new File(SAVE_FOLDER + pageLink.getTxt() + File.separator + pageLink.getVideo() + "!");
            if (ff.exists()) {
                try {
                    file = new DLFile(pageLink, ff);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                int size = checkSize(pageLink.getVideoUrl(), pageLink.getReferer2());
                pageLink.setSize(size);
                file = new DLFile(pageLink, count);
            }

            // start to download
            for (ThreadInfo info : file.getThreads()) {
                Thread t = new Thread(new DLThread(file, info));
                t.start();
            }
        } else {
            log.info(pageLink.getVideo() + " already downloaded");
        }
        return file;
    }

    private static int checkSize(String url, String referer) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        long size = 0l;

        try {
            // define http get
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("User-Agent", USER_AGENT_IPHONE6);
            httpGet.addHeader("Referer", referer);

            // execute http get
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (200 == statusCode) {
                HttpEntity entity = httpResponse.getEntity();
                size = entity.getContentLength();
            } else {
                log.error(LOG_PREFIX + "status code is " + statusCode);
            }
        } catch (Exception e) {
            log.error(LOG_PREFIX + e.getMessage());
        } finally {
            IOUtils.closeQuietly(httpClient);
        }

        if (size > Integer.MAX_VALUE) {
            log.error("too big");
        }
        return Integer.parseInt(Long.toString(size));
    }

    public static void main(String[] args) throws IOException {
        PageLink pl = new PageLink();
        pl.setVideo("SampleVideo_1080x720_10mb.mp4");
        pl.setVideoUrl("http://localhost:8080/SampleVideo_1080x720_10mb.mp4");
        DLTask.download(pl, 1);
    }
}
