package com.aspsine.swipetoloadlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Scroller;

/**
 * Created by Aspsine on 2015/8/13.
 */
public class SwipeToLoadLayout extends ViewGroup {

    private static final String TAG = SwipeToLoadLayout.class.getSimpleName();

    private static final int DEFAULT_RELEASE_TO_REFRESHING_SCROLLING_DURATION = 200;

    private static final int DEFAULT_REFRESH_COMPLETE_DELAY_DURATION = 300;

    private static final int DEFAULT_REFRESH_COMPLETE_TO_DEFAULT_SCROLLING_DURATION = 500;

    private static final int DEFAULT_DEFAULT_TO_REFRESHING_SCROLLING_DURATION = 500;

    private static final int DEFAULT_RELEASE_TO_LOADING_MORE_SCROLLING_DURATION = 200;

    private static final int DEFAULT_LOAD_MORE_COMPLETE_DELAY_DURATION = 300;

    private static final int DEFAULT_LOAD_MORE_COMPLETE_TO_DEFAULT_SCROLLING_DURATION = 300;

    private static final int DEFAULT_DEFAULT_TO_LOADING_MORE_SCROLLING_DURATION = 300;


    /**
     * how hard to drag
     */
    private static final float DEFAULT_DRAG_RATIO = 0.5f;

    private static final int INVALID_POINTER = -1;

    private static final int INVALID_COORDINATE = -1;

    private AutoScroller mAutoScroller;

    private OnRefreshListener mRefreshListener;

    private OnLoadMoreListener mLoadMoreListener;

    private View mHeaderView;

    private View mTargetView;

    private View mFooterView;

    private int mHeaderHeight;

    private int mFooterHeight;

    private boolean mHasHeaderView;

    private boolean mHasFooterView;

    /**
     * indicate whether in debug mode
     */
    private boolean mDebug;

    /**
     * the threshold of the touch event
     */
    private final int mTouchSlop;

    /**
     * status of SwipeToLoadLayout
     */
    private int mStatus = STATUS.STATUS_DEFAULT;

    /**
     * target view top offset
     */
    private int mHeaderOffset;

    /**
     * target offset
     */
    private int mTargetOffset;

    /**
     * target view bottom offset
     */
    private int mFooterOffset;

    /**
     * init touch action down point.y
     */
    private float mInitDownY;

    /**
     * init touch action down point.x
     */
    private float mInitDownX;

    /**
     * last touch point.y
     */
    private float mLastY;

    /**
     * last touch point.x
     */
    private float mLastX;

    /**
     * action touch pointer's id
     */
    private int mActivePointerId;

    /**
     * indicate whither is loading
     */
    private boolean mLoading;

    /**
     * <b>ATTRIBUTE:</b>
     * a switcher indicate whither refresh function is enabled
     */
    private boolean mRefreshEnabled = true;

    /**
     * <b>ATTRIBUTE:</b>
     * a switcher indicate whiter load more function is enabled
     */
    private boolean mLoadMoreEnabled = true;

    /**
     * <b>ATTRIBUTE:</b>
     * the style default classic
     */
    private int mStyle = STYLE.CLASSIC;

    /**
     * <b>ATTRIBUTE:</b>
     * offset to trigger refresh
     */
    private float mRefreshTriggerOffset;

    /**
     * <b>ATTRIBUTE:</b>
     * offset to trigger load more
     */
    private float mLoadMoreTriggerOffset;

    /**
     * <b>ATTRIBUTE:</b>
     * the max value of top offset
     */
    private float mRefreshFinalDragOffset;

    /**
     * <b>ATTRIBUTE:</b>
     * the max value of bottom offset
     */
    private float mLoadMoreFinalDragOffset;

    /**
     * <b>ATTRIBUTE:</b>
     * Scrolling duration status release to refresh -> refreshing
     */
    private int mReleaseToRefreshingScrollingDuration = DEFAULT_RELEASE_TO_REFRESHING_SCROLLING_DURATION;

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
    private int mReleaseToLoadingMoreScrollingDuration = DEFAULT_RELEASE_TO_LOADING_MORE_SCROLLING_DURATION;


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

