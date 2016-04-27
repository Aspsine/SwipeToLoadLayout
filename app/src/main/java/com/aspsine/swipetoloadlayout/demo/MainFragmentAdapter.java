package com.aspsine.swipetoloadlayout.demo;

import android.support.v4.app.Fragment;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.aspsine.swipetoloadlayout.demo.fragment.NavGoogleFragment;
import com.aspsine.swipetoloadlayout.demo.fragment.NavJDFragment;
import com.aspsine.swipetoloadlayout.demo.fragment.NavJavaCodeFragment;
import com.aspsine.swipetoloadlayout.demo.fragment.NavTwitterFragment;
import com.aspsine.swipetoloadlayout.demo.fragment.NavYalantisFragment;

/**
 * Created by aspsine on 16/4/28.
 */
public class MainFragmentAdapter implements FragmentNavigatorAdapter {

    @Override
    public Fragment onCreateFragment(int position) {
        switch (position) {
            case 0:
                return NavTwitterFragment.newInstance();

            case 1:
                return NavGoogleFragment.newInstance();

            case 2:
                return NavYalantisFragment.newInstance();

            case 3:
                return NavJDFragment.newInstance();

            case 4:
                return NavJavaCodeFragment.newInstance();
        }
        return null;
    }

    @Override
    public String getTag(int position) {
        return String.valueOf(position);
    }

    @Override
    public int getCount() {
        return 5;
    }
}