package com.aspsine.swipetoloadlayout.demo.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.model.*;
import com.aspsine.swipetoloadlayout.demo.model.Character;
import com.squareup.picasso.CircleTransformation;
import com.squareup.picasso.Picasso;



/**
 * Created by aspsine on 15/9/11.
 */
public class CharacterAdapter extends BaseArrayAdapter<Character> {

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hero_grid, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.ivAvatar);
            holder.tvName.setMaxLines(1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Character character = getItem(position);
        holder.tvName.setText(character.getName());
        Resources resources = parent.getResources();
        int size = resources.getDimensionPixelOffset(R.dimen.hero_avatar_size);
        int width = resources.getDimensionPixelOffset(R.dimen.hero_avatar_border);
        Picasso.with(parent.getContext())
                .load(character.getAvatar())
                .resize(size, size)
                .transform(new CircleTransformation(width))
                .into(holder.ivAvatar);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), character.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    public class ViewHolder {
        ImageView ivAvatar;
        TextView tvName;
    }
}
