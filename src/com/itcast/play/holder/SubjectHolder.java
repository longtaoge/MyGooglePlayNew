package com.itcast.play.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.itcast.play.R;
import com.itcast.play.bean.SubjectInfo;
import com.itcast.play.http.image.ImageLoader;
import com.itcast.play.utils.UIUtils;

/**
 * Created by zlr on 2014/7/20.
 */
public class SubjectHolder extends BaseHolder<SubjectInfo>{
	private ImageView mIcon;
	private TextView mText;
	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.subject_item);
		mIcon = (ImageView) view.findViewById(R.id.item_icon);
		mText = (TextView) view.findViewById(R.id.item_txt);
		return view;
	}

	@Override
	public void refreshView() {
		SubjectInfo info = getData();
		if(info == null){
			return;
		}

		mText.setText(info.getDes());
		ImageLoader.load(mIcon,info.getUrl());
	}
}
