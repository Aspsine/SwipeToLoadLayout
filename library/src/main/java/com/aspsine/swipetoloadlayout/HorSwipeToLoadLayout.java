package com.aspsine.swipetoloadlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Scroller;

public class HorSwipeToLoadLayout extends ViewGroup {
    private static final String TAG = HorSwipeToLoadLayout.class.getSimpleName();
    private static final int DEFAULT_SWIPING_TO_REFRESH_TO_DEFAULT_SCROLLING_DURATION = 200;
    private static final int DEFAULT_RELEASE_TO_REFRESHING_SCROLLING_DURATION = 200;
    private static final int DEFAULT_REFRESH_COMPLETE_DELAY_DURATION = 300;
    private static final int DEFAULT_REFRESH_COMPLETE_TO_DEFAULT_SCROLLING_DURATION = 500;
    private static final int DEFAULT_DEFAULT_TO_REFRESHING_SCROLLING_DURATION = 500;
    private static final int DEFAULT_SWIPING_TO_LOAD_MORE_TO_DEFAULT_SCROLLING_DURATION = 200;
    private static final int DEFAULT_RELEASE_TO_LOADING_MORE_SCROLLING_DURATION = 200;
    private static final int DEFAULT_LOAD_MORE_COMPLETE_DELAY_DURATION = 300;
    private static final int DEFAULT_LOAD_MORE_COMPLETE_TO_DEFAULT_SCROLLING_DURATION = 300;
    private static final int DEFAULT_DEFAULT_TO_LOADING_MORE_SCROLLING_DURATION = 300;
    private static final float DEFAULT_DRAG_RATIO = 0.5F;
    private static final int INVALID_POINTER = -1;
    private static final int INVALID_COORDINATE = -1;
    private HorSwipeToLoadLayout.AutoScroller mAutoScroller;
    private OnRefreshListener mRefreshListener;
    private OnLoadMoreListener mLoadMoreListener;
    private View mHeaderView;
    private View mTargetView;
    private View mFooterView;
    private int mHeaderWidth;
    private int mFooterWidth;
    private boolean mHasHeaderView;
    private boolean mHasFooterView;
    private boolean mDebug;
    private float mDragRatio = DEFAULT_DRAG_RATIO;
    private boolean mAutoLoading;
    private final int mTouchSlop;
    private int mStatus = STATUS.STATUS_DEFAULT;
    private int mHeaderOffset;
    private int mTargetOffset;
    private int mFooterOffset;
    private float mInitDownY;
    private float mInitDownX;
    private float mLastY;
    private float mLastX;
    private int mActivePointerId;
    private boolean mRefreshEnabled = true;
    private boolean mLoadMoreEnabled = true;
    private int mStyle = STYLE.CLASSIC;
    private float mRefreshTriggerOffset;
    private float mLoadMoreTriggerOffset;
    private float mRefreshFinalDragOffset;
    private float mLoadMoreFinalDragOffset;
    /**
     * <b>ATTRIBUTE:</b>
     * Scrolling duration swiping to refresh -> default
     */
    private int mSwipingToRefreshToDefaultScrollingDuration = DEFAULT_SWIPING_TO_REFRESH_TO_DEFAULT_SCROLLING_DURATION;

    /**
     * <b>ATTRIBUTE:</b>
     * Scrolling duration status release to refresh -> refreshing
     */
    private int mReleaseToRefreshToRefreshingScrollingDuration = DEFAULT_RELEASE_TO_REFRESHING_SCROLLING_DURATION;

    /**
     * <b>ATTRIBUTE:</b>
     * Refresh complete delay duration
     */
    private int mRefreshCompleteDelayDuration = DEFAULT_REFRESH_COMPLETE_DELAY_DURATION;

    /**
     * <b>ATTRIBUTE:</b>
     * Scrolling duration status refresh complete -> default
     * {@link #setRefreshing(boolean)} false
     */
    private int mRefreshCompleteToDefaultScrollingDuration = DEFAULT_REFRESH_COMPLETE_TO_DEFAULT_SCROLLING_DURATION;

    /**
     * <b>ATTRIBUTE:</b>
     * Scrolling duration status default -> refreshing, mainly for auto refresh
     * {@link #setRefreshing(boolean)} true
     */
    private int mDefaultToRefreshingScrollingDuration = DEFAULT_DEFAULT_TO_REFRESHING_SCROLLING_DURATION;

    /**
     * <b>ATTRIBUTE:</b>
     * Scrolling duration status release to loading more -> loading more
     */
    private int mReleaseToLoadMoreToLoadingMoreScrollingDuration = DEFAULT_RELEASE_TO_LOADING_MORE_SCROLLING_DURATION;


    /**
     * <b>ATTRIBUTE:</b>
     * Load more complete delay duration
     */
    private int mLoadMoreCompleteDelayDuration = DEFAULT_LOAD_MORE_COMPLETE_DELAY_DURATION;

    /**
     * <b>ATTRIBUTE:</b>
     * Scrolling duration status load more complete -> default
     * {@link #setLoadingMore(boolean)} false
     */
    private int mLoadMoreCompleteToDefaultScrollingDuration = DEFAULT_LOAD_MORE_COMPLETE_TO_DEFAULT_SCROLLING_DURATION;

    /**
     * <b>ATTRIBUTE:</b>
     * Scrolling duration swiping to load more -> default
     */
    private int mSwipingToLoadMoreToDefaultScrollingDuration = DEFAULT_SWIPING_TO_LOAD_MORE_TO_DEFAULT_SCROLLING_DURATION;

    /**
     * <b>ATTRIBUTE:</b>
     * Scrolling duration status default -> loading more, mainly for auto load more
     * {@link #setLoadingMore(boolean)} true
     */
    private int mDefaultToLoadingMoreScrollingDuration = DEFAULT_DEFAULT_TO_LOADING_MORE_SCROLLING_DURATION;

    /**
     * the style enum
     */
    public static final class STYLE {
        public static final int CLASSIC = 0;
        public static final int ABOVE = 1;
        public static final int BLEW = 2;
        public static final int SCALE = 3;
    }


