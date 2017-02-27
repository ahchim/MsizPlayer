package com.ahchim.android.simplemusicplayer.util.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahchim on 2017-02-27.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragments;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public void add(Fragment fragment){
        fragments.add(fragment);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
