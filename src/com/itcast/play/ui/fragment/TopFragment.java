package com.itcast.play.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.itcast.play.http.protocol.HotProtocol;
import com.itcast.play.ui.widget.FlowLayout;
import com.itcast.play.ui.widget.LoadPager;
import com.itcast.play.utils.DrawableUtils;
import com.itcast.play.utils.UIUtils;

import java.util.List;
import java.util.Random;

/**
 * Created by zlr on 2014/7/19.
 */
public class TopFragment extends BaseFragment {
	private List<String> mData;
	@Override
	protected LoadPager.LoadResult load() {
		HotProtocol protocol = new HotProtocol();
		mData = protocol.load(0);
		return checkLoadResult(mData);
	}

	@Override
	protected View createdLoadedView() {
		FlowLayout layout = new FlowLayout(UIUtils.getContext());
		int backColor = 0xffcecece;
		Drawable pressDrawable = DrawableUtils.createDrawable(backColor, backColor, 5);
		int textPaddingH = UIUtils.dip2px(7);
		int textPaddingV = UIUtils.dip2px(4);
		for (int i = 0; i < mData.size(); i++) {
			TextView tv = new TextView(UIUtils.getContext());

			Random mRdm = new Random();
			int r = 32 + mRdm.nextInt(200);
			int g = 32 + mRdm.nextInt(200);
			int b = 32 + mRdm.nextInt(200);
			int backGroudColor = Color.rgb(r,g,b);

			Drawable drawable = DrawableUtils.createDrawable(backGroudColor, backColor, 5);
			StateListDrawable selector = DrawableUtils.createSelector(drawable, pressDrawable);
			tv.setPadding(textPaddingH, textPaddingV, textPaddingH, textPaddingV);
			tv.setBackgroundDrawable(selector);
			tv.setText(mData.get(i));
			tv.setGravity(Gravity.CENTER);

			tv.setTextColor(Color.rgb(255,255,255));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

			layout.addView(tv);
		}
		return layout;
	}
}
