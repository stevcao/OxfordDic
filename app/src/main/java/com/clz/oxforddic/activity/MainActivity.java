package com.clz.oxforddic.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
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
        final ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), tabFactory));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                viewPager.setCurrentItem(curSelect);
            }
        });
        mTabLayout.setSelect(0);
    }
}
