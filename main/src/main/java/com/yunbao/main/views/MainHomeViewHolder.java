package com.yunbao.main.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.AbsMainViewHolder;
import com.yunbao.main.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

/**
 * Created by cxf on 2018/9/22.
 * MainActivity 首页
 */

public class MainHomeViewHolder extends AbsMainHomeParentViewHolder {

    private MainHomeFollowViewHolder mFollowViewHolder;
    private MainHomeLiveViewHolder mLiveViewHolder;
    private MainHomeVideoViewHolder mVideoViewHolder;
    private MainHomeNearViewHolder mNearViewHolder;
    private MainActiveViewHolder mActiveViewHolder;
    private View[] mIcons;
    private boolean mViewLoaded;

    public MainHomeViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_home;
    }

    @Override
    public void init() {
        super.init();
        mIcons = new View[5];
        mIcons[0] = findViewById(R.id.icon_home_top_follow);
        mIcons[1] = findViewById(R.id.icon_home_top_live);
        mIcons[2] = findViewById(R.id.icon_home_top_video);
        mIcons[3] = findViewById(R.id.icon_home_top_near);
        mIcons[4] = findViewById(R.id.icon_home_top_active);
        mIndicator.post(new Runnable() {
            @Override
            public void run() {
                mViewLoaded = true;
                CommonNavigator commonNavigator = (CommonNavigator) mIndicator.getNavigator();
                View titleView0 = (View) commonNavigator.getPagerTitleView(0);
                View titleView1 = (View) commonNavigator.getPagerTitleView(1);
                View titleView2 = (View) commonNavigator.getPagerTitleView(2);
                View titleView3 = (View) commonNavigator.getPagerTitleView(3);
                View titleView4 = (View) commonNavigator.getPagerTitleView(4);
                int[] location = new int[2];
                titleView0.getLocationOnScreen(location);
                mIcons[0].setTranslationX(location[0] + titleView0.getWidth() - DpUtil.dp2px(17));
                titleView1.getLocationOnScreen(location);
                mIcons[1].setTranslationX(location[0] + titleView1.getWidth() - DpUtil.dp2px(18));
                titleView2.getLocationOnScreen(location);
                mIcons[2].setTranslationX(location[0] + titleView2.getWidth() - DpUtil.dp2px(16));
                titleView3.getLocationOnScreen(location);
                mIcons[3].setTranslationX(location[0] + titleView3.getWidth() - DpUtil.dp2px(16));
                titleView4.getLocationOnScreen(location);
                mIcons[4].setTranslationX(location[0] + titleView4.getWidth() - DpUtil.dp2px(18));
                if (mViewPager != null) {
                    mIcons[mViewPager.getCurrentItem()].setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void loadPageData(int position) {
        if (mViewHolders == null) {
            return;
        }
        AbsMainViewHolder vh = mViewHolders[position];
        if (vh == null) {
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }
                if (position == 0) {
                    mVideoViewHolder = new MainHomeVideoViewHolder(mContext, parent);
                    vh = mVideoViewHolder;

                } else if (position == 1) {
                    mFollowViewHolder = new MainHomeFollowViewHolder(mContext, parent);
                    vh = mFollowViewHolder;
                } else if (position == 2) {
                    mLiveViewHolder = new MainHomeLiveViewHolder(mContext, parent);
                    vh = mLiveViewHolder;
                } else if (position == 3) {
                    mNearViewHolder = new MainHomeNearViewHolder(mContext, parent);
                    vh = mNearViewHolder;
                }else if (position==4){
                    mActiveViewHolder=new MainActiveViewHolder(mContext,parent);
                    vh = mActiveViewHolder;
                }
                if (vh == null) {
                    return;
                }
                mViewHolders[position] = vh;
                vh.addToParent();
                vh.subscribeActivityLifeCycle();
            }
        }
        if (vh != null) {
            vh.loadData();
        }
        if (mViewLoaded && mIcons != null) {
            for (int i = 0, len = mIcons.length; i < len; i++) {
                View v = mIcons[i];
                if (v != null) {
                    if (i == position) {
                        if (v.getVisibility() != View.VISIBLE) {
                            v.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (v.getVisibility() == View.VISIBLE) {
                            v.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected int getPageCount() {
        return 5;
    }

    @Override
    protected String[] getTitles() {
        return new String[]{
                WordUtil.getString(R.string.video),
                WordUtil.getString(R.string.follow),
                WordUtil.getString(R.string.live),
                WordUtil.getString(R.string.same_city),
                WordUtil.getString(R.string.main_active)
        };
    }


}
