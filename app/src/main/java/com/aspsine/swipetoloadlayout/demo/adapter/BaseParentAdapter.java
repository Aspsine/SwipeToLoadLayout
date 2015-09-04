package com.aspsine.swipetoloadlayout.demo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.aspsine.swipetoloadlayout.demo.model.Child;
import com.aspsine.swipetoloadlayout.demo.model.Parent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aspsine on 15/9/4.
 */
public abstract class BaseParentAdapter extends BaseAdapter {

    static final int TYPE_PARENT = -1;
    static final int TYPE_CHILD = 1;

    private List<Integer> mParentPositions;

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        int count = 0;
        for (int i = 0; i < getParentCount(); i++) {
            count += (getChildCount(i) + 1);
        }
        return count;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mParentPositions = getParentPositions();
    }

    @Override
    public int getItemViewType(int position) {
        if (mParentPositions.contains(position)) {
            return TYPE_PARENT;
        } else {
            return TYPE_CHILD;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        int type = getItemViewType(position);
        if (type == TYPE_PARENT) {
            int parentPosition = getParentPosition(position);
            return getParentItem(parentPosition);
        } else if (type == TYPE_CHILD) {
            int parentPosition = getParentPosition(position);
            int childPosition = getChildPositionInParent(position);
            return getChildItem(parentPosition, childPosition);
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (type == TYPE_PARENT) {
            return getParentView(getParentPosition(position), convertView, parent);
        } else if (type == TYPE_CHILD) {
            return getChildView(getParentPosition(position), getChildCount(position), convertView, parent);
        }
        return null;
    }

    public int getParentPosition(int position) {
        int parentPosition = 0;
        for (int i = 0; i < mParentPositions.size(); i++) {
            parentPosition = mParentPositions.get(i);
            if (position >= parentPosition) {
                break;
            }
        }
        return parentPosition;
    }

    public int getChildPositionInParent(int position) {
        return position - getParentPosition(position) - 1;
    }

    public List<Integer> getParentPositions() {
        int position = 0;
        List<Integer> parentPositions = new ArrayList<>();
        for (int i = 0; i < getParentCount(); i++) {
            position += i + (i > 0 ? getParentItem(i - 1).getChildren().size() : 0);
            parentPositions.add(position);
        }
        return parentPositions;
    }

    public abstract int getParentCount();

    protected abstract Parent getParentItem(int parentPosition);

    protected abstract View getParentView(int parentPosition, View convertView, ViewGroup parent);

    public abstract int getChildCount(int parentPosition);

    protected abstract Child getChildItem(int parentPosition, int childPosition);

    protected abstract View getChildView(int parentPosition, int childPosition, View convertView, ViewGroup parent);


}
