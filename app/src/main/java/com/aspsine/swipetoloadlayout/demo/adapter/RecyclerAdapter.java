package com.aspsine.swipetoloadlayout.demo.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.demo.OnRecyclerClickListener;
import com.aspsine.swipetoloadlayout.demo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wang
 * on 2017/1/22
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TextViewHolder> {

    private List<String> mNames;

    private OnRecyclerClickListener mListener;

    public RecyclerAdapter(List<String> names, OnRecyclerClickListener listener) {
        mNames = names;
        mListener = listener;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
        return new TextViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        holder.mNameTv.setText(mNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    class TextViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_tv)
        AppCompatTextView mNameTv;

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
