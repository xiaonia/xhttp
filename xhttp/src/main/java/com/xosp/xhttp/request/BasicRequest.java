package com.xosp.xhttp.request;

import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xosp.xhttp.bean.VolleyResult;
import com.xosp.xhttp.inter.VolleyCallback;
import com.xosp.xhttp.utils.GenericClassHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: xuqingqi
 * E-mail: xuqingqi01@gmail.com
 * Date: 2018/6/22
 * Description: class for custom network requests.
 */
@SuppressWarnings({"WeakerAccess"})
public abstract class BasicRequest<T> extends Request<VolleyResult<T>> {

    /** Lock to guard mListener as it is cleared on cancel() and read on delivery. */
    protected final Object mMutex = new Object();

    protected String mRequestId;
    protected Class<T> mReturnType;

    protected String mContentType;
    protected final Map<String, String> mHeaderMap;
    protected final Map<String, String> mParamMap;
    protected final Map<String, String> mFileMap;

    protected VolleyCallback<T> mCallback;

    public BasicRequest(int method, String url, VolleyCallback<T> callback) {
        super(method, url, callback);
        mCallback = callback;
        mHeaderMap = new HashMap<String, String>();
        mParamMap = new HashMap<String, String>();
        mFileMap = new HashMap<String, String>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParamMap;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaderMap;
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
        return super.getBody();
    }

    @Override
    protected void deliverResponse(VolleyResult<T> response) {
        Response.Listener<VolleyResult<T>> listener;
        synchronized (mMutex) {
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
        synchronized (mMutex) {
            this.mCallback = null;
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public Class<T> getResultClass() {
        if (mReturnType == null && mCallback != null) {
            mReturnType = GenericClassHelper.
                    getGenericClass(mCallback, VolleyCallback.class);
        }
        return mReturnType;
    }

    public void setRequestId(String requestId) {
        this.mRequestId = requestId;
    }

    public void setReturnType(Class<T> returnType) {
        this.mReturnType = returnType;
    }

    public void setContentType(String contentType) {
        this.mContentType = contentType;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.mHeaderMap.clear();
        if (headerMap != null) {
            this.mHeaderMap.putAll(headerMap);
        }
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.mParamMap.clear();
        if (paramMap != null) {
            this.mParamMap.putAll(paramMap);
        }
    }

    public void setFileMap(Map<String, String> fileMap) {
        this.mFileMap.clear();
        if (fileMap != null) {
            this.mFileMap.putAll(fileMap);
        }
    }

}
