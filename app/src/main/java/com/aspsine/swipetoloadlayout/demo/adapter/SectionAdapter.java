package com.aspsine.swipetoloadlayout.demo.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.model.Character;
import com.aspsine.swipetoloadlayout.demo.model.Section;
import com.squareup.picasso.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aspsine on 15/9/4.
 */
public class SectionAdapter extends BaseGroupAdapter<Section, Character> {
    List<Section> mSections;

    public SectionAdapter() {
        mSections = new ArrayList<>();
    }

    public void setList(List<Section> sections) {
        mSections.clear();
        append(sections);
    }

    public void append(List<Section> sections) {
        mSections.addAll(sections);
        notifyDataSetChanged();
    }

    @Override
    protected int getParentViewType(int groupPosition) {
        return -1;
    }

    @Override
    public int getGroupCount() {
        return mSections.size();
    }

    @Override
    protected Section getGroup(int groupPosition) {
        return mSections.get(groupPosition);
    }

    @Override
    protected View getGroupView(final int groupPosition, View convertView, ViewGroup parent) {
        HeaderViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            holder = new HeaderViewHolder();
            holder.tvHeader = (TextView) convertView.findViewById(R.id.tvHeader);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        final String header = getGroup(groupPosition).getName();
        holder.tvHeader.setText(header);
        return convertView;
    }

    @Override
    protected int getChildViewType(int groupPosition, int childPositionInGroup) {
        return 1;
    }

    @Override
    public int getChildCount(int groupPosition) {
        List<Character> characters = getGroup(groupPosition).getCharacters();
        return characters != null ? characters.size() : 0;
    }

    @Override
    protected Character getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).getCharacters().get(childPosition);
    }

    @Override
    protected View getChildView(final int groupPosition, final int childPosition, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hero, parent, false);
            holder = new ChildViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.ivAvatar);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        final Character character = getChild(groupPosition, childPosition);
        holder.tvName.setText(character.getName());
        Resources resources = parent.getResources();
        int size = resources.getDimensionPixelOffset(R.dimen.hero_avatar_size);
        int width = resources.getDimensionPixelOffset(R.dimen.hero_avatar_border);
        Picasso.with(parent.getContext())
                .load(character.getAvatar())
                .resize(size, size)
                .transform(new CircleTransformation(width))
                .into(holder.ivAvatar);
        final View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChildItemClickListener != null) {
                    mOnChildItemClickListener.onChildItemClick(groupPosition, childPosition, character, finalConvertView);
                }
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnChildItemLongClickListener != null) {
                    return mOnChildItemLongClickListener.onClickItemLongClick(groupPosition, childPosition, character, finalConvertView);
                }
                return false;
            }
        });
        return convertView;
    }

    public class HeaderViewHolder {
        TextView tvHeader;

    }

    public class ChildViewHolder {
        ImageView ivAvatar;
        TextView tvName;
    }
}