    public SwipeToLoadLayout(Context context) {
        this(context, null);
    }

    public SwipeToLoadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeToLoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeToLoadLayout, defStyleAttr, 0);
        try {
            final int N = a.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.SwipeToLoadLayout_refresh_enabled) {
                    setRefreshEnabled(a.getBoolean(attr, true));

                } else if (attr == R.styleable.SwipeToLoadLayout_load_more_enabled) {
                    setLoadMoreEnabled(a.getBoolean(attr, true));

                } else if (attr == R.styleable.SwipeToLoadLayout_swipe_style) {
                    setSwipeStyle(a.getInt(attr, STYLE.CLASSIC));

                } else if (attr == R.styleable.SwipeToLoadLayout_refresh_final_drag_offset) {
                    setRefreshFinalDragOffset(a.getDimensionPixelOffset(attr, 0));

                } else if (attr == R.styleable.SwipeToLoadLayout_load_more_final_drag_offset) {
                    setLoadMoreFinalDragOffset(a.getDimensionPixelOffset(attr, 0));

                } else if (attr == R.styleable.SwipeToLoadLayout_refresh_trigger_offset) {
                    setRefreshTriggerOffset(a.getDimensionPixelOffset(attr, 0));

                } else if (attr == R.styleable.SwipeToLoadLayout_load_more_trigger_offset) {
                    setLoadMoreTriggerOffset(a.getDimensionPixelOffset(attr, 0));

                } else if (attr == R.styleable.SwipeToLoadLayout_release_to_refreshing_scrolling_duration) {
                    setReleaseToRefreshingScrollingDuration(a.getInt(attr, DEFAULT_RELEASE_TO_REFRESHING_SCROLLING_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_refresh_complete_delay_duration) {
                    setRefreshCompleteDelayDuration(a.getInt(attr, DEFAULT_REFRESH_COMPLETE_DELAY_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_refresh_complete_to_default_scrolling_duration) {
                    setRefreshCompleteToDefaultScrollingDuration(a.getInt(attr, DEFAULT_REFRESH_COMPLETE_TO_DEFAULT_SCROLLING_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_default_to_refreshing_scrolling_duration) {
                    setDefaultToRefreshingScrollingDuration(a.getInt(attr, DEFAULT_DEFAULT_TO_REFRESHING_SCROLLING_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_release_to_loading_more_scrolling_duration) {
                    setReleaseToLoadingMoreScrollingDuration(a.getInt(attr, DEFAULT_RELEASE_TO_LOADING_MORE_SCROLLING_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_load_more_complete_delay_duration) {
                    setLoadMoreCompleteDelayDuration(a.getInt(attr, DEFAULT_LOAD_MORE_COMPLETE_DELAY_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_load_more_complete_to_default_scrolling_duration) {
                    setLoadMoreCompleteToDefaultScrollingDuration(a.getInt(attr, DEFAULT_LOAD_MORE_COMPLETE_TO_DEFAULT_SCROLLING_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_default_to_loading_more_scrolling_duration) {
                    setDefaultToLoadingMoreScrollingDuration(a.getInt(attr, DEFAULT_DEFAULT_TO_LOADING_MORE_SCROLLING_DURATION));

                }
            }
        } finally {
            a.recycle();
        }

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mAutoScroller = new AutoScroller();
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
     * is load more function is enabled
     *
     * @return
     */
    public boolean isLoadMoreEnabled() {
        return mLoadMoreEnabled;
    }

    /**
     * is current status refreshing
     *
     * @return
     */
    public boolean isRefreshing() {
        return STATUS.isRefreshing(mStatus) && mLoading;
    }

    /**
     * is current status loading more
     *
     * @return
     */
    public boolean isLoadingMore() {
        return STATUS.isLoadingMore(mStatus) && mLoading;
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
     * switch refresh function on or off
     *
     * @param enable
     */
    public void setRefreshEnabled(boolean enable) {
        this.mRefreshEnabled = enable;
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
     * set the style of the refresh header
     *
     * @param style
     */
    public void setSwipeStyle(int style) {
        this.mStyle = style;
        requestLayout();
    }

    /**
     * set the value of {@link #mRefreshTriggerOffset}.
     * Default value is the refresh header view height {@link #mHeaderHeight}<p/>
     * If the offset you set is smaller than {@link #mHeaderHeight} or not set,
     * using {@link #mHeaderHeight} as default value
     *
     * @param offset
     */
    public void setRefreshTriggerOffset(int offset) {
        mRefreshTriggerOffset = offset;
    }

    /**
     * set the value of {@link #mLoadMoreTriggerOffset}.
     * Default value is the load more footer view height {@link #mFooterHeight}<p/>
     * If the offset you set is smaller than {@link #mFooterHeight} or not set,
     * using {@link #mFooterHeight} as default value
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
     * set {@link #mReleaseToRefreshingScrollingDuration} in milliseconds
     *
     * @param duration
     */
    public void setReleaseToRefreshingScrollingDuration(int duration) {
        this.mReleaseToRefreshingScrollingDuration = duration;
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
     * set {@link #mReleaseToLoadingMoreScrollingDuration} in milliseconds
     *
     * @param duration
     */
    public void setReleaseToLoadingMoreScrollingDuration(int duration) {
        this.mReleaseToLoadingMoreScrollingDuration = duration;
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
        this.mLoading = refreshing;
        if (refreshing) {
            // can not perform refresh when it is refreshing or loading more
            if (STATUS.isLoadingMore(mStatus)) {
                return;
            }
            setStatus(STATUS.STATUS_REFRESHING);
            int duration;
            if (mHeaderOffset > mRefreshTriggerOffset) {
                duration = mReleaseToRefreshingScrollingDuration;
            } else {
                duration = mDefaultToRefreshingScrollingDuration;
            }
            mAutoScroller.autoScroll(mHeaderHeight - mHeaderOffset, duration);
        } else {
            if (STATUS.isRefreshing(mStatus)) {
                setStatus(STATUS.STATS_REFRESH_COMPLETE);
                mRefreshCallback.complete();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAutoScroller.autoScroll(-mHeaderOffset, mRefreshCompleteToDefaultScrollingDuration);
                    }
                }, mRefreshCompleteDelayDuration);
            } else if (STATUS.isSwipingToRefresh(mStatus)) {
                mAutoScroller.autoScroll(-mHeaderOffset, mRefreshCompleteToDefaultScrollingDuration);
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
        this.mLoading = loadingMore;
        if (loadingMore) {
            // can not perform load more when it is refreshing or loading more
            if (STATUS.isRefreshing(mStatus)) {
                return;
            }
            setStatus(STATUS.STATUS_LOADING_MORE);
            int duration;
            if (-mFooterOffset > mLoadMoreTriggerOffset) {
                duration = mReleaseToLoadingMoreScrollingDuration;
            } else {
                duration = mDefaultToLoadingMoreScrollingDuration;
            }
            mAutoScroller.autoScroll(-mFooterOffset - mFooterHeight, duration);
        } else {
            if (STATUS.isLoadingMore(mStatus)) {
                setStatus(STATUS.STATUS_LOAD_MORE_COMPLETE);
                mLoadMoreCallback.complete();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAutoScroller.autoScroll(-mFooterOffset, mLoadMoreCompleteToDefaultScrollingDuration);
                    }
                }, mLoadMoreCompleteDelayDuration);
            } else if (STATUS.isSwipingToLoadMore(mStatus)) {
                mAutoScroller.autoScroll(-mFooterOffset, mLoadMoreCompleteToDefaultScrollingDuration);
            }
        }
    }

    /**
     * invoke when {@link AutoScroller#finish()}
     *
     * @param autoScrollAbort
     */
    private void autoScrollFinished(boolean autoScrollAbort) {
        if (mLoading) {
            if (STATUS.isRefreshing(mStatus) && !autoScrollAbort) {
                mRefreshCallback.onRefresh();
            } else if (STATUS.isLoadingMore(mStatus) && !autoScrollAbort) {
                mLoadMoreCallback.onLoadMore();
            }
        }
    }

    /**
     * TODO add gravity
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new SwipeToLoadLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new SwipeToLoadLayout.LayoutParams(p);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new SwipeToLoadLayout.LayoutParams(getContext(), attrs);
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
            mHeaderHeight = headerView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (mRefreshTriggerOffset < mHeaderHeight) {
                mRefreshTriggerOffset = mHeaderHeight;
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
            mFooterHeight = footerView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (mLoadMoreTriggerOffset < mFooterHeight) {
                mLoadMoreTriggerOffset = mFooterHeight;
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
            final int headerLeft = paddingLeft + lp.leftMargin;
            final int headerTop;
            switch (mStyle) {
                case STYLE.CLASSIC:
                    // classic
                    headerTop = paddingTop + lp.topMargin - mHeaderHeight + mHeaderOffset;
                    break;
                case STYLE.ABOVE:
                    // classic
                    headerTop = paddingTop + lp.topMargin - mHeaderHeight + mHeaderOffset;
                    break;
                case STYLE.BLEW:
                    // blew
                    headerTop = paddingTop + lp.topMargin;
                    break;
                case STYLE.SCALE:
                    // scale
                    headerTop = paddingTop + lp.topMargin - mHeaderHeight / 2 + mHeaderOffset / 2;
                    break;
                default:
                    // classic
                    headerTop = paddingTop + lp.topMargin - mHeaderHeight + mHeaderOffset;
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
            final int targetLeft = paddingLeft + lp.leftMargin;
            final int targetTop;

            switch (mStyle) {
                case STYLE.CLASSIC:
                    // classic
                    targetTop = paddingTop + lp.topMargin + mTargetOffset;
                    break;
                case STYLE.ABOVE:
                    // above
                    targetTop = paddingTop + lp.topMargin;
                    break;
                case STYLE.BLEW:
                    // classic
                    targetTop = paddingTop + lp.topMargin + mTargetOffset;
                    break;
                case STYLE.SCALE:
                    // classic
                    targetTop = paddingTop + lp.topMargin + mTargetOffset;
                    break;
                default:
                    // classic
                    targetTop = paddingTop + lp.topMargin + mTargetOffset;
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
            final int footerLeft = paddingLeft + lp.leftMargin;
            final int footerBottom;
            switch (mStyle) {
                case STYLE.CLASSIC:
                    // classic
                    footerBottom = height - paddingBottom - lp.bottomMargin + mFooterHeight + mFooterOffset;
                    break;
                case STYLE.ABOVE:
                    // classic
                    footerBottom = height - paddingBottom - lp.bottomMargin + mFooterHeight + mFooterOffset;
                    break;
                case STYLE.BLEW:
                    // blew
                    footerBottom = height - paddingBottom - lp.bottomMargin;
                    break;
                case STYLE.SCALE:
                    // scale
                    footerBottom = height - paddingBottom - lp.bottomMargin + mFooterHeight / 2 + mFooterOffset / 2;
                    break;
                default:
                    // classic
                    footerBottom = height - paddingBottom - lp.bottomMargin + mFooterHeight + mFooterOffset;
                    break;
            }
            final int footerTop = footerBottom - footerView.getMeasuredHeight();
            final int footerRight = footerLeft + footerView.getMeasuredWidth();

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

    /**
     * check if it can refresh
     *
     * @return
     */
    private boolean onCheckCanRefresh() {

        return mRefreshEnabled && !canChildScrollUp() && mHasHeaderView && mRefreshTriggerOffset > 0;
    }

    /**
     * check if it can load more
     *
     * @return
     */
    private boolean onCheckCanLoadMore() {

        return mLoadMoreEnabled && !canChildScrollDown() && mHasFooterView && mLoadMoreTriggerOffset > 0;
    }

    /**
     * copy from {@link android.support.v4.widget.SwipeRefreshLayout#canChildScrollUp()}
     *
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    protected boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTargetView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTargetView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTargetView, -1) || mTargetView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTargetView, -1);
        }
    }

    /**
     * Whether it is possible for the child view of this layout to
     * scroll down. Override this if the child view is a custom view.
     *
     * @return
     */
    protected boolean canChildScrollDown() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTargetView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTargetView;
                return absListView.getChildCount() > 0
                        && (absListView.getLastVisiblePosition() < absListView.getChildCount() - 1
                        || absListView.getChildAt(absListView.getChildCount() - 1).getBottom() > absListView.getPaddingBottom());
            } else {
                return ViewCompat.canScrollVertically(mTargetView, 1) || mTargetView.getScrollY() < 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTargetView, 1);
        }
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
                // if status is not ing status && not complete status
                // abort autoScrolling
                if (!(STATUS.isRefreshComplete(mStatus) || STATUS.isLoadMoreComplete(mStatus))
                        && !(STATUS.isRefreshing(mStatus) || STATUS.isLoadingMore(mStatus))) {
                    mAutoScroller.abortIfRunning();
                }
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                mInitDownY = getMotionEventY(event, mActivePointerId);
                mInitDownX = getMotionEventX(event, mActivePointerId);
                if (mInitDownY == INVALID_COORDINATE) {
                    return false;
                }
                mLastY = mInitDownY;
                mLastX = mInitDownX;
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
                boolean moved = Math.abs(yInitDiff) > Math.abs(xInitDiff);
                boolean triggerCondition = (yInitDiff > 0 && moved && onCheckCanRefresh())
                        || (yInitDiff < 0 && moved && onCheckCanLoadMore());
                if (triggerCondition) {
                    // the trigger condition refresh or load more is true
                    // intercept the move action event and pass it to SwipeToLoadLayout#onTouchEvent()
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP: {
                onSecondaryPointerUp(event);
                mLastY = getMotionEventY(event, mActivePointerId);
                mLastX = getMotionEventX(event, mActivePointerId);
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
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
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

                if (Math.abs(xDiff) > Math.abs(yDiff) && Math.abs(xDiff) > mTouchSlop) {
                    return false;
                }

                if (STATUS.isStatusDefault(mStatus)) {
                    if (yDiff > 0 && onCheckCanRefresh()) {
                        mRefreshCallback.onPrepare();
                        setStatus(STATUS.STATUS_SWIPING_TO_REFRESH);
                    } else if (yDiff < 0 && onCheckCanLoadMore()) {
                        mLoadMoreCallback.onPrepare();
                        setStatus(STATUS.STATUS_SWIPING_TO_LOAD_MORE);
                    }
                }

                if (STATUS.isSwipingToRefresh(mStatus)
                        || STATUS.isSwipingToLoadMore(mStatus)
                        || STATUS.isReleaseToRefresh(mStatus)
                        || STATUS.isReleaseToLoadMore(mStatus)) {
                    //refresh or loadMore
                    fingerScroll(yDiff);
                }
                return true;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);
                if (pointerId != INVALID_POINTER) {
                    mActivePointerId = pointerId;
                }
                mLastY = getMotionEventY(event, mActivePointerId);
                mLastX = getMotionEventX(event, mActivePointerId);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                onSecondaryPointerUp(event);
                mLastY = getMotionEventY(event, mActivePointerId);
                mLastX = getMotionEventX(event, mActivePointerId);
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
     * scrolling by physical touch with your fingers
     *
     * @param yDiff
     */
    private void fingerScroll(final float yDiff) {
        float ratio = DEFAULT_DRAG_RATIO;
        if (STATUS.isSwipingToRefresh(mStatus) || STATUS.isReleaseToRefresh(mStatus)) {
            if (yDiff > 0) {
                // swiping to refresh
                ratio = DEFAULT_DRAG_RATIO;
            } else if (yDiff < 0) {
                // refresh returning
                ratio = DEFAULT_DRAG_RATIO * 4;
            }
        } else if (STATUS.isSwipingToLoadMore(mStatus) || STATUS.isReleaseToLoadMore(mStatus)) {
            if (yDiff > 0) {
                // load more returning
                ratio = DEFAULT_DRAG_RATIO * 4;
            } else if (yDiff < 0) {
                // swiping to load more
                ratio = DEFAULT_DRAG_RATIO;
            }
        } else if (STATUS.isRefreshing(mStatus)) {
            if (yDiff < 0 && mTargetOffset <= mRefreshTriggerOffset) {
                // Refreshing + swipe up
                ratio = 0;
            } else if (yDiff > 0) {
                // Refreshing + swipe down
                ratio = DEFAULT_DRAG_RATIO;
            } else {
                ratio = DEFAULT_DRAG_RATIO * 4;
            }
        } else if (STATUS.isLoadingMore(mStatus)) {
            if (yDiff > 0 && -mTargetOffset <= mLoadMoreTriggerOffset) {
                // loading more + swipe down
                ratio = 0;
            } else if (yDiff < 0) {
                // swiping + swipe up
                ratio = DEFAULT_DRAG_RATIO;
            } else {
                ratio = DEFAULT_DRAG_RATIO * 4;
            }
        }

        float yScrolled = yDiff * ratio;

        // make sure (refresh -> default -> load more) or (load more -> default -> refresh)
        // forbidden fling jump default status (refresh -> load more)
        // I am so smart :)

        float tmpTargetOffset = yScrolled + mTargetOffset;
        if ((tmpTargetOffset > 0 && mTargetOffset < 0)
                || (yScrolled + mTargetOffset < 0 && mTargetOffset > 0)) {
            yScrolled = -mTargetOffset;
        }

        if (mRefreshFinalDragOffset >= mRefreshTriggerOffset
                && tmpTargetOffset > mRefreshFinalDragOffset) {
            yScrolled = mRefreshFinalDragOffset - mTargetOffset;
        } else if (mLoadMoreFinalDragOffset >= mLoadMoreTriggerOffset && -tmpTargetOffset > mLoadMoreFinalDragOffset) {
            yScrolled = -mLoadMoreFinalDragOffset - mTargetOffset;
        }
        updateScroll(yScrolled);
    }

    /**
     * Process the scrolling(auto or physical) and append the diff values to mTargetOffset
     * I think it's the most busy and core method. :) a ha ha ha ha...
     *
     * @param yScrolled
     */
    private void updateScroll(final float yScrolled) {
        if (yScrolled == 0) {
            return;
        }
        mTargetOffset += yScrolled;
        if (mTargetOffset > 0) {
            if (STATUS.isRefreshStatus(mStatus)) {
                mHeaderOffset = mTargetOffset;
                mFooterOffset = 0;
                if (mTargetOffset < mRefreshTriggerOffset) {
                    if (STATUS.isReleaseToRefresh(mStatus)) {
                        setStatus(STATUS.STATUS_SWIPING_TO_REFRESH);
                    }
                } else if (mTargetOffset >= mRefreshTriggerOffset) {
                    if (!STATUS.isRefreshing(mStatus)) {
                        setStatus(STATUS.STATUS_RELEASE_TO_REFRESH);
                    }
                }
            } else if (STATUS.isStatusDefault(mStatus)) {
                mHeaderOffset = mTargetOffset;
                mFooterOffset = 0;
            }
        } else if (mTargetOffset < 0) {
            if (STATUS.isLoadMoreStatus(mStatus)) {
                mFooterOffset = mTargetOffset;
                mHeaderOffset = 0;
                if (-mTargetOffset < mLoadMoreTriggerOffset) {
                    if (STATUS.isReleaseToLoadMore(mStatus)) {
                        setStatus(STATUS.STATUS_SWIPING_TO_LOAD_MORE);
                    }
                } else if (-mTargetOffset >= mLoadMoreTriggerOffset) {
                    if (!STATUS.isLoadingMore(mStatus)) {
                        setStatus(STATUS.STATUS_RELEASE_TO_LOAD_MORE);
                    }
                }
            } else if (STATUS.isStatusDefault(mStatus)) {
                mFooterOffset = mTargetOffset;
                mHeaderOffset = 0;
            }
        } else if (mTargetOffset == 0) {
            if (STATUS.isRefreshing(mStatus) && mLoading) {
                mTargetOffset = 1;
                mHeaderOffset = mTargetOffset;
                mFooterOffset = 0;
            } else if (STATUS.isLoadingMore(mStatus) && mLoading) {
                mTargetOffset = -1;
                mFooterOffset = mTargetOffset;
                mHeaderOffset = 0;
            } else {
                mLoading = false;
                mHeaderOffset = 0;
                mFooterOffset = 0;
                if (STATUS.isRefreshComplete(mStatus) || STATUS.isSwipingToRefresh(mStatus)
                        //FIX quick scrolling jump (swiping to refresh) directly lead mTargetOffset < 0 bug
                        // release to refresh(quick scroll) -> mTargetOffset ==0 (not change status to default) -> mTargetOffset<0
                        || STATUS.isReleaseToRefresh(mStatus)) {
                    setStatus(STATUS.STATUS_DEFAULT);
                    mRefreshCallback.onReset();
                } else if (STATUS.isLoadMoreComplete(mStatus) || STATUS.isSwipingToLoadMore(mStatus)
                        //FIX quick scrolling release to load more jump swiping to load more directly lead mTargetOffset > 0 bug
                        // scrolling release(quick scroll) -> mTargetOffset ==0 (not change status to default) -> mTargetOffset>0
                        || STATUS.isReleaseToLoadMore(mStatus)) {
                    setStatus(STATUS.STATUS_DEFAULT);
                    mLoadMoreCallback.onReset();
                }
            }
        }

        if (mTargetOffset > 0) {
            mRefreshCallback.onSwipe(mTargetOffset, STATUS.isRefreshComplete(mStatus));
        } else if (mTargetOffset < 0) {
            mLoadMoreCallback.onSwipe(mTargetOffset, STATUS.isLoadMoreComplete(mStatus));
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
            setRefreshing(false);
        } else if (STATUS.isSwipingToLoadMore(mStatus)) {
            // simply return
            setLoadingMore(false);
        } else if (STATUS.isReleaseToRefresh(mStatus)) {
            // return to header height and perform refresh
            mRefreshCallback.onRelease();
            setRefreshing(true);
        } else if (STATUS.isReleaseToLoadMore(mStatus)) {
            // return to footer height and perform loadMore
            mLoadMoreCallback.onRelease();
            setLoadingMore(true);
        }
    }

    /**
     * on not active finger up
     *
     * @param ev
     */
    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    private float getMotionEventY(MotionEvent event, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(event, activePointerId);
        if (index < 0) {
            return INVALID_COORDINATE;
        }
        return MotionEventCompat.getY(event, index);
    }

    private float getMotionEventX(MotionEvent event, int activePointId) {
        final int index = MotionEventCompat.findPointerIndex(event, activePointId);
        if (index < 0) {
            return INVALID_COORDINATE;
        }
        return MotionEventCompat.getX(event, index);
    }


    private class AutoScroller implements Runnable {

        private Scroller mScroller;

        private int mmLastY;

        private boolean mRunning = false;

        private boolean mAbort = false;

        public AutoScroller() {
            mScroller = new Scroller(getContext());
        }

        @Override
        public void run() {
            boolean finish = !mScroller.computeScrollOffset() || mScroller.isFinished();
            int currY = mScroller.getCurrY();
            int yDiff = currY - mmLastY;
            if (finish) {
                finish();
            } else {
                mmLastY = currY;
                updateScroll(yDiff);
                post(this);
            }
        }

        /**
         * remove the post callbacks and reset default values
         */
        private void finish() {
            mmLastY = 0;
            mRunning = false;
            removeCallbacks(this);
            autoScrollFinished(mAbort);
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
         * The param yScrolled here isn't final pos of y.
         * It's just like the yScrolled param in the
         * {@link #updateScroll(float yScrolled)}
         *
         * @param yScrolled
         * @param duration
         */
        private void autoScroll(int yScrolled, int duration) {
            removeCallbacks(this);
            mmLastY = 0;
            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }
            mScroller.startScroll(0, 0, 0, yScrolled, duration);
            post(this);
            mRunning = true;
        }
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
        public void onSwipe(int y, boolean isComplete) {
            if (mHeaderView != null && mHeaderView instanceof SwipeTrigger && STATUS.isRefreshStatus(mStatus)) {
                if (mHeaderView.getVisibility() != VISIBLE) {
                    mHeaderView.setVisibility(VISIBLE);
                }
                ((SwipeTrigger) mHeaderView).onSwipe(y, isComplete);
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
            if (mHeaderView != null && STATUS.isRefreshing(mStatus) && mLoading) {
                if (mHeaderView instanceof SwipeRefreshTrigger) {
                    ((SwipeRefreshTrigger) mHeaderView).onRefresh();
                }
                if (mRefreshListener != null) {
                    mRefreshListener.onRefresh();
                }
            }
        }

        @Override
        public void complete() {
            if (mHeaderView != null && mHeaderView instanceof SwipeTrigger) {
                ((SwipeTrigger) mHeaderView).complete();
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
        public void onSwipe(int y, boolean isComplete) {
            if (mFooterView != null && mFooterView instanceof SwipeTrigger && STATUS.isLoadMoreStatus(mStatus)) {
                if (mFooterView.getVisibility() != VISIBLE) {
                    mFooterView.setVisibility(VISIBLE);
                }
                ((SwipeTrigger) mFooterView).onSwipe(y, isComplete);
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
            if (mFooterView != null && STATUS.isLoadingMore(mStatus) && mLoading) {
                if (mFooterView instanceof SwipeLoadMoreTrigger) {
                    ((SwipeLoadMoreTrigger) mFooterView).onLoadMore();
                }
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore();
                }
            }
        }

        @Override
        public void complete() {
            if (mFooterView != null && mFooterView instanceof SwipeTrigger) {
                ((SwipeTrigger) mFooterView).complete();
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
        private static final int STATUS_REFRESH_RETURNING = -5;
        private static final int STATS_REFRESH_COMPLETE = -4;
        private static final int STATUS_REFRESHING = -3;
        private static final int STATUS_RELEASE_TO_REFRESH = -2;
        private static final int STATUS_SWIPING_TO_REFRESH = -1;
        private static final int STATUS_DEFAULT = 0;
        private static final int STATUS_SWIPING_TO_LOAD_MORE = 1;
        private static final int STATUS_RELEASE_TO_LOAD_MORE = 2;
        private static final int STATUS_LOADING_MORE = 3;
        private static final int STATUS_LOAD_MORE_COMPLETE = 4;
        private static final int STATUS_LOAD_MORE_RETURNING = 5;

        private static boolean isRefreshing(final int status) {
            return status == STATUS.STATUS_REFRESHING;
        }

        private static boolean isLoadingMore(final int status) {
            return status == STATUS.STATUS_LOADING_MORE;
        }

        private static boolean isRefreshComplete(final int status) {
            return status == STATS_REFRESH_COMPLETE;
        }

        private static boolean isLoadMoreComplete(final int status) {
            return status == STATUS_LOAD_MORE_COMPLETE;
        }

        @SuppressWarnings({"unused"})
        private static boolean isRefreshReturning(final int status) {
            return status == STATUS.STATUS_REFRESH_RETURNING;
        }

        @SuppressWarnings({"unused"})
        private static boolean isLoadMoreReturning(final int status) {
            return status == STATUS.STATUS_LOAD_MORE_RETURNING;
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
                case STATS_REFRESH_COMPLETE:
                    statusInfo = "stats_refresh_complete";
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
                case STATUS_LOAD_MORE_COMPLETE:
                    statusInfo = "status_load_more_complete";
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
            Log.d(TAG, "printStatus:" + getStatus(status));
        }
    }
}
