package com.clz.oxforddic.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Create by stevcao on 2019/4/28
 */
public class TabsAdapter extends FragmentStatePagerAdapter {
    TabFactory mFactory;

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
        return mFactory.getFragment(position);
    }


}
