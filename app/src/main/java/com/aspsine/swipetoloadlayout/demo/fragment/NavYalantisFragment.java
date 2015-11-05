package com.aspsine.swipetoloadlayout.demo.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.adapter.BaseArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavYalantisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavYalantisFragment extends BaseNavigationFragment implements OnRefreshListener {

    private SwipeToLoadLayout swipeToLoadLayout;

    public static final String KEY_ICON = "icon";
    public static final String KEY_COLOR = "color";

    protected List<Map<String, Integer>> mSampleList;

    public NavYalantisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NavYalantisFragment.
     */
    public static NavYalantisFragment newInstance() {
        NavYalantisFragment fragment = new NavYalantisFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, Integer> map;
        mSampleList = new ArrayList<>();

        int[] icons = {
                R.mipmap.yalantis_icon_1,
                R.mipmap.yalantis_icon_2,
                R.mipmap.yalantis_icon_3};

        int[] colors = {
                R.color.yalantis_saffron,
                R.color.yalantis_eggplant,
                R.color.yalantis_sienna};

        for (int i = 0; i < icons.length; i++) {
            map = new HashMap<>();
            map.put(KEY_ICON, icons[i]);
            map.put(KEY_COLOR, colors[i]);
            mSampleList.add(map);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_yalantis, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("Yalantis Style");
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        ListView listView = (ListView) view.findViewById(R.id.swipe_target);
        BaseArrayAdapter adapter = new SampleAdapter();
        listView.setAdapter(adapter);
        adapter.setList(mSampleList);
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
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
            }
        }, 3000);
    }

    class SampleAdapter extends BaseArrayAdapter<Map<String, Integer>> {

        public static final String KEY_ICON = "icon";
        public static final String KEY_COLOR = "color";

        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yalantis, parent, false);
                viewHolder.imageViewIcon = (ImageView) convertView.findViewById(R.id.image_view_icon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Map<String, Integer> map = getItem(position);

            viewHolder.imageViewIcon.setImageResource(map.get(KEY_ICON));
            convertView.setBackgroundResource(map.get(KEY_COLOR));

            return convertView;
        }

        class ViewHolder {
            ImageView imageViewIcon;
        }

    }
}
