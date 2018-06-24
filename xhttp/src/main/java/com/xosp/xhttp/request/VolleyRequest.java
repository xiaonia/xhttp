package com.xosp.xhttp.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.xosp.xhttp.bean.VolleyResult;
import com.xosp.xhttp.inter.VolleyCallback;
import com.xosp.xhttp.constant.VolleyConstants;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/22
 * Description: class for custom network requests.
 */
@SuppressWarnings({"WeakerAccess"})
public class VolleyRequest<T> extends Request<VolleyResult<T>> {

    /** Lock to guard mListener as it is cleared on cancel() and read on delivery. */
    protected final Object mLock = new Object();

    protected Class<T> mReturnType;
    protected String mContentType;
    protected final Map<String, String> mHeaderMap;
    protected final Map<String, String> mParamMap;
    protected String mRequestBody;

    protected String mRequestId;
    protected VolleyCallback<T> mCallback;

    public VolleyRequest(int method, String url, VolleyCallback<T> callback) {
        super(method, url, callback);
        mCallback = callback;
        mHeaderMap = new HashMap<String, String>();
        mParamMap = new HashMap<String, String>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return getParamMap();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return getHeaderMap();
    }

    @Override
    public String getBodyContentType() {
        if (mContentType != null) {
            return mContentType;
        }
        return super.getBodyContentType();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        final byte[] bytes = getRequestBody();
        if (bytes != null) {
            return bytes;
        }
        return super.getBody();
    }

    protected byte[] getRequestBody() {
        if (mRequestBody != null) {
            try {
                return mRequestBody.getBytes(VolleyConstants.DEFAULT_CHARSET);
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
            final Class<T> resultClass = getResultClass();
            if (resultClass == null) {
                return Response.error(new ParseError(response));
            }

            T result = null;
            if (Bitmap.class.isAssignableFrom(resultClass)) {
                Bitmap bitmap =
                        BitmapFactory.decodeByteArray(response.data, 0, response.data.length);
                if (bitmap != null) {
                    result = (T) bitmap;
                }
            } else {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, VolleyConstants.DEFAULT_CHARSET));
                if (String.class.isAssignableFrom(resultClass)) {
                    result = (T) jsonString;
                } else if (JSONObject.class.isAssignableFrom(resultClass)) {
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
    protected void deliverResponse(VolleyResult<T> response) {
        Response.Listener<VolleyResult<T>> listener;
        synchronized (mLock) {
            listener = mCallback;
        }
        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
    }

    @Override
    public void cancel() {
        super.cancel();
        synchronized (mLock) {
            this.mCallback = null;
        }
        //TODO
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public Class<T> getResultClass() {
        //如果mCallback是匿名内部类，则可以通过此方法获取泛型参数
        if (mReturnType == null && mCallback != null) {
            try {
                Type type = mCallback.getClass().getGenericSuperclass();
                if (type instanceof ParameterizedType) {
                    ParameterizedType paramType = (ParameterizedType) type;
                    Type rawType = paramType.getRawType();
                    if (VolleyCallback.class.isAssignableFrom((Class<?>) rawType)) {
                        Type[] types = paramType.getActualTypeArguments();
                        if (types != null && types.length > 0) {
                            mReturnType = (Class<T>) types[0];
                            return mReturnType;
                        }
                    }
                }

                Type[] interfaces = mCallback.getClass().getGenericInterfaces();
                if (interfaces != null && interfaces.length > 0) {
                    for (int i = 0; i < interfaces.length; i++) {
                        type = interfaces[i];
                        if (type instanceof ParameterizedType) {
                            ParameterizedType paramType = (ParameterizedType) type;
                            Type rawType = paramType.getRawType();
                            if (VolleyCallback.class.isAssignableFrom((Class<?>) rawType)) {
                                Type[] types = paramType.getActualTypeArguments();
                                if (types != null && types.length > 0) {
                                    mReturnType = (Class<T>) types[0];
                                    return mReturnType;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return mReturnType;
    }

    public Map<String, String> getHeaderMap() {
        return mHeaderMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.mHeaderMap.clear();
        if (headerMap != null) {
            this.mHeaderMap.putAll(headerMap);
        }
    }

    public Map<String, String> getParamMap() {
        return mParamMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.mParamMap.clear();
        if (paramMap != null) {
            this.mParamMap.putAll(paramMap);
        }
    }

    public void setContentType(String contentType) {
        this.mContentType = contentType;
    }

    public void setRequestBody(String requestBody) {
        this.mRequestBody = requestBody;
    }

    public void setRequestId(String requestId) {
        this.mRequestId = requestId;
    }

    public void setReturnType(Class<T> returnType) {
        this.mReturnType = returnType;
    }

}
