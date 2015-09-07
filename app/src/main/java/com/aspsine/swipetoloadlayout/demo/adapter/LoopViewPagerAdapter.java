package com.aspsine.swipetoloadlayout.demo.adapter;

import android.support.v4.view.ViewPager;
import android.util.Log;
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
 * Created by Aspsine on 2015/9/7.
 */
public class LoopViewPagerAdapter extends BaseLoopPagerAdapter {

    private final List<Hero> mHeroes;

    public LoopViewPagerAdapter(ViewPager viewPager) {
        super(viewPager);
        mHeroes = new ArrayList<>();
    }

    public void setList(List<Hero> heroes) {
        mHeroes.clear();
        mHeroes.addAll(heroes);
        notifyDataSetChanged();
    }

    @Override
    public int getPagerCount() {
        return mHeroes.size();
    }

    @Override
    public Hero getItem(int position) {
        return mHeroes.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("TestViewPager", "position = " + position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewpager, parent, false);
            holder = new ViewHolder();
            holder.ivBanner = (ImageView) convertView.findViewById(R.id.ivBanner);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Hero hero = mHeroes.get(position);
        holder.tvName.setText(hero.getName());
        Picasso.with(parent.getContext()).load(hero.getAvatar()).into(holder.ivBanner);
        return convertView;
    }

    @Override
    public void onPageItemSelected(int position) {

    }

    public static class ViewHolder {
        ImageView ivBanner;
        TextView tvName;
    }
}
