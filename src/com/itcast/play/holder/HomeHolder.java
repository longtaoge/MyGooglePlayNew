package com.itcast.play.holder;

import android.view.View;
import android.widget.*;
import com.itcast.play.R;
import com.itcast.play.bean.AppInfo;
import com.itcast.play.http.image.ImageLoader;
import com.itcast.play.utils.StringUtils;
import com.itcast.play.utils.UIUtils;

/**
 * Created by zlr on 2014/7/19.
 */
public class HomeHolder extends BaseHolder<AppInfo> {
	private ImageView icon;
	private TextView tvTitle, tvSize, tvDes;
	private RatingBar rb;
	private RelativeLayout mActionLayout;
	private FrameLayout mProgressLayout;

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.list_item);
		icon = (ImageView) view.findViewById(R.id.item_icon);
		tvTitle = (TextView) view.findViewById(R.id.item_title);
		tvSize = (TextView) view.findViewById(R.id.item_size);
		tvDes = (TextView) view.findViewById(R.id.item_bottom);
		rb = (RatingBar) view.findViewById(R.id.item_rating);
		return view;
	}

	@Override
	public void refreshView() {
		AppInfo data = getData();
		if(data == null){
			return;
		}
		tvTitle.setText(data.getName());
		tvSize.setText(StringUtils.formatFileSize(getData().getSize()));
		tvDes.setText(data.getDes());
		rb.setRating(data.getStars());
		ImageLoader.load(icon,data.getIconUrl());
	}
}
