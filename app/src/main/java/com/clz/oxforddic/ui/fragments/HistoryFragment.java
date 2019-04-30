package com.clz.oxforddic.ui.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clz.oxforddic.R;
import com.clz.oxforddic.activity.MainActivity;
import com.clz.oxforddic.model.db.DataBaseHelper;
import com.clz.oxforddic.model.entity.SearchHistory;
import com.clz.oxforddic.model.repository.RepositoryManager;
import com.clz.oxforddic.ui.widget.EmptyViewObserver;

/**
 * Create by stevcao on 2019/4/28
 */
public class HistoryFragment extends Fragment{

    RecyclerView mHistoryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHistoryList = view.findViewById(R.id.history_list);
        mHistoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHistoryList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        final HistoryListAdapter adapter = new HistoryListAdapter();
        PagedList.Config config = new PagedList.Config.Builder().setPageSize(30)
                .setEnablePlaceholders(false).build();
        LiveData<PagedList<SearchHistory>> histories = new LivePagedListBuilder<Integer, SearchHistory>(
                DataBaseHelper.getInstance().getHistoryDao().getSearchHistoryDataFactory(),
                config)
                .build();

        histories.observe(this, new Observer<PagedList<SearchHistory>>() {
            @Override
            public void onChanged(@Nullable PagedList<SearchHistory> searchHistories) {
                adapter.submitList(searchHistories);
            }
        });
        mHistoryList.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new EmptyViewObserver(mHistoryList, view.findViewById(R.id.no_history)));
    }

    class HistoryListAdapter extends PagedListAdapter<SearchHistory, HistoryListAdapter.ViewHolder> {

        public HistoryListAdapter() {
            super(DIFF_CALLBACK);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_history_item, viewGroup, false);
            ViewHolder holder = new ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.onBind(getItem(i));
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView historyKeyWord;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                historyKeyWord = itemView.findViewById(R.id.history_key);
            }

            public void onBind(final SearchHistory history) {
                historyKeyWord.setText(history.searchKey);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToSearch(history.searchKey);
                    }
                });
            }
        }
    }

    private static DiffUtil.ItemCallback<SearchHistory> DIFF_CALLBACK = new DiffUtil.ItemCallback<SearchHistory>() {
        @Override
        public boolean areItemsTheSame(@NonNull SearchHistory history, @NonNull SearchHistory t1) {
            return history == t1;
        }

        @Override
        public boolean areContentsTheSame(@NonNull SearchHistory history, @NonNull SearchHistory t1) {
            return history.searchKey.equals(t1.searchKey);
        }
    };

    public void goToSearch(String searchKey) {
        MainActivity activity = (MainActivity)getActivity();
        activity.goSearch(searchKey);
    }

}
