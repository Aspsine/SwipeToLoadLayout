package com.aspsine.swipetoloadlayout.demo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aspsine on 15/9/4.
 */
public abstract class BaseGroupAdapter<G, C> extends BaseAdapter {

    private List<Integer> mHeaderPositions;

    protected OnGroupItemClickListener mOnGroupItemClickListener;

    protected OnGroupItemLongClickListener mOnGroupItemLongClickListener;

    protected OnChildItemClickListener mOnChildItemClickListener;

    protected OnChildItemLongClickListener mOnChildItemLongClickListener;

    public interface OnGroupItemClickListener<G> {
        void onGroupItemClick(int groupPosition, G g, View view);
    }

    public interface OnGroupItemLongClickListener<G> {
        boolean onGroupLongClick(int groupPosition, G g, View view);
    }

    public interface OnChildItemClickListener<C> {
        void onChildItemClick(int groupPosition, int childPosition, C c, View view);
    }

    public interface OnChildItemLongClickListener<C> {
        boolean onClickItemLongClick(int groupPosition, int childPosition, C c, View view);
    }

    public void setOnGroupItemClickListener(OnGroupItemClickListener onGroupItemClickListener) {
        this.mOnGroupItemClickListener = onGroupItemClickListener;
    }

    public void setOnGroupItemLongClickListener(OnGroupItemLongClickListener onGroupItemLongClickListener) {
        this.mOnGroupItemLongClickListener = onGroupItemLongClickListener;
    }

    public void setOnChildItemClickListener(OnChildItemClickListener onChildItemClickListener) {
        this.mOnChildItemClickListener = onChildItemClickListener;
    }

    public void setOnChildItemLongClickListener(OnChildItemLongClickListener onChildItemLongClickListener) {
        this.mOnChildItemLongClickListener = onChildItemLongClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        int count = 0;
        for (int i = 0; i < getGroupCount(); i++) {
            count += (getChildCount(i) + 1);
        }
        return count;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        initHeaderPositions();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return getParentViewType(getGroupPosition(position));
        } else {
            return getChildViewType(getGroupPosition(position), getChildPositionInGroup(position));
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        int type = getItemViewType(position);
        int groupPosition = getGroupPosition(position);
        if (type == getParentViewType(groupPosition)) {
            return getGroup(groupPosition);
        } else if (type == getChildViewType(groupPosition, getChildPositionInGroup(position))) {
            return getChild(groupPosition, getChildPositionInGroup(position));
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        int groupPosition = getGroupPosition(position);
        if (type == getParentViewType(groupPosition)) {
            return getGroupView(groupPosition, convertView, parent);
        } else if (type == getChildViewType(groupPosition, getChildPositionInGroup(position))) {
            return getChildView(groupPosition, getChildPositionInGroup(position), convertView, parent);
        }
        return null;
    }

    public void initHeaderPositions() {
        if (mHeaderPositions == null) {
            mHeaderPositions = new ArrayList<>();
        } else {
            mHeaderPositions.clear();
        }
        int headerPosition = 0;
        for (int i = 0; i < getGroupCount(); i++) {
            if (i == 0) {
                headerPosition = 0;
            } else {
                headerPosition += getChildCount(i - 1) + 1;
            }
            mHeaderPositions.add(headerPosition);
        }
    }

    public boolean isPositionHeader(int position) {
        return mHeaderPositions.contains(position);
    }

    public int getGroupPosition(int position) {
        int groupPosition = 0;
        for (int i = mHeaderPositions.size() - 1; i >= 0; i--) {
            if (position >= mHeaderPositions.get(i)) {
                groupPosition = i;
                break;
            }
        }
        return groupPosition;
    }

    public int getChildPositionInGroup(int position) {
        int groupPosition = getGroupPosition(position);
        int absGroupPosition = mHeaderPositions.get(groupPosition);
        int childPositionInGroup = position - absGroupPosition - 1;
        return childPositionInGroup;
    }

    protected abstract int getParentViewType(int groupPosition);

    public abstract int getGroupCount();

    protected abstract G getGroup(int groupPosition);

    protected abstract View getGroupView(int groupPosition, View convertView, ViewGroup parent);

    protected abstract int getChildViewType(int groupPosition, int childPositionInGroup);

    public abstract int getChildCount(int groupPosition);

    protected abstract C getChild(int groupPosition, int childPosition);

    protected abstract View getChildView(int groupPosition, int childPosition, View convertView, ViewGroup parent);


}
