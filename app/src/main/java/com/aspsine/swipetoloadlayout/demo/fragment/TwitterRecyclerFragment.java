package com.aspsine.swipetoloadlayout.demo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
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
import com.aspsine.swipetoloadlayout.demo.adapter.SectionAdapter;
import com.aspsine.swipetoloadlayout.demo.model.Character;
import com.aspsine.swipetoloadlayout.demo.model.SectionCharacters;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterRecyclerFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener,
        SectionAdapter.OnChildItemClickListener<Character>,
        SectionAdapter.OnChildItemLongClickListener<Character> {
    private static final String TAG = TwitterRecyclerFragment.class.getSimpleName();

    public static final int TYPE_LINEAR = 0;

    public static final int TYPE_GRID = 1;

    public static final int TYPE_STAGGERED_GRID = 2;

    private SwipeToLoadLayout swipeToLoadLayout;

    private RecyclerView recyclerView;

    private RecyclerCharactersAdapter mAdapter;

    private int mType;

    private int mPageNum;

    public static Fragment newInstance(int type) {
        TwitterRecyclerFragment fragment = new TwitterRecyclerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("LAYOUT_MANAGER_TYPE", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public TwitterRecyclerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt("LAYOUT_MANAGER_TYPE", TYPE_LINEAR);
        mAdapter = new RecyclerCharactersAdapter(mType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_twitter_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        RecyclerView.LayoutManager layoutManager = null;
        if (mType == TYPE_LINEAR) {
            layoutManager = new LinearLayoutManager(getContext());
        } else if (mType == TYPE_GRID) {
            layoutManager = new GridLayoutManager(getContext(), 2);
        } else if (mType == TYPE_STAGGERED_GRID) {
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
        recyclerView.setLayoutManager(layoutManager);
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
    public void onChildItemClick(int groupPosition, int childPosition, Character character, View view) {

    }

    @Override
    public boolean onClickItemLongClick(int groupPosition, int childPosition, Character character, View view) {
        return false;
    }

    @Override
    public void onLoadMore() {
        GsonRequest request = new GsonRequest<SectionCharacters>(Constants.API.CHARACTERS, SectionCharacters.class, new Response.Listener<SectionCharacters>() {
            @Override
            public void onResponse(SectionCharacters characters) {
                if (mPageNum < 3) {
                    mPageNum++;
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

    @Override
    public void onRefresh() {
        GsonRequest request = new GsonRequest<SectionCharacters>(Constants.API.CHARACTERS, SectionCharacters.class, new Response.Listener<SectionCharacters>() {
            @Override
            public void onResponse(SectionCharacters characters) {
                mPageNum = 0;
                mAdapter.setList(characters.getCharacters(), characters.getSections().subList(0, mPageNum + 1));
                swipeToLoadLayout.setRefreshing(false);
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
}
