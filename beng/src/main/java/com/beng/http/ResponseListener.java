package com.beng.http;

import com.android.volley.NetworkResponse;
import com.sohu.focus.framework.loader.FocusResponseError;

import java.util.HashMap;
import java.util.Map;

public interface ResponseListener<T> {

	/**
	 * 如果cache未过期，直接返回，如果已经过期，从服务器上拿下来在返回，期间界面上可以显示loadFromCache里的内容。
	 * 这里了返回的数据可能是未过期的缓存，也可能是从服务器上拿下来的
	 * 
	 * @param response
	 * @param dataTime
	 */
	public void loadFinish(T response, long dataTime);


	/**
	 * 返回已经过期的cache数据，然后在加载新数据并从loadFinish里返回
	 * 
	 * @param cacheResponse
	 * @param dataTime
	 */
	public void loadFromCache(T cacheResponse, long dataTime);


	/**
	 * 加载失败
	 * 
	 * @param errorCode
	 */
	public void loadError(FocusResponseError.CODE errorCode);

    /**
     * 一般是处理服务器上返回的头信息
     * @param headers
     */

    public void loadNativeReqHeader(Map<String, String> headers);
}
