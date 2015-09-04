package com.aspsine.swipetoloadlayout.demo.model;

import java.util.List;

/**
 * Created by aspsine on 15/9/4.
 */
public class Section implements Parent<String,Hero> {

    @Override
    public List<Hero> getChildren() {
        return null;
    }

    @Override
    public void setChildren(List<Hero> heros) {

    }

    @Override
    public String getParent() {
        return null;
    }

    @Override
    public void setParent(String parent) {

    }
}
