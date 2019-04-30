package com.clz.oxforddic.ui.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Create by stevcao on 2019/4/30
 */
public class EmptyViewObserver extends RecyclerView.AdapterDataObserver {
    private View mEmptyView;
    private RecyclerView mRecyclerView;


    public EmptyViewObserver(RecyclerView recyclerView, View emptyView) {
        mRecyclerView = recyclerView;
        mEmptyView = emptyView;
        checkIfEmpty();
    }

    /**
     Abstract method implementations
     */
    @Override
    public void onChanged() {
        checkIfEmpty();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        checkIfEmpty();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if (mEmptyView != null && mRecyclerView.getAdapter() != null) {
            boolean emptyViewVisible = mRecyclerView.getAdapter().getItemCount() == 0;
            mEmptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            mRecyclerView.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
        }
    }
}
