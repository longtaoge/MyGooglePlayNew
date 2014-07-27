package com.itcast.play.ui.widget.randomLayout;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import java.util.*;

public class RandomLayout extends ViewGroup {

	private Random mRdm;
	// X分布规则性，该值越高，子view在x方向的分布越规则、平均。最小值为1。
	private int mXRegularity;
	// Y分布规则性，该值越高，子view在y方向的分布越规则、平均。最小值为1。
	private int mYRegularity;
	//星星的个数
	private int mAreaCount;
	//星星的密度
	private int[][] mAreaDensity;
	private Set<View> mFixedViews;
	private Adapter mAdapter;
	private List<View> mRecycledViews;
	private boolean mLayouted;
	private int mOverlapAdd = 2;
	/** 构造方法 */
	public RandomLayout(Context context) {
		super(context);
		init();
	}

	/** get方法 */
	public int getXRegularity() {
		return mXRegularity;
	}

	public int getYRegularity() {
		return mYRegularity;
	}

	public boolean hasLayouted() {
		return mLayouted;
	}

	/** 设置mXRegularity和mXRegularity，以确定星星的分布 */
	public void setRegularity(int xRegularity, int yRegularity) {
		if (xRegularity > 1) {
			this.mXRegularity = xRegularity;
		} else {
			this.mXRegularity = 1;
		}
		if (yRegularity > 1) {
			this.mYRegularity = yRegularity;
		} else {
			this.mYRegularity = 1;
		}
		this.mAreaCount = mXRegularity * mYRegularity;
		this.mAreaDensity = new int[mYRegularity][mXRegularity];
	}

	public void setAdapter(Adapter adapter) {
		this.mAdapter = adapter;
	}

	/** 初始化方法 */
	private void init() {
		mLayouted = false;
		mRdm = new Random();
		setRegularity(1, 1);
		mFixedViews = new HashSet<View>();
		mRecycledViews = new LinkedList<View>();
	}

	/** 重新设置区域 */
	private void resetAllAreas() {
		mFixedViews.clear();
		for (int i = 0; i < mYRegularity; i++) {
			for (int j = 0; j < mXRegularity; j++) {
				mAreaDensity[i][j] = 0;
			}
		}
	}

	/** 把复用的View加入集合，新加入的放入集合第一个。 */
	private void pushRecycler(View scrapView) {
		if (null != scrapView) {
			mRecycledViews.add(0, scrapView);
		}
	}

	/** 把复用的View加入集合，新加入的放入集合最后一个 */
	private void enqueueRecycler(View scrapView) {
		if (null != scrapView) {
			mRecycledViews.add(scrapView);
		}
	}

	/** 取出复用的View，从集合的第一个位置取出 */
	private View popRecycler() {
		final int size = mRecycledViews.size();
		if (size > 0) {
			return mRecycledViews.remove(0);
		} else {
			return null;
		}
	}

	/** 产生子View */
	private void generateChildren() {
		if (null == mAdapter) {
			return;
		}
		// 先把子View全部存入集合
		final int childCount = super.getChildCount();
		for (int i = childCount - 1; i >= 0; i--) {
			pushRecycler(super.getChildAt(i));
		}
		// 删除所有子View
		super.removeAllViewsInLayout();
		// 得到Adapter中的数据量
		final int count = mAdapter.getCount();
		for (int i = 0; i < count; i++) {
			//从集合中取出之前存入的子View
			View convertView = popRecycler();
			//把该子View作为adapter的getView的历史View传入，得到返回的View
			View newChild = mAdapter.getView(i, convertView);
			if (newChild != convertView) {//如果发生了复用，那么newChild应该等于convertView
				// 这说明没发生复用，所以重新把这个没用到的子View存入集合中
				pushRecycler(convertView);
			}
			//调用父类的方法把子View添加进来
			super.addView(newChild, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
	}

	/** 重新分配区域 */
	public void redistribute() {
		resetAllAreas();//重新设置区域
		forceLayout();//请求Layout
		invalidate();//重绘
	}

	/** 重新更新子View */
	public void refresh() {
		resetAllAreas();//重新分配区域
		generateChildren();//重新产生子View
		forceLayout();
		invalidate();
	}

	/** 重写父类的removeAllViews */
	@Override
	public void removeAllViews() {
		super.removeAllViews();//先删除所有View
		resetAllAreas();//重新设置所有区域
	}

	/** 测绘子View的大小 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = 0;
		int height = 0;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		// 设置尺寸的最大值
		if (widthMode != MeasureSpec.UNSPECIFIED) {
			width = widthSize;
		}
		if (heightMode != MeasureSpec.UNSPECIFIED) {
			height = heightSize;
		}

		setMeasuredDimension(width, height);
	}

	/** 确定子View的位置，这个就是星图分布的关键 */
	@Override
	public void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = getChildCount();
		//确定自身的宽高
		int thisW = r - l - this.getPaddingLeft() - this.getPaddingRight();
		int thisH = b - t - this.getPaddingTop() - this.getPaddingBottom();

		int childW;
		int childH;
		float colW;
		float rowH;
		int areaIdx;
		int col;
		int row;
		int childWidthMeasureSpec;
		int childHeightMeasureSpec;
		int areaCapacity = (count + 1) / mAreaCount + 1;    //区域密度
		List<Integer> availAreas = new ArrayList<Integer>(mAreaCount);
		int availAreaCount = mAreaCount;
		int contentRight = r - getPaddingRight();
		for (int i = 0; i < mAreaCount; i++) {
			availAreas.add(i);
		}

		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			if (!mFixedViews.contains(child)) {//mFixedViews用于存放已经确定好位置的View
				LayoutParams params = (LayoutParams) child.getLayoutParams();
				//先测量子View的大小
				childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.AT_MOST);
				childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), MeasureSpec.AT_MOST);
				child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
				childW = child.getMeasuredWidth();
				childH = child.getMeasuredHeight();

