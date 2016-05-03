package com.kimeeo.kAndroid.volleyDataProvider;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kimeeo.kAndroid.listViews.dataProvider.DataModel;
import com.kimeeo.kAndroid.listViews.dataProvider.NetworkDataProvider;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by bpa001 on 5/3/16.
 */
abstract public class BaseVolleyDataProvider extends NetworkDataProvider
{
    //private long cachingTime=1 * 60 * 1000;
    private long cachingTime=-1;
    protected long getCachingTime()
    {
        return cachingTime;
    }
    protected void setCachingTime(long value)
    {
        cachingTime = value;
    }

    public void garbageCollectorCall()
    {
        super.garbageCollectorCall();
        ajaxCancel();
        volleyRequestController=null;
    }
    protected void dataIn(String url, Object data)
    {

    }

    public void ajaxCancel()
    {

    }
    abstract protected Request getPostRequest(String url,Map<String, String> params,Response.Listener done,Response.ErrorListener error);
    abstract protected Request getGetRequest(String url,Response.Listener done,Response.ErrorListener error);

    private void invokePostService(String url, Map<String, Object> params)
    {
        Map<String, String> paramsFinal=getParamParse(params);
        String tag=url;
        Response.Listener done=getDone(url);
        Response.ErrorListener error=getError(url);
        Request request= getPostRequest(url, paramsFinal,done,error);
        volleyRequestController.addToRequestQueue(request,tag);
    }

    private Map<String, String> getParamParse(Map<String, Object> params) {
        Map<String, String> paramsFinal=null;
        if(params!=null)
        {
            paramsFinal = new HashMap<>();
            for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
                paramsFinal.put(stringObjectEntry.getKey(),stringObjectEntry.getValue().toString());
            }
        }
        return paramsFinal;
    }

    protected Response.ErrorListener getError(final String url) {
        Response.ErrorListener error=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                onError(url,error);

            }
        };
        return error;
    }

    protected void onError(String url,VolleyError error) {
        dataLoadError(error);
    }

    protected Response.Listener getDone(final String url) {
        Response.Listener listener=new Response.Listener<Object>() {

            @Override
            public void onResponse(Object response) {
                onResult(url,response);
            }
        };
        return listener;
    }

    protected void onResult(String url,Object response) {

    }

    private void invokeGetService(String url)
    {
        String tag=url;
        Response.Listener done=getDone(url);
        Response.ErrorListener error=getError(url);
        Request request= getGetRequest(url,done,error);
        volleyRequestController.addToRequestQueue(request,tag);
    }

    @Override
    protected void invokeLoadNext()
    {
        String url = getNextURL();
        if(url!=null) {
            if (getMethod() == METHOD_GET) {
                invokeGetService(url);
            }
            else if (getMethod() == METHOD_POST) {
                Object param = getNextParam();
                if(param instanceof Map) {
                    Map<String, Object> params = (Map<String, Object>) param;
                    invokePostService(url, params);
                }
            }
        }
        else {
            setCanLoadNext(false);
            dataLoadError(null);
        }
    }
    @Override
    protected void invokeLoadRefresh()
    {
        String url = getRefreshURL();
        if(url!=null) {
            if (getMethod() == METHOD_GET)
                invokeGetService(url);
            else if (getMethod() == METHOD_POST) {
                Object param = getRefreshParam();
                if(param instanceof Map) {
                    Map<String, Object> params = (Map<String, Object>) param;
                    invokePostService(url, params);
                }
            }
        }
        else {
            setCanLoadRefresh(false);
            dataLoadError(null);
        }
    }

    public BaseVolleyDataProvider(IVolleyRequestProvider volleyRequestController)
    {
        this.volleyRequestController=volleyRequestController;
    }

    public IVolleyRequestProvider getVolleyRequestController() {
        return volleyRequestController;
    }
    protected IVolleyRequestProvider volleyRequestController;
}
