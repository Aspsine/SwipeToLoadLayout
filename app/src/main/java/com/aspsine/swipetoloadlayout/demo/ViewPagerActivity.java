package com.aspsine.swipetoloadlayout.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.aspsine.swipetoloadlayout.demo.adapter.ViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wang
 * on 2017/1/22
 */

public class ViewPagerActivity extends AppCompatActivity implements OnRefreshListener, OnLoadMoreListener, OnRecyclerClickListener {

    @BindView(R.id.swipe_target)
    ViewPager mViewPager;
    @BindView(R.id.load_layout)
    SwipeToLoadLayout mLoadLayout;

    private ArrayList<String> mNames;

    private int mName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        ButterKnife.bind(this);
        mLoadLayout.setOnRefreshListener(this);
        mLoadLayout.setOnLoadMoreListener(this);
        mNames = new ArrayList<>();
        mViewPager.setAdapter(new ViewPagerAdapter(mNames, this));
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mNames.clear();
        mName = 0;
        for (int i = 0; i < 3; i++) {
            mNames.add("name: " + mName);
            mName ++;
        }
        mViewPager.getAdapter().notifyDataSetChanged();
        mLoadLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        for (int i = 0; i < 3; i++) {
            mNames.add("name: " + mName);
            mName ++;
        }
        mViewPager.getAdapter().notifyDataSetChanged();
        mLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onClick(int position) {
        Toast.makeText(this, mNames.get(position), Toast.LENGTH_SHORT).show();
    }


}
