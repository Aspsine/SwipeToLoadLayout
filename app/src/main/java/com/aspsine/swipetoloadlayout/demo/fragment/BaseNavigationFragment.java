package com.aspsine.swipetoloadlayout.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Aspsine on 2015/9/8.
 */
public class BaseNavigationFragment extends BaseToolbarFragment {

    public static final String NAVIGATION_FRAGMENT_TITLE = "navigation_fragment_title";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView tv = new TextView(getActivity());
        tv.setText("Not developed yet. Support latter...");
        tv.setGravity(Gravity.CENTER);
        return tv;
    }
}
