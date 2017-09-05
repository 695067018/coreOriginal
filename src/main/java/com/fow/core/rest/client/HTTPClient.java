package com.fow.core.rest.client;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.SyncBasicHttpParams;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Greg on 2016/3/4.
 */
public class HTTPClient {


    private static final int TIMEOUT = 5000;
    private static final int CONNECTION_POOL_MAX_SIZE = 200;


    private static final ReentrantLock lock = new ReentrantLock();
    private static HttpClient httpClient;

    public static HttpClient getHttpClient() {
        if (httpClient == null) {
            try {
                lock.lock();
                if (httpClient == null)
                    httpClient = createDefaultHttpClient();
            } finally {
                lock.unlock();
            }
        }
        return httpClient;
    }

    /**
     * 获取HttpClient
     * @return
     */
    private static HttpClient createDefaultHttpClient() {
        BasicHttpParams params = new SyncBasicHttpParams();

        // set default time out
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);
        params.setParameter(CoreConnectionPNames.SO_KEEPALIVE, true);

        ClientConnectionManager connectionManager = createClientConnectionManager();
        registerHttpScheme(connectionManager);
        registerHttpsScheme(connectionManager);

        return new DefaultHttpClient(connectionManager, params);
    }

    private static ClientConnectionManager createClientConnectionManager() {
        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
        connectionManager.setMaxTotal(CONNECTION_POOL_MAX_SIZE);
        connectionManager.setDefaultMaxPerRoute(CONNECTION_POOL_MAX_SIZE);

        return connectionManager;
    }

    private static void registerHttpScheme(ClientConnectionManager connectionManager){
        connectionManager.getSchemeRegistry().register(new Scheme(HTTPConstants.SCHEME_HTTP,
                HTTPConstants.STANDARD_PORT_HTTP, PlainSocketFactory.getSocketFactory()));
    }


    private static void registerHttpsScheme(ClientConnectionManager connectionManager) {
        TrustManager[] trustManagers = new TrustManager[]{new SelfSignedX509TrustManager()};
        try {
            SSLContext context = SSLContext.getInstance(SSLSocketFactory.TLS);
            context.init(null, trustManagers, null);

            X509HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            SSLSocketFactory socketFactory = new SSLSocketFactory(context, hostnameVerifier);

            Scheme scheme = new Scheme(HTTPConstants.SCHEME_HTTPS, HTTPConstants.STANDARD_PORT_HTTPS, socketFactory);
            connectionManager.getSchemeRegistry().register(scheme);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new HTTPException(e);
        }
    }
}
