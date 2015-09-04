package com.aspsine.swipetoloadlayout.demo.model;

import java.util.List;

/**
 * Created by aspsine on 15/9/5.
 */
public interface Parent<P, Child> {


    public List<Child> getChildren();

    public void setChildren(List<Child> children);

    public P getParent();

    public void setParent(P parent);
}
