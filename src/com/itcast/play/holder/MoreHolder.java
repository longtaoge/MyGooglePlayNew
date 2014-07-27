package com.itcast.play.holder;

import android.view.View;
import com.itcast.play.Adapter.MyAdapter;
import com.itcast.play.R;
import com.itcast.play.utils.UIUtils;

/**
 * Created by zlr on 2014/7/19.
 */
public class MoreHolder extends BaseHolder<Integer> implements View.OnClickListener {

	public static final int HASMORE = 0;
	public static final int EROOR = 1;
	public static final int EMPTY = 2;

	private View mErrorView, mLoadingView;
	private MyAdapter mAdapter;

	public MoreHolder(MyAdapter adapter) {
		mAdapter = adapter;
	}

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.list_more_loading);
		mErrorView = view.findViewById(R.id.rl_more_error);
		mLoadingView = view.findViewById(R.id.rl_more_loading);
		mErrorView.setOnClickListener(this);

		return view;
	}

	@Override
	public void refreshView() {
		mErrorView.setVisibility(getData() == EROOR ? View.VISIBLE : View.GONE);
		mLoadingView.setVisibility(getData() == HASMORE ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onClick(View v) {
		setData(HASMORE);
		mAdapter.loadMore();
	}
}
