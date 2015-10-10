/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.sohu.focus.framework.loader;

import java.io.File;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.os.Environment;
import android.widget.ImageView;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.ClearCacheRequest;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoadFinishListener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.UploadRequest;

/**
 * 工具类，建立http通道，可单独使用 USEAGE:
 * 
 * getRequestQueue.add() exec()
 * 
 * 
 * @author lbritney
 * 
 */
public class RequestLoader {

    private static final String DEFAULT_CACHE_DIR = "volley_cache";
    private volatile static RequestLoader instance;
    private RequestQueue queue;
    private DiskBasedCache diskCache;
    private ImageLoader imageLoader;
    private LruMemoryCache memoryCache;

    /** Returns singleton class instance */
    public static RequestLoader getInstance(Context context) {
        if (instance == null) {
            synchronized (RequestLoader.class) {
                if (instance == null) {
                    instance = new RequestLoader(context);
                }
            }
        }
        return instance;
    }

    /**
     * 确认之前调用过getInstance(Context context)
     * 
     * @return
     */
    public static RequestLoader getInstance() {
        return instance;
    }

    private RequestLoader(Context context) {
        init(context, null);
    }

    public RequestQueue getRequestQueue() {
        return queue;
    }

    public <T> Request<T> exec(Request<T> req) {
        return queue.add(req);
    }

    /**
     * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
     * 
     * @param context A {@link Context} to use for creating the cache dir.
     * @param stack An {@link HttpStack} to use for the network, or null for default.
     * @return A started {@link RequestQueue} instance.
     */
    public synchronized void init(Context context, HttpStack stack) {
        File cacheDir = null;
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED) && context.getExternalCacheDir() != null && context.getExternalCacheDir().exists())
            cacheDir = new File(context.getExternalCacheDir(), DEFAULT_CACHE_DIR);
        else
            cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
        String userAgent = "com.sohu.focus";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent =
                    packageName + "Android/v_" + info.versionCode + ",build/"
                            + Build.VERSION.RELEASE;
        } catch (NameNotFoundException e) {
        }

        if (stack == null) {
            if (Build.VERSION.SDK_INT >= 9) {
                stack = new HurlStack();
            } else {
                stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
            }
        }
        final int  maxCacheSizeInBytes = 30 * 1024 * 1024;
        diskCache = new DiskBasedCache(cacheDir,maxCacheSizeInBytes);
        Network network = new BasicNetwork(stack);
        queue = new RequestQueue(diskCache, network);
        queue.start();

        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        memoryCache = new LruMemoryCache(memoryCacheSize);
        imageLoader = new ImageLoader(queue, memoryCache);
    }

    /**
     * destroy volleytool
     */
    public void destroy() {
        queue.stop();
        queue = null;
        instance = null;
    }

    /**
     * 清理缓存(disk,disk内存缓存,图片内存)
     * 
     * @param callback
     */
    public void clearDiskCache(Runnable callback) {
        clearMemoryCache();
        exec(new ClearCacheRequest(diskCache, callback));
    }
    
    public long getDiskCacheSize(){
        return diskCache.getTotalSize();
    }
    
    /**
     * 清理内存图片
     */
    public void clearMemoryCache(){
        memoryCache.clear();
    }
    /**
     * 图片加载器
     * 
     * @return
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    /**
     * 加载图片
     * 
     * @param url
     * @param view
     * @param scaleType 如果不需要可以为空
     * @param defaultImageResId
     * @param errorImageResId
     * @param tag
     */
    public void displayImage(final String url, final ImageView view, final ImageView.ScaleType scaleType, final int defaultImageResId,
            final int errorImageResId,final String tag,final ImageLoadFinishListener listener) {
        view.setTag(url);
        displayImage(url, ImageLoader.getImageListener(view, defaultImageResId, errorImageResId,tag,scaleType,listener),
                0, 0);  
    }

    private void displayImage(final String url, ImageListener listener, int maxWidth, int maxHeight) {
        imageLoader.get(url, listener, maxWidth, maxHeight);
    }

    /**
     * 
     * 加载图片
     * 
     * @param url
     * @param view
     * @param scaleType 如果不需要可以为空
     * @param defaultImageResId
     * @param errorImageResId
     * @param maxWidth
     * @param maxHeight
     * @param tag
     */
    public void displayImage(final String url, final ImageView view, final ImageView.ScaleType scaleType, final int defaultImageResId,
            final int errorImageResId, int maxWidth, int maxHeight,String tag ,final ImageLoadFinishListener listener) {
        displayImage(url, ImageLoader.getImageListener(view, defaultImageResId, errorImageResId,tag,scaleType,listener),
                maxWidth, maxHeight);
    }

    /**
     * 获取bitmap
     * 
     * @param url
     * @param listener
     */
    public void getImage(final String url, final ImageLoadingListener listener,final String tag) {
        imageLoader.get(url, new ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(FocusResponseError.getErrorCode(error));
            }

            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {
                listener.onResponse(response.getBitmap());
            }

            @Override
            public String getTag() {
                return tag;
            }
        }, 0, 0);
    }

    /**
     * Creates a new upload request.
     * 
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     * @param file file to upload
     * @param rquestParams request params
     */
    public void doUpload(String url, Listener<String> listener, ErrorListener errorListener,
            File file, Map<String, String> rquestParams) {
        exec(new UploadRequest(url, listener, errorListener, file, rquestParams));
    }
}
