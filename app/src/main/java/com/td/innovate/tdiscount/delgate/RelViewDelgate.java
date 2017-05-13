package com.td.innovate.tdiscount.delgate;



import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class RelViewDelgate implements ViewDelegate {

    public boolean isViewBeingDragged(MotionEvent event, RelativeLayout view) {
        Log.i("ME", "ME");
        System.out.println(view.isInTouchMode());
        return false;
    }

    @Override public boolean isReadyForPull(View view, float x, float y) {
        return view.getScrollY() <= 0;
    }
}

