package com.xosp.xhttp.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.xosp.xhttp.bean.MediaType;
import com.xosp.xhttp.bean.VolleyResult;
import com.xosp.xhttp.constant.HeaderConstants;
import com.xosp.xhttp.constant.VolleyConstants;
import com.xosp.xhttp.inter.VolleyCallback;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/22
 * Description: class for custom network requests.
 */
@SuppressWarnings({"WeakerAccess"})
public class VolleyRequest<T> extends BasicRequest<T> {

    protected String mStringBody;

    public VolleyRequest(int method, String url, VolleyCallback<T> callback) {
        super(method, url, callback);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        byte[] bytes = getStringBody();
        return bytes != null? bytes : super.getBody();
    }

    protected byte[] getStringBody() {
        if (mStringBody != null) {
            try {
                return mStringBody.getBytes(VolleyConstants.DEFAULT_CHARSET);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Response<VolleyResult<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            //parse response media type
            String media = null;
            final Map<String, String> headers = response.headers;
            if (headers != null) {
                String contentType = headers.get(HeaderConstants.HEAD_KEY_CONTENT_TYPE);
                if (contentType != null) {
                    MediaType mediaType = MediaType.parse(contentType);
                    if (mediaType != null) {
                        media = mediaType.type();
                    }
                }
            }

            final Class<T> resultClass = getResultClass();
            if (resultClass == null) {
                return Response.success(new VolleyResult<T>(response, null),
                        HttpHeaderParser.parseCacheHeaders(response));
                //return Response.error(new ParseError(response));
            }

            T result = null;
            if (Bitmap.class.equals(resultClass)) {
                if (MediaType.IMAGE_TYPE.equals(media)) {
                    Bitmap bitmap = BitmapFactory
                            .decodeByteArray(response.data, 0, response.data.length);
                    if (bitmap != null) {
                        result = (T) bitmap;
                    }
                }
            } else {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, VolleyConstants.DEFAULT_CHARSET));

                if (String.class.equals(resultClass) || Object.class.equals(resultClass)) { //Object.class 默认返回String
                    result = (T) jsonString;
                } else if (JSONObject.class.equals(resultClass)) {
                    JSONObject jsonObject = JSON.parseObject(jsonString);
                    if (jsonObject != null) {
                        result = (T) jsonObject;
                    }
                } else {
                    result = JSON.parseObject(jsonString, resultClass);
                }
            }

            if (result != null) {
                return Response.success(new VolleyResult<T>(response, result),
                        HttpHeaderParser.parseCacheHeaders(response));
            }

            return Response.error(new ParseError(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if (volleyError == null) {
            volleyError = new VolleyError(new IllegalStateException("Unknown error."));
        }
        return super.parseNetworkError(volleyError);
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    public void setStringBody(String stringBody) {
        this.mStringBody = stringBody;
    }

}
