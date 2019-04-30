package com.clz.oxforddic.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.clz.oxforddic.R;
import com.clz.oxforddic.ui.TabFactory;
import com.clz.oxforddic.ui.TabsAdapter;
import com.clz.oxforddic.ui.fragments.DictionaryFragment;
import com.clz.oxforddic.ui.fragments.FavorFragment;
import com.clz.oxforddic.ui.fragments.HistoryFragment;
import com.clz.oxforddic.ui.widget.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    TabLayout mTabLayout;
    TabsAdapter mTabAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {// <5.0
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.GRAY);
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.status_bar_bg).setVisibility(View.GONE);
        }
        mTabLayout = findViewById(R.id.tab);
        initTabs();
    }

    public void initTabs() {
        List<TabFactory.TabInfo> tabs = new ArrayList<>();
        tabs.add(new TabFactory.TabInfo(getString(R.string.search), R.drawable.search, DictionaryFragment.class));
        tabs.add(new TabFactory.TabInfo("历史", R.drawable.history, HistoryFragment.class));
        tabs.add(new TabFactory.TabInfo("收藏", R.drawable.favorites, FavorFragment.class));
        TabFactory tabFactory = new TabFactory(tabs);
        mTabLayout.setTabFactory(tabFactory);
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        mTabAdapter = new TabsAdapter(getSupportFragmentManager(), tabFactory);
        mViewPager.setAdapter(mTabAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                mTabLayout.setSelect(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        mTabLayout.setOnTabChangedListener(new TabLayout.OnTabChangedListener() {
            @Override
            public void onTabChanged(int curSelect) {
                mViewPager.setCurrentItem(curSelect);
            }
        });
        mTabLayout.setSelect(0);
    }

    public void goSearch(String keyWord) {
        mViewPager.setCurrentItem(0);
        DictionaryFragment fragment = (DictionaryFragment) mTabAdapter.getFragment(0);
        if (fragment != null) {
            fragment.startSearch(keyWord);
        }
    }
}
