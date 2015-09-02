package com.aspsine.swipetoloadlayout.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.model.Friend;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aspsine on 2015/9/2.
 */
public class FriendAdapter extends BaseAdapter {
    private List<Friend> mFriends;

    public FriendAdapter() {
        this.mFriends = new ArrayList<Friend>();
    }

    public void append(List<Friend> friends) {
        mFriends.clear();
        mFriends.addAll(friends);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFriends.size();
    }

    @Override
    public Friend getItem(int position) {
        return mFriends.get(position);
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
        Friend friend = getItem(position);
        holder.tvName.setText(friend.getName());
        Picasso.with(parent.getContext()).load(friend.getAvatar()).resize(100, 100).into(holder.ivAvatar);
        return convertView;
    }

    public static class ViewHolder {
        ImageView ivAvatar;
        TextView tvName;
    }
}
