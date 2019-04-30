package com.clz.oxforddic.ui.fragments;

import android.support.v4.app.Fragment;

import com.clz.oxforddic.activity.MainActivity;

/**
 * Create by stevcao on 2019/4/30
 */
public class BaseFragment extends Fragment {

    public void goToSearch(String searchKey) {
        MainActivity activity = (MainActivity)getActivity();
        activity.goSearch(searchKey);
    }

}
