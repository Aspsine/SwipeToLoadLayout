package com.aspsine.swipetoloadlayout.demo;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.aspsine.swipetoloadlayout.demo.fragment.BaseToolbarFragment;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        , BaseToolbarFragment.ToggleDrawerCallBack {

    private static final Integer ID_ARRAY[] = {
            R.id.nav_Twitter_style,
            R.id.nav_google_style,
            R.id.nav_yalantis_style,
            R.id.nav_jd_style,
            R.id.nav_set_header_footer_via_code
    };

    private static final List<Integer> IDS = Arrays.asList(ID_ARRAY);

    private static final int DEFAULT_POSITION = 0;

    private DrawerLayout drawerLayout;

    /**
     * https://github.com/Aspsine/FragmentNavigator
     */
    private FragmentNavigator mFragmentNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentNavigator = new FragmentNavigator(getSupportFragmentManager(), new MainFragmentAdapter(), R.id.container);

        mFragmentNavigator.setDefaultPosition(DEFAULT_POSITION);

        mFragmentNavigator.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(IDS.get(DEFAULT_POSITION));

        mFragmentNavigator.showFragment(mFragmentNavigator.getCurrentPosition());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFragmentNavigator.onSaveInstanceState(outState);
    }

    @Override
    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        drawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_about) {
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                } else {
                    mFragmentNavigator.showFragment(IDS.indexOf(itemId));
                }
            }
        }, 200);
        return true;
    }
}
