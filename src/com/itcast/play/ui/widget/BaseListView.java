package com.itcast.play.ui.widget;

import android.content.Context;
import android.widget.ListView;
import com.itcast.play.R;
import com.itcast.play.utils.UIUtils;

/**
 * Created by zlr on 2014/7/19.
 */
public class BaseListView extends ListView {
	public BaseListView(Context context) {
		super(context);
	}

	protected void init() {
		setDivider(UIUtils.getResources().getDrawable(R.drawable.nothing));
		setCacheColorHint(UIUtils.getColor(R.color.bg_page));
		setSelector(UIUtils.getResources().getDrawable(R.drawable.nothing));
		setBackgroundColor(UIUtils.getColor(R.color.bg_page));
	}
}
