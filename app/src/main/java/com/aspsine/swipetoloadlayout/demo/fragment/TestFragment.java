package com.aspsine.swipetoloadlayout.demo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.demo.R;
import com.aspsine.swipetoloadlayout.demo.adapter.SectionAdapter;
import com.aspsine.swipetoloadlayout.demo.model.Hero;
import com.aspsine.swipetoloadlayout.demo.model.Section;
import com.aspsine.swipetoloadlayout.demo.util.AssetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {

    private SectionAdapter mAdapter;

    public TestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new SectionAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String json = AssetUtils.getStringFromAsset(getActivity(), "characters.json");
        List<Hero> heroes = new ArrayList<Hero>();
        List<Section> sections = new ArrayList<Section>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonHeroes = jsonObject.getJSONArray("heroes");
            for (int i = 0; i < jsonHeroes.length(); i++) {
                JSONObject item = jsonHeroes.getJSONObject(i);
                Hero hero = new Hero();
                hero.setAvatar(item.getString("avatar"));
                hero.setName(item.getString("name"));
                heroes.add(hero);
            }
            JSONArray jsonSections = jsonObject.getJSONArray("sections");
            for (int i = 0; i<jsonSections.length(); i++){
                JSONObject jsonSection = jsonSections.getJSONObject(i);
                Section section = new Section();
                section.setName(jsonSection.getString("name"));
                JSONArray jsonSectionHeroes = jsonSection.getJSONArray("heroes");
                List<Hero> sectionHeroes = new ArrayList<Hero>();
                for (int j = 0; j<jsonSectionHeroes.length(); j++){
                    JSONObject jsonSectionHero = jsonSectionHeroes.getJSONObject(j);
                    Hero hero = new Hero();
                    hero.setName(jsonSectionHero.getString("name"));
                    hero.setAvatar(jsonSectionHero.getString("avatar"));
                    sectionHeroes.add(hero);
                }
                section.setHeroes(sectionHeroes);
                sections.add(section);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter.setList(sections);
    }
}
