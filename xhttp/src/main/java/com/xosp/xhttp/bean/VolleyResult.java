package com.xosp.xhttp.bean;

import com.android.volley.NetworkResponse;

/**
 * description: Detailed result information
 * author: xuqingqi
 * e-mail: xuqingqi01@gmail.com
 * date: 2018/6/24
 */
public class VolleyResult<T> {

    private NetworkResponse networkResponse;
    private long networkTimeMs;
    private T responseData;

    public VolleyResult(NetworkResponse networkResponse, T responseData) {
        this.networkResponse = networkResponse;
        this.responseData = responseData;
    }

    public void setNetworkTimeMs(long networkTimeMs) {
        this.networkTimeMs = networkTimeMs;
    }

    public long getNetworkTimeMs() {
        return networkTimeMs;
    }

    public NetworkResponse getNetworkResponse() {
        return networkResponse;
    }

    public void setNetworkResponse(NetworkResponse networkResponse) {
        this.networkResponse = networkResponse;
    }

    public T getResponseData() {
        return responseData;
    }

    public void setResponseData(T responseData) {
        this.responseData = responseData;
    }

}
