package com.aspsine.swipetoloadlayout.demo.adapter;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.model.Hero;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by aspsine on 15/9/5.
 */
public class ViewPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener, View.OnTouchListener, Runnable {

    private static final int DELAY_MILLIS = 5000;

    private final ViewPager mViewPager;

    private final Handler mHandler;

    private final List<View> mViews;

    private final List<Hero> mHeroes;

    private int mChildCount;

    /**
     * indicate if user is touching viewpager
     */
    private boolean mTouching;

    /**
     * indicate if viewpager loop need pause
     */
    private boolean mPause;

    public ViewPagerAdapter(ViewPager viewPager) {
        mHandler = new Handler(Looper.getMainLooper());
        mHeroes = new ArrayList<>();
        mViews = new LinkedList<>();

        mViewPager = viewPager;
        mViewPager.setOnTouchListener(this);
    }

    public void setList(List<Hero> heroes) {
        mHeroes.clear();
        // add last element in position 0, add all, add first element in last position
        mHeroes.add(heroes.get(heroes.size() - 1));
        mHeroes.addAll(heroes);
        mHeroes.add(heroes.get(0));

        // init a view list
        for (int i = 0; i < mHeroes.size(); i++) {
            mViews.add(null);
        }
        notifyDataSetChanged();

        if (mViewPager.getCurrentItem() == 0) {
            mViewPager.setCurrentItem(1, false);
        }
        stop();
        start();
    }

    /**
     * start loop
     */
    public void start() {
        resume();
        post();
    }

    /**
     * stop loop
     */
    public void stop() {
        mHandler.removeCallbacks(this);
    }

    /**
     * resume loop
     */
    public void resume() {
        mPause = false;
    }

    /**
     * pause loop
     */
    public void pause() {
        mPause = true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mTouching = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mTouching = false;
        }
        return false;
    }

    @Override
    public void run() {
        int currentPosition = mViewPager.getCurrentItem();
        if (0 < currentPosition && currentPosition < mHeroes.size() - 1) {
            if (!mTouching && !mPause) {
                if (currentPosition + 1 == mHeroes.size() - 1) {
                    currentPosition = 1;
                } else {
                    currentPosition++;
                }
                mViewPager.setCurrentItem(currentPosition, true);
            }
            post();
        }
    }

    private void post() {
        mHandler.postDelayed(this, DELAY_MILLIS);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewHolder holder = null;
        if (mViews.get(position) == null) {
            ViewGroup item = (ViewGroup) LayoutInflater.from(container.getContext()).inflate(R.layout.item_viewpager, container, false);
            holder = new ViewHolder();
            holder.ivBanner = (ImageView) item.findViewById(R.id.ivBanner);
            holder.tvName = (TextView) item.findViewById(R.id.tvName);
            item.setTag(holder);
            mViews.set(position, item);
        } else {
            holder = (ViewHolder) mViews.get(position).getTag();
        }

        Hero hero = mHeroes.get(position);
        holder.tvName.setText(hero.getName());
        Picasso.with(container.getContext()).load(hero.getAvatar()).into(holder.ivBanner);
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mChildCount = getCount();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mHeroes.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (0 < position && position < mHeroes.size() - 1) {
            // change indicator here
            Log.e("ViewPager", "Fixed position = " + (position - 1));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            // if heroes size cannot be smaller than 3
            if (mHeroes.size() > 3) {
                if (mViewPager.getCurrentItem() == 0) {
                    mViewPager.setCurrentItem(mHeroes.size() - 2, false);
                } else if (mViewPager.getCurrentItem() == mHeroes.size() - 1) {
                    mViewPager.setCurrentItem(1, false);
                }
            }
        }
    }

    public static class ViewHolder {
        ImageView ivBanner;
        TextView tvName;
    }
}
