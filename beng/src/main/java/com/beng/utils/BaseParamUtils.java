package com.beng.utils;

import android.content.Context;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 *  URL 
 * @author jomeslu
 * 
 */
public class BaseParamUtils {

	// 应用ID
	private static String appId;
	// 设备Token
	private static String deviceToken;
	// 推送
	private static String pushToken;
	// 版本号
	private static String version;
	// 秘钥
	private static final String secretKey = "u7252309b3977d81d7bd486e85e98ue7";
	private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";


	/**
	 * 封装Get请求URL和参数
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String parase( String url, Map<String, String> params ) {

		StringBuilder encodedParams = new StringBuilder();
		try {
			encodedParams.append( '?' );
			for( Map.Entry<String, String> entry : params.entrySet() ) {
				encodedParams.append( URLEncoder.encode( entry.getKey(), DEFAULT_PARAMS_ENCODING ) );
				encodedParams.append( '=' );
				if(!CommonUtil.isEmpty(entry.getValue())){
					encodedParams.append( URLEncoder.encode( entry.getValue(), DEFAULT_PARAMS_ENCODING ) );
				}
				encodedParams.append( '&' );
			}
			String paramstr = encodedParams.toString();
			if( !"?".equals( paramstr ) ) {
				return url + paramstr.substring( 0, paramstr.length() - 1 );
			}

		} catch( UnsupportedEncodingException uee ) {
			throw new RuntimeException( "Encoding not supported: " + DEFAULT_PARAMS_ENCODING, uee );
		}

		return "";
	}


	/**
	 * 用于post
	 * 
	 * @param params
	 * @return
	 */
	public static String parase( Map<String, String> params ) {

		StringBuilder encodedParams = new StringBuilder();
		try {
			for( Map.Entry<String, String> entry : params.entrySet() ) {
				encodedParams.append( URLEncoder.encode( entry.getKey(), DEFAULT_PARAMS_ENCODING ) );
				encodedParams.append( '=' );
				if(!CommonUtil.isEmpty(entry.getValue())){
					encodedParams.append( URLEncoder.encode( entry.getValue(), DEFAULT_PARAMS_ENCODING ) );
				}
				encodedParams.append( '&' );
			}

			String paramstr = encodedParams.toString();
			if( !"".equals( paramstr ) ) {
				return paramstr.substring( 0, paramstr.length() - 1 );
			}
		} catch( UnsupportedEncodingException uee ) {
			throw new RuntimeException( "Encoding not supported: " + DEFAULT_PARAMS_ENCODING, uee );
		}
		return "";

	}





}
