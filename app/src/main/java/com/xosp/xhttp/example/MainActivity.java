package com.xosp.xhttp.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xosp.xhttp.bean.VolleyResult;
import com.xosp.xhttp.builder.PostFormBuilder;
import com.xosp.xhttp.core.VolleyClient;
import com.xosp.xhttp.inter.VolleyCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    private TextView mTvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvTest = findViewById(R.id.tv_test);
        mTvTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTvTest) {
            String url = "https://www.m.baidu.com";
            new PostFormBuilder<>()
                    .setRequestUrl(url)
                    .setHeaders(null)
                    .setParams(null)
                    .setResultClass(String.class)
                    .setRequestCallback(new DefaultVolleyCallback(null) {
                        @Override
                        public void onResponse(VolleyResult<String> response) {
                            Log.e(TAG, "on success");
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

    static abstract class DefaultVolleyCallback implements VolleyCallback<String> {

        private VolleyCallback<String> callback;

        public DefaultVolleyCallback(VolleyCallback<String> callback) {
            this.callback = callback;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if (this.callback != null) {
                this.callback.onErrorResponse(error);
            }
        }

        @Override
        public void onResponse(VolleyResult<String> response) {
            if (this.callback != null) {
                this.callback.onResponse(response);
            }
        }
    }


}
