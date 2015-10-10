package com.sohu.focus.framework.loader;

import java.io.File;
import java.util.Map;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.UploadRequest;

public class UploadRequestLoader extends StringRequestLoader {

    private File file;
    private Map<String, String> rquestParams;

    public UploadRequestLoader(Context context) {
        super(context);
    }

    @Override
    public Request<String> exec() {
        UploadRequest ur = new UploadRequest(param, new Listener<String>() {

            @Override
            public void onResponse(String response) {
                listener.onResponse(response, 0);
            }

        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(FocusResponseError.getErrorCode(error));
            }

        }, file, rquestParams);
        ur.setShouldCache(false);
        ur.setTag(tag);
        return RequestLoader.getInstance(context).exec(ur);
    }

    public UploadRequestLoader file(File file) {
        this.file = file;
        return this;
    }

    public UploadRequestLoader rquestParams(Map<String, String> rquestParams) {
        this.rquestParams = rquestParams;
        return this;
    }
}
