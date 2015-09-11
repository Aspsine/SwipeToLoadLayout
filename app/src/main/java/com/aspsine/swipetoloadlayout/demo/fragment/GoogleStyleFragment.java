package com.aspsine.swipetoloadlayout.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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
import com.aspsine.swipetoloadlayout.demo.view.LoadAbleRecyclerView;

/**
 * Created by Aspsine on 2015/9/10.
 */
public class GoogleStyleFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    public static final String TAG = GoogleStyleFragment.class.getSimpleName();
    public static final String GOOGLE_REFRESH_TYPE = "google_refresh_type";

    private int mType;
    private int mPageNum;
    private LoadAbleRecyclerView recyclerView;
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
        mAdapter = new RecyclerCharactersAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_google_swipe_refresh, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        recyclerView = (LoadAbleRecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mAdapter);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
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
            public void onResponse(SectionCharacters characters) {
                int end = mType + 1;
                if (mType == 3) {
                    end = mType + 2;
                }
                mAdapter.setList(characters.getCharacters().subList(mType, end), characters.getSections().subList(mType, mType + 1));
                swipeToLoadLayout.setRefreshing(false);
                mPageNum = mType;
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
            public void onResponse(SectionCharacters characters) {
                mPageNum++;
                if (mPageNum < 4) {
                    mAdapter.append(characters.getSections().subList(mPageNum, mPageNum + 1));
                } else {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                }
                swipeToLoadLayout.setLoadingMore(false);
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
