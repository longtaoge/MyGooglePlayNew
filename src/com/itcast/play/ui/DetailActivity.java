package com.itcast.play.ui;

import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import com.itcast.play.R;
import com.itcast.play.bean.AppInfo;
import com.itcast.play.holder.DetailInfoHolder;
import com.itcast.play.http.protocol.DetailProtocol;
import com.itcast.play.ui.widget.LoadPager;
import com.itcast.play.utils.UIUtils;

/**
 * Created by zlr on 2014/7/20.
 */
public class DetailActivity extends BaseActivity {
	public static final String PACKAGENAME = "PACKAGENMAE";
	public AppInfo mInfo;
	//各区域布局及其holder
	private FrameLayout mInfoLayout, mSafeLayout, mDesLayout, mBottomLayout;
	private HorizontalScrollView mScreenLayout;
	private LoadPager mPager;

	@Override
	protected void initView() {
		mPager = new LoadPager(UIUtils.getContext()) {
			@Override
			protected LoadResult load() {
				return DetailActivity.this.load();
			}

			@Override
			protected View createdLoadedView() {
				return DetailActivity.this.createdLoadedView();
			}
		};
		setContentView(mPager);
		mPager.showSafe();
	}

	protected LoadPager.LoadResult load() {
		String packageNmae = getIntent().getStringExtra(PACKAGENAME);
		DetailProtocol protocol = new DetailProtocol();
		protocol.setPackageName(packageNmae);
		mInfo = protocol.load(0);
		return mPager.checkLoadResult(mInfo);
	}

	protected View createdLoadedView() {
		View view = UIUtils.inflate(R.layout.activity_detail);
		// 添加信息区域
		mInfoLayout = (FrameLayout) view.findViewById(R.id.detail_info);
		DetailInfoHolder detailInfoHolder = new DetailInfoHolder();
		detailInfoHolder.setData(mInfo);
		mInfoLayout.addView(detailInfoHolder.getView());

		// 添加安全区域
		mSafeLayout = (FrameLayout) view.findViewById(R.id.detail_safe);

		// 截图区域
		mScreenLayout = (HorizontalScrollView) view.findViewById(R.id.detail_screen);

		// 介绍区域
		mDesLayout = (FrameLayout) view.findViewById(R.id.detail_des);

		// 底部区域
		mBottomLayout = (FrameLayout) view.findViewById(R.id.bottom_layout);
		return view;
	}

	@Override
	protected void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("应用详情");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}
}
