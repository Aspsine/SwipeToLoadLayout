package com.aspsine.swipetoloadlayout.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wang
 * on 2017/1/22
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.recycler_btn, R.id.pager_btn, R.id.ver_btn})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ver_btn:
                intent.setClass(this, VerActivity.class);
                break;
            case R.id.recycler_btn:
                intent.setClass(this, RecyclerActivity.class);
                break;
            case R.id.pager_btn:
                intent.setClass(this, ViewPagerActivity.class);
                break;
        }
        startActivity(intent);
    }
}
