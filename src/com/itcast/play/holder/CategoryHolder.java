package com.itcast.play.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.itcast.play.R;
import com.itcast.play.bean.CategoryInfo;
import com.itcast.play.http.image.ImageLoader;
import com.itcast.play.utils.StringUtils;
import com.itcast.play.utils.UIUtils;

/**
 * Created by zlr on 2014/7/20.
 */
public class CategoryHolder extends BaseHolder<CategoryInfo> implements View.OnClickListener {
	private RelativeLayout mRl1,mRl2,mRl3;
	private ImageView mIv1,mIv2,mIv3;
	private TextView mTxt1,mTxt2,mTxt3;

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.category_item);
		mRl1 = (RelativeLayout) view.findViewById(R.id.rl_1);
		mRl2 = (RelativeLayout) view.findViewById(R.id.rl_2);
		mRl3 = (RelativeLayout) view.findViewById(R.id.rl_3);

		mTxt1 = (TextView) view.findViewById(R.id.tv_1);
		mTxt2 = (TextView) view.findViewById(R.id.tv_2);
		mTxt3 = (TextView) view.findViewById(R.id.tv_3);

		mIv1 = (ImageView) view.findViewById(R.id.iv_1);
		mIv2 = (ImageView) view.findViewById(R.id.iv_2);
		mIv3 = (ImageView) view.findViewById(R.id.iv_3);

		mRl1.setOnClickListener(this);
		mRl2.setOnClickListener(this);
		mRl3.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshView() {
		CategoryInfo info = getData();
		if(info == null){
			return;
		}

		String url1 = info.getImageUrl1();
		if(!StringUtils.isEmpty(url1)){
			ImageLoader.load(mIv1,url1);
			mTxt1.setText(info.getName1());
			mRl1.setEnabled(true);
			mRl1.setTag(info.getName1());
		}else{
			mIv1.setVisibility(View.INVISIBLE);
			mTxt1.setVisibility(View.INVISIBLE);
			mRl1.setEnabled(false);
		}

		String url2 = info.getImageUrl2();
		if(!StringUtils.isEmpty(url2)){
			ImageLoader.load(mIv2,url2);
			mTxt2.setText(info.getName2());
			mRl2.setEnabled(true);
			mRl2.setTag(info.getName2());
		}else{
			mIv2.setVisibility(View.INVISIBLE);
			mTxt2.setVisibility(View.INVISIBLE);
			mRl2.setEnabled(false);
		}

		String url3 = info.getImageUrl3();
		if(!StringUtils.isEmpty(url3)){
			ImageLoader.load(mIv3,url3);
			mTxt3.setText(info.getName3());
			mRl3.setEnabled(true);
			mRl3.setTag(info.getName3());
		}else{
			mIv3.setVisibility(View.INVISIBLE);
			mTxt3.setVisibility(View.INVISIBLE);
			mRl3.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		UIUtils.showToastSafe(v.getTag().toString());
	}
}
