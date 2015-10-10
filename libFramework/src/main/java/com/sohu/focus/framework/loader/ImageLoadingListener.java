package com.sohu.focus.framework.loader;

import android.graphics.Bitmap;

import com.sohu.focus.framework.loader.FocusResponseError.CODE;

public interface ImageLoadingListener {

    public void onResponse(Bitmap bitmap);

    public void onError(CODE errorCode);
}
