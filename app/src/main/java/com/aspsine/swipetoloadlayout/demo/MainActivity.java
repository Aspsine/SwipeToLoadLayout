package com.aspsine.swipetoloadlayout.demo;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.aspsine.swipetoloadlayout.demo.fragment.BaseNavigationFragment;
import com.aspsine.swipetoloadlayout.demo.fragment.BaseToolbarFragment;
import com.aspsine.swipetoloadlayout.demo.fragment.ClassicStyleFragment;

public class MainActivity extends AppCompatActivity implements BaseToolbarFragment.ToggleDrawerCallBack
        , NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(this);

        handler = new Handler(Looper.getMainLooper());

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_home);
            showNavigationFragment(R.id.nav_home);
        } else {

        }
//        if (savedInstanceState == null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.container, new ClassicStyleFragment(), ClassicStyleFragment.TAG)
//                    .commit();
//        }
    }

    @Override
    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    int lastNavItemId = -1;

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_settings) {

                } else if (itemId == R.id.nav_about) {

                } else {
                    showNavigationFragment(itemId);
                }
            }
        }, 500);
        return true;
    }

    void showNavigationFragment(int itemId) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment lastFragment = fm.findFragmentByTag(getTag(lastNavItemId));
        if (lastFragment != null) {
            ft.detach(lastFragment);
        }
        Fragment fragment = fm.findFragmentByTag(getTag(itemId));
        if (fragment == null) {
            fragment = getItem(itemId);
            ft.add(R.id.container, fragment, getTag(itemId));
        } else {
            ft.attach(fragment);
        }
        ft.commit();
    }

    private BaseNavigationFragment getItem(int itemId) {
        BaseNavigationFragment navigationFragment = null;

        switch (itemId) {
            case R.id.nav_home:
                navigationFragment = new BaseNavigationFragment();
                break;

            case R.id.nav_comics:
                navigationFragment = new BaseNavigationFragment();
                break;

            case R.id.nav_movies:
                navigationFragment = new BaseNavigationFragment();
                break;

            case R.id.nav_videos:
                navigationFragment = new BaseNavigationFragment();
                break;

            case R.id.nav_games:
                navigationFragment = new BaseNavigationFragment();
                break;

            case R.id.nav_tv:
                navigationFragment = new BaseNavigationFragment();
                break;

            case R.id.nav_characters:
                navigationFragment = new ClassicStyleFragment();
                break;

            case R.id.nav_news:
                navigationFragment = new BaseNavigationFragment();
                break;

            case R.id.nav_images:
                navigationFragment = new BaseNavigationFragment();
                break;
        }
        return navigationFragment;
    }

    private String getTag(int itemId) {
        return String.valueOf(itemId);
    }
}
