package com.xosp.xhttp.example;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xosp.xhttp.bean.VolleyResult;
import com.xosp.xhttp.builder.PostFormBuilder;
import com.xosp.xhttp.core.VolleyClient;
import com.xosp.xhttp.inter.VolleyCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    private TextView mTvTest;
    private ImageView mIvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvTest = findViewById(R.id.tv_test);
        mTvTest.setOnClickListener(this);
        mIvTest = findViewById(R.id.iv_test);
    }

    @Override
    public void onClick(View v) {
        if (v == mTvTest) {
            //String url = "https://www.m.baidu.com";
            String url = "https://www.baidu.com/img/bd_logo1.png";
            new PostFormBuilder<>(Bitmap.class)
                    .setRequestUrl(url)
                    .setHeaders(null)
                    .setParams(null)
                    .setRequestCallback(new VolleyCallback<Bitmap>() {
                        @Override
                        public void onResponse(VolleyResult<Bitmap> response) {
                            Log.e(TAG, "on success");
                            mIvTest.setImageBitmap(response.getResponseData());
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "on error");
                        }
                    })
                    .request();
            //VolleyClient.getInstance().cancel(TAG);
        }
    }

    static abstract class DefaultVolleyCallback<T> implements VolleyCallback<T> {

        private VolleyCallback<T> callback;

        public DefaultVolleyCallback(VolleyCallback<T> callback) {
            this.callback = callback;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if (this.callback != null) {
                this.callback.onErrorResponse(error);
            }
        }

        @Override
        public void onResponse(VolleyResult<T> response) {
            if (this.callback != null) {
                this.callback.onResponse(response);
            }
        }
    }


}
