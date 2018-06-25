package com.xosp.xhttp.builder;

import com.android.volley.Request;
import com.xosp.xhttp.bean.VolleyResult;
import com.xosp.xhttp.inter.VolleyCallback;
import com.xosp.xhttp.core.VolleyClient;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public abstract class RequestBuilder<T, R extends RequestBuilder> {

    protected String id;
    protected Object tag;

    protected String url;
    protected Map<String, String> headers;
    protected Map<String, String> params;
    protected String body;

    protected VolleyCallback<T> callback;
    protected Class<T> clazz;

    public RequestBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    public R setRequestId(String id) {
        this.id = id;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R setRequestUrl(String url) {
        this.url = url;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R setRequestTag(Object tag) {
        this.tag = tag;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R addHeader(String key, String val) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<String, String>();
        }
        this.headers.put(key, val);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R setParams(Map<String, String> params) {
        this.params = params;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R addParams(String key, String val) {
        if (this.params == null) {
            this.params = new LinkedHashMap<String, String>();
        }
        this.params.put(key, val);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R setRequestCallback(VolleyCallback<? extends T> callback) {
        this.callback = (VolleyCallback<T>) callback;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R setRequestBody(String body) {
        this.body = body;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R setResultClass(Class<? extends T> clazz) {
        this.clazz = (Class<T>) clazz;
        return (R) this;
    }

    public abstract Request<VolleyResult<T>> build();

    public Request<VolleyResult<T>> request() {
        Request<VolleyResult<T>> request = build();
        VolleyClient.getInstance().request(request);
        return request;
    }
    
}
