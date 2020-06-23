package com.yunbao.beauty.ui.views.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.yunbao.beauty.R;

public class ScaleImageButton extends android.support.v7.widget.AppCompatImageButton {
    public ScaleImageButton(Context context) {
        super(context);
    }

    public ScaleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                beginScale(R.anim.button_zoom_in);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                beginScale(R.anim.button_zoom_out);
                break;
            case MotionEvent.ACTION_CANCEL:
                beginScale(R.anim.button_zoom_in);
                break;
        }
        return super.onTouchEvent(event);
    }

    private synchronized void beginScale(int animation) {
        Animation an = AnimationUtils.loadAnimation(getContext(), animation);
        an.setDuration(80);
        an.setFillAfter(true);
        this.startAnimation(an);
    }
}
