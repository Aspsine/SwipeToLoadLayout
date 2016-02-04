package com.aspsine.swipetoloadlayout.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.aspsine.swipetoloadlayout.demo.adapter.RecyclerCharactersAdapter;
import com.aspsine.swipetoloadlayout.demo.model.SectionCharacters;

/**
 * Created by Aspsine on 2015/9/10.
 */
public class GoogleStyleFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    public static final String TAG = GoogleStyleFragment.class.getSimpleName();
    public static final String GOOGLE_REFRESH_TYPE = "google_refresh_type";

    private int mType;
    private int mPageNum;
    private RecyclerView recyclerView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerCharactersAdapter mAdapter;


    public static GoogleStyleFragment newInstance(int type) {
        GoogleStyleFragment fragment = new GoogleStyleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(GOOGLE_REFRESH_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt(GOOGLE_REFRESH_TYPE, 0);
        mAdapter = new RecyclerCharactersAdapter(0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int layoutId;
        if (mType == 0) {
            layoutId = R.layout.fragment_google_style;
        } else {
            layoutId = R.layout.fragment_google_style_custom;
        }
        return inflater.inflate(layoutId, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mAdapter);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE ){
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)){
                        swipeToLoadLayout.setLoadingMore(true);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
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
        mAdapter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getRequestQueue().cancelAll(TAG + "refresh" + mType);
        App.getRequestQueue().cancelAll(TAG + "loadmore" + mType);
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
        if (swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.setLoadingMore(false);
        }
        mAdapter.stop();
    }

    @Override
    public void onRefresh() {
        GsonRequest request = new GsonRequest<SectionCharacters>(Constants.API.CHARACTERS, SectionCharacters.class, new Response.Listener<SectionCharacters>() {
            @Override
            public void onResponse(final SectionCharacters characters) {
                // here, I use post delay to show more animation, you don't have to.
                swipeToLoadLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPageNum = 0;
                        mAdapter.setList(characters.getCharacters(), characters.getSections().subList(0, mPageNum + 1));
                        swipeToLoadLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                swipeToLoadLayout.setRefreshing(false);
                volleyError.printStackTrace();
            }
        });
        App.getRequestQueue().add(request).setTag(TAG + "refresh" + mType);
    }

    @Override
    public void onLoadMore() {
        GsonRequest request = new GsonRequest<SectionCharacters>(Constants.API.CHARACTERS, SectionCharacters.class, new Response.Listener<SectionCharacters>() {
            @Override
            public void onResponse(final SectionCharacters characters) {
                // here, I use post delay to show more animation, you don't have to.
                swipeToLoadLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mPageNum < 3) {
                            mPageNum++;
                            mAdapter.append(characters.getSections().subList(mPageNum, mPageNum + 1));
                        } else {
                            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                        }
                        swipeToLoadLayout.setLoadingMore(false);
                    }
                }, 2000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                swipeToLoadLayout.setLoadingMore(false);
                volleyError.printStackTrace();
            }
        });
        App.getRequestQueue().add(request).setTag(TAG + "loadmore" + mType);
    }
}
