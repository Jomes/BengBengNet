package com.beng.http;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.beng.Constants;
import com.beng.bean.BaseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sohu.focus.framework.loader.FocusResponseError;
import com.sohu.focus.framework.loader.LoaderInterface;
import com.sohu.focus.framework.loader.LoaderInterface.StringResponseListener;
import com.sohu.focus.framework.loader.StringRequestLoader;
import com.sohu.focus.framework.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

public class Request<T extends BaseModel> implements StringResponseListener {

	private Class<T> clazz = null;
	private ResponseListener<T> listener;
	private String url = null;
	/**
	 * post请求时必传此字段
	 */
	private String postContent = null;
	/**
	 * 默认为get请求
	 * 
	 * int GET = 0; int POST = 1;
	 */
	private int method = LoaderInterface.RequestMethod.GET;

	/**
	 * cookies字段
	 */
	private String cookies = null;

	/**
	 * 默认不缓存
	 */
	private boolean cache = false;
	/**
	 */
	private long expiredTime = 0;
	private String tag = null;
	private Context context;


	public Request(Context context) {

		this.context = context;
	}


	public Request<T> url(String url) {

		this.url = url;
		return this;
	}


	public Request<T> method(int method) {

		this.method = method;
		return this;
	}


	public Request<T> clazz(Class<T> clazz) {

		this.clazz = clazz;
		return this;
	}


	public Request<T> cache(boolean cache) {

		this.cache = cache;
		return this;
	}


	public Request<T> expiredTime(long expiredTime) {

		this.expiredTime = expiredTime;
		return this;
	}


	public Request<T> tag(String tag) {

		this.tag = tag;
		return this;
	}


	public Request<T> setCookies(String cookies) {

		this.cookies = cookies;
		return this;
	}


	public Request<T> postContent(String postContent) {

		this.postContent = postContent;
		this.method = LoaderInterface.RequestMethod.POST;
		return this;
	}
    private HashMap<String,String>params;

    public Request<T>addHeaders(HashMap<String,String>params){
        this.params = params;
        return this;
    }

	public Request<T> listener(ResponseListener<T> listener) {

		this.listener = listener;
		return this;
	}


	public void exec() {

		if (method == LoaderInterface.RequestMethod.POST) {// post请求 不缓存
			new StringRequestLoader(context).url(url).listener(this).cache(cache).post()
					.postContent(postContent).setCookies(cookies).tag(tag).addHeaders(params).exec();
		} else if (method == LoaderInterface.RequestMethod.GET) {
			new StringRequestLoader(context).url(url).listener(this).cache(cache).get()
					.expiredTime(expiredTime == 0 ? Constants.DEFAULT_EXPIREDTIME : expiredTime)
					.setCookies(cookies).tag(tag).addHeaders(params).exec();
		}
	}


	@Override
	public String toString() {

		return "["
				+ (method == LoaderInterface.RequestMethod.GET ? "GET" : "POST")
				+ "]"
				+ ",[URL]:"
				+ url
				+ ",[CACHE]"
				+ "["
				+ (cache ? "Y" : "N")
				+ "]"
				+ (method == LoaderInterface.RequestMethod.GET ? ""
						: ("[POSTCONTENT]" + postContent));
	}


	@Override
	public void onResponse(String response, long dataTime) {

		try {
			LogUtils.i("请求地址:"+url+"   data from cache : " + response);
			ObjectMapper mapper = new ObjectMapper();
			T t = (T) mapper.readValue(response, clazz);
			if (t != null) {

					listener.loadFinish(t, dataTime);
			}else{
				listener.loadError(FocusResponseError.CODE.ParseError);
			}

		} catch (Exception e) {
			e.printStackTrace();
			listener.loadError(FocusResponseError.CODE.ParseError);
		}
	}


	@Override
	public void onError(FocusResponseError.CODE code) {

		listener.loadError(code);
	}

    @Override
    public void onNativeResponse(Map<String, String> headers) {
        listener.loadNativeReqHeader(headers);


    }


    @Override
	public void onCache(String response, long dataTime) {

		try {
			LogUtils.i("请求地址:"+url+"___data from cache : " + response);
			ObjectMapper mapper = new ObjectMapper();
			T t = (T) mapper.readValue(response, clazz);
			listener.loadFromCache(t, dataTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
