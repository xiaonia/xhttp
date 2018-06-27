package com.xosp.xhttp.stack;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.HurlStack;
import com.xosp.xhttp.utils.HeaderUtils;
import com.xosp.xhttp.utils.VolleyHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpMethod;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/22
 * Description:
 */
public class OkhttpStack extends BaseHttpStack{

    private static final int SOCKET_TIMEOUT_MILLS = 15000;

    private final HurlStack.UrlRewriter mUrlRewriter;
    @SuppressWarnings({"FieldCanBeLocal"})
    private final SSLSocketFactory mSslSocketFactory;
    @SuppressWarnings({"FieldCanBeLocal"})
    private final HostnameVerifier mHostnameVerifier;

    private final OkHttpClient mOkHttpClient;

    @SuppressWarnings({"unused"})
    public OkhttpStack() {
        this(null);
    }

    /**
     * @param urlRewriter Rewriter to use for request URLs
     */
    @SuppressWarnings("WeakerAccess")
    public OkhttpStack(HurlStack.UrlRewriter urlRewriter) {
        this(urlRewriter, null);
    }

    /**
     * @param urlRewriter Rewriter to use for request URLs
     * @param sslSocketFactory SSL factory to use for HTTPS connections
     */
    public OkhttpStack(HurlStack.UrlRewriter urlRewriter, SSLSocketFactory sslSocketFactory) {
        mUrlRewriter = urlRewriter;
        mSslSocketFactory = sslSocketFactory;
        mHostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(SOCKET_TIMEOUT_MILLS, TimeUnit.SECONDS);
        builder.readTimeout(SOCKET_TIMEOUT_MILLS, TimeUnit.SECONDS);
        builder.writeTimeout(SOCKET_TIMEOUT_MILLS, TimeUnit.SECONDS);
        if (mSslSocketFactory != null) {
            builder.sslSocketFactory(mSslSocketFactory);
        }
        builder.hostnameVerifier(mHostnameVerifier);
        mOkHttpClient = builder.build();
    }

    @Override
    public HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders)
            throws IOException, AuthFailureError {
        //generate url
        String url = request.getUrl();
        if (mUrlRewriter != null) {
            String rewritten = mUrlRewriter.rewriteUrl(url);
            if (rewritten == null) {
                throw new IOException("URL blocked by rewriter: " + url);
            }
            url = rewritten;
        }

        //generate header
        HashMap<String, String> map = new HashMap<String, String>();
        map.putAll(request.getHeaders());
        map.putAll(additionalHeaders);
        Headers.Builder headersBuilder = new Headers.Builder();
        String key;
        String value;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            key = entry.getKey();
            if (!TextUtils.isEmpty(key)) {
                value = entry.getValue();
                headersBuilder.add(key, value);
            }
        }

        //generate body
        final String method = VolleyHelper.getMethodName(request.getMethod());
        final MediaType mediaType = MediaType.parse(request.getBodyContentType());
        final byte[] bytes = request.getBody();
        final RequestBody requestBody ;
        if (!HttpMethod.permitsRequestBody(method)) {
            requestBody = null;
        } else if (HttpMethod.requiresRequestBody(method)) {
            requestBody = RequestBody.create(mediaType, bytes == null? new byte[0] : bytes);
        } else {
            requestBody = bytes == null? null : RequestBody.create(mediaType, bytes);
        }

        //execute request
        final okhttp3.Request okhttpRequest = new okhttp3.Request.Builder()
                .url(url)
                .headers(headersBuilder.build())
                .method(method, requestBody)
                .build();
        final Call call = mOkHttpClient.newCall(okhttpRequest);
        final Response okhttpResponse = call.execute();

        //parse response
        final int statusCode = okhttpResponse.code();
        if (statusCode == -1) {
            // Signal to the caller that something was wrong with the connection.
            throw new IOException("Could not retrieve response code from response.");
        }

        ResponseBody responseBody = okhttpResponse.body();
        final List<Header> headers = HeaderUtils.convertHeaders(
                okhttpResponse.headers().toMultimap());
        if (responseBody != null) {
            final int contentLength = (int) responseBody.contentLength();
            final InputStream inputStream = responseBody.byteStream();
            return new HttpResponse(statusCode, headers, contentLength, inputStream);
        }

        return new HttpResponse(statusCode, headers);
    }

}
