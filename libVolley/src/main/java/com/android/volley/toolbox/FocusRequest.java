/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.volley.toolbox;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.FocusListener;

import java.io.UnsupportedEncodingException;

/**
 * FocusRequest,处理不标准的http返回
 * 
 * 如果服务器返回严格，可以用StringRequest，不影响以后扩展
 * 
 * A canned request for retrieving the response body at a given URL as a String.
 */
public class FocusRequest extends Request<String> {
    private final FocusListener<String> mListener;

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public FocusRequest(int method, String url,String cookies, FocusListener<String> listener,
            ErrorListener errorListener) {
        super(method, url, cookies,errorListener);
        mListener = listener;
    }

    /**
     * Creates a new GET request.
     *
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public FocusRequest(String url, String cookies,FocusListener<String> listener, ErrorListener errorListener) {
        this(Method.GET, url, cookies,listener, errorListener);
    }

    @Override
    protected void deliverResponse(String response, long dataTime) {
        mListener.onResponse(response,dataTime);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers)); //注意passport的字符编码问题 gb18030
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, FocusHttpHeaderParser.parseCacheHeaders(response)); //by lbritney 利用自己的解析器
    }

    @Override
    protected void deliverCache(String fromCache,long dataTime){
        mListener.onCache(fromCache,dataTime);
    }
}