    public HorSwipeToLoadLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public HorSwipeToLoadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorSwipeToLoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HorSwipeToLoadLayout, defStyleAttr, 0);

        try {
            int N = a.getIndexCount();

            for (int i = 0; i < N; ++i) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.HorSwipeToLoadLayout_refresh_enabled) {
                    this.setRefreshEnabled(a.getBoolean(attr, mRefreshEnabled));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_load_more_enabled) {
                    this.setLoadMoreEnabled(a.getBoolean(attr, mLoadMoreEnabled));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_swipe_style) {
                    this.setSwipeStyle(a.getInt(attr, mStyle));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_drag_ratio) {
                    this.setDragRatio(a.getFloat(attr, mDragRatio));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_refresh_final_drag_offset) {
                    this.setRefreshFinalDragOffset(a.getDimensionPixelOffset(attr, 0));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_load_more_final_drag_offset) {
                    this.setLoadMoreFinalDragOffset(a.getDimensionPixelOffset(attr, 0));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_refresh_trigger_offset) {
                    this.setRefreshTriggerOffset(a.getDimensionPixelOffset(attr, 0));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_load_more_trigger_offset) {
                    this.setLoadMoreTriggerOffset(a.getDimensionPixelOffset(attr, 0));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_swiping_to_refresh_to_default_scrolling_duration) {
                    this.setSwipingToRefreshToDefaultScrollingDuration(a.getInt(attr, mSwipingToRefreshToDefaultScrollingDuration));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_release_to_refreshing_scrolling_duration) {
                    this.setReleaseToRefreshingScrollingDuration(a.getInt(attr, mReleaseToRefreshToRefreshingScrollingDuration));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_refresh_complete_delay_duration) {
                    this.setRefreshCompleteDelayDuration(a.getInt(attr, mRefreshCompleteDelayDuration));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_refresh_complete_to_default_scrolling_duration) {
                    this.setRefreshCompleteToDefaultScrollingDuration(a.getInt(attr, mRefreshCompleteToDefaultScrollingDuration));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_default_to_refreshing_scrolling_duration) {
                    this.setDefaultToRefreshingScrollingDuration(a.getInt(attr, mDefaultToRefreshingScrollingDuration));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_swiping_to_load_more_to_default_scrolling_duration) {
                    this.setSwipingToLoadMoreToDefaultScrollingDuration(a.getInt(attr, mSwipingToLoadMoreToDefaultScrollingDuration));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_release_to_loading_more_scrolling_duration) {
                    this.setReleaseToLoadingMoreScrollingDuration(a.getInt(attr, mReleaseToLoadMoreToLoadingMoreScrollingDuration));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_load_more_complete_delay_duration) {
                    this.setLoadMoreCompleteDelayDuration(a.getInt(attr, mLoadMoreCompleteDelayDuration));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_load_more_complete_to_default_scrolling_duration) {
                    this.setLoadMoreCompleteToDefaultScrollingDuration(a.getInt(attr, mLoadMoreCompleteToDefaultScrollingDuration));
                } else if (attr == R.styleable.HorSwipeToLoadLayout_default_to_loading_more_scrolling_duration) {
                    this.setDefaultToLoadingMoreScrollingDuration(a.getInt(attr, mDefaultToLoadingMoreScrollingDuration));
                }
            }
        } finally {
            a.recycle();
        }

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mAutoScroller = new HorSwipeToLoadLayout.AutoScroller();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final int childNum = getChildCount();
        if (childNum == 0) {
            // no child return
            return;
        } else if (0 < childNum && childNum < 4) {
            mHeaderView = findViewById(R.id.swipe_refresh_header);
            mTargetView = findViewById(R.id.swipe_target);
            mFooterView = findViewById(R.id.swipe_load_more_footer);
        } else {
            // more than three children: unsupported!
            throw new IllegalStateException("Children num must equal or less than 3");
        }
        if (mTargetView == null) {
            return;
        }
        if (mHeaderView != null && mHeaderView instanceof SwipeTrigger) {
            mHeaderView.setVisibility(GONE);
        }
        if (mFooterView != null && mFooterView instanceof SwipeTrigger) {
            mFooterView.setVisibility(GONE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // header
        if (mHeaderView != null) {
            final View headerView = mHeaderView;
            measureChildWithMargins(headerView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = ((MarginLayoutParams) headerView.getLayoutParams());
            mHeaderWidth = headerView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            if (mRefreshTriggerOffset < mHeaderWidth) {
                mRefreshTriggerOffset = mHeaderWidth;
            }
        }
        // target
        if (mTargetView != null) {
            final View targetView = mTargetView;
            measureChildWithMargins(targetView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        // footer
        if (mFooterView != null) {
            final View footerView = mFooterView;
            measureChildWithMargins(footerView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = ((MarginLayoutParams) footerView.getLayoutParams());
            mFooterWidth = footerView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            if (mLoadMoreTriggerOffset < mFooterWidth) {
                mLoadMoreTriggerOffset = mFooterWidth;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildren();

        mHasHeaderView = (mHeaderView != null);
        mHasFooterView = (mFooterView != null);
    }

    /**
     * LayoutParams of RefreshLoadMoreLayout
     */
    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new HorSwipeToLoadLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new HorSwipeToLoadLayout.LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new HorSwipeToLoadLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // swipeToRefresh -> finger up -> finger down if the status is still swipeToRefresh
                // in onInterceptTouchEvent ACTION_DOWN event will stop the scroller
                // if the event pass to the child view while ACTION_MOVE(condition is false)
                // in onInterceptTouchEvent ACTION_MOVE the ACTION_UP or ACTION_CANCEL will not be
                // passed to onInterceptTouchEvent and onTouchEvent. Instead It will be passed to
                // child view's onTouchEvent. So we must deal this situation in dispatchTouchEvent
                onActivePointerUp();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mActivePointerId = event.getPointerId(0);
                mInitDownY = mLastY = getMotionEventY(event, mActivePointerId);
                mInitDownX = mLastX = getMotionEventX(event, mActivePointerId);

                // if it isn't an ing status or default status
                if (STATUS.isSwipingToRefresh(mStatus) || STATUS.isSwipingToLoadMore(mStatus) ||
                        STATUS.isReleaseToRefresh(mStatus) || STATUS.isReleaseToLoadMore(mStatus)) {
                    // abort autoScrolling, not trigger the method #autoScrollFinished()
                    mAutoScroller.abortIfRunning();
                    if (mDebug) {
                        Log.i(TAG, "Another finger down, abort auto scrolling, let the new finger handle");
                    }
                }

                if (STATUS.isSwipingToRefresh(mStatus) || STATUS.isReleaseToRefresh(mStatus)
                        || STATUS.isSwipingToLoadMore(mStatus) || STATUS.isReleaseToLoadMore(mStatus)) {
                    return true;
                }

                // let children view handle the ACTION_DOWN;

                // 1. children consumed:
                // if at least one of children onTouchEvent() ACTION_DOWN return true.
                // ACTION_DOWN event will not return to SwipeToLoadLayout#onTouchEvent().
                // but the others action can be handled by SwipeToLoadLayout#onInterceptTouchEvent()

                // 2. children not consumed:
                // if children onTouchEvent() ACTION_DOWN return false.
                // ACTION_DOWN event will return to SwipeToLoadLayout's onTouchEvent().
                // SwipeToLoadLayout#onTouchEvent() ACTION_DOWN return true to consume the ACTION_DOWN event.

                // anyway: handle action down in onInterceptTouchEvent() to init is an good option
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                float y = getMotionEventY(event, mActivePointerId);
                float x = getMotionEventX(event, mActivePointerId);
                final float yInitDiff = y - mInitDownY;
                final float xInitDiff = x - mInitDownX;
                mLastY = y;
                mLastX = x;
                boolean moved = Math.abs(xInitDiff) > Math.abs(yInitDiff)
                        && Math.abs(xInitDiff) > mTouchSlop;
                boolean triggerCondition =
                        // refresh trigger condition
                        (xInitDiff > 0 && moved && onCheckCanRefresh()) ||
                                //load more trigger condition
                                (xInitDiff < 0 && moved && onCheckCanLoadMore());
                if (triggerCondition) {
                    // if the refresh's or load more's trigger condition  is true,
                    // intercept the move action event and pass it to SwipeToLoadLayout#onTouchEvent()
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP: {
                onSecondaryPointerUp(event);
                mInitDownY = mLastY = getMotionEventY(event, mActivePointerId);
                mInitDownX = mLastX = getMotionEventX(event, mActivePointerId);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER;
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = event.getPointerId(0);
                return true;

            case MotionEvent.ACTION_MOVE:
                // take over the ACTION_MOVE event from SwipeToLoadLayout#onInterceptTouchEvent()
                // if condition is true
                final float y = getMotionEventY(event, mActivePointerId);
                final float x = getMotionEventX(event, mActivePointerId);

                final float yDiff = y - mLastY;
                final float xDiff = x - mLastX;
                mLastY = y;
                mLastX = x;

                if (Math.abs(yDiff) > Math.abs(xDiff) && Math.abs(yDiff) > mTouchSlop) {
                    return false;
                }

                if (STATUS.isStatusDefault(mStatus)) {
                    if (xDiff > 0 && onCheckCanRefresh()) {
                        mRefreshCallback.onPrepare();
                        setStatus(STATUS.STATUS_SWIPING_TO_REFRESH);
                    } else if (xDiff < 0 && onCheckCanLoadMore()) {
                        mLoadMoreCallback.onPrepare();
                        setStatus(STATUS.STATUS_SWIPING_TO_LOAD_MORE);
                    }
                } else if (STATUS.isRefreshStatus(mStatus)) {
                    if (mTargetOffset <= 0) {
                        setStatus(STATUS.STATUS_DEFAULT);
                        fixCurrentStatusLayout();
                        return false;
                    }
                } else if (STATUS.isLoadMoreStatus(mStatus)) {
                    if (mTargetOffset >= 0) {
                        setStatus(STATUS.STATUS_DEFAULT);
                        fixCurrentStatusLayout();
                        return false;
                    }
                }

                if (STATUS.isRefreshStatus(mStatus)) {
                    if (STATUS.isSwipingToRefresh(mStatus) || STATUS.isReleaseToRefresh(mStatus)) {
                        if (mTargetOffset >= mRefreshTriggerOffset) {
                            setStatus(STATUS.STATUS_RELEASE_TO_REFRESH);
                        } else {
                            setStatus(STATUS.STATUS_SWIPING_TO_REFRESH);
                        }
                        fingerScroll(xDiff);
                    }
                } else if (STATUS.isLoadMoreStatus(mStatus)) {
                    if (STATUS.isSwipingToLoadMore(mStatus) || STATUS.isReleaseToLoadMore(mStatus)) {
                        if (-mTargetOffset >= mLoadMoreTriggerOffset) {
                            setStatus(STATUS.STATUS_RELEASE_TO_LOAD_MORE);
                        } else {
                            setStatus(STATUS.STATUS_SWIPING_TO_LOAD_MORE);
                        }
                        fingerScroll(xDiff);
                    }
                }
                return true;

            case MotionEvent.ACTION_POINTER_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId != INVALID_POINTER) {
                    mActivePointerId = pointerId;
                }
                mInitDownY = mLastY = getMotionEventY(event, mActivePointerId);
                mInitDownX = mLastX = getMotionEventX(event, mActivePointerId);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                onSecondaryPointerUp(event);
                mInitDownY = mLastY = getMotionEventY(event, mActivePointerId);
                mInitDownX = mLastX = getMotionEventX(event, mActivePointerId);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                mActivePointerId = INVALID_POINTER;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * set debug mode(default value false)
     *
     * @param debug if true log on, false log off
     */
    public void setDebug(boolean debug) {
        this.mDebug = debug;
    }

    /**
     * is refresh function is enabled
     *
     * @return
     */
    public boolean isRefreshEnabled() {
        return mRefreshEnabled;
    }

    /**
     * switch refresh function on or off
     *
     * @param enable
     */
    public void setRefreshEnabled(boolean enable) {
        this.mRefreshEnabled = enable;
    }

    /**
     * is load more function is enabled
     *
     * @return
     */
    public boolean isLoadMoreEnabled() {
        return mLoadMoreEnabled;
    }

    /**
     * switch load more function on or off
     *
     * @param enable
     */
    public void setLoadMoreEnabled(boolean enable) {
        this.mLoadMoreEnabled = enable;
    }

    /**
     * is current status refreshing
     *
     * @return
     */
    public boolean isRefreshing() {
        return STATUS.isRefreshing(mStatus);
    }

    /**
     * is current status loading more
     *
     * @return
     */
    public boolean isLoadingMore() {
        return STATUS.isLoadingMore(mStatus);
    }

    /**
     * set refresh header view, the view must at lease be an implement of {@code SwipeRefreshTrigger}.
     * the view can also implement {@code SwipeTrigger} for more extension functions
     *
     * @param view
     */
    public void setRefreshHeaderView(View view) {
        if (view instanceof SwipeRefreshTrigger) {
            if (mHeaderView != null && mHeaderView != view) {
                removeView(mHeaderView);
            }
            if (mHeaderView != view) {
                this.mHeaderView = view;
                addView(view);
            }
        } else {
            Log.e(TAG, "Refresh header view must be an implement of SwipeRefreshTrigger");
        }
    }

    /**
     * set load more footer view, the view must at least be an implement of SwipeLoadTrigger
     * the view can also implement {@code SwipeTrigger} for more extension functions
     *
     * @param view
     */
    public void setLoadMoreFooterView(View view) {
        if (view instanceof SwipeLoadMoreTrigger) {
            if (mFooterView != null && mFooterView != view) {
                removeView(mFooterView);
            }
            if (mFooterView != view) {
                this.mFooterView = view;
                addView(mFooterView);
            }
        } else {
            Log.e(TAG, "Load more footer view must be an implement of SwipeLoadTrigger");
        }
    }

    /**
     * set the style of the refresh header
     *
     * @param style
     */
    public void setSwipeStyle(int style) {
        this.mStyle = style;
        requestLayout();
    }

    /**
     * set how hard to drag. bigger easier, smaller harder;
     *
     * @param dragRatio default value is {@link #DEFAULT_DRAG_RATIO}
     */
    public void setDragRatio(float dragRatio) {
        this.mDragRatio = dragRatio;
    }

    /**
     * set the value of {@link #mRefreshTriggerOffset}.
     * Default value is the refresh header view height {@link #mHeaderWidth}<p/>
     * If the offset you set is smaller than {@link #mHeaderWidth} or not set,
     * using {@link #mHeaderWidth} as default value
     *
     * @param offset
     */
    public void setRefreshTriggerOffset(int offset) {
        mRefreshTriggerOffset = offset;
    }

    /**
     * set the value of {@link #mLoadMoreTriggerOffset}.
     * Default value is the load more footer view height {@link #mFooterWidth}<p/>
     * If the offset you set is smaller than {@link #mFooterWidth} or not set,
     * using {@link #mFooterWidth} as default value
     *
     * @param offset
     */
    public void setLoadMoreTriggerOffset(int offset) {
        mLoadMoreTriggerOffset = offset;
    }

    /**
     * Set the final offset you can swipe to refresh.<br/>
     * If the offset you set is 0(default value) or smaller than {@link #mRefreshTriggerOffset}
     * there no final offset
     *
     * @param offset
     */
    public void setRefreshFinalDragOffset(int offset) {
        mRefreshFinalDragOffset = offset;
    }

    /**
     * Set the final offset you can swipe to load more.<br/>
     * If the offset you set is 0(default value) or smaller than {@link #mLoadMoreTriggerOffset},
     * there no final offset
     *
     * @param offset
     */
    public void setLoadMoreFinalDragOffset(int offset) {
        mLoadMoreFinalDragOffset = offset;
    }

    /**
     * set {@link #mSwipingToRefreshToDefaultScrollingDuration} in milliseconds
     *
     * @param duration
     */
    public void setSwipingToRefreshToDefaultScrollingDuration(int duration) {
        this.mSwipingToRefreshToDefaultScrollingDuration = duration;
    }

    /**
     * set {@link #mReleaseToRefreshToRefreshingScrollingDuration} in milliseconds
     *
     * @param duration
     */
    public void setReleaseToRefreshingScrollingDuration(int duration) {
        this.mReleaseToRefreshToRefreshingScrollingDuration = duration;
    }

    /**
     * set {@link #mRefreshCompleteDelayDuration} in milliseconds
     *
     * @param duration
     */
    public void setRefreshCompleteDelayDuration(int duration) {
        this.mRefreshCompleteDelayDuration = duration;
    }

    /**
     * set {@link #mRefreshCompleteToDefaultScrollingDuration} in milliseconds
     *
     * @param duration
     */
    public void setRefreshCompleteToDefaultScrollingDuration(int duration) {
        this.mRefreshCompleteToDefaultScrollingDuration = duration;
    }

    /**
     * set {@link #mDefaultToRefreshingScrollingDuration} in milliseconds
     *
     * @param duration
     */
    public void setDefaultToRefreshingScrollingDuration(int duration) {
        this.mDefaultToRefreshingScrollingDuration = duration;
    }

    /**
     * set {@link @mSwipingToLoadMoreToDefaultScrollingDuration} in milliseconds
     *
     * @param duration
     */
    public void setSwipingToLoadMoreToDefaultScrollingDuration(int duration) {
        this.mSwipingToLoadMoreToDefaultScrollingDuration = duration;
    }

    /**
     * set {@link #mReleaseToLoadMoreToLoadingMoreScrollingDuration} in milliseconds
     *
     * @param duration
     */
    public void setReleaseToLoadingMoreScrollingDuration(int duration) {
        this.mReleaseToLoadMoreToLoadingMoreScrollingDuration = duration;
    }

    /**
     * set {@link #mLoadMoreCompleteDelayDuration} in milliseconds
     *
     * @param duration
     */
    public void setLoadMoreCompleteDelayDuration(int duration) {
        this.mLoadMoreCompleteDelayDuration = duration;
    }

    /**
     * set {@link #mLoadMoreCompleteToDefaultScrollingDuration} in milliseconds
     *
     * @param duration
     */
    public void setLoadMoreCompleteToDefaultScrollingDuration(int duration) {
        this.mLoadMoreCompleteToDefaultScrollingDuration = duration;
    }

    /**
     * set {@link #mDefaultToLoadingMoreScrollingDuration} in milliseconds
     *
     * @param duration
     */
    public void setDefaultToLoadingMoreScrollingDuration(int duration) {
        this.mDefaultToLoadingMoreScrollingDuration = duration;
    }

    /**
     * set an {@link OnRefreshListener} to listening refresh event
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mRefreshListener = listener;
    }

    /**
     * set an {@link OnLoadMoreListener} to listening load more event
     *
     * @param listener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mLoadMoreListener = listener;
    }

    /**
     * auto refresh or cancel
     *
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing) {
        if (!isRefreshEnabled() || mHeaderView == null) {
            return;
        }
        this.mAutoLoading = refreshing;
        if (refreshing) {
            if (STATUS.isStatusDefault(mStatus)) {
                setStatus(STATUS.STATUS_SWIPING_TO_REFRESH);
                scrollDefaultToRefreshing();
            }
        } else {
            if (STATUS.isRefreshing(mStatus)) {
                mRefreshCallback.onComplete();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollRefreshingToDefault();
                    }
                }, mRefreshCompleteDelayDuration);
            }
        }
    }

    /**
     * auto loading more or cancel
     *
     * @param loadingMore
     */
    public void setLoadingMore(boolean loadingMore) {
        if (!isLoadMoreEnabled() || mFooterView == null) {
            return;
        }
        this.mAutoLoading = loadingMore;
        if (loadingMore) {
            if (STATUS.isStatusDefault(mStatus)) {
                setStatus(STATUS.STATUS_SWIPING_TO_LOAD_MORE);
                scrollDefaultToLoadingMore();
            }
        } else {
            if (STATUS.isLoadingMore(mStatus)) {
                mLoadMoreCallback.onComplete();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollLoadingMoreToDefault();
                    }
                }, mLoadMoreCompleteDelayDuration);
            }
        }
    }

    /**
     * copy from {@link android.support.v4.widget.SwipeRefreshLayout#canChildScrollUp()}
     *
     * @return Whether it is possible for the child view of this layout to
     * scroll left. Override this if the child view is a custom view.
     */
    protected boolean canChildScrollLeft() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTargetView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTargetView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getLeft() < absListView.getPaddingLeft());
            } else if (mTargetView instanceof ViewPager) {
                ViewPager pager = (ViewPager) mTargetView;
                return pager.getCurrentItem() > 0;
            } else {
                return ViewCompat.canScrollHorizontally(mTargetView, -1) || mTargetView.getScrollX() > 0;
            }
        } else {
            if (mTargetView instanceof ViewPager) {
                ViewPager pager = (ViewPager) mTargetView;
                return pager.getCurrentItem() > 0;
            }
            return ViewCompat.canScrollHorizontally(mTargetView, -1);
        }
    }

    /**
     * Whether it is possible for the child view of this layout to
     * scroll right. Override this if the child view is a custom view.
     *
     * @return
     */
    protected boolean canChildScrollRight() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTargetView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTargetView;
                return absListView.getChildCount() > 0
                        && (absListView.getLastVisiblePosition() < absListView.getChildCount() - 1
                        || absListView.getChildAt(absListView.getChildCount() - 1).getRight() > absListView.getPaddingRight());
            } else if (mTargetView instanceof ViewPager) {
                ViewPager pager = (ViewPager) mTargetView;
                return pager.getCurrentItem() < pager.getAdapter().getCount() - 1;
            } else {
                return ViewCompat.canScrollHorizontally(mTargetView, 1) || mTargetView.getScrollX() < 0;
            }
        } else {
            if (mTargetView instanceof ViewPager) {
                ViewPager pager = (ViewPager) mTargetView;
                return pager.getCurrentItem() < pager.getAdapter().getCount() - 1;
            }
            return ViewCompat.canScrollHorizontally(mTargetView, 1);
        }
    }

    /**
     * @see #onLayout(boolean, int, int, int, int)
     */
    private void layoutChildren() {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();

        if (mTargetView == null) {
            return;
        }

        // layout header
        if (mHeaderView != null) {
            final View headerView = mHeaderView;
            MarginLayoutParams lp = (MarginLayoutParams) headerView.getLayoutParams();
            final int headerLeft;
            final int headerTop = paddingTop + lp.topMargin;
            switch (mStyle) {
                case STYLE.CLASSIC:
                    // classic
                    headerLeft = paddingLeft + lp.leftMargin - mHeaderWidth + mHeaderOffset;
                    break;
                case STYLE.ABOVE:
                    // classic
                    headerLeft = paddingLeft + lp.leftMargin - mHeaderWidth + mHeaderOffset;
                    break;
                case STYLE.BLEW:
                    // blew
                    headerLeft = paddingLeft + lp.leftMargin;
                    break;
                case STYLE.SCALE:
                    // scale
                    headerLeft = paddingLeft + lp.leftMargin - mHeaderWidth / 2 + mHeaderOffset / 2;
                    break;
                default:
                    // classic
                    headerLeft = paddingLeft + lp.leftMargin - mHeaderWidth + mHeaderOffset;
                    break;
            }
            final int headerRight = headerLeft + headerView.getMeasuredWidth();
            final int headerBottom = headerTop + headerView.getMeasuredHeight();
            headerView.layout(headerLeft, headerTop, headerRight, headerBottom);
        }


        // layout target
        if (mTargetView != null) {
            final View targetView = mTargetView;
            MarginLayoutParams lp = (MarginLayoutParams) targetView.getLayoutParams();
            final int targetLeft;
            final int targetTop = paddingTop + lp.topMargin;

            switch (mStyle) {
                case STYLE.CLASSIC:
                    // classic
                    targetLeft = paddingLeft + lp.leftMargin + mTargetOffset;
                    break;
                case STYLE.ABOVE:
                    // above
                    targetLeft = paddingLeft + lp.leftMargin;
                    break;
                case STYLE.BLEW:
                    // classic
                    targetLeft = paddingLeft + lp.leftMargin + mTargetOffset;
                    break;
                case STYLE.SCALE:
                    // classic
                    targetLeft = paddingLeft + lp.leftMargin + mTargetOffset;
                    break;
                default:
                    // classic
                    targetLeft = paddingLeft + lp.leftMargin + mTargetOffset;
                    break;
            }
            final int targetRight = targetLeft + targetView.getMeasuredWidth();
            final int targetBottom = targetTop + targetView.getMeasuredHeight();
            targetView.layout(targetLeft, targetTop, targetRight, targetBottom);
        }

        // layout footer
        if (mFooterView != null) {
            final View footerView = mFooterView;
            MarginLayoutParams lp = (MarginLayoutParams) footerView.getLayoutParams();
            final int footerRight;
            final int footerTop = paddingTop + lp.topMargin;
            switch (mStyle) {
                case STYLE.CLASSIC:
                    // classic
                    footerRight = width - paddingRight - lp.rightMargin + mFooterWidth + mFooterOffset;
                    break;
                case STYLE.ABOVE:
                    // classic
                    footerRight = width - paddingRight - lp.rightMargin + mFooterWidth + mFooterOffset;
                    break;
                case STYLE.BLEW:
                    // blew
                    footerRight = width - paddingRight - lp.rightMargin;
                    break;
                case STYLE.SCALE:
                    // scale
                    footerRight = width - paddingRight - lp.rightMargin + mFooterWidth / 2 + mFooterOffset / 2;
                    break;
                default:
                    // classic
                    footerRight = width - paddingRight - lp.rightMargin + mFooterWidth + mFooterOffset;
                    break;
            }
            final int footerLeft = footerRight - footerView.getMeasuredWidth();
            final int footerBottom = footerTop + footerView.getMeasuredHeight();

            footerView.layout(footerLeft, footerTop, footerRight, footerBottom);
        }

        if (mStyle == STYLE.CLASSIC
                || mStyle == STYLE.ABOVE) {
            if (mHeaderView != null) {
                mHeaderView.bringToFront();
            }
            if (mFooterView != null) {
                mFooterView.bringToFront();
            }
        } else if (mStyle == STYLE.BLEW || mStyle == STYLE.SCALE) {
            if (mTargetView != null) {
                mTargetView.bringToFront();
            }
        }
    }

    private void fixCurrentStatusLayout() {
        if (STATUS.isRefreshing(mStatus)) {
            mTargetOffset = (int) (mRefreshTriggerOffset + 0.5f);
            mHeaderOffset = mTargetOffset;
            mFooterOffset = 0;
            layoutChildren();
            invalidate();
        } else if (STATUS.isStatusDefault(mStatus)) {
            mTargetOffset = 0;
            mHeaderOffset = 0;
            mFooterOffset = 0;
            layoutChildren();
            invalidate();
        } else if (STATUS.isLoadingMore(mStatus)) {
            mTargetOffset = -(int) (mLoadMoreTriggerOffset + 0.5f);
            mHeaderOffset = 0;
            mFooterOffset = mTargetOffset;
            layoutChildren();
            invalidate();
        }
    }

    /**
     * scrolling by physical touch with your fingers
     *
     * @param xDiff
     */
    private void fingerScroll(final float xDiff) {
        float ratio = mDragRatio;
        float xScrolled = xDiff * ratio;

        // make sure (targetOffset>0 -> targetOffset=0 -> default status)
        // or (targetOffset<0 -> targetOffset=0 -> default status)
        // forbidden fling (targetOffset>0 -> targetOffset=0 ->targetOffset<0 -> default status)
        // or (targetOffset<0 -> targetOffset=0 ->targetOffset>0 -> default status)
        // I am so smart :)

        float tmpTargetOffset = xScrolled + mTargetOffset;
        if ((tmpTargetOffset > 0 && mTargetOffset < 0)
                || (tmpTargetOffset < 0 && mTargetOffset > 0)) {
            xScrolled = -mTargetOffset;
        }


        if (mRefreshFinalDragOffset >= mRefreshTriggerOffset && tmpTargetOffset > mRefreshFinalDragOffset) {
            xScrolled = mRefreshFinalDragOffset - mTargetOffset;
        } else if (mLoadMoreFinalDragOffset >= mLoadMoreTriggerOffset && -tmpTargetOffset > mLoadMoreFinalDragOffset) {
            xScrolled = -mLoadMoreFinalDragOffset - mTargetOffset;
        }

        if (STATUS.isRefreshStatus(mStatus)) {
            mRefreshCallback.onMove(mTargetOffset, false, false);
        } else if (STATUS.isLoadMoreStatus(mStatus)) {
            mLoadMoreCallback.onMove(mTargetOffset, false, false);
        }
        updateScroll(xScrolled);
    }

    private void autoScroll(final float xScrolled) {

        if (STATUS.isSwipingToRefresh(mStatus)) {
            mRefreshCallback.onMove(mTargetOffset, false, true);
        } else if (STATUS.isReleaseToRefresh(mStatus)) {
            mRefreshCallback.onMove(mTargetOffset, false, true);
        } else if (STATUS.isRefreshing(mStatus)) {
            mRefreshCallback.onMove(mTargetOffset, true, true);
        } else if (STATUS.isSwipingToLoadMore(mStatus)) {
            mLoadMoreCallback.onMove(mTargetOffset, false, true);
        } else if (STATUS.isReleaseToLoadMore(mStatus)) {
            mLoadMoreCallback.onMove(mTargetOffset, false, true);
        } else if (STATUS.isLoadingMore(mStatus)) {
            mLoadMoreCallback.onMove(mTargetOffset, true, true);
        }
        updateScroll(xScrolled);
    }

    /**
     * Process the scrolling(auto or physical) and append the diff values to mTargetOffset
     * I think it's the most busy and core method. :) a ha ha ha ha...
     *
     * @param xScrolled
     */
    private void updateScroll(final float xScrolled) {
        if (xScrolled == 0) {
            return;
        }
        mTargetOffset += xScrolled;

        if (STATUS.isRefreshStatus(mStatus)) {
            mHeaderOffset = mTargetOffset;
            mFooterOffset = 0;
        } else if (STATUS.isLoadMoreStatus(mStatus)) {
            mFooterOffset = mTargetOffset;
            mHeaderOffset = 0;
        }

        if (mDebug) {
            Log.i(TAG, "mTargetOffset = " + mTargetOffset);
        }
        layoutChildren();
        invalidate();
    }

    /**
     * on active finger up
     */
    private void onActivePointerUp() {
        if (STATUS.isSwipingToRefresh(mStatus)) {
            // simply return
            scrollSwipingToRefreshToDefault();

        } else if (STATUS.isSwipingToLoadMore(mStatus)) {
            // simply return
            scrollSwipingToLoadMoreToDefault();

        } else if (STATUS.isReleaseToRefresh(mStatus)) {
            // return to header height and perform refresh
            mRefreshCallback.onRelease();
            scrollReleaseToRefreshToRefreshing();

        } else if (STATUS.isReleaseToLoadMore(mStatus)) {
            // return to footer height and perform loadMore
            mLoadMoreCallback.onRelease();
            scrollReleaseToLoadMoreToLoadingMore();

        }
    }

    /**
     * on not active finger up
     *
     * @param ev
     */
    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private void scrollDefaultToRefreshing() {
        mAutoScroller.autoScroll((int) (mRefreshTriggerOffset + 0.5f), mDefaultToRefreshingScrollingDuration);
    }

    private void scrollDefaultToLoadingMore() {
        mAutoScroller.autoScroll(-(int) (mLoadMoreTriggerOffset + 0.5f), mDefaultToLoadingMoreScrollingDuration);
    }

    private void scrollSwipingToRefreshToDefault() {
        mAutoScroller.autoScroll(-mHeaderOffset, mSwipingToRefreshToDefaultScrollingDuration);
    }

    private void scrollSwipingToLoadMoreToDefault() {
        mAutoScroller.autoScroll(-mFooterOffset, mSwipingToLoadMoreToDefaultScrollingDuration);
    }

    private void scrollReleaseToRefreshToRefreshing() {
        mAutoScroller.autoScroll(mHeaderWidth - mHeaderOffset, mReleaseToRefreshToRefreshingScrollingDuration);
    }

    private void scrollReleaseToLoadMoreToLoadingMore() {
        mAutoScroller.autoScroll(-mFooterOffset - mFooterWidth, mReleaseToLoadMoreToLoadingMoreScrollingDuration);
    }

    private void scrollRefreshingToDefault() {
        mAutoScroller.autoScroll(-mHeaderOffset, mRefreshCompleteToDefaultScrollingDuration);
    }

    private void scrollLoadingMoreToDefault() {
        mAutoScroller.autoScroll(-mFooterOffset, mLoadMoreCompleteToDefaultScrollingDuration);
    }

    /**
     * invoke when {@link AutoScroller#finish()} is automatic
     */
    private void autoScrollFinished() {
        int mLastStatus = mStatus;

        if (STATUS.isReleaseToRefresh(mStatus)) {
            setStatus(STATUS.STATUS_REFRESHING);
            fixCurrentStatusLayout();
            mRefreshCallback.onRefresh();

        } else if (STATUS.isRefreshing(mStatus)) {
            setStatus(STATUS.STATUS_DEFAULT);
            fixCurrentStatusLayout();
            mRefreshCallback.onReset();

        } else if (STATUS.isSwipingToRefresh(mStatus)) {
            if (mAutoLoading) {
                mAutoLoading = false;
                setStatus(STATUS.STATUS_REFRESHING);
                fixCurrentStatusLayout();
                mRefreshCallback.onRefresh();
            } else {
                setStatus(STATUS.STATUS_DEFAULT);
                fixCurrentStatusLayout();
                mRefreshCallback.onReset();
            }
        } else if (STATUS.isStatusDefault(mStatus)) {

        } else if (STATUS.isSwipingToLoadMore(mStatus)) {
            if (mAutoLoading) {
                mAutoLoading = false;
                setStatus(STATUS.STATUS_LOADING_MORE);
                fixCurrentStatusLayout();
                mLoadMoreCallback.onLoadMore();
            } else {
                setStatus(STATUS.STATUS_DEFAULT);
                fixCurrentStatusLayout();
                mLoadMoreCallback.onReset();
            }
        } else if (STATUS.isLoadingMore(mStatus)) {
            setStatus(STATUS.STATUS_DEFAULT);
            fixCurrentStatusLayout();
            mLoadMoreCallback.onReset();
        } else if (STATUS.isReleaseToLoadMore(mStatus)) {
            setStatus(STATUS.STATUS_LOADING_MORE);
            fixCurrentStatusLayout();
            mLoadMoreCallback.onLoadMore();
        } else {
            throw new IllegalStateException("illegal state: " + STATUS.getStatus(mStatus));
        }

        if (mDebug) {
            Log.i(TAG, STATUS.getStatus(mLastStatus) + " -> " + STATUS.getStatus(mStatus));
        }
    }

    /**
     * check if it can refresh
     *
     * @return
     */
    private boolean onCheckCanRefresh() {

        return mRefreshEnabled && !canChildScrollLeft() && mHasHeaderView && mRefreshTriggerOffset > 0;
    }

    /**
     * check if it can load more
     *
     * @return
     */
    private boolean onCheckCanLoadMore() {

        return mLoadMoreEnabled && !canChildScrollRight() && mHasFooterView && mLoadMoreTriggerOffset > 0;
    }

    private float getMotionEventY(MotionEvent event, int activePointerId) {
        final int index = event.findPointerIndex(activePointerId);
        if (index < 0) {
            return INVALID_COORDINATE;
        }
        return event.getY(index);
    }

    private float getMotionEventX(MotionEvent event, int activePointId) {
        final int index = event.findPointerIndex(activePointId);
        if (index < 0) {
            return INVALID_COORDINATE;
        }
        return event.getX(index);
    }

    RefreshCallback mRefreshCallback = new RefreshCallback() {
        @Override
        public void onPrepare() {
            if (mHeaderView != null && mHeaderView instanceof SwipeTrigger && STATUS.isStatusDefault(mStatus)) {
                mHeaderView.setVisibility(VISIBLE);
                ((SwipeTrigger) mHeaderView).onPrepare();
            }
        }

        @Override
        public void onMove(int x, boolean isComplete, boolean automatic) {
            if (mHeaderView != null && mHeaderView instanceof SwipeTrigger && STATUS.isRefreshStatus(mStatus)) {
                if (mHeaderView.getVisibility() != VISIBLE) {
                    mHeaderView.setVisibility(VISIBLE);
                }
                ((SwipeTrigger) mHeaderView).onMove(x, isComplete, automatic);
            }
        }

        @Override
        public void onRelease() {
            if (mHeaderView != null && mHeaderView instanceof SwipeTrigger && STATUS.isReleaseToRefresh(mStatus)) {
                ((SwipeTrigger) mHeaderView).onRelease();
            }
        }

        @Override
        public void onRefresh() {
            if (mHeaderView != null && STATUS.isRefreshing(mStatus)) {
                if (mHeaderView instanceof SwipeRefreshTrigger) {
                    ((SwipeRefreshTrigger) mHeaderView).onRefresh();
                }
                if (mRefreshListener != null) {
                    mRefreshListener.onRefresh();
                }
            }
        }

        @Override
        public void onComplete() {
            if (mHeaderView != null && mHeaderView instanceof SwipeTrigger) {
                ((SwipeTrigger) mHeaderView).onComplete();
            }
        }

        @Override
        public void onReset() {
            if (mHeaderView != null && mHeaderView instanceof SwipeTrigger && STATUS.isStatusDefault(mStatus)) {
                ((SwipeTrigger) mHeaderView).onReset();
                mHeaderView.setVisibility(GONE);
            }
        }
    };

    LoadMoreCallback mLoadMoreCallback = new LoadMoreCallback() {

        @Override
        public void onPrepare() {
            if (mFooterView != null && mFooterView instanceof SwipeTrigger && STATUS.isStatusDefault(mStatus)) {
                mFooterView.setVisibility(VISIBLE);
                ((SwipeTrigger) mFooterView).onPrepare();
            }
        }

        @Override
        public void onMove(int x, boolean isComplete, boolean automatic) {
            if (mFooterView != null && mFooterView instanceof SwipeTrigger && STATUS.isLoadMoreStatus(mStatus)) {
                if (mFooterView.getVisibility() != VISIBLE) {
                    mFooterView.setVisibility(VISIBLE);
                }
                ((SwipeTrigger) mFooterView).onMove(x, isComplete, automatic);
            }
        }

        @Override
        public void onRelease() {
            if (mFooterView != null && mFooterView instanceof SwipeTrigger && STATUS.isReleaseToLoadMore(mStatus)) {
                ((SwipeTrigger) mFooterView).onRelease();
            }
        }

        @Override
        public void onLoadMore() {
            if (mFooterView != null && STATUS.isLoadingMore(mStatus)) {
                if (mFooterView instanceof SwipeLoadMoreTrigger) {
                    ((SwipeLoadMoreTrigger) mFooterView).onLoadMore();
                }
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore();
                }
            }
        }

        @Override
        public void onComplete() {
            if (mFooterView != null && mFooterView instanceof SwipeTrigger) {
                ((SwipeTrigger) mFooterView).onComplete();
            }
        }

        @Override
        public void onReset() {
            if (mFooterView != null && mFooterView instanceof SwipeTrigger && STATUS.isStatusDefault(mStatus)) {
                ((SwipeTrigger) mFooterView).onReset();
                mFooterView.setVisibility(GONE);
            }
        }
    };

    /**
     * refresh event callback
     */
    abstract class RefreshCallback implements SwipeTrigger, SwipeRefreshTrigger {
    }

    /**
     * load more event callback
     */
    abstract class LoadMoreCallback implements SwipeTrigger, SwipeLoadMoreTrigger {
    }

    private class AutoScroller implements Runnable {

        private Scroller mScroller;

        private int mmLastX;

        private boolean mRunning = false;

        private boolean mAbort = false;

        public AutoScroller() {
            mScroller = new Scroller(getContext());
        }

        @Override
        public void run() {
            boolean finish = !mScroller.computeScrollOffset() || mScroller.isFinished();
            int currx = mScroller.getCurrX();
            int xDiff = currx - mmLastX;
            if (finish) {
                finish();
            } else {
                mmLastX = currx;
                HorSwipeToLoadLayout.this.autoScroll(xDiff);
                post(this);
            }
        }

        /**
         * remove the post callbacks and reset default values
         */
        private void finish() {
            mmLastX = 0;
            mRunning = false;
            removeCallbacks(this);
            // if abort by user, don't call
            if (!mAbort) {
                autoScrollFinished();
            }
        }

        /**
         * abort scroll if it is scrolling
         */
        public void abortIfRunning() {
            if (mRunning) {
                if (!mScroller.isFinished()) {
                    mAbort = true;
                    mScroller.forceFinished(true);
                }
                finish();
                mAbort = false;
            }
        }

        /**
         * The param xScrolled here isn't final pos of y.
         * It's just like the yScrolled param in the
         * {@link #updateScroll(float yScrolled)}
         *
         * @param xScrolled
         * @param duration
         */
        private void autoScroll(int xScrolled, int duration) {
            removeCallbacks(this);
            mmLastX = 0;
            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }
            mScroller.startScroll(0, 0, xScrolled, 0, duration);
            post(this);
            mRunning = true;
        }
    }

    /**
     * Set the current status for better control
     *
     * @param status
     */
    private void setStatus(int status) {
        mStatus = status;
        if (mDebug) {
            STATUS.printStatus(status);
        }
    }

    /**
     * an inner util class.
     * enum of status
     */
    private final static class STATUS {
        private static final int STATUS_REFRESH_RETURNING = -4;
        private static final int STATUS_REFRESHING = -3;
        private static final int STATUS_RELEASE_TO_REFRESH = -2;
        private static final int STATUS_SWIPING_TO_REFRESH = -1;
        private static final int STATUS_DEFAULT = 0;
        private static final int STATUS_SWIPING_TO_LOAD_MORE = 1;
        private static final int STATUS_RELEASE_TO_LOAD_MORE = 2;
        private static final int STATUS_LOADING_MORE = 3;
        private static final int STATUS_LOAD_MORE_RETURNING = 4;

        private static boolean isRefreshing(final int status) {
            return status == STATUS.STATUS_REFRESHING;
        }

        private static boolean isLoadingMore(final int status) {
            return status == STATUS.STATUS_LOADING_MORE;
        }

        private static boolean isReleaseToRefresh(final int status) {
            return status == STATUS.STATUS_RELEASE_TO_REFRESH;
        }

        private static boolean isReleaseToLoadMore(final int status) {
            return status == STATUS.STATUS_RELEASE_TO_LOAD_MORE;
        }

        private static boolean isSwipingToRefresh(final int status) {
            return status == STATUS.STATUS_SWIPING_TO_REFRESH;
        }

        private static boolean isSwipingToLoadMore(final int status) {
            return status == STATUS.STATUS_SWIPING_TO_LOAD_MORE;
        }

        private static boolean isRefreshStatus(final int status) {
            return status < STATUS.STATUS_DEFAULT;
        }

        public static boolean isLoadMoreStatus(final int status) {
            return status > STATUS.STATUS_DEFAULT;
        }

        private static boolean isStatusDefault(final int status) {
            return status == STATUS.STATUS_DEFAULT;
        }

        private static String getStatus(int status) {
            final String statusInfo;
            switch (status) {
                case STATUS_REFRESH_RETURNING:
                    statusInfo = "status_refresh_returning";
                    break;
                case STATUS_REFRESHING:
                    statusInfo = "status_refreshing";
                    break;
                case STATUS_RELEASE_TO_REFRESH:
                    statusInfo = "status_release_to_refresh";
                    break;
                case STATUS_SWIPING_TO_REFRESH:
                    statusInfo = "status_swiping_to_refresh";
                    break;
                case STATUS_DEFAULT:
                    statusInfo = "status_default";
                    break;
                case STATUS_SWIPING_TO_LOAD_MORE:
                    statusInfo = "status_swiping_to_load_more";
                    break;
                case STATUS_RELEASE_TO_LOAD_MORE:
                    statusInfo = "status_release_to_load_more";
                    break;
                case STATUS_LOADING_MORE:
                    statusInfo = "status_loading_more";
                    break;
                case STATUS_LOAD_MORE_RETURNING:
                    statusInfo = "status_load_more_returning";
                    break;
                default:
                    statusInfo = "status_illegal!";
                    break;
            }
            return statusInfo;
        }

        private static void printStatus(int status) {
            Log.i(TAG, "printStatus:" + getStatus(status));
        }
    }
}
