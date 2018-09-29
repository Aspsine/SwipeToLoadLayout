package com.tzj.swipetoloadlayout.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.CpSwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{

    private SwipeToLoadLayout loadLayout;
    private RecyclerView recyclerView;
    private MyAdapter adapter = new MyAdapter();
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 50; i++) {
            list.add(""+i);
        }
        loadLayout = findViewById(R.id.loadLayout);
        loadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadLayout.setRefreshing(false);
//                        loadLayout.setLoadingMore(false);
                    }
                },3000);
            }
        });
//        loadLayout.setRefreshing(true);
//        loadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                loadLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadLayout.setRefreshing(false);
//                        loadLayout.setLoadingMore(false);
//                    }
//                },3000);
//            }
//        });

        recyclerView = findViewById(R.id.swipe_target);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        loadLayout.setRefreshing(true);
    }


    class MyAdapter extends RecyclerView.Adapter<Holder>{

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
            return new Holder(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    class Holder extends RecyclerView.ViewHolder{

        public Holder(View itemView) {
            super(itemView);
        }
    }

}
