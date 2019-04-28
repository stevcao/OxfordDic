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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clz.oxforddic.R;
import com.clz.oxforddic.model.db.DataBaseHelper;
import com.clz.oxforddic.model.entity.Word;

import java.util.ArrayList;

/**
 * Create by stevcao on 2019/4/28
 */
public class DictionaryFragment extends Fragment {


    EditText mSearchKeyWord;
    View mResultLayout;
    WebView mResultWebView;
    View mNoResultView;
    View clearTextBtn;
    Button searchTv;


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

        mSearchKeyWord.addTextChangedListener(new TextWatcher() {
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
                } else {
                    clearTextBtn.setVisibility(View.GONE);
                }
            }
        });

        mSearchKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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

    public void startSearch(final String keyWord) {
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
                }
            };
            searchThread.start();
        } else {
            Toast.makeText(getActivity(), "关键字为空，无法搜索!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showBlank() {
        mResultLayout.setVisibility(View.GONE);
    }

    public void showResult(Word word) {
        mResultLayout.setVisibility(View.VISIBLE);
        if (word == null) {
            mNoResultView.setVisibility(View.VISIBLE);
            mResultWebView.setVisibility(View.GONE);
        } else {
            mNoResultView.setVisibility(View.GONE);
            mResultWebView.setVisibility(View.VISIBLE);
            mResultWebView.loadData(word.html, "text/html", "UTF-8");
        }
    }

}
