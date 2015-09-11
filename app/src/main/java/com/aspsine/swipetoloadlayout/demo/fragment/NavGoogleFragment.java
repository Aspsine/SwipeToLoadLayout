package com.aspsine.swipetoloadlayout.demo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavGoogleFragment extends BaseNavPagerFragment {

    public NavGoogleFragment() {
        // Required empty public constructor
    }

    @Override
    protected String[] getTitles() {
        return new String[]{"Top", "Bad guys", "Women", "Teams"};
    }

    @Override
    protected Fragment getFragment(int i) {
        return GoogleStyleFragment.newInstance(i);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("Google Style");
    }

}
