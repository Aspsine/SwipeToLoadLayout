package com.aspsine.swipetoloadlayout;

import android.view.animation.Animation;

/**
 * Created by Aspsine on 2015/8/17.
 */
public interface SwipeTrigger {
    /**
     *  SwipeRefreshLayout 里用到的
     */
    void setAnimationListener(Animation.AnimationListener listener);

    /**
     * 开始滑动 start
     */
    void onPrepare();

    void onMove(int y, boolean isComplete, boolean automatic);

    /**
     * 到刷新位置 start
     */
    void onRelease();

    /**
     * 刷新结束 start
     */
    void onComplete();

    /**
     * 开始结束 复位 end
     */
    void onReset();
}
