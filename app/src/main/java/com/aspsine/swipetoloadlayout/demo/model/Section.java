package com.aspsine.swipetoloadlayout.demo.model;

import java.util.List;

/**
 * Created by aspsine on 15/9/4.
 */
public class Section {
    private String name;
    private List<Hero> heroes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<Hero> heroes) {
        this.heroes = heroes;
    }
}
