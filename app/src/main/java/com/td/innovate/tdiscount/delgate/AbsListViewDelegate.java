package com.td.innovate.tdiscount.delgate;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

public class AbsListViewDelegate implements ViewDelegate {

    private final int[] mViewLocationResult = new int[2];
    private final int[] mViewLocationResult2 = new int[2];
    private final Rect mRect2 = new Rect();
    private final Rect mRect = new Rect();

    public boolean isViewBeingDragged(MotionEvent event, AbsListView view, RelativeLayout ... rl) {
        if (view.getAdapter() == null || view.getAdapter().isEmpty()) {
            return true;
        }
        view.getLocationOnScreen(mViewLocationResult);
        final int viewLeft = mViewLocationResult[0], viewTop = mViewLocationResult[1];
        mRect.set(viewLeft, viewTop, viewLeft + view.getWidth(), viewTop + view.getHeight());
        final int rawX = (int) event.getRawX(), rawY = (int) event.getRawY();

        if (mRect.contains(rawX, rawY)) {
            return isReadyForPull(view, rawX - mRect.left, rawY - mRect.top);
        }
       if (rawY > viewTop + view.getHeight()){
            return true;
        }
        if (rl.length == 1){

            rl[0].getLocationOnScreen(mViewLocationResult2);
            final int viewLeft2 = mViewLocationResult2[0], viewTop2 = mViewLocationResult2[1];
            mRect2.set(viewLeft2, viewTop2, viewLeft2 + rl[0].getWidth(), viewTop2 + rl[0].getHeight());
            if (mRect2.contains(rawX, rawY)) {
                return true;
            }
            return false;
        }


        return false;
    }

    @Override
    public boolean isReadyForPull(View view, final float x, final float y) {
        boolean ready = false;

        // First we check whether we're scrolled to the top
        AbsListView absListView = (AbsListView) view;
        if (absListView.getCount() == 0) {
            ready = true;
        } else if (absListView.getFirstVisiblePosition() == 0) {
            final View firstVisibleChild = absListView.getChildAt(0);
            ready = firstVisibleChild != null
                    && firstVisibleChild.getTop() >= absListView.getPaddingTop();
        }

        return ready;
    }
}