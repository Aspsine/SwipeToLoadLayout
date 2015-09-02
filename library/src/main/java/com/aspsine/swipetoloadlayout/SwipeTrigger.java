package com.aspsine.swipetoloadlayout;

/**
 * Created by Aspsine on 2015/8/17.
 */
interface SwipeTrigger {

    void onPrepare();

    void onSwipe(int y);

    void onReset();
}
