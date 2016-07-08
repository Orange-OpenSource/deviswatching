package com.orange.clara.tool.crawlers.github.client;

import org.eclipse.egit.github.core.client.GitHubClient;

import java.io.IOException;
import java.net.*;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 27/06/2016
 */
public class ProxyGithubClient extends GitHubClient {
    private static final String PROXY_HOST = System.getProperty("http.proxyHost", null);

    private static final String PROXY_PASSWD = System.getProperty("http.proxyPassword", null);

    private static final int PROXY_PORT = Integer.getInteger("http.proxyPort", 80);

    private static final String PROXY_USER = System.getProperty("http.proxyUsername", null);

    @Override
    protected HttpURLConnection createConnection(String uri) throws IOException {
        URL url = new URL(createUri(uri));
        Proxy proxy = null;
        try {
            proxy = this.getProxy();
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
        if (proxy != null) {
            return (HttpURLConnection) url.openConnection(proxy);
        }
        return (HttpURLConnection) url.openConnection();
    }

    private Proxy getProxy() throws Exception {
        if (PROXY_HOST == null) {
            return null;
        }
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
        if (PROXY_USER == null) {
            return proxy;
        }
        Authenticator authenticator = new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication(PROXY_USER,
                        PROXY_PASSWD.toCharArray()));
            }
        };
        Authenticator.setDefault(authenticator);
        return proxy;
    }
}
