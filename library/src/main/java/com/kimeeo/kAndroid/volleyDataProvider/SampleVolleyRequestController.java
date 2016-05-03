package com.kimeeo.kAndroid.volleyDataProvider;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by bhavinpadhiyar on 2/29/16.
 */
public class SampleVolleyRequestController implements IVolleyRequestProvider {

    public static final String TAG = SampleVolleyRequestController.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static SampleVolleyRequestController mInstance;

    public static synchronized SampleVolleyRequestController getInstance(Context app)
    {
        if (mInstance == null)
        {
            mInstance = new SampleVolleyRequestController();
            mInstance.mRequestQueue  = Volley.newRequestQueue(app.getApplicationContext(), new OkHttpStack());
        }
        return mInstance;
    }

    public static class OkHttpStack extends HurlStack {
        private final OkUrlFactory mFactory;

        public OkHttpStack() {
            this(new OkHttpClient());
        }

        public OkHttpStack(OkHttpClient client) {
            if (client == null) {
                throw new NullPointerException("Client must not be null.");
            }
            mFactory = new OkUrlFactory(client);
        }

        @Override protected HttpURLConnection createConnection(URL url) throws IOException {
            return mFactory.open(url);
        }
    }

    public RequestQueue getRequestQueue() {

        return mRequestQueue;
    }
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {

    }

    @Override
    public Map<String, String> getCookies() {
        return null;
    }

    @Override
    public void setCookies(Map<String, String> cookies) {

    }
}
