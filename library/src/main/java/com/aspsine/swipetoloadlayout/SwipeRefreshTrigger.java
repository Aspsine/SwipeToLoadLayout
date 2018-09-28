package com.aspsine.swipetoloadlayout;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by Aspsine on 2015/8/13.
 */
public interface SwipeRefreshTrigger extends SwipeRefreshLayout.OnRefreshListener{

    void onRefresh();

}
