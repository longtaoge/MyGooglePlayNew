package com.itcast.play.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import com.itcast.play.holder.BaseHolder;
import com.itcast.play.holder.MoreHolder;
import com.itcast.play.manager.ThreadManager;
import com.itcast.play.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlr on 2014/7/19.
 */
public abstract class MyAdapter<T> extends BaseAdapter implements AbsListView.RecyclerListener {
	public static final int ITEM_TYPE = 0;
	public static final int MORE_TYPE = ITEM_TYPE + 1;

	public static final int LOAD_SIZE = 20;
	private List<T> mData;
	private AbsListView mListView;
	private List<BaseHolder> mHolder = new ArrayList<BaseHolder>();
	private BaseHolder<Integer> mMoreHolder;
	private boolean mIsLoading;

	public MyAdapter(List<T> data, AbsListView list) {
		mData = data;
		mListView = list;
		mListView.setRecyclerListener(this);
		mMoreHolder = new MoreHolder(this);
		mMoreHolder.setData(hasMore() ? MoreHolder.HASMORE : MoreHolder.EMPTY);
	}

	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (position < mData.size()) {
			return ITEM_TYPE;
		} else {
			return MORE_TYPE;
		}
	}

	@Override
	public int getCount() {
		return mData.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		if (position < mData.size()) {
			return mData.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public List<T>  getData(){
		return mData;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder holder;
		if (convertView != null && convertView.getTag() instanceof BaseHolder) {
			holder = (BaseHolder) convertView.getTag();
		} else {
			if (position < mData.size()) {
				holder = getHolder();
			} else {
				holder = mMoreHolder;
			}
		}
		if (position < mData.size()) {
			holder.setData(mData.get(position));
		}else{
			loadMore();
		}
		mHolder.add(holder);
		return holder.getView();
	}

	public abstract BaseHolder getHolder();

	@Override
	public void onMovedToScrapHeap(View view) {
		Object tag = null;
		if (view != null && (tag = view.getTag()) != null && tag instanceof BaseHolder) {
			mHolder.remove((BaseHolder) tag);
		}
	}

	public void loadMore() {
		if (!mIsLoading && mMoreHolder.getData() != MoreHolder.EMPTY) {
			mIsLoading = true;
			ThreadManager.getLongPool().execute(new Runnable() {
				@Override
				public void run() {
					final List<T> moreData = onLoadMore(mData.size());
					UIUtils.runInMainThread(new Runnable() {
						@Override
						public void run() {
							if (moreData == null) {
								mMoreHolder.setData(MoreHolder.EROOR);
								mIsLoading = false;
								return;
							}
							if (moreData.size() < LOAD_SIZE) {
								System.out.println("EMPTY");
								mMoreHolder.setData(MoreHolder.EMPTY);
							} else {
								System.out.println("HASMORE");
								mMoreHolder.setData(MoreHolder.HASMORE);
							}
							mData.addAll(moreData);
							notifyDataSetChanged();
							mIsLoading = false;
						}
					});
				}
			});
		}
	}

	public boolean hasMore() {
		return true;
	}

	protected List<T> onLoadMore(int index){
		return null;
	}
}
