package com.clz.oxforddic.ui;

import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Create by stevcao on 2019/4/28
 */
public class TabFactory {

    List<TabInfo> mTabs;

    public TabFactory(List<TabInfo> tabs) {
        this.mTabs = tabs;
    }

    public int getTabCount() {
        return mTabs.size();
    }

    public List<TabInfo> getTabs() {
        return mTabs;
    }

    Fragment getFragment(int pageIndex) {
        TabInfo info = mTabs.get(pageIndex);
        try {
            return (Fragment) info.fragment.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static class TabInfo {
        public String tabName;
        public int tabIcon;
        public Class fragment;

        public TabInfo(String tabName, int tabIcon, Class fragment) {
            this.tabName = tabName;
            this.tabIcon = tabIcon;
            this.fragment = fragment;
        }
    }

}
