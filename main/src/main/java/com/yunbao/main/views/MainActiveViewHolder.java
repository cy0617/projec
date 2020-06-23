package com.yunbao.main.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yunbao.common.utils.L;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;

/**
 * Created by cxf on 2018/9/22.
 * 首页 动态
 */

public class MainActiveViewHolder extends AbsMainHomeParentViewHolder {

    private AbsMainActiveViewHolder[] mActiveViewHolders;
    private MainActiveRecommendViewHolder mRecommendViewHolder;
    private MainActiveFollowViewHolder mFollowViewHolder;
    private MainActiveNewestViewHolder mNewsetViewHolder;

    public MainActiveViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected boolean isShowIndicatorLine() {
        return false;
    }

    @Override
    protected boolean isOneLevelIndicator() {
        return false;
    }

    @Override
    public void init() {
        mActiveViewHolders = new AbsMainActiveViewHolder[3];
        super.init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_active;
    }

    @Override
    protected void loadPageData(int position) {
        if (mActiveViewHolders == null) {
            return;
        }
        stopActiveVoice();
        AbsMainActiveViewHolder vh = mActiveViewHolders[position];
        if (vh == null) {
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }
                if (position == 0) {
                    mRecommendViewHolder = new MainActiveRecommendViewHolder(mContext, parent);
                    vh = mRecommendViewHolder;
                } else if (position == 1) {
                    mFollowViewHolder = new MainActiveFollowViewHolder(mContext, parent);
                    vh = mFollowViewHolder;
                } else if (position == 2) {
                    mNewsetViewHolder = new MainActiveNewestViewHolder(mContext, parent);
                    vh = mNewsetViewHolder;
                }
                if (vh == null) {
                    return;
                }
                mActiveViewHolders[position] = vh;
                vh.addToParent();
                vh.subscribeActivityLifeCycle();
            }
        }
        if (vh != null) {
            vh.loadData();
        }
    }

    @Override
    protected int getPageCount() {
        return 3;
    }

    @Override
    protected String[] getTitles() {
        return new String[]{
                WordUtil.getString(R.string.recommend),
                WordUtil.getString(R.string.follow),
                WordUtil.getString(R.string.newest)
        };
    }

    @Override
    public void loadData() {
        loadPageData(0);
    }


    @Override
    public void setShowed(boolean showed) {
        super.setShowed(showed);
//        L.e("MainActiveViewHolder----showed-----> "+showed);
        stopActiveVoice();
    }


    /**
     * 停止播放动态声音
     */
    private void stopActiveVoice() {
        if (mActiveViewHolders != null) {
            for (AbsMainActiveViewHolder vh : mActiveViewHolders) {
                if (vh != null) {
                    vh.stopActiveVoice();
                }
            }
        }
    }

}
