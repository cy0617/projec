package com.yunbao.main.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.AbsMainViewHolder;
import com.yunbao.main.R;

/**
 * 主页消息列表
 */
public class MainMessageViewHolder extends AbsMainHomeParentViewHolder implements View.OnClickListener {
    private AbsMainViewHolder[] mViewHolders;
    private MainHomeMessageChildViewHolder mMessageChildViewHolder;
    public MainMessageViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void init() {
        super.init();
        mViewHolders=new AbsMainViewHolder[1];
        findViewById(R.id.btn_ignore).setOnClickListener(this);
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
                    mMessageChildViewHolder = new MainHomeMessageChildViewHolder(mContext, parent);
                    vh = mMessageChildViewHolder;
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
    }

    @Override
    protected int getPageCount() {
        return 1;
    }

    @Override
    protected String[] getTitles() {
        return new String[]{
                WordUtil.getString(R.string.main_mi_chat)
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_message;
    }

    @Override
    public void onClick(View v) {
        int i=v.getId();
        if (i == com.yunbao.im.R.id.btn_ignore) {
            if (mMessageChildViewHolder!=null){
                mMessageChildViewHolder.ignoreUnReadCount();
            }
        }
    }
}
