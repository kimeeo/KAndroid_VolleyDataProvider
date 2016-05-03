package com.kimeeo.kAndroid.volleyDataProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.util.Map;

/**
 * Created by bhavinpadhiyar on 2/29/16.
 */
public interface IVolleyRequestProvider {

    RequestQueue getRequestQueue() ;
    ImageLoader getImageLoader();

    <T> void addToRequestQueue(Request<T> req, String tag);
    <T> void addToRequestQueue(Request<T> req);
    void cancelPendingRequests(Object tag);

    Map<String,String> getHeaders();
    void setHeaders(Map<String,String> headers);
    Map<String,String> getCookies();
    void setCookies(Map<String,String> cookies);
}
