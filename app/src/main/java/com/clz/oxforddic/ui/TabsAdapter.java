package com.clz.oxforddic.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by stevcao on 2019/4/28
 */
public class TabsAdapter extends FragmentStatePagerAdapter {
    TabFactory mFactory;

    Map<Integer, Fragment> mFragments = new HashMap<>();

    public TabsAdapter(FragmentManager fm, TabFactory factory){
        super(fm);
        this.mFactory = factory;
    }
    @Override
    public int getCount() {
        return mFactory.getTabCount();
    }
    @Override
    public Fragment getItem(int position){
        Fragment fragment = mFactory.getFragment(position);
        mFragments.put(position, fragment);
        return fragment;
    }

    public Fragment getFragment(int position) {
        return mFragments.get(position);
    }


}
