package com.itcast.play.ui.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;
import com.itcast.play.Adapter.MyAdapter;
import com.itcast.play.bean.SubjectInfo;
import com.itcast.play.holder.BaseHolder;
import com.itcast.play.holder.SubjectHolder;
import com.itcast.play.http.protocol.SubjectProtocol;
import com.itcast.play.ui.widget.BaseListView;
import com.itcast.play.ui.widget.LoadPager;
import com.itcast.play.utils.UIUtils;

import java.util.List;

/**
 * Created by zlr on 2014/7/19.
 */
public class SubjectFragment extends BaseFragment {
	private BaseListView mListView;
	List<SubjectInfo> mData;

	@Override
	protected LoadPager.LoadResult load() {
		SubjectProtocol protocol = new SubjectProtocol();
		mData = protocol.load(0);
		return checkLoadResult(mData);
	}

	@Override
	protected View createdLoadedView() {
		mListView = new BaseListView(UIUtils.getContext());
		mListView.setAdapter(new SubjectAdapter(mData, mListView));
		return mListView;
	}

	class SubjectAdapter extends MyAdapter<SubjectInfo> {

		public SubjectAdapter(List<SubjectInfo> data, AbsListView list) {
			super(data, list);
		}

		@Override
		public BaseHolder getHolder() {
			return new SubjectHolder();
		}

		@Override
		protected List onLoadMore(int index) {
			SubjectProtocol protocol = new SubjectProtocol();
			return protocol.load(index);
		}
	}
}
