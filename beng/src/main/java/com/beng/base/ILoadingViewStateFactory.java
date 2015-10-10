package com.beng.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.beng.utils.ILoadingViewState;


/**
 * 
 * @author jomeslu
 *
 */
public class ILoadingViewStateFactory implements ILoadingViewState {
	
	private Context mContext;
	private View mRefreshView;
	private View mFailedView;
//	private final static int REFRESH_CODE=0;
//	private final static int SUCCESS_CODE=1;
//	private final static int FAILED_CODE=2;
	public ILoadingViewStateFactory(Context mContext){
		this.mContext=mContext;
		initFailedView();
		initRefreshView();
	}
	private void initRefreshView() {
//		mRefreshView = LayoutInflater.from( mContext ).inflate( R.layout.lib_refresh_view, null );
	}
	private void initFailedView() {
//		mFailedView = LayoutInflater.from( mContext ).inflate( R.layout.lib_failed_view, null );
	}

	
	
	@Override
	public View getFailedViewSingle() {

		return mFailedView;
	}

	@Override
	public View getRefreshViewSingle() {

		return mRefreshView;
	}

	
	
	
	@Override
	public void onLoadEmpty( int imageResId, int tipsTitleId, int tipsContentId ) {

		
	}

	@Override
	public void onFailed( onReloadListener onReloadListener ) {

	}

	
	

}
