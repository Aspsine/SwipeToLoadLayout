package com.aspsine.swipetoloadlayout.demo.adapter;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aspsine on 15/9/4.
 */
public abstract class BaseArrayAdapter<T> extends BaseAdapter {
    private List<T> mList;


    public BaseArrayAdapter() {
        this.mList = new ArrayList<>();
    }

    public void setList(List<T> list) {
        this.mList.clear();
        append(list);
    }

    public void append(List<T> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
