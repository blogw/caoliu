package com.github.blogw.caoliu.utils;

import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.WebConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import static com.github.blogw.caoliu.constant.WebConstants.*;

/**
 * Created by blogw on 2015/09/23.
 */
public class HttpUtils {
    private static Log log = LogFactory.getLog(HttpUtils.class);

    public static String readUrl(String url) throws Exception {
        return readUrl(url, url, "GB2312");
    }

    public static String readUrl(String url, String referer, String encode) throws Exception {
        log.info("===>" + url);

        // get default httpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // new HttpGet instance
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(3000)
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
            //result = EntityUtils.toString(httpResponse.getEntity(), encode);
            result = HttpUtils.toUTF8String(httpResponse.getEntity(), Charset.forName(encode));
        }

        // close httpclient
        httpClient.close();

        // return url content
        return result;
    }

    public static void downloadPoster(PageLink pl) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            // define http get
            HttpGet httpGet = new HttpGet(pl.getPosterUrl());
            httpGet.addHeader("User-Agent", USER_AGENT_IPHONE6);
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
                log.info(pl.getTxt() + " poster download ok");
            } else {
                log.error(pl.getTxt() + " poster download error, status code is " + statusCode);
            }
        } catch (Exception e) {
            log.error(pl.getTxt() + " poster download error, " + e.getMessage());
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // ignore this error
            }
        }
    }

    public static String toUTF8String(
            final HttpEntity entity, final Charset defaultCharset) throws IOException, ParseException {
        Args.notNull(entity, "Entity");
        final InputStream instream = entity.getContent();
        if (instream == null) {
            return null;
        }
        try {
            Args.check(entity.getContentLength() <= Integer.MAX_VALUE,
                    "HTTP entity too large to be buffered in memory");
            int i = (int)entity.getContentLength();
            if (i < 0) {
                i = 4096;
            }
            Charset charset = null;
            try {
                final ContentType contentType = ContentType.get(entity);
                if (contentType != null) {
                    charset = contentType.getCharset();
                }
            } catch (final UnsupportedCharsetException ex) {
                if (defaultCharset == null) {
                    throw new UnsupportedEncodingException(ex.getMessage());
                }
            }
            if (charset == null) {
                charset = defaultCharset;
            }
            if (charset == null) {
                charset = HTTP.DEF_CONTENT_CHARSET;
            }
            final Reader reader = new InputStreamReader(instream, charset);
            final CharArrayBuffer buffer = new CharArrayBuffer(i);
            final char[] tmp = new char[1024];
            int l;
            while((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
            return buffer.toString();
        } finally {
            instream.close();
        }
    }
}
