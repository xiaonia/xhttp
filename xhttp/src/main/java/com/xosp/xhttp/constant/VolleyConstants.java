package com.xosp.xhttp.constant;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/22
 * Description:
 */
public class VolleyConstants {

    public static final int HTTP_CONTINUE = 100;

    public static final String DEFAULT_CHARSET = "utf-8";

    public static final String CONTENT_TYPE_JSON =
            String.format("application/json; charset=%s", DEFAULT_CHARSET);

    public static final String CONTENT_TYPE_FORM =
            String.format("application/x-www-form-urlencoded; charset=%s", DEFAULT_CHARSET);

    public static final String CONTENT_TYPE_PLAIN =
            String.format("text/plain; charset=%s", DEFAULT_CHARSET);

    public static final String CONTENT_TYPE_STREAM =
            "application/octet-stream";


}
