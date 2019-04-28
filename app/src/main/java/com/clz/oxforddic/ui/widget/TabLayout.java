package com.clz.oxforddic.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clz.oxforddic.R;
import com.clz.oxforddic.ui.TabFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by stevcao on 2019/4/28
 */
public class TabLayout extends LinearLayout {

    List<ViewHolder> itemList = new ArrayList<>();

    OnTabChangedListener mOnTabChangedListener;

    public TabLayout(Context context) {
        super(context);
        init();
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    TabFactory mTabFactory;

    public void setTabFactory(TabFactory factory) {
        if (mTabFactory != null) {
            removeAllViews();
        }
        mTabFactory = factory;
        int index = 0;
        for(TabFactory.TabInfo tabInfo : mTabFactory.getTabs()) {
            addTabItem(tabInfo.tabName, tabInfo.tabIcon, index);
            index++;
        }
    }

    public void addTabItem(String word, int icon, final int index) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_tab_item, this, false);
        ViewHolder holder = new ViewHolder(itemView);
        holder.itemText.setText(word);
        holder.itemIcon.setImageResource(icon);
        itemList.add(holder);
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(index);
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
        addView(itemView, lp);
    }

    public void setSelect(int index) {
        for (int i = 0; i < itemList.size(); i++) {
            ViewHolder holder = itemList.get(i);
            if (i == index) {
                holder.itemIcon.setAlpha(1f);
                holder.itemText.setAlpha(1f);
                holder.itemText.setTextColor(Color.BLACK);
            } else {
                holder.itemIcon.setAlpha(0.5f);
                holder.itemText.setAlpha(0.5f);
                holder.itemText.setTextColor(Color.GRAY);
            }
        }
        if (mOnTabChangedListener != null) {
            this.mOnTabChangedListener.onTabChanged(index);
        }
    }

    public void setOnTabChangedListener(OnTabChangedListener mOnTabChangedListener) {
        this.mOnTabChangedListener = mOnTabChangedListener;
    }

    public interface OnTabChangedListener {
        void onTabChanged(int curSelect);
    }

    class ViewHolder {
        public View itemView;
        public ImageView itemIcon;
        public TextView itemText;
        ViewHolder(View itemView) {
            itemText = itemView.findViewById(R.id.tab_text);
            itemIcon = itemView.findViewById(R.id.tab_icon);
            this.itemView = itemView;
        }
    }

}
