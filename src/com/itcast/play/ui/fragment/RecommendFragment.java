package com.itcast.play.ui.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import com.itcast.play.http.protocol.RecommendProtocol;
import com.itcast.play.ui.widget.LoadPager;
import com.itcast.play.ui.widget.randomLayout.ShakeListener;
import com.itcast.play.ui.widget.randomLayout.StellarMap;
import com.itcast.play.utils.UIUtils;

import java.util.List;
import java.util.Random;

/**
 * Created by zlr on 2014/7/19.
 */
public class RecommendFragment extends BaseFragment {
	private List<String> mData;

	@Override
	protected LoadPager.LoadResult load() {
		RecommendProtocol protocol = new RecommendProtocol();
		mData = protocol.load(0);
		return checkLoadResult(mData);
	}

	@Override
	protected View createdLoadedView() {
		final StellarMap layout = new StellarMap(UIUtils.getContext());
		layout.setRegularity(6, 9);
		layout.setInnerPadding(20, 20, 20, 20);
		layout.setAdapter(new StellMapAdapter());
		layout.setGroup(0, false);
		ShakeListener listener = new ShakeListener(UIUtils.getContext());
		listener.setOnShakeListener(new ShakeListener.OnShakeListener() {
			@Override
			public void onShake() {
				layout.setGroup(layout.getCurrentGroup() + 1, true);
			}
		});
		return layout;
	}

	class StellMapAdapter implements StellarMap.Adapter {

		@Override
		public int getGroupCount() {
			return 2;
		}

		@Override
		public int getCount(int group) {
			return 15;
		}

		@Override
		public View getView(int group, int position, View convertView) {
			TextView tv = null;
			if (convertView == null) {
				tv = new TextView(UIUtils.getContext());
			} else {
				tv = (TextView) convertView;
			}

			Random mRdm = new Random();
			tv.setTextSize(16 + mRdm.nextInt(6));

			int r = 32 + mRdm.nextInt(200);
			int g = 32 + mRdm.nextInt(200);
			int b = 32 + mRdm.nextInt(200);
			tv.setTextColor(Color.rgb(r, g, b));

			tv.setText(mData.get(group * 15 + position));
			return tv;
		}

		@Override
		public int getNextGroupOnPan(int group, float degree) {
			return group + 1;
		}

		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			return group - 1;
		}
	}
}
