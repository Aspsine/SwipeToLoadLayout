package com.aspsine.swipetoloadlayout.demo.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.model.Hero;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aspsine on 15/9/5.
 */
public class ViewPagerAdapter extends PagerAdapter {

    List<Hero> mHeroes;

    private int mChildCount;

    public ViewPagerAdapter() {
        mHeroes = new ArrayList<>();
    }

    public void setList(List<Hero> heroes) {
        mHeroes.clear();
        mHeroes.addAll(heroes);
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup item = (ViewGroup) LayoutInflater.from(container.getContext()).inflate(R.layout.item_viewpager, container, false);
        ImageView ivBanner = (ImageView) item.findViewById(R.id.ivBanner);
        TextView tvName = (TextView) item.findViewById(R.id.tvName);

        Hero hero = mHeroes.get(position);
        tvName.setText(hero.getName());
        Picasso.with(container.getContext()).load(hero.getAvatar()).into(ivBanner);
        container.addView(item);
        return item;
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
}
