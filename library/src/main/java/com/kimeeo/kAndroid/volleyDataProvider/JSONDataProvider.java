package com.kimeeo.kAndroid.volleyDataProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.kimeeo.kAndroid.listViews.dataProvider.DataModel;
import com.kimeeo.kAndroid.listViews.dataProvider.IParseableObject;

import java.util.List;
import java.util.Map;

/**
 * Created by bpa001 on 5/3/16.
 */
abstract public class JSONDataProvider extends BaseVolleyDataProvider
{
    protected Gson gson;
    public JSONDataProvider(IVolleyRequestProvider volleyRequestController)
    {
        super(volleyRequestController);
        gson= new Gson();
    }
    public void garbageCollectorCall()
    {
        super.garbageCollectorCall();
        gson=null;
    }
    @Override
    protected void dataHandler(String url, Object json)
    {
        try
        {
            Class<DataModel> clazz = getDataModel();
            DataModel dataModel = gson.fromJson((String)json, clazz);
            List<?> list=dataModel.getDataProvider();
            if(list!=null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) instanceof IParseableObject)
                        ((IParseableObject) list.get(i)).dataLoaded(dataModel);
                }
                dataIn(url,dataModel);
                addData(list);
            }
            else
            {
                dataIn(url,json);
                dataLoadError(null);
            }
        }
        catch (Throwable e)
        {
            dataIn(url,json);
            dataLoadError(e);
        }
    }
}
