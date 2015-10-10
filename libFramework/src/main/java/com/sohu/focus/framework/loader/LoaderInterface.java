package com.sohu.focus.framework.loader;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.sohu.focus.framework.loader.FocusResponseError.CODE;

import java.util.HashMap;
import java.util.Map;

public interface LoaderInterface {

    public interface StringResponseListener {
        public void onResponse(String response, long dataTime);

        public void onCache(String response ,long dataTime);

        public void onError(CODE errorCode);

        public void onNativeResponse(Map<String, String> headers);
    }
    
    public interface RequestMethod extends Method {
        int UPLOAD = 8;
    }
}
