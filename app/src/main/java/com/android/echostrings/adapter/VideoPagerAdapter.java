package com.android.echostrings.adapter;

import androidx.fragment.app.Fragment;

import com.android.echostrings.fragments.VideoListFragment;

public class VideoPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {

    private String[] categories;

    public VideoPagerAdapter(androidx.fragment.app.FragmentManager fm, String[] categories) {
        super(fm);
        this.categories = categories;
    }

    @Override
    public Fragment getItem(int position) {
        return VideoListFragment.newInstance(categories[position]);
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories[position];
    }
}