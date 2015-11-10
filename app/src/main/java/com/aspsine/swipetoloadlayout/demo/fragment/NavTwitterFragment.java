package com.aspsine.swipetoloadlayout.demo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.demo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavTwitterFragment extends BaseNavPagerFragment {

    public static BaseNavigationFragment newInstance() {
        BaseNavigationFragment fragment = new NavTwitterFragment();
        return fragment;
    }


    public NavTwitterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // override the method
        // delete app:layout_scrollFlags="scroll|enterAlways"
        // delete reason: ListView don't support coordinate scroll
        // these property lead height measure issue and scroll issue
        // it's not an bug of SwipeToLoadLayout
        return inflater.inflate(R.layout.fragment_nav_twitter, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle("Twitter Style");
    }

    @Override
    protected String[] getTitles() {
        return new String[]{"ListView", "GridView",
                "RecyclerView", "Grid RecyclerView", "StaggeredGrid RecyclerView",
                "ScrollView", "WebView",
                "FrameLayout", "RelativeLayout",
                "LinearLayout", "ImageView", "TextView"};
    }

    @Override
    protected Fragment getFragment(int position) {
        String title = getTitles()[position];
        Fragment fragment = null;
        if (title.equals("ListView")) {
            fragment = new TwitterListViewFragment();
        } else if (title.equals("GridView")) {
            fragment = new TwitterGridViewFragment();
        } else if (title.equals("RecyclerView")) {
            fragment = TwitterRecyclerFragment.newInstance(TwitterRecyclerFragment.TYPE_LINEAR);
        } else if (title.equals("Grid RecyclerView")) {
            // grid
            fragment = TwitterRecyclerFragment.newInstance(TwitterRecyclerFragment.TYPE_GRID);
        } else if (title.equals("StaggeredGrid RecyclerView")) {
            fragment = TwitterRecyclerFragment.newInstance(TwitterRecyclerFragment.TYPE_STAGGERED_GRID);
        } else if (title.equals("ScrollView")) {
            fragment = new TwitterScrollViewFragment();
        } else if (title.equals("WebView")) {
            fragment = new TwitterWebViewFragment();
        } else if (title.equals("FrameLayout")
                || title.equals("RelativeLayout")
                || title.equals("LinearLayout")
                || title.equals("ImageView")
                || title.equals("TextView")) {
            fragment = TwitterOtherViewFragment.newInstance(title);
        }
        return fragment;
    }


}
