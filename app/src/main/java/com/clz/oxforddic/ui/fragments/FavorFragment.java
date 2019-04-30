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
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clz.oxforddic.R;
import com.clz.oxforddic.model.db.DataBaseHelper;
import com.clz.oxforddic.model.entity.FavorWord;
import com.clz.oxforddic.model.entity.SearchHistory;
import com.clz.oxforddic.ui.widget.EmptyViewObserver;

/**
 * Create by stevcao on 2019/4/28
 */
public class FavorFragment extends BaseFragment {

    RecyclerView mFavorList;

    boolean isEditMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favor, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
        mFavorList = view.findViewById(R.id.favor_list);
        mFavorList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFavorList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        final FavorListAdapter adapter = new FavorListAdapter();
        PagedList.Config config = new PagedList.Config.Builder().setPageSize(30)
                .setEnablePlaceholders(false).build();
        LiveData<PagedList<FavorWord>> histories = new LivePagedListBuilder<Integer, FavorWord>(
                DataBaseHelper.getInstance().getFavorDao().getFavorWordsDataFactory(),
                config)
                .build();

        histories.observe(this, new Observer<PagedList<FavorWord>>() {
            @Override
            public void onChanged(@Nullable PagedList<FavorWord> searchHistories) {
                adapter.submitList(searchHistories);
            }
        });
        mFavorList.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new EmptyViewObserver(mFavorList, view.findViewById(R.id.no_history)));
    }


    class FavorListAdapter extends PagedListAdapter<FavorWord, FavorListAdapter.ViewHolder> {

        public FavorListAdapter() {
            super(DIFF_CALLBACK);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_favor_word_item, viewGroup, false);
            ViewHolder holder = new ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.onBind(getItem(i));
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView favorTv;
            View deleteIv;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                favorTv = itemView.findViewById(R.id.favor_word);
                deleteIv = itemView.findViewById(R.id.delete_iv);
                if(isEditMode) {
                    deleteIv.setVisibility(View.VISIBLE);
                } else {
                    deleteIv.setVisibility(View.GONE);
                }
                deleteIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 删除收藏
                    }
                });
            }

            public void onBind(final FavorWord favorWord) {
                favorTv.setText(favorWord.word);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToSearch(favorWord.word);
                    }
                });
            }
        }
    }

    private static DiffUtil.ItemCallback<FavorWord> DIFF_CALLBACK = new DiffUtil.ItemCallback<FavorWord>() {
        @Override
        public boolean areItemsTheSame(@NonNull FavorWord o, @NonNull FavorWord t1) {
            return o == t1;
        }

        @Override
        public boolean areContentsTheSame(@NonNull FavorWord o, @NonNull FavorWord t1) {
            return o.equals(t1);
        }
    };

}
