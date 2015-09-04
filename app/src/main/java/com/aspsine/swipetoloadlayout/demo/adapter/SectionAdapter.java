package com.aspsine.swipetoloadlayout.demo.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.demo.model.Hero;
import com.aspsine.swipetoloadlayout.demo.model.Section;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aspsine on 15/9/4.
 */
public class SectionAdapter extends BaseParentAdapter {

    List<Section> mSections;

    List<Hero> mHeroes;

    public SectionAdapter(){
        mSections = new ArrayList<>();
    }

    public void setList(List<Section> sections){
        mSections.clear();
        mHeroes.clear();
        append(sections);
    }

    public void append(List<Section> sections){
        mSections.addAll(sections);
        for (Section section : sections){
            mHeroes.addAll(section.getChildren());
        }
        notifyDataSetChanged();
    }




    @Override
    public int getParentCount() {
        return 0;
    }

    @Override
    protected Section getParentItem(int parentPosition) {
        return null;
    }

    @Override
    protected View getParentView(int parentPosition, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getChildCount(int parentPosition) {
        return 0;
    }

    @Override
    protected Hero getChildItem(int parentPosition, int childPosition) {
        return null;
    }

    @Override
    protected View getChildView(int parentPosition, int childPosition, View convertView, ViewGroup parent) {
        return null;
    }



}
