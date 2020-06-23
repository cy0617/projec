package com.yunbao.main.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.AbsMainViewHolder;
import com.yunbao.main.R;

public class MainShopViewHolder extends AbsMainHomeParentViewHolder {

    private AbsMainViewHolder[] mViewHolders;
    private MainShopChildViewHolder mShopChildViewHolder;

    public MainShopViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void init() {
        super.init();
        mViewHolders = new AbsMainViewHolder[1];
    }

    @Override
    protected void loadPageData(int position) {
        if (mViewHolders == null) {
            return;
        }
        AbsMainViewHolder vh = mViewHolders[position];
        if (vh==null){
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }
                if (position == 0) {
                    mShopChildViewHolder = new MainShopChildViewHolder(mContext, parent);
                    vh = mShopChildViewHolder;
                }
                mViewHolders[position] = vh;
                if (vh!=null){
                    vh.addToParent();
                    vh.subscribeActivityLifeCycle();
                }
            }
        }
        if (vh != null) {
            vh.loadData();
        }

    }

    @Override
    protected int getPageCount() {
        return 1;
    }

    @Override
    protected String[] getTitles() {
        return new String[]{
                WordUtil.getString(R.string.main_shop)
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_shop;
    }
}
