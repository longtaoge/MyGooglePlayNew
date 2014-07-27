package com.itcast.play.ui;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.itcast.play.R;
import com.itcast.play.ui.fragment.FragmentFactory;
import com.itcast.play.ui.widget.PagerTab;
import com.itcast.play.utils.UIUtils;

public class MyActivity extends BaseActivity {
	private ActionBarDrawerToggle mToggle;
	private DrawerLayout mDrawer;
	private RelativeLayout mContent, mMenu;
	private ViewPager mPager;
	private PagerTab mTab;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void initView() {
		setContentView(R.layout.main);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mMenu = (RelativeLayout) findViewById(R.id.menu);
		mContent = (RelativeLayout) findViewById(R.id.content);

		mDrawer.setDrawerListener(new MyDrawerListener());
		mDrawer.setDrawerShadow(R.drawable.ic_drawer_shadow, GravityCompat.START);

		mPager = (ViewPager) findViewById(R.id.pager);
		mTab = (PagerTab) findViewById(R.id.tabs);

		MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(adapter);

		mTab.setViewPager(mPager);
	}

	@Override
	protected void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(getString(R.string.app_name));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		mToggle = new ActionBarDrawerToggle(this, mDrawer, R.drawable.ic_drawer, R.string.app_name, R.string.app_name);
		mToggle.syncState();
	}

	class MyDrawerListener implements DrawerLayout.DrawerListener {

		@Override
		public void onDrawerSlide(View view, float v) {
			mToggle.onDrawerSlide(view, v);
		}

		@Override
		public void onDrawerOpened(View view) {
			mToggle.onDrawerOpened(view);
		}

		@Override
		public void onDrawerClosed(View view) {
			mToggle.onDrawerClosed(view);
		}

		@Override
		public void onDrawerStateChanged(int i) {
			mToggle.onDrawerStateChanged(i);
		}
	}

	class MyPagerAdapter extends FragmentPagerAdapter {
		String[] titles;

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			titles = UIUtils.getStringArray(R.array.tab_names);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public Fragment getItem(int i) {
			return FragmentFactory.createFragment(i);
		}

		@Override
		public int getCount() {
			return titles.length;
		}
	}
}
