package com.aspsine.swipetoloadlayout.demo.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.demo.OnRecyclerClickListener;
import com.aspsine.swipetoloadlayout.demo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wang
 * on 2017/1/22
 */

public class ViewPagerAdapter extends PagerAdapter {

    private List<String> mName;
    private OnRecyclerClickListener mListener;

    public ViewPagerAdapter(List<String> name, OnRecyclerClickListener listener) {
        mName = name;
        mListener = listener;
    }

    public ViewPagerAdapter(List<String> name) {
        mName = name;
    }

    public List<String> getName() {
        return mName;
    }

    @Override
    public int getCount() {
        return mName.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ViewHolder) object).itemView;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View convertView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_pager_text, container, false);
        container.addView(convertView);
        return new ViewHolder(convertView, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((ViewHolder) object).itemView);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    class ViewHolder {

        @BindView(R.id.name_tv)
        AppCompatTextView mNameTv;

        View itemView;

        private int mPosition;

        public ViewHolder(View view, int position) {
            itemView = view;
            mPosition = position;
            ButterKnife.bind(this, view);
            onBindView(mName.get(position));
        }

        @OnClick(R.id.name_tv)
        public void onClick() {
            if (mListener != null) {
                mListener.onClick(mPosition);
            }
        }

        private void onBindView(String name) {
            mNameTv.setText(name);
        }
    }
}
