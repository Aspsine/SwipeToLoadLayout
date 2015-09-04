package com.aspsine.swipetoloadlayout.demo.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.adapter.HeroAdapter;
import com.aspsine.swipetoloadlayout.demo.model.Hero;
import com.aspsine.swipetoloadlayout.demo.util.AssetUtils;
import com.aspsine.swipetoloadlayout.demo.view.LoadAbleListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassicStyleFragment extends Fragment implements OnRefreshListener {

    private SwipeToLoadLayout swipeToLoadLayout;

    private LoadAbleListView listView;

    private HeroAdapter mAdapter;

    public ClassicStyleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new HeroAdapter();
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
        listView.setAdapter(mAdapter);
        swipeToLoadLayout.setOnRefreshListener(this);
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
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String json = AssetUtils.getStringFromAsset(getActivity(), "characters.json");
                List<Hero> heroes = new ArrayList<Hero>();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonHeroes = jsonObject.getJSONArray("heroes");
                    for (int i = 0; i < jsonHeroes.length(); i++) {
                        JSONObject item = jsonHeroes.getJSONObject(i);
                        Hero hero = new Hero();
                        hero.setAvatar(item.getString("avatar"));
                        hero.setName(item.getString("name"));
                        heroes.add(hero);
                    }
                    JSONArray jsonSections = jsonObject.getJSONArray("sections");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter.append(heroes);
                swipeToLoadLayout.setRefreshing(false);
            }
        }, 4000);
    }

}
