package com.itcast.play.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.itcast.play.R;
import com.itcast.play.http.image.ImageLoader;
import com.itcast.play.ui.widget.IndicatorView;
import com.itcast.play.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlr on 2014/7/20.
 */
public class HomeHeaderHolder extends BaseHolder<List<String>> implements ViewPager.OnPageChangeListener {
	private ViewPager mViewPager;
	private HeaderAdapter mAdapter;
	private IndicatorView mIndicatorView;
	private AutoPlay mAutoPlay;

	@Override
	protected View initView() {
		mViewPager = new ViewPager(UIUtils.getContext());
		mViewPager.setOnPageChangeListener(this);

		mIndicatorView = new IndicatorView(UIUtils.getContext());
		mIndicatorView.setIndicatorDrawable(UIUtils.getDrawable(R.drawable.indicator));
		mIndicatorView.setInterval(5);

		RelativeLayout rl = new RelativeLayout(UIUtils.getContext());

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
		rl.addView(mViewPager, params);

		params = new RelativeLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.setMargins(0, 0, 20, 20);
		rl.addView(mIndicatorView, params);

		rl.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, UIUtils.dip2px(134)));

		mAutoPlay = new AutoPlay();

		mViewPager.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mAutoPlay.stop();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					mAutoPlay.start();
				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
					mAutoPlay.start();
				}
				return false;
			}
		});

		return rl;
	}

	@Override
	public void refreshView() {
		if (mAdapter == null) {
			mAdapter = new HeaderAdapter(getData());
			mViewPager.setAdapter(mAdapter);
			mViewPager.setCurrentItem(10000 * getData().size());
			mIndicatorView.setCount(getData().size());
			mIndicatorView.setSelection(0);
			mAutoPlay.start();
		}
	}

	@Override
	public void onPageScrolled(int i, float v, int i2) {

	}

	@Override
	public void onPageScrollStateChanged(int i) {

	}

	@Override
	public void onPageSelected(int i) {
		mIndicatorView.setSelection(i % getData().size());
	}

	class HeaderAdapter extends PagerAdapter {
		private List<String> mData;
		private List<ImageView> mListCache;

		public HeaderAdapter(List<String> data) {
			mData = data;
			mListCache = new ArrayList<ImageView>();
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (object instanceof ImageView) {
				ImageView iv = (ImageView) object;
				container.removeView(iv);
				mListCache.add(iv);
			}
		}

		@Override
		public boolean isViewFromObject(View view, Object o) {
			if (view.getTag() == o) {
				return true;
			} else {
				return false;
			}
//			else {
//				int position = (Integer) (view.getTag(R.id.tag_2));
//				String url = mData.get(position);
//				ImageLoader.load((ImageView) view, url);
//				return true;
//			}
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			System.out.println("position : " + position);
			ImageView iv;
			if (mListCache.size() > 0) {
				iv = mListCache.remove(0);
				System.out.println("复用");
			} else {
				iv = new ImageView(UIUtils.getContext());
				//iv.setScaleType(ImageView.ScaleType.FIT_XY);
				System.out.println("创建新的");
			}
			String url = mData.get(position % (mData.size()));
			System.out.println("url:" + url);
//			iv.setTag(R.id.tag_1, url);
//			iv.setTag(R.id.tag_2, position);
			ImageLoader.load(iv, url);
			container.addView(iv);
			return url;
		}
	}

	class AutoPlay implements Runnable {
		private boolean isStart;

		public void start() {
			if (!isStart) {
				isStart = true;
				UIUtils.postDelayed(this, 2000);
			}
		}

		public void stop() {
			if (isStart) {
				isStart = false;
				UIUtils.removeCallbacks(this);
			}
		}

		@Override
		public void run() {
			if (isStart) {
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
				UIUtils.postDelayed(this, 2000);
			}
		}
	}
}
