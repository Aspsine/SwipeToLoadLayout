package com.aspsine.swipetoloadlayout;

import android.view.View;

/**
 * Created by Alex on 2017/9/12.
 */

public interface SwipeViewFactory<H extends View & SwipeTrigger & SwipeRefreshTrigger,F extends View & SwipeTrigger & SwipeLoadMoreTrigger>{

	H createHeaderView(SwipeToLoadLayout parent);

	F createFooterView(SwipeToLoadLayout parent);

}
