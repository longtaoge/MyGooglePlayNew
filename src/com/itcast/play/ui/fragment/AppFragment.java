package com.itcast.play.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import com.itcast.play.Adapter.MyAdapter;
import com.itcast.play.bean.AppInfo;
import com.itcast.play.holder.BaseHolder;
import com.itcast.play.holder.HomeHolder;
import com.itcast.play.http.protocol.AppProtocol;
import com.itcast.play.http.protocol.HomeProtocol;
import com.itcast.play.ui.DetailActivity;
import com.itcast.play.ui.widget.BaseListView;
import com.itcast.play.ui.widget.LoadPager;
import com.itcast.play.utils.UIUtils;

import java.util.List;

/**
 * Created by zlr on 2014/7/19.
 */
public class AppFragment extends BaseFragment {
	private BaseListView mListView;
	private List<AppInfo> mData;

	@Override
	protected LoadPager.LoadResult load() {
		AppProtocol protocol = new AppProtocol();
		mData = protocol.load(0);
		return checkLoadResult(mData);
	}

	@Override
	protected View createdLoadedView() {
		mListView = new BaseListView(UIUtils.getContext());
		mListView.setAdapter(new HomeAdapter(mData, mListView));
		return mListView;
	}

	class HomeAdapter extends MyAdapter<AppInfo> implements AdapterView.OnItemClickListener {

		public HomeAdapter(List<AppInfo> data, AbsListView list) {
			super(data, list);
			list.setOnItemClickListener(this);
		}

		@Override
		public BaseHolder getHolder() {
			return new HomeHolder();
		}

		@Override
		protected List<AppInfo> onLoadMore(int index) {
			AppProtocol protocol = new AppProtocol();
			List<AppInfo> load = protocol.load(index);
			System.out.println("index:"+index + "   LOAD:"+load.size());
			return load;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);
			AppInfo info = getData().get(position);
			intent.putExtra(DetailActivity.PACKAGENAME,info.getPackageName());
			UIUtils.startActivity(intent);
		}
	}
}
