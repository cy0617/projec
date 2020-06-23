package com.yunbao.beauty.ui.views.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ContentViewPager extends ViewPager {

    private HashMap<Integer, View> mChildrenViews = new LinkedHashMap<>();

    public ContentViewPager(Context context) {
        super(context);
    }

    public ContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int index = getCurrentItem();
        int height = 0;
        if (mChildrenViews.size() > index) {
            View child = mChildrenViews.get(index);
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                height = child.getMeasuredHeight();
            }
        }
        if (mChildrenViews.size() != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 保存View对应的索引,需要自适应高度的一定要设置这个
     */
    public void setViewForPosition(View view, int position) {
        mChildrenViews.put(position, view);
    }
}
