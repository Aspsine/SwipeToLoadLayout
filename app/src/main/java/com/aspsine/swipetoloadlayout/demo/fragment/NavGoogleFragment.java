package com.aspsine.swipetoloadlayout.demo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavGoogleFragment extends BaseNavigationFragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Adapter mAdapter;

    private String[] mTitles = {"Top", "Bad guys", "Women", "Teams"};

    public NavGoogleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new Adapter(getChildFragmentManager());
        for (int i = 0; i < 4; i++) {
            mAdapter.addFragment(GoogleStyleFragment.newInstance(i), mTitles[i]);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_google, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("Google Style");
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public static class Adapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<CharSequence> titles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, CharSequence title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}
