/*
 * Copyright (C) 2011 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.android.volley.toolbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

/**
 * A canned request for retrieving the response body at a given URL as a String.
 */
public class UploadRequest extends Request<String> {
    private final Listener<String> mListener;
    private File mFile;
    private Map<String, String> mParams;
    String BOUNDARY = "------b2712b89bd8";
    String lineEnd = "\r\n";
    String twoHyphens = "--";

    /**
     * Creates a new upload request.
     * 
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     * @param file file to upload
     * @param params request params
     */
    public UploadRequest(String url, Listener<String> listener, ErrorListener errorListener,
            File file, Map<String, String> params) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mFile = file;
        mParams = params;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        StringBuilder sb_start = new StringBuilder();
//        sb_start.append(twoHyphens + BOUNDARY + lineEnd);
//        sb_start.append("Content-Type: multipart/form-data; boundary=" + BOUNDARY + lineEnd);
        if(mParams!=null && !mParams.isEmpty()) {
            for (Entry<String, String> entry : mParams.entrySet()) {
                sb_start.append(twoHyphens + BOUNDARY+lineEnd);
                sb_start.append("Content-Disposition: form-data; name=\"" + entry.getKey()).append("\"").append(lineEnd).append(lineEnd);
                sb_start.append(entry.getValue());
                sb_start.append(lineEnd);
            }
        }

        sb_start.append(twoHyphens + BOUNDARY+ lineEnd);
        sb_start.append("Content-Disposition: form-data; name=\"file\";filename=\"image.jpg\"" + lineEnd);
        sb_start.append("Content-Type: image/jpeg" + lineEnd);
        sb_start.append(lineEnd);

        int size = (int) mFile.length();
        byte[] filedatabtye = new byte[size];
        try {
            new FileInputStream(mFile).read(filedatabtye, 0, size);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb_end = new StringBuilder();
        sb_end.append(lineEnd);
        sb_end.append(twoHyphens + BOUNDARY+twoHyphens+lineEnd);
        return arraycat(arraycat(sb_start.toString().getBytes(), filedatabtye), sb_end.toString()
                .getBytes());
    }

    private byte[] arraycat(byte[] buf1, byte[] buf2) {
        byte[] bufret = null;
        int len1 = 0;
        int len2 = 0;
        if (buf1 != null)
            len1 = buf1.length;
        if (buf2 != null)
            len2 = buf2.length;
        if (len1 + len2 > 0)
            bufret = new byte[len1 + len2];
        if (len1 > 0)
            System.arraycopy(buf1, 0, bufret, 0, len1);
        if (len2 > 0)
            System.arraycopy(buf2, 0, bufret, len1, len2);
        return bufret;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + BOUNDARY;
    }

    @Override
    protected void deliverResponse(String response, long dataTime) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}
