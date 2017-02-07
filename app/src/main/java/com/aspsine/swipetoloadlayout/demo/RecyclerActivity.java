package com.aspsine.swipetoloadlayout.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.aspsine.swipetoloadlayout.demo.adapter.RecyclerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerActivity extends AppCompatActivity implements OnRefreshListener, OnLoadMoreListener, OnRecyclerClickListener {

    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.load_layout)
    SwipeToLoadLayout mLoadLayout;

    private ArrayList<String> mNames;
    private int mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        ButterKnife.bind(this);
        mLoadLayout.setOnRefreshListener(this);
        mLoadLayout.setOnLoadMoreListener(this);
        mNames = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(new RecyclerAdapter(mNames, this));
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
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mLoadLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        for (int i = 0; i < 3; i++) {
            mNames.add("name: " + mName);
            mName ++;
        }
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onClick(int position) {
        Toast.makeText(this, mNames.get(position), Toast.LENGTH_SHORT).show();
    }
}