				//用自身的高度去除以分配值
				colW = thisW / (float) mXRegularity;
				rowH = thisH / (float) mYRegularity;

				while (availAreaCount > 0) {
					int arrayIdx = mRdm.nextInt(availAreaCount);
					areaIdx = availAreas.get(arrayIdx);
					col = areaIdx % mXRegularity;
					row = areaIdx / mXRegularity;
					if (mAreaDensity[row][col] < areaCapacity) {
						// 区域密度未超过限定，将view置入该区域
						int xOffset = (int) colW - childW;
						if (xOffset <= 0) {
							xOffset = 1;
						}
						int yOffset = (int) rowH - childH;
						if (yOffset <= 0) {
							yOffset = 1;
						}
						params.mLeft = getPaddingLeft() + (int) (colW * col + mRdm.nextInt(xOffset));
						int edge = contentRight - childW;
						if (params.mLeft > edge) {
							params.mLeft = edge;
						}
						params.mRight = params.mLeft + childW;
						params.mTop = getPaddingTop() + (int) (rowH * row + mRdm.nextInt(yOffset));
						params.mBottom = params.mTop + childH;
						if(!isOverlap(params)){
							mAreaDensity[row][col]++;
							child.layout(params.mLeft, params.mTop, params.mRight, params.mBottom);
							mFixedViews.add(child);
							break;
						}else{
							availAreas.remove(arrayIdx);
							availAreaCount--;
						}
					} else {
						// 区域密度超过限定
						// 将该区域从可选区域中移除
						availAreas.remove(arrayIdx);
						availAreaCount--;
					}
				}
			}
		}
		mLayouted = true;
	}

	private boolean isOverlap(LayoutParams params) {
		int l = params.mLeft - mOverlapAdd;
		int t = params.mTop - mOverlapAdd;
		int r = params.mRight + mOverlapAdd;
		int b = params.mBottom + mOverlapAdd;

		Rect rect = new Rect();

		for (View v : mFixedViews) {
			int vl = v.getLeft() - mOverlapAdd;
			int vt = v.getTop()- mOverlapAdd;
			int vr = v.getRight() + mOverlapAdd;
			int vb = v.getBottom()+ mOverlapAdd;
			rect.left = Math.max(l, vl);
			rect.top = Math.max(t, vt);
			rect.right = Math.min(r, vr);
			rect.bottom = Math.min(b, vb);
			if(rect.right >= rect.left && rect.bottom >= rect.top){
				System.out.println("------------------------------");
				System.out.println("left:"+rect.left);
				System.out.println("right"+rect.right);
				System.out.println("top:"+rect.top);
				System.out.println("bottom:"+rect.bottom);
				return true;
			}
		}
		return false;
	}

	/** 内部类、接口 */
	public static interface Adapter {

		public abstract int getCount();

		public abstract View getView(int position, View convertView);
	}

	public static class LayoutParams extends ViewGroup.LayoutParams {

		private int mLeft;
		private int mRight;
		private int mTop;
		private int mBottom;

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}

		public LayoutParams(int w, int h) {
			super(w, h);
		}
	}
}
