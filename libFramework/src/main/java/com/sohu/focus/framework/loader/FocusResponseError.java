package com.sohu.focus.framework.loader;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.sohu.focus.framework.util.LogUtils;


public class FocusResponseError {
    public enum CODE {
        AuthFailureError, NetworkError, NoConnectionError, ParseError, ServerError, TimeoutError
    }

    public static CODE getErrorCode(VolleyError error) {
        if(LogUtils.DEBUG)
            error.printStackTrace();
        if (error instanceof AuthFailureError) {
            return (FocusResponseError.CODE.AuthFailureError);
        } else if (error instanceof NetworkError) {
            return (FocusResponseError.CODE.NetworkError);
        } else if (error instanceof NoConnectionError) {
            return (FocusResponseError.CODE.NoConnectionError);
        } else if (error instanceof ParseError) {
            return (FocusResponseError.CODE.ParseError);
        } else if (error instanceof ServerError) {
            return (FocusResponseError.CODE.ServerError);
        } else if (error instanceof TimeoutError) {
            return (FocusResponseError.CODE.TimeoutError);
        } else
            return null;
    }
}
