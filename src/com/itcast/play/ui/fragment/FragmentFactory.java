package com.itcast.play.ui.fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zlr on 2014/7/19.
 */
public class FragmentFactory {
	public static final int TAB_HOME = 0;
	public static final int TAB_APP = 1;
	public static final int TAB_GAME = 2;
	public static final int TAB_SUBJECT = 3;
	public static final int TAB_RECOMMEND = 4;
	public static final int TAB_CATEGORY = 5;
	public static final int TAB_TOP = 6;

	private static Map<Integer, BaseFragment> mFragmentCache = new HashMap<Integer, BaseFragment>();

	public static Fragment createFragment(int position) {

		BaseFragment fragment = mFragmentCache.get(position);
		if (fragment != null) {
			return fragment;
		}
		switch (position) {
			case TAB_HOME:
				fragment = new HomeFragment();
				break;
			case TAB_APP:
				fragment = new AppFragment();
				break;
			case TAB_GAME:
				fragment = new GameFragment();
				break;
			case TAB_SUBJECT:
				fragment = new SubjectFragment();
				break;
			case TAB_RECOMMEND:
				fragment = new RecommendFragment();
				break;
			case TAB_CATEGORY:
				fragment = new CategoryFragment();
				break;
			case TAB_TOP:
				fragment = new TopFragment();
				break;
		}
		mFragmentCache.put(position, fragment);
		return fragment;
	}
}
