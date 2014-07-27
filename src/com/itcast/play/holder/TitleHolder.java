package com.itcast.play.holder;

import android.view.View;
import android.widget.TextView;
import com.itcast.play.R;
import com.itcast.play.bean.CategoryInfo;
import com.itcast.play.utils.UIUtils;

/**
 * Created by zlr on 2014/7/20.
 */
public class TitleHolder extends BaseHolder<CategoryInfo>{
	private TextView mTitle;
	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.category_item_title);
		mTitle = (TextView) view.findViewById(R.id.tv_title);
		return view;
	}

	@Override
	public void refreshView() {
		mTitle.setText(getData().getTitle());
	}
}
