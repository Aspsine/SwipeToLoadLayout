package com.aspsine.swipetoloadlayout.demo.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.aspsine.swipetoloadlayout.demo.App;
import com.aspsine.swipetoloadlayout.demo.Constants;
import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.adapter.SectionAdapter;
import com.aspsine.swipetoloadlayout.demo.adapter.ViewPagerAdapter;
import com.aspsine.swipetoloadlayout.demo.model.Characters;
import com.aspsine.swipetoloadlayout.demo.model.Hero;
import com.aspsine.swipetoloadlayout.demo.model.Section;
import com.aspsine.swipetoloadlayout.demo.util.AssetUtils;
import com.aspsine.swipetoloadlayout.demo.view.LoadAbleListView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassicStyleFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {
    private static final String TAG = ClassicStyleFragment.class.getSimpleName();

    private SwipeToLoadLayout swipeToLoadLayout;

    private LoadAbleListView listView;

    private ViewPager viewPager;

    private SectionAdapter mAdapter;

    private ViewPagerAdapter mPagerAdapter;

    public ClassicStyleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new SectionAdapter();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classic_style, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        listView = (LoadAbleListView) view.findViewById(R.id.listview);
        viewPager = (ViewPager) LayoutInflater.from(view.getContext()).inflate(R.layout.layout_viewpager, listView, false);
        mPagerAdapter = new ViewPagerAdapter(viewPager);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.addOnPageChangeListener(mPagerAdapter);
        listView.addHeaderView(viewPager);
        listView.setAdapter(mAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    mPagerAdapter.resume();
                } else {
                    mPagerAdapter.pause();
                }
            }
        });
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
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
    public void onResume() {
        super.onResume();
        mPagerAdapter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getRequestQueue().cancelAll(TAG);
        mPagerAdapter.stop();
    }

    @Override
    public void onRefresh() {
        StringRequest request = new StringRequest(Constants.API.CHARACTERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Characters characters = new Gson().fromJson(s, Characters.class);
                mAdapter.setList(characters.getSections());
                mPagerAdapter.setList(characters.getHeroes());
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
}
