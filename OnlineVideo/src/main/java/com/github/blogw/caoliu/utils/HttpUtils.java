package com.github.blogw.caoliu.utils;

import com.github.blogw.caoliu.Progress;
import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.WebConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import static com.github.blogw.caoliu.constant.WebConstants.BUFFER;
import static com.github.blogw.caoliu.constant.WebConstants.USER_AGENT_IPHONE6;

/**
 * Created by blogw on 2015/09/23.
 */
public class HttpUtils {
    private static Log log = LogFactory.getLog(HttpUtils.class);

    public static String readUrl(String url) throws Exception {
        return readUrl(url, url, "UTF-8");
    }

    public static String readUrl(String url, String referer, String encode) throws Exception {
        log.info("===>" + url);

        // get default httpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // set proxy
//        HttpHost proxy = new HttpHost(ipPortResult[0], Integer.parseInt(ipPortResult[1]));
//        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

        // new HttpGet instance
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        // set http header
        httpGet.addHeader("User-Agent", WebConstants.USER_AGENT_IPHONE6);
        httpGet.addHeader("Referer", referer);

        // get http response
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        // get status code
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        log.debug("statusCode:" + statusCode);

        // debug all header
        Header[] headers = httpResponse.getAllHeaders();
        for (Header header : headers) {
            log.debug(header.getName() + "=" + header.getValue());
        }

        String result = "";
        if (statusCode == 200) {
            result = EntityUtils.toString(httpResponse.getEntity(), encode);
        }

        // close httpclient
        httpClient.close();

        // return url content
        return result;
    }

    public static int downloadPoster(File dir, PageLink pl) {
        log.info("download " + pl.getPosterUrl());
        return download(dir, pl.getPoster(), pl.getPosterUrl(), pl.getReferer2());
    }

    public static int downloadVideo(File dir, PageLink pl) {
        log.info("download " + pl.getVideoUrl());
        return download(dir, pl.getVideo(), pl.getVideoUrl(), pl.getReferer2());
    }

    private static int download(File dir, String name, String url, String referer) {
        if (!dir.exists()) {
            try {
                FileUtils.forceMkdir(dir);
            } catch (IOException e) {
                log.error(dir.getName() + " create fail");
                return 0;
            }
        }

        File target = new File(dir, name);
        if (target.exists()) return 1;

        CloseableHttpClient httpClient = HttpClients.createDefault();

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
                long size = httpResponse.getEntity().getContentLength();
                RandomAccessFile raf = new RandomAccessFile(target, "rw");

                byte[] buffer = new byte[BUFFER];
                int read;
                long total = 0;
                InputStream is = entity.getContent();
                while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                    raf.write(buffer, 0, read);
                    total += read;
                    Progress.on(System.out, size).tick(total);

                    // change line when finish
                    if (total >= size) {
                        System.out.println("");
                    }
                }
                raf.close();
                is.close();
                return 1;
            } else {
                log.error("status code is " + statusCode);
                return statusCode;
            }
        } catch (Exception e) {
            log.error("download error is " + e.getMessage());
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // ignore this error
            }
        }
        return 0;
    }
}
