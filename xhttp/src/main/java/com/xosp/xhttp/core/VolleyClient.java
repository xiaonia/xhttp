package com.xosp.xhttp.core;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.NoCache;
import com.xosp.xhttp.builder.GetFormBuilder;
import com.xosp.xhttp.builder.PostFormBuilder;
import com.xosp.xhttp.builder.PostJsonBuilder;
import com.xosp.xhttp.inter.VolleyCallback;
import com.xosp.xhttp.stack.OkhttpStack;
import com.xosp.xhttp.utils.HttpsUtils;

import java.util.Map;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/22
 * Description:
 */
public class VolleyClient {

    private static VolleyClient INSTANCE;

    private final RequestQueue mRequestQueue;

    private VolleyClient() {
        // Instantiate the cache
        //Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Cache cache = new NoCache();
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(
                new OkhttpStack(null,
                        HttpsUtils.getSslSocketFactory(null, null, null)));
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();
    }

    public static VolleyClient getInstance() {
        if (INSTANCE == null) {
            synchronized (VolleyClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VolleyClient();
                }
            }
        }
        return INSTANCE;
    }

    public void request(Request<?> request) {
        mRequestQueue.add(request);
    }

    public void cancel(Object tag) {
        if (tag == null) {
            mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        } else {
            mRequestQueue.cancelAll(tag);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void getForm(Object tag, String url, Map<String, String> headers, Map<String, String> params,
                        VolleyCallback<T> callback) {
        Request<T> request = new GetFormBuilder<T>()
                .setRequestTag(tag)
                .setRequestUrl(url)
                .setHeaders(headers)
                .setParams(params)
                .setRequestCallback(callback)
                .request();
    }

    public void postForm(Object tag, String url, Map<String, String> headers, Map<String, String> params,
                         VolleyCallback<?> callback) {
        Request<?> request = new PostFormBuilder<>()
                .setRequestTag(tag)
                .setRequestUrl(url)
                .setHeaders(headers)
                .setParams(params)
                .setRequestCallback(callback)
                .request();
    }

    public void postJson(Object tag, String url, Map<String, String> headers, String json, VolleyCallback<?> callback) {
        Request request = new PostJsonBuilder()
                .setRequestTag(tag)
                .setRequestUrl(url)
                .setHeaders(headers)
                .setRequestBody(json)
                .setRequestCallback(callback)
                .request();
    }

}
