package com.xosp.xhttp.utils;

import com.android.volley.Header;
import com.google.common.net.MediaType;
import com.xosp.xhttp.constant.HeaderConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/26
 * Description:
 */
public class HeaderUtils {

    private static final String DEFAULT_CHARSET = "utf-8";
    private static final String HEADER_CONTENT_TYPE = HeaderConstants.HEAD_KEY_CONTENT_TYPE;
    private static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";

    public static List<Header> convertHeaders(Map<String, List<String>> responseHeaders) {
        List<Header> headerList = new ArrayList<Header>(responseHeaders.size());
        for (Map.Entry<String, List<String>> entry : responseHeaders.entrySet()) {
            // HttpUrlConnection includes the status line as a header with a null key; omit it here
            // since it's not really a header and the rest of Volley assumes non-null keys.
            if (entry.getKey() != null) {
                for (String value : entry.getValue()) {
                    headerList.add(new Header(entry.getKey(), value));
                }
            }
        }
        return headerList;
    }

    public static Map<String, List<String>> convertHeaders(List<Header> allHeaders) {
        if (allHeaders == null) {
            return null;
        }
        if (allHeaders.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, List<String>> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        String key;
        List<String> values;
        // Later elements in the list take precedence.
        for (Header header : allHeaders) {
            key = header.getName();
            values = headers.get(key);
            if (values == null) {
                values = new ArrayList<String>();
                headers.put(key, values);
            }
            values.add(header.getValue());
        }
        return headers;
    }

    public static String parseCharset(Map<String, List<String>> headers) {
        return parseCharset(headers, DEFAULT_CHARSET);
    }

    public static String parseCharset(Map<String, List<String>> headers, String defaultCharset) {
        if (headers == null) {
            return defaultCharset;
        }

        List<String> values = headers.get(HEADER_CONTENT_TYPE);
        if (values == null) {
            return defaultCharset;
        }

        for (String contentType : values) {
            String[] params = contentType.split(";");
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("charset")) {
                        return pair[1];
                    }
                }
            }
        }

        return defaultCharset;
    }

    public static MediaType parseMediaType(Map<String, List<String>> headers) {
        if (headers == null) {
            return null;
        }
        List<String> values = headers.get(HEADER_CONTENT_TYPE);
        if (values == null) {
            return null;
        }

        for (String contentType : values) {
            try {
                MediaType mediaType = MediaType.parse(contentType);
                if (mediaType != null) {
                    return mediaType;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
