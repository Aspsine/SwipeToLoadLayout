package com.aspsine.swipetoloadlayout.demo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.aspsine.swipetoloadlayout.demo.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterOtherViewFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    private SwipeToLoadLayout swipeToLoadLayout;
    private TextView tvTitle;
    private ImageView ivContent;
    private String mTitle;

    public static Fragment newInstance(String title) {
        TwitterOtherViewFragment fragment = new TwitterOtherViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("OTHER_VIEW_TITLE", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    public TwitterOtherViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString("OTHER_VIEW_TITLE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        if (mTitle.equals("FrameLayout")) {
            view = inflater.inflate(R.layout.fragment_twitter_other_framelayout, container, false);
        } else if (mTitle.equals("RelativeLayout")) {
            view = inflater.inflate(R.layout.fragment_twitter_other_relativelayout, container, false);
        } else if (mTitle.equals("LinearLayout")) {
            view = inflater.inflate(R.layout.fragment_twitter_other_linearlayout, container, false);
        } else if (mTitle.equals("ImageView")) {
            view = inflater.inflate(R.layout.fragment_twitter_other_imageview, container, false);
        } else if (mTitle.equals("TextView")) {
            view = inflater.inflate(R.layout.fragment_twitter_other_textview, container, false);
        }
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        View targetView = view.findViewById(R.id.swipe_target);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        if (targetView != null) {
            if (targetView instanceof TextView) {
                tvTitle = (TextView) targetView;
            } else if (targetView instanceof ImageView) {
                ivContent = (ImageView) targetView;
            }
        }
        if (tvTitle != null) {
            tvTitle.setText(mTitle + " Demo");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.setLoadingMore(false);
        }
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ivContent != null) {
                    Picasso.with(getActivity()).load("https://avatars0.githubusercontent.com/u/1912775?v=3&s=460").into(ivContent);
                }
                swipeToLoadLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
