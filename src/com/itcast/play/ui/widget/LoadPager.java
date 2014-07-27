package com.itcast.play.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.itcast.play.R;
import com.itcast.play.manager.ThreadManager;
import com.itcast.play.utils.UIUtils;

import java.util.List;

/**
 * Created by zlr on 2014/7/19.
 */
public abstract class LoadPager extends FrameLayout {
	private static final int STATE_UNLOADED = 0;//未知状态
	private static final int STATE_LOADING = 1;//加载状态
	private static final int STATE_ERROR = 3;//加载完毕，但是出错状态
	private static final int STATE_EMPTY = 4;//加载完毕，但是没有数据状态
	private static final int STATE_SUCCEED = 5;//加载成功

	private View mLoadingView, mLoadedView, mErrorView, mEmptyView;

	private int mState = STATE_UNLOADED;

	public LoadPager(Context context) {
		super(context);
		initView();
	}

	protected void initView() {
		mLoadingView = UIUtils.inflate(R.layout.loading_page_loading);
		mErrorView = UIUtils.inflate(R.layout.loading_page_error);
		mEmptyView = UIUtils.inflate(R.layout.loading_page_empty);

		addView(mLoadingView);
		addView(mErrorView);
		addView(mEmptyView);
	}

	private void showCurrentView() {
		mLoadingView.setVisibility(mState == STATE_LOADING ? VISIBLE : INVISIBLE);
		mErrorView.setVisibility(mState == STATE_ERROR ? VISIBLE : INVISIBLE);
		mEmptyView.setVisibility(mState == STATE_EMPTY ? VISIBLE : INVISIBLE);

		if (mState == STATE_SUCCEED && mLoadedView == null) {
			mLoadedView = createdLoadedView();
			addView(mLoadedView);
		}
		if (mState == STATE_SUCCEED) {
			mLoadedView.setVisibility(VISIBLE);
		}
	}

	public void showSafe() {
		UIUtils.runInMainThread(new Runnable() {
			@Override
			public void run() {
				show();
			}
		});
	}

	private void show() {
		if (mState == STATE_UNLOADED || mState == STATE_ERROR) {
			mState = STATE_LOADING;
			ThreadManager.getLongPool().execute(new Task());
		}
		showCurrentView();
	}

	protected abstract LoadResult load();

	protected abstract View createdLoadedView();

	class Task implements Runnable {
		@Override
		public void run() {
			final LoadResult result = load();
			UIUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					mState = result.getId();
					showCurrentView();
				}
			});
		}
	}

	public enum LoadResult {
		ERROR(3), EMPTY(4), SUCCEED(5);
		int id;

		LoadResult(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}


	public LoadPager.LoadResult checkLoadResult(Object obj) {
		if (obj == null) {
			return LoadPager.LoadResult.ERROR;
		}
		if (obj instanceof List) {
			List list = (List) obj;
			if (list.size() == 0) {
				return LoadPager.LoadResult.EMPTY;
			} else {
				return LoadPager.LoadResult.SUCCEED;
			}
		} else {
			return LoadPager.LoadResult.SUCCEED;
		}
	}
}
