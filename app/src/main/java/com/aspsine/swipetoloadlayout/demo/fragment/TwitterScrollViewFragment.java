package com.aspsine.swipetoloadlayout.demo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.GsonRequest;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.aspsine.swipetoloadlayout.demo.App;
import com.aspsine.swipetoloadlayout.demo.Constants;
import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.model.Character;
import com.aspsine.swipetoloadlayout.demo.model.SectionCharacters;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterScrollViewFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    private static final String TAG = TwitterScrollViewFragment.class.getSimpleName();

    private SwipeToLoadLayout swipeToLoadLayout;

    private ScrollView scrollView;

    private TextView tvTitle;

    private ImageView[] ivArray;

    public TwitterScrollViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_twitter_scroll_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        scrollView = (ScrollView) view.findViewById(R.id.swipe_target);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.group);
        // use this method find view is not an good way.
        // do not learn! do not learn! do not learn! important thing say three times. :)
        ivArray = new ImageView[viewGroup.getChildCount()];
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            ivArray[i] = (ImageView) viewGroup.getChildAt(i);
        }

        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener);
    }

    ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {

        @Override
        public void onScrollChanged() {
            if (scrollView.getChildAt(0).getHeight() < scrollView.getScrollY() + scrollView.getHeight() && !ViewCompat.canScrollVertically(scrollView, 1)) {
                swipeToLoadLayout.setLoadingMore(true);
            }
        }
    };

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
        App.getRequestQueue().cancelAll(TAG);
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
        if (swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.setLoadingMore(false);
        }
    }

    @Override
    public void onDestroyView() {
        scrollView.getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        GsonRequest request = new GsonRequest<SectionCharacters>(Constants.API.CHARACTERS, SectionCharacters.class, new Response.Listener<SectionCharacters>() {
            @Override
            public void onResponse(SectionCharacters sectionCharacters) {
                List<Character> characters = sectionCharacters.getCharacters();
                for (int i = 0; i < characters.size(); i++) {
                    String img = characters.get(i).getAvatar();
                    if (i < ivArray.length) {
                        Picasso.with(getActivity()).load(img).into(ivArray[i]);
                    }
                }
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
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
            }
        }, 3000);
    }
}
