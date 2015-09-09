package com.aspsine.swipetoloadlayout.demo.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.GsonRequest;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.aspsine.swipetoloadlayout.demo.App;
import com.aspsine.swipetoloadlayout.demo.Constants;
import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.adapter.LoopViewPagerAdapter;
import com.aspsine.swipetoloadlayout.demo.adapter.SectionAdapter;
import com.aspsine.swipetoloadlayout.demo.model.Character;
import com.aspsine.swipetoloadlayout.demo.model.SectionCharacters;
import com.aspsine.swipetoloadlayout.demo.view.LoadAbleListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavListViewFragment extends BaseNavigationFragment implements OnRefreshListener, OnLoadMoreListener,
        SectionAdapter.OnChildItemClickListener<Character>,
        SectionAdapter.OnChildItemLongClickListener<Character> {
    public static final String TAG = NavListViewFragment.class.getSimpleName();

    private SwipeToLoadLayout swipeToLoadLayout;

    private LoadAbleListView listView;

    private ViewPager viewPager;

    private ViewGroup indicators;

    private SectionAdapter mAdapter;

    private LoopViewPagerAdapter mPagerAdapter;

    public NavListViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new SectionAdapter();
        mAdapter.setOnChildItemClickListener(this);
        mAdapter.setOnChildItemLongClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_listview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View pagerView = LayoutInflater.from(view.getContext()).inflate(R.layout.layout_viewpager, listView, false);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        listView = (LoadAbleListView) view.findViewById(R.id.listView);
        viewPager = (ViewPager) pagerView.findViewById(R.id.viewPager);
        indicators = (ViewGroup) pagerView.findViewById(R.id.indicators);
        mPagerAdapter = new LoopViewPagerAdapter(viewPager, indicators);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.addOnPageChangeListener(mPagerAdapter);
        listView.addHeaderView(pagerView);
        listView.setAdapter(mAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    mPagerAdapter.start();
                } else {
                    mPagerAdapter.stop();
                }
            }
        });
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle("ListView Top Characters");
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPagerAdapter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getRequestQueue().cancelAll(TAG);
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
        if (swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.setLoadingMore(false);
        }
        mPagerAdapter.stop();
    }

    @Override
    public void onRefresh() {
        GsonRequest request = new GsonRequest<SectionCharacters>(Constants.API.CHARACTERS, SectionCharacters.class, new Response.Listener<SectionCharacters>() {
            @Override
            public void onResponse(SectionCharacters characters) {
                mAdapter.setList(characters.getSections());
                mPagerAdapter.setList(characters.getCharacters());
                viewPager.setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_viewpager));
                swipeToLoadLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                swipeToLoadLayout.setRefreshing(false);
                volleyError.printStackTrace();
            }
        });
        App.getRequestQueue().add(request).setTag(TAG);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
            }
        }, 1000);
    }

    @Override
    public void onChildItemClick(int groupPosition, int childPosition, Character character, View view) {
        Toast.makeText(getActivity(), character.getName() + " Click", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onClickItemLongClick(int groupPosition, int childPosition, Character character, View view) {
        Toast.makeText(getActivity(), character.getName() + " Long Click", Toast.LENGTH_SHORT).show();
        return true;
    }
}
