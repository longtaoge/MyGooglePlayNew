package com.itcast.play.ui.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import com.itcast.play.Adapter.MyAdapter;
import com.itcast.play.bean.CategoryInfo;
import com.itcast.play.holder.BaseHolder;
import com.itcast.play.holder.CategoryHolder;
import com.itcast.play.holder.TitleHolder;
import com.itcast.play.http.protocol.CategoryProtocol;
import com.itcast.play.ui.widget.BaseListView;
import com.itcast.play.ui.widget.LoadPager;
import com.itcast.play.utils.UIUtils;

import java.util.List;

/**
 * Created by zlr on 2014/7/19.
 */
public class CategoryFragment extends BaseFragment {

	private List<CategoryInfo> mData;
	private BaseListView mListView;
	@Override
	protected LoadPager.LoadResult load() {
		CategoryProtocol protocol = new CategoryProtocol();
		mData = protocol.load(0);
		return checkLoadResult(mData);
	}

	@Override
	protected View createdLoadedView() {
		mListView = new BaseListView(UIUtils.getContext());
		mListView.setAdapter(new CategoryAdapter(mData,mListView));
		return mListView;
	}

	class CategoryAdapter extends MyAdapter<CategoryInfo>{
		public static final int TITILE_TYPE = MORE_TYPE + 1;
		private int mCurrentPositon;

		public CategoryAdapter(List<CategoryInfo> data, AbsListView list) {
			super(data, list);
		}

		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount() + 1;
		}

		@Override
		public int getItemViewType(int position) {
			int type = super.getItemViewType(position);
			if(type == ITEM_TYPE){
				CategoryInfo info = getData().get(position);
				if(info.isTitle()){
					return TITILE_TYPE;
				}
			}
			return type;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			mCurrentPositon = position;
			return super.getView(position, convertView, parent);
		}

		@Override
		public BaseHolder getHolder() {
			CategoryInfo info = getData().get(mCurrentPositon);
			if(info.isTitle()){
				return new TitleHolder();
			}
			return new CategoryHolder();
		}

		@Override
		public boolean hasMore() {
			return false;
		}
	}
}
