package com.itcast.play.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by zlr on 2014/7/19.
 */
public class BaseActivity extends ActionBarActivity {
	private static BaseActivity mForegroundActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initActionBar();
		View view = null;
	}

	protected void initView(){

	}

	protected void initActionBar(){

	}

	@Override
	protected void onResume() {
		mForegroundActivity = this;
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (mForegroundActivity == this) {
			mForegroundActivity = null;
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public static BaseActivity getForegroundActivity(){
		return mForegroundActivity;
	}
}
