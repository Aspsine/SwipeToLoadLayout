package com.aspsine.swipetoloadlayout.demo.model;

/**
 * Created by Aspsine on 2015/9/2.
 */
public class Hero{
    private String name;
    private String avatar;

    public Hero() {
    }

    public Hero(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
