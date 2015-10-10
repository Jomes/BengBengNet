package com.beng.utils;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * @author jomeslu
 *
 */
public class PreferenceManager {

	private static final String USER_NAME = "focus_preference";

	private static PreferenceManager mPreferenceManager;
	private Context mContext;
	private SharedPreferences mPreference;

	private PreferenceManager(){
		
	}

	
	private PreferenceManager( Context context ) {
		this.mContext = context;
		mPreference = context.getSharedPreferences( USER_NAME, Context.MODE_PRIVATE );
	}


	public static synchronized PreferenceManager getInstance( Context context ) {

		if( mPreferenceManager == null ) {
			mPreferenceManager = new PreferenceManager( context );
		}
		return mPreferenceManager;

	}


	public void clearData() {

		mPreference.edit().clear().commit();
	}


	public void saveData( String key, String value ) {

		mPreference.edit().putString( key, value ).commit();
	}


	public void saveData( String key, int value ) {

		mPreference.edit().putInt( key, value ).commit();
	}


	public void saveData( String key, long value ) {

		mPreference.edit().putLong( key, value ).commit();
	}


	public void saveData( String key, boolean value ) {

		mPreference.edit().putBoolean( key, value ).commit();
	}


	public String getStringData( String key, String defaultValue ) {

		return mPreference.getString( key, defaultValue );
	}


	public boolean getBoolData( String key, boolean defaultValue ) {

		return mPreference.getBoolean( key, defaultValue );
	}


	public int getIntData( String key, int defaultValue ) {

		return mPreference.getInt( key, defaultValue );
	}


	public long getLongData( String key, long defaultValue ) {

		return mPreference.getLong( key, defaultValue );
	}


	public boolean containsValue( String key ) {

		return mPreference.contains( key );
	}
}
