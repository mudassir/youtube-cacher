package io.gitlab.mudassir.youtubecacher.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple FragmentPagerAdapter
 */
public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

	private List<Fragment> mFragments;

	public FragmentPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
		mFragments = new ArrayList<>();
	}

	public void addFragment(Fragment fragment) {
		mFragments.add(fragment);
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.isEmpty() || position > getCount() ? null : mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}
}
