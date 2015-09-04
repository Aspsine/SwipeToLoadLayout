package com.aspsine.swipetoloadlayout.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.model.Hero;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aspsine on 2015/9/2.
 */
public class HeroAdapter extends BaseAdapter {
    private List<Hero> mHeroes;

    public HeroAdapter() {
        this.mHeroes = new ArrayList<Hero>();
    }

    public void append(List<Hero> heros) {
        mHeroes.clear();
        mHeroes.addAll(heros);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mHeroes.size();
    }

    @Override
    public Hero getItem(int position) {
        return mHeroes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
            holder = new ViewHolder();
            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.ivAvatar);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Hero hero = getItem(position);
        holder.tvName.setText(hero.getName());
        Picasso.with(parent.getContext()).load(hero.getAvatar()).resize(100, 100).into(holder.ivAvatar);
        return convertView;
    }

    public static class ViewHolder {
        ImageView ivAvatar;
        TextView tvName;
    }
}
