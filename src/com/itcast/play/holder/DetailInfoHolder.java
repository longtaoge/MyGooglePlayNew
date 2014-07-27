package com.itcast.play.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.itcast.play.R;
import com.itcast.play.bean.AppInfo;
import com.itcast.play.http.image.ImageLoader;
import com.itcast.play.utils.StringUtils;
import com.itcast.play.utils.UIUtils;

/**
 * Created by zlr on 2014/7/19.
 */
public class DetailInfoHolder extends BaseHolder<AppInfo>{
	private ImageView mIcon;
	private TextView mTitleTxt, mDownloadTxt, mViesionTxt, mDateTxt, mSizeTxt;
	private RatingBar mRating;

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.app_detail_info);
		mIcon = (ImageView) view.findViewById(R.id.item_icon);
		mRating = (RatingBar) view.findViewById(R.id.item_rating);
		mTitleTxt = (TextView) view.findViewById(R.id.item_title);
		mDownloadTxt = (TextView) view.findViewById(R.id.item_download);
		mViesionTxt = (TextView) view.findViewById(R.id.item_version);
		mDateTxt = (TextView) view.findViewById(R.id.item_date);
		mSizeTxt = (TextView) view.findViewById(R.id.item_size);
		return view;
	}

	@Override
	public void refreshView() {
		AppInfo info = getData();
		if(info == null){
			return;
		}
		ImageLoader.load(mIcon,info.getIconUrl());
		mRating.setRating(info.getStars());
		mTitleTxt.setText(info.getName());
		mDownloadTxt.setText(UIUtils.getString(R.string.app_detail_download) + info.getDownloadNum());
		mViesionTxt.setText(info.getVersion());
		mDateTxt.setText(info.getDate());
		mSizeTxt.setText(StringUtils.formatFileSize(info.getSize()));
	}
}
