package com.itcast.play.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.itcast.play.ui.widget.LoadPager;
import com.itcast.play.utils.UIUtils;
import com.itcast.play.utils.ViewUtils;

import java.util.List;

/**
 * Created by zlr on 2014/7/19.
 */
public abstract class BaseFragment extends Fragment {
	private LoadPager mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = new LoadPager(UIUtils.getContext()) {
				@Override
				protected LoadResult load() {
					return BaseFragment.this.load();
				}

				@Override
				protected View createdLoadedView() {
					return BaseFragment.this.createdLoadedView();
				}
			};
		}else{
			ViewUtils.removeSelfFromParent(mRootView);
		}
		mRootView.showSafe();
		return mRootView;
	}

	protected LoadPager.LoadResult checkLoadResult(Object obj) {
		return mRootView.checkLoadResult(obj);
	}

	protected abstract LoadPager.LoadResult load();

	protected abstract View createdLoadedView();
}
