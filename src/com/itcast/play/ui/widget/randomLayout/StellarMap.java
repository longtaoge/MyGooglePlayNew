package com.itcast.play.ui.widget.randomLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;


public class StellarMap extends FrameLayout implements AnimationListener, OnTouchListener, OnGestureListener {

	private RandomLayout mHidenGroup;

	private RandomLayout mShownGroup;

	private Adapter mAdapter;
	private RandomLayout.Adapter mShownGroupAdapter;
	private RandomLayout.Adapter mHidenGroupAdapter;

	private int mShownGroupIndex;
	private int mHidenGroupIndex;
	private int mGroupCount;

	private Animation mZoomInNearAnim;
	private Animation mZoomInAwayAnim;
	private Animation mZoomOutNearAnim;
	private Animation mZoomOutAwayAnim;
	private Animation mPanInAnim;
	private Animation mPanOutAnim;

	private GestureDetector mGestureDetector;

	/** 构造方法 */
	public StellarMap(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public StellarMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public StellarMap(Context context) {
		super(context);
		init();
	}

	/** 初始化方法 */
	private void init() {
		mGroupCount = 0;
		mHidenGroupIndex = -1;
		mShownGroupIndex = -1;

		mHidenGroup = new RandomLayout(getContext());
		mShownGroup = new RandomLayout(getContext());

		addView(mHidenGroup, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mHidenGroup.setVisibility(View.GONE);
		addView(mShownGroup, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		mGestureDetector = new GestureDetector(this);
		setOnTouchListener(this);
		//设置动画
		mZoomInNearAnim = AnimationUtil.createZoomInNearAnim();
		mZoomInNearAnim.setAnimationListener(this);
		mZoomInAwayAnim = AnimationUtil.createZoomInAwayAnim();
		mZoomInAwayAnim.setAnimationListener(this);
		mZoomOutNearAnim = AnimationUtil.createZoomOutNearAnim();
		mZoomOutNearAnim.setAnimationListener(this);
		mZoomOutAwayAnim = AnimationUtil.createZoomOutAwayAnim();
		mZoomOutAwayAnim.setAnimationListener(this);
	}

	/** 设置隐藏组和显示组的x和y的规则 */
	public void setRegularity(int xRegularity, int yRegularity) {
		mHidenGroup.setRegularity(xRegularity, yRegularity);
		mShownGroup.setRegularity(xRegularity, yRegularity);
	}

	private void setChildAdapter() {
		if (null == mAdapter) {
			return;
		}
		mHidenGroupAdapter = new RandomLayout.Adapter() {
			//取出本Adapter的View对象给HidenGroup的Adapter
			@Override
			public View getView(int position, View convertView) {
				return mAdapter.getView(mHidenGroupIndex, position, convertView);
			}

			@Override
			public int getCount() {
				return mAdapter.getCount(mHidenGroupIndex);
			}
		};
		mHidenGroup.setAdapter(mHidenGroupAdapter);

		mShownGroupAdapter = new RandomLayout.Adapter() {
			//取出本Adapter的View对象给ShownGroup的Adapter
			@Override
			public View getView(int position, View convertView) {
				return mAdapter.getView(mShownGroupIndex, position, convertView);
			}

			@Override
			public int getCount() {
				return mAdapter.getCount(mShownGroupIndex);
			}
		};
		mShownGroup.setAdapter(mShownGroupAdapter);
	}

	/** 设置本Adapter */
	public void setAdapter(Adapter adapter) {
		mAdapter = adapter;
		mGroupCount = mAdapter.getGroupCount();
		if (mGroupCount > 0) {
			mShownGroupIndex = 0;
		}
		setChildAdapter();
	}

	/** 设置显示区域 */
	public void setInnerPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
		mHidenGroup.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		mShownGroup.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
	}

	/** 给指定的Group设置动画 */
	public void setGroup(int groupIndex, boolean playAnimation) {
		switchGroup(groupIndex, playAnimation, mZoomInNearAnim, mZoomInAwayAnim);
	}

	/** 获取当前显示的group角标 */
	public int getCurrentGroup() {
		return mShownGroupIndex;
	}

	/** 给下一个Group设置动画入 */
	public void zoomIn() {
		final int nextGroupIndex = mAdapter.getNextGroupOnZoom(mShownGroupIndex, true);
		switchGroup(nextGroupIndex, true, mZoomInNearAnim, mZoomInAwayAnim);
	}

	/** 给下一个Group设置出动画 */
	public void zoomOut() {
		final int nextGroupIndex = mAdapter.getNextGroupOnZoom(mShownGroupIndex, false);
		switchGroup(nextGroupIndex, true, mZoomOutNearAnim, mZoomOutAwayAnim);
	}

	/** 给下一个Group设置动画 */
	public void pan(float degree) {
		final int nextGroupIndex = mAdapter.getNextGroupOnPan(mShownGroupIndex, degree);
		mPanInAnim = AnimationUtil.createPanInAnim(degree);
		mPanInAnim.setAnimationListener(this);
		mPanOutAnim = AnimationUtil.createPanOutAnim(degree);
		mPanOutAnim.setAnimationListener(this);
		switchGroup(nextGroupIndex, true, mPanInAnim, mPanOutAnim);
	}

	/** 给下一个Group设置进出动画 */
	private void switchGroup(int newGroupIndex, boolean playAnimation, Animation inAnim, Animation outAnim) {
		if (newGroupIndex < 0 || newGroupIndex >= mGroupCount) {
			return;
		}
		//把当前显示Group角标设置为隐藏的
		mHidenGroupIndex = mShownGroupIndex;
		//把下一个Group角标设置为显示的
		mShownGroupIndex = newGroupIndex;
		// 交换两个Group
		RandomLayout temp = mShownGroup;
		mShownGroup = mHidenGroup;
		mShownGroup.setAdapter(mShownGroupAdapter);
		mHidenGroup = temp;
		mHidenGroup.setAdapter(mHidenGroupAdapter);
		//刷新显示的Group
		mShownGroup.refresh();
		//显示Group
		mShownGroup.setVisibility(View.VISIBLE);

		//启动动画
		if (playAnimation) {
			if (mShownGroup.hasLayouted()) {
				mShownGroup.startAnimation(inAnim);
			}
			mHidenGroup.startAnimation(outAnim);
		} else {
			mHidenGroup.setVisibility(View.GONE);
		}
	}

	// 重新分配显示区域
	public void redistribute() {
		mShownGroup.redistribute();
	}

	/** 动画监听 */
	@Override
	public void onAnimationStart(Animation animation) {
		// 当动画启动
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// 当动画结束
		if (animation == mZoomInAwayAnim || animation == mZoomOutAwayAnim || animation == mPanOutAnim) {
			mHidenGroup.setVisibility(View.GONE);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// 当动画重复

	}

	/** 定位 */
	@Override
	public void onLayout(boolean changed, int l, int t, int r, int b) {
		//用以判断ShownGroup是否onLayout的变量
		boolean hasLayoutedBefore = mShownGroup.hasLayouted();
		super.onLayout(changed, l, t, r, b);
		//如果在父View的onLayout之前没有onLayout，在之后onLayout了，则只启动动画，否则设置为显示的
		if (!hasLayoutedBefore && mShownGroup.hasLayouted()) {
			// This is the first time layout, play animation
			mShownGroup.startAnimation(mZoomInNearAnim);
		} else {
			mShownGroup.setVisibility(View.VISIBLE);
		}
	}

	/** 重写onTouch事件，把onTouch事件分配给手势识别 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	/** 消费掉onDown事件 */
	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	/** 空实现 */
	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (velocityX > 0) {
			zoomIn();
		} else {
			zoomOut();
		}
		return true;
	}

	/** 内部类、接口 */
	public static interface Adapter {
		public abstract int getGroupCount();

		public abstract int getCount(int group);

		public abstract View getView(int group, int position, View convertView);

		public abstract int getNextGroupOnPan(int group, float degree);

		public abstract int getNextGroupOnZoom(int group, boolean isZoomIn);
	}
}
