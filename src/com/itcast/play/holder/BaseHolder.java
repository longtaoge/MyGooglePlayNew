package com.itcast.play.holder;

import android.view.View;

/**
 * Created by zlr on 2014/7/19.
 */
public abstract class BaseHolder<T> {
	private View mRootView;
	private T mData;

	public BaseHolder() {
		mRootView = initView();
		mRootView.setTag(this);
	}

	protected abstract View initView();

	public void setData(T data) {
		mData = data;
		refreshView();
	}

	public T getData() {
		return mData;
	}

	public View getView() {
		return mRootView;
	}

	public abstract void refreshView();
}
