package com.beng.utils;

import android.view.View;


public interface ILoadingViewState {


	/**********只获取失败的页面***********/
    abstract View getFailedViewSingle();
    /**********只获取刷新的页面***********/
    abstract View getRefreshViewSingle();
    
    /**
     * 第一页加载没拿到数据时调用
     * @param imageResId 提示图片的ResId
     * @param tipsTitleId 提示标题的ResId
     * @param tipsContentId 提示内容的ResId
     */
    abstract void onLoadEmpty(int imageResId, int tipsTitleId, int tipsContentId);

    abstract void onFailed(onReloadListener onReloadListener);

    public interface onReloadListener {
        abstract void onReload();
    }
}
