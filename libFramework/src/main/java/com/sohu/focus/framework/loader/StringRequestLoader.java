package com.sohu.focus.framework.loader;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.FocusListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.FocusRequest;
import com.sohu.focus.framework.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * string类型的返回值，需要json或者xml对象的话 自己在实现
 * 
 * @author lbritney
 * 
 */
public class StringRequestLoader {
	protected LoaderInterface.StringResponseListener listener = null;
	protected String param = null;
	protected String postContent = null;
	protected String cookies = null;
	protected FocusRequest req = null;
	protected int method = LoaderInterface.RequestMethod.GET;
	protected boolean cache = false;
	protected long expiredTime = 0;
	protected String tag = null;
	protected Context context;
	private boolean force = false;

	public StringRequestLoader(Context context) {
		this.context = context;
	}

	public StringRequestLoader url(String url) {
		this.param = url;
		return this;
	}

	public StringRequestLoader cache(boolean cache) {
		this.cache = cache;
		return this;
	}

	public StringRequestLoader setCookies(String cookies){
		this.cookies = cookies;
		return this;
	}
	
	public StringRequestLoader expiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
		return this;
	}

	public StringRequestLoader tag(String tag) {
		this.tag = tag;
		return this;
	}

	public StringRequestLoader postContent(String postContent) {
		this.postContent = postContent;
		return this;
	}

	public StringRequestLoader force(boolean force) {
		this.force = force;
		return this;
	}

	public StringRequestLoader listener(
			LoaderInterface.StringResponseListener listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public String toString() {
		return "["
				+ (method == LoaderInterface.RequestMethod.GET ? "GET" : "POST")
				+ "]"
				+ ",[URL]:"
				+ param
				+ ",[CACHE]"
				+ "["
				+ (cache ? "Y" : "N")
				+ "]"
				+ (method == LoaderInterface.RequestMethod.GET ? ""
						: ("[POSTCONTENT]" + postContent));
	}

	public StringRequestLoader get() {
		method = LoaderInterface.RequestMethod.GET;
		return this;
	}

	public StringRequestLoader post() {
		method = LoaderInterface.RequestMethod.POST;
		return this;
	}

	public Request<String> exec() {
		if (method == LoaderInterface.RequestMethod.GET) {
			LogUtils.i("GET URL is " + param);
			req = new FocusRequest(param,cookies, new FocusListener<String>() {
				@Override
				public void onResponse(String response, long dataTime) {
					listener.onResponse(response, dataTime);
				}

				@Override
				public void onCache(String response, long dataTime) {
					listener.onCache(response, dataTime);
				}

			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					listener.onError(FocusResponseError.getErrorCode(error));
				}



			}){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
//                    LogUtils.i("GET HEADERS is " + params.toString());
                    return params;
                }



            };
		} else if (method == LoaderInterface.RequestMethod.POST) {
			LogUtils.i("POST URL is " + param);
			LogUtils.i("POST CONTENT is " + postContent);
			req = new FocusRequest(Request.Method.POST, param,cookies,
					new FocusListener<String>() {

						@Override
						public void onResponse(String response, long dataTime) {
							listener.onResponse(response, dataTime);
						}

						@Override
						public void onCache(String response, long dataTime) {
							listener.onCache(response, dataTime);
						}

					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							listener.onError(FocusResponseError
									.getErrorCode(error));
						}

					}) {

				@Override
				protected String getBodyString() throws AuthFailureError {
					return postContent;
				}

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
//                    LogUtils.i("POST HEADERS is " + params.toString());
                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    listener.onNativeResponse(response.headers);
                    return super.parseNetworkResponse(response);
                }
            };
		}

		req.setShouldCache(cache);
		if (cache)
			req.setExpiredTime(expiredTime);
		if (tag != null)
			req.setTag(tag);
		if(force)
			req.setGetFromServer(force);
		return RequestLoader.getInstance(context).exec(req);
	}
    private HashMap<String,String>params= new HashMap<>();
    public StringRequestLoader addHeaders(HashMap<String,String>params){
        this.params = params;
        return this;
    }

}
