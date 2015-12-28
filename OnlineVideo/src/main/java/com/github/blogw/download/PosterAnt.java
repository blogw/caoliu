package com.github.blogw.download;

import com.github.blogw.caoliu.beans.PageLink;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import static com.github.blogw.caoliu.constant.WebConstants.*;

/**
 * Created by blogw on 2015/12/22.
 */
public class PosterAnt implements Runnable {
    protected static Log log = LogFactory.getLog(PosterAnt.class);

    private PageLink pl;
    private String prefix;
    private boolean finished;

    public PosterAnt(PageLink pl) {
        this.pl = pl;
        prefix=LOG_PREFIX + pl.getTxt();
    }

    @Override
    public void run() {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            // define http get
            HttpGet httpGet = new HttpGet(pl.getPosterUrl());
            httpGet.addHeader("User-Agent", USER_AGENT);
            httpGet.addHeader("Referer", pl.getReferer2());

            // execute http get
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (200 == statusCode) {
                HttpEntity entity = httpResponse.getEntity();
                RandomAccessFile raf = new RandomAccessFile(SAVE_FOLDER + pl.getPoster(), "rw");

                byte[] buffer = new byte[BUFFER];
                int read;
                InputStream is = entity.getContent();
                while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                    raf.write(buffer, 0, read);
                }
                raf.close();
                is.close();
                log.info(prefix + " poster download ok");
                finished=true;
            } else {
                log.error(prefix + "status code is " + statusCode);
            }
        } catch (Exception e) {
            log.error(prefix + e.getMessage());
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // ignore this error
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
