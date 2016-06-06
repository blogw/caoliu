package com.github.blogw.download;

import com.github.blogw.caoliu.Progress;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sun.misc.Cleaner;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static com.github.blogw.caoliu.constant.WebConstants.*;

/**
 * Created by blogw on 2016/06/02.
 */
public class DLThread implements Runnable {
    private static Log log = LogFactory.getLog(DLThread.class);

    private DLFile file;
    private ThreadInfo info;
    private int current;
    private int end;
    private int headsize;
    private String runningName;

    public DLThread(DLFile file, ThreadInfo info) {
        this.file = file;
        this.info = info;
        this.current = this.info.getCurrent();
        this.end = this.info.getEnd();

        // int is 4 bytes and short is 2 bytes
        headsize = 4 + 2 + 12 * file.getCount();

        // running name
        runningName = this.file.getPath() + "!";
    }

    @Override
    public void run() {
        log.info("download " + file.getUrl());
        RandomAccessFile raf = null;
        CloseableHttpClient httpClient = null;

        if (!isOver()) {
            try {
                // define random file access
                raf = new RandomAccessFile(runningName, "rw");

                // define http client
                httpClient = HttpClients.createDefault();

                // define http get
                HttpGet httpGet = new HttpGet(this.file.getUrl());
                httpGet.addHeader("User-Agent", USER_AGENT_IPHONE6);
                httpGet.addHeader("Referer", this.file.getReferer());

                // execute http get
                CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                if (200 == statusCode) {
                    HttpEntity entity = httpResponse.getEntity();

                    byte[] buffer = new byte[BUFFER];
                    int read;

                    InputStream is = entity.getContent();
                    is.skip(current);
                    raf.skipBytes(headsize + current);

                    while ((read = is.read(buffer, 0, BUFFER)) != -1) {
                        raf.write(buffer, 0, read);
                        current += read;

                        // todo: 根据线程数skip and calc
                        raf.seek(0);
                        raf.skipBytes(4 + 2 + 4 + 4);
                        raf.writeInt(current);
                        raf.seek(0);
                        raf.skipBytes(headsize + current);

                        info.setCurrent(current);
                        Progress.on(System.out, file.getSize()).tick(current);
                    }
                }
            } catch (FileNotFoundException e1) {
                log.error(SAVE_FOLDER + this.file.getPath() + " not found");
            } catch (ClientProtocolException e2) {
                log.error(e2.getMessage());
            } catch (IOException e3) {
                log.error(e3.getMessage());
            } finally {
                IOUtils.closeQuietly(raf);
                IOUtils.closeQuietly(httpClient);

                if (this.current == this.file.getSize()) {
                    removeHead();
                }
            }
        }
    }

    public boolean isOver() {
        if (this.info.getCurrent() == this.info.getEnd()) {
            return true;
        }
        return false;
    }

    public void removeHead() {
        RandomAccessFile file = null;
        FileChannel chan = null;
        try {
            file = new RandomAccessFile(runningName, "rw");
            chan = file.getChannel();
            MappedByteBuffer buffer = chan.map(FileChannel.MapMode.READ_WRITE, this.headsize, this.file.getSize());

            File f = new File(this.file.getPath());
            FileChannel out = new FileOutputStream(f).getChannel();
            out.write(buffer);
            out.close();

            Cleaner cleaner = ((sun.nio.ch.DirectBuffer) buffer).cleaner();
            if (cleaner != null) {
                cleaner.clean();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(chan);
            IOUtils.closeQuietly(file);
            File f = new File(runningName);
            FileUtils.deleteQuietly(f);
        }
    }
}
