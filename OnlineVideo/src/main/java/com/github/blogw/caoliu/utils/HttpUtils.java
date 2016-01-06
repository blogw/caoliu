package com.github.blogw.caoliu.utils;

import com.github.blogw.caoliu.constant.WebConstants;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.StringWriter;

/**
 * Created by blogw on 2015/09/23.
 */
public class HttpUtils {
    private static Log log = LogFactory.getLog(HttpUtils.class);

    public static String readUrl(String url) throws Exception {
        return readUrl(url,url,"GB2312");
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
        httpGet.addHeader("User-Agent", WebConstants.USER_AGENT);
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

        // instance StringWriter
        StringWriter sw = new StringWriter();

        if (statusCode == 200) {
            // copy response to StringWriter when status code is 200
            IOUtils.copy(httpResponse.getEntity().getContent(), sw, encode);
        }

        // close httpclient
        httpClient.close();

        // return url content
        return sw.toString();
    }
}
