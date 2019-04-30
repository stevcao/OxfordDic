package com.clz.oxforddic.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clz.oxforddic.R;
import com.clz.oxforddic.model.db.DataBaseHelper;
import com.clz.oxforddic.model.entity.Word;
import com.clz.oxforddic.model.repository.RepositoryManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by stevcao on 2019/4/28
 */
public class DictionaryFragment extends Fragment {

    View mResultLayout;
    View mSearchKeyWordLayout;
    View mSearchProgressLayout;


    EditText mSearchKeyWord;
    WebView mResultWebView;
    View mNoResultView;
    View clearTextBtn;
    Button searchTv;
    ListView mSearchKeyWordList;
    KeyWordsAdapter mKeyWordsAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchKeyWord = root.findViewById(R.id.et_search_keyword);
        mResultLayout = root.findViewById(R.id.result_layout);
        mResultWebView = root.findViewById(R.id.search_result);
        mResultWebView.getSettings().setJavaScriptEnabled(true);

        mNoResultView = root.findViewById(R.id.no_result);
        clearTextBtn = root.findViewById(R.id.ib_clear_text);
        searchTv = root.findViewById(R.id.btn_cancel_search);

        mSearchKeyWordLayout = root.findViewById(R.id.search_key_word_layout);
        mSearchKeyWordList = root.findViewById(R.id.search_key_word_list);
        mKeyWordsAdapter = new KeyWordsAdapter();
        mSearchKeyWordList.setAdapter(mKeyWordsAdapter);

        mSearchProgressLayout = root.findViewById(R.id.search_progress_layout);

        mSearchKeyWord.addTextChangedListener(mTextWatcher);

        mSearchKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch(mSearchKeyWord.getText().toString());
                }
                return false;
            }
        });

        searchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchTv.getText().equals("取消")) {
                    showBlank();
                } else {
                    startSearch(mSearchKeyWord.getText().toString());
                }
            }
        });

        return root;
    }

    private void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RepositoryManager.getInstance().getWordRepository();
    }

    public void startSearch(final String keyWord) {
        hideSoftInput(mSearchKeyWord);
        setTextWithWatcher(keyWord);
        showSearchProgress();
        if (!TextUtils.isEmpty(keyWord)) {
            Thread searchThread = new Thread() {
                @Override
                public void run() {
                    final ArrayList<Word> words = (ArrayList) DataBaseHelper.getInstance().getWordDao().queryWord(keyWord);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (words != null && words.size() > 0) {
                                showResult(words.get(0));
                            } else {
                                showResult(null);
                            }
                        }
                    });
                    if (words.size() > 0) {
                        Word word = words.get(0);
                        RepositoryManager.getInstance().getHistoryRepository().saveHistory(keyWord, word);
                    }
                }
            };
            searchThread.start();
        } else {
            Toast.makeText(getActivity(), "关键字为空，无法搜索!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showBlank() {
        mResultLayout.setVisibility(View.GONE);
        mSearchKeyWordLayout.setVisibility(View.GONE);
        mSearchProgressLayout.setVisibility(View.GONE);
    }

    public void showSearchProgress() {
        mResultLayout.setVisibility(View.GONE);
        mSearchKeyWordLayout.setVisibility(View.GONE);
        mSearchProgressLayout.setVisibility(View.VISIBLE);
    }

    public void showResult(Word word) {
        mResultLayout.setVisibility(View.VISIBLE);
        mSearchKeyWordLayout.setVisibility(View.GONE);
        mSearchProgressLayout.setVisibility(View.GONE);
        if (word == null) {
            mNoResultView.setVisibility(View.VISIBLE);
            mResultWebView.setVisibility(View.GONE);
        } else {
            mNoResultView.setVisibility(View.GONE);
            mResultWebView.setVisibility(View.VISIBLE);
            mResultWebView.clearCache(true);
            mResultWebView.clearHistory();
//            mResultWebView.loadData(word.html, "text/html", "UTF-8");
            mResultWebView.loadDataWithBaseURL("", word.html, "text/html", "UTF-8", null);
        }
    }

    public void showAssociatedKeys(List<String> keys) {
        mResultLayout.setVisibility(View.GONE);
        mSearchKeyWordLayout.setVisibility(View.VISIBLE);
        mSearchProgressLayout.setVisibility(View.GONE);
        mKeyWordsAdapter.update(keys);

    }

    private void setTextWithWatcher(String text) {
        mSearchKeyWord.removeTextChangedListener(mTextWatcher);
        mSearchKeyWord.setText(text);
        mSearchKeyWord.addTextChangedListener(mTextWatcher);
        mSearchKeyWord.setSelection(text.length());
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                clearTextBtn.setVisibility(View.VISIBLE);
                List<String> assocatedKeys = RepositoryManager.getInstance().getWordRepository().getAssociatedKey(s.toString());
                showAssociatedKeys(assocatedKeys);
                searchTv.setText("搜索");
            } else {
                clearTextBtn.setVisibility(View.GONE);
                showBlank();
            }
        }
    };

    class KeyWordsAdapter extends BaseAdapter {

        List<String> keys;

        KeyWordsAdapter() {
            keys = new ArrayList<>();
        }

        public void update(List<String> keys) {
            this.keys = keys;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return keys.size();
        }

        @Override
        public Object getItem(int position) {
            return keys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                TextView tv = new TextView(getActivity());
                convertView = tv;
                holder = new ViewHolder();
                holder.tv = tv;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final String key = (String)getItem(position);

            holder.tv.setText(key);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSearch(key);
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView tv;
        }
    }

}
