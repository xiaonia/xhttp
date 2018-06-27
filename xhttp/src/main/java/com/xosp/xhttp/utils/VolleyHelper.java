package com.xosp.xhttp.utils;

import android.text.TextUtils;

import com.android.volley.Header;
import com.android.volley.Request;
import com.xosp.xhttp.constant.VolleyConstants;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/22
 * Description:
 */
public class VolleyHelper {

    private static final String MARK_QUESTION = "?";
    private static final String MARK_AMPERSAND = "&";
    private static final String MARK_VERTICAL_BAR = "|";
    private static final String MARK_EQUAL = "=";

    public static String getMethodName(int methodCode) {
        switch (methodCode) {
            case Request.Method.DEPRECATED_GET_OR_POST:
                return "POST";
            case Request.Method.GET:
                return "GET";
            case Request.Method.POST:
                return "POST";
            case Request.Method.PUT:
                return "PUT";
            case Request.Method.DELETE:
                return "DELETE";
            case Request.Method.HEAD:
                return "HEAD";
            case Request.Method.OPTIONS:
                return "OPTIONS";
            case Request.Method.TRACE:
                return "TRACE";
            case Request.Method.PATCH:
                return "PATCH";
        }
        return "POST";
    }

    public static String buildHttpUrl(String url, Map<String, String> params, String encoding) {
        if (url == null) {
            url = "";
        }
        if (params == null || params.size() == 0) {
            return url;
        }

        StringBuilder sb = new StringBuilder(url);
        if (sb.indexOf(MARK_QUESTION) < 0) {
            sb.append(MARK_QUESTION);
        }
        if (sb.lastIndexOf(MARK_QUESTION) == sb.length() - 1
                || sb.lastIndexOf(MARK_AMPERSAND) == sb.length() - 1) {
            //nope
        } else {
            sb.append(MARK_AMPERSAND);
        }

        String query = null;
        try {
            query = buildHttpQuery(params, MARK_AMPERSAND, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(query)) {
            return sb.toString();
        }
        sb.append(query);
        return sb.toString();
    }

    public static String buildHttpQuery(Map<String, String> map, String joiner, String encoding)
            throws UnsupportedEncodingException {

        if (joiner == null) {
            joiner = "";
        }
        if (map == null || map.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder("");
        boolean needJoiner = false;
        boolean needEncode = !TextUtils.isEmpty(encoding);
        String key;
        String value;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            key = entry.getKey();
            if (TextUtils.isEmpty(key)) {
                continue;
            }
            value = entry.getValue();

            if (needJoiner) {
                sb.append(joiner);
            }
            if (needEncode) {
                sb.append(URLEncoder.encode(key, encoding));
                sb.append(MARK_EQUAL);
                if (value != null) {
                    sb.append(URLEncoder.encode(value, encoding));
                }
            } else {
                sb.append(key);
                sb.append(MARK_EQUAL);
                if (value != null) {
                    sb.append(value);
                }
            }
            needJoiner = true;
        }

        return sb.toString();
    }

    public static boolean hasResponseBody(int requestMethod, int responseCode) {
        return requestMethod != Request.Method.HEAD
                && !(VolleyConstants.HTTP_CONTINUE <= responseCode && responseCode < HttpURLConnection.HTTP_OK)
                && responseCode != HttpURLConnection.HTTP_NO_CONTENT
                && responseCode != HttpURLConnection.HTTP_NOT_MODIFIED;
    }

}
