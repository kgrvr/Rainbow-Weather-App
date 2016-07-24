package com.itskush.kushal.sunshine;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created by Kush on 17-07-2016.
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {

    CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

//    @Override
//    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
//                                       int position) {
//        RecyclerView.SmoothScroller smoothScroller = new TopSnappedSmoothScroller(recyclerView.getContext());
//        smoothScroller.setTargetPosition(position);
//        startSmoothScroll(smoothScroller);
//    }

    private class TopSnappedSmoothScroller extends LinearSmoothScroller {
        public TopSnappedSmoothScroller(Context context) {
            super(context);

        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return CustomLinearLayoutManager.this
                    .computeScrollVectorForPosition(targetPosition);
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        final LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    private static final float MILLISECONDS_PER_INCH = 100f;

                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return CustomLinearLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    @Override
                    protected float calculateSpeedPerPixel
                            (DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                    }
                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

//    @Override
//    public int scrollVerticallyBy(int delta, RecyclerView.Recycler recycler, RecyclerView.State state)
//    {
//        // write your limiting logic here to prevent the delta from exceeding the limits of your list.
//
//        int prevDelta = delta;
//        if (getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING)
//            delta = (int)(delta > 0 ? Math.max(delta * MANUAL_SCROLL_SLOW_RATIO, 1) : Math.min(delta * MANUAL_SCROLL_SLOW_RATIO, -1));
//
//        // MANUAL_SCROLL_SLOW_RATIO is between 0 (no manual scrolling) to 1 (normal speed) or more (faster speed).
//        // write your scrolling logic code here whereby you move each view by the given delta
//
//        if (getScrollState() == SCROLL_STATE_DRAGGING)
//            delta = prevDelta;
//
//        return delta;
//    }

}
