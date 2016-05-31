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
 * download
 */
public class Ant implements Runnable {
    protected static Log log = LogFactory.getLog(Ant.class);

    private PageLink pl;
    private long block;
    private long index;

    private String prefix;
    private TaskStatus status;
    private long current;
    private boolean last;

    public Ant(PageLink pl, long block, long index, boolean last) {
        this.pl = pl;
        this.block = block;
        this.index = index;
        this.last = last;
        prefix = LOG_PREFIX + pl.getTxt();
        status = TaskStatus.RUNNING;
    }

    @Override
    public void run() {
        download();
    }

    private void download() {
        log.info(prefix + index + " start");
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            // define http get
            HttpGet httpGet = new HttpGet(pl.getVideoUrl());
            httpGet.addHeader("User-Agent", USER_AGENT_IPHONE6);
            httpGet.addHeader("Referer", pl.getReferer2());

            // execute http get
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (200 == statusCode) {
                HttpEntity entity = httpResponse.getEntity();

                RandomAccessFile raf = new RandomAccessFile(SAVE_FOLDER + pl.getVideo(), "rw");

                byte[] buffer = new byte[BUFFER];
                int read;

                InputStream is = entity.getContent();
                long l = is.skip(block * index + current);
                if (l != block * index + current) {
                    log.error(prefix + "skip error");
                    status = TaskStatus.ERROR;
                    return;
                }
                raf.seek(block * index + current);

                while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                    if (last) {
                        raf.write(buffer, 0, read);
                        current += read;
                    } else {
                        if (current + read > block) {
                            long o = current + read - block;
                            raf.write(buffer, 0, Integer.parseInt(Long.toString(o)));
                            current = block;
                            break;
                        } else {
                            raf.write(buffer, 0, read);
                            current += read;
                        }
                    }
                }

                raf.close();
                is.close();
                status = TaskStatus.FINISH;
                log.info(prefix + index + " end");
            } else {
                log.error(prefix + "status code is " + statusCode);
                status = TaskStatus.ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = TaskStatus.ERROR;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // ignore this error
            }
        }
    }

    public TaskStatus getStatus() {
        return status;
    }

    public long getCurrent() {
        return current;
    }
}
