package com.itcast.play.ui.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;
import com.itcast.play.Adapter.MyAdapter;
import com.itcast.play.bean.AppInfo;
import com.itcast.play.holder.BaseHolder;
import com.itcast.play.holder.HomeHolder;
import com.itcast.play.http.protocol.AppProtocol;
import com.itcast.play.http.protocol.GameProtocol;
import com.itcast.play.ui.widget.BaseListView;
import com.itcast.play.ui.widget.LoadPager;
import com.itcast.play.utils.UIUtils;

import java.util.List;

/**
 * Created by zlr on 2014/7/19.
 */
public class GameFragment extends BaseFragment {
	private BaseListView mListView;
	private List<AppInfo> mData;

	@Override
	protected LoadPager.LoadResult load() {
		GameProtocol protocol = new GameProtocol();
		mData = protocol.load(0);
		return checkLoadResult(mData);
	}

	@Override
	protected View createdLoadedView() {
		mListView = new BaseListView(UIUtils.getContext());
		mListView.setAdapter(new HomeAdapter(mData, mListView));
		return mListView;
	}

	class HomeAdapter extends MyAdapter<AppInfo> {

		public HomeAdapter(List<AppInfo> data, AbsListView list) {
			super(data, list);
		}

		@Override
		public BaseHolder getHolder() {
			return new HomeHolder();
		}

		@Override
		protected List<AppInfo> onLoadMore(int index) {
			GameProtocol protocol = new GameProtocol();
			List<AppInfo> load = protocol.load(index);
			System.out.println("index:"+index + "   LOAD:"+load.size());
			return load;
		}
	}
}
