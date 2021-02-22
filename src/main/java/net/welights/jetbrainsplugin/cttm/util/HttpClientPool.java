package net.welights.jetbrainsplugin.cttm.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.net.URLDecoder;

public class HttpClientPool {

    private static volatile HttpClientPool INSTANCE;
    private PoolingHttpClientConnectionManager connectionManager;

    private HttpClientPool() {
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(20);
        connectionManager.setDefaultMaxPerRoute(100);
    }

    public static synchronized HttpClientPool getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpClientPool.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpClientPool();
                }
            }
        }
        return INSTANCE;
    }

    private CloseableHttpClient getHttpClient() {
        return getHttpClient(2000, 2000);
    }

    private CloseableHttpClient getHttpClient(int connectionTimeout, int socketTimeOut) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionTimeout).setSocketTimeout(socketTimeOut).build();
        return HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig).build();
    }

    public String get(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        return getResponseContent(url, httpGet);
    }

    private String getResponseContent(String url, HttpRequestBase request) throws Exception {
        HttpResponse response = null;
        try {
            response = this.getHttpClient().execute(request);
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw new Exception("got an error from HTTP for url : " + URLDecoder.decode(url, "UTF-8"), e);
        } finally {
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
            request.releaseConnection();
        }
    }

}
