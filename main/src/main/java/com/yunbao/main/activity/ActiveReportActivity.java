package com.yunbao.main.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.KeyBoardHeightChangeListener;
import com.yunbao.common.utils.KeyBoardHeightUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.video.R;
import com.yunbao.video.adapter.VideoReportAdapter;
import com.yunbao.video.bean.VideoReportBean;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cxf on 2018/12/15.
 * 动态举报
 */

public class ActiveReportActivity extends AbsActivity implements VideoReportAdapter.ActionListener, KeyBoardHeightChangeListener {

    public static void forward(Context context, String videoId) {
        Intent intent = new Intent(context, ActiveReportActivity.class);
        intent.putExtra(Constants.VIDEO_ID, videoId);
        context.startActivity(intent);
    }

    private String mActiveId;
    private RecyclerView mRecyclerView;
    private VideoReportAdapter mAdapter;
    private KeyBoardHeightUtil mKeyBoardHeightUtil;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_report;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.report));
        mActiveId = getIntent().getStringExtra(Constants.VIDEO_ID);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mKeyBoardHeightUtil = new KeyBoardHeightUtil(mContext, findViewById(android.R.id.content), this);
        MainHttpUtil.getActiveReportList(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    List<VideoReportBean> list = JSON.parseArray(Arrays.toString(info), VideoReportBean.class);
                    mAdapter = new VideoReportAdapter(mContext, list);
                    mAdapter.setActionListener(ActiveReportActivity.this);
                    if (mRecyclerView != null) {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    if (mKeyBoardHeightUtil != null) {
                        mKeyBoardHeightUtil.start();
                    }
                }
            }
        });
    }

    @Override
    public void onReportClick(VideoReportBean bean, String text) {
        if (TextUtils.isEmpty(mActiveId)) {
            return;
        }
        if (bean == null) {
            ToastUtil.show(R.string.video_report_tip_3);
            return;
        }
        String content = bean.getName();
        if (!TextUtils.isEmpty(text)) {
            content += " " + text;
        }
        MainHttpUtil.activeReport(mActiveId, content, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    ToastUtil.show(R.string.video_report_tip_4);
                    finish();
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }


    @Override
    public void onKeyBoardHeightChanged(int visibleHeight, int keyboardHeight) {
        if (mRecyclerView != null) {
            mRecyclerView.setTranslationY(-keyboardHeight);
        }
        if (keyboardHeight > 0) {
            if (mAdapter != null && mAdapter.getItemCount() >= 9) {
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            }
        }
    }

    @Override
    public boolean isSoftInputShowed() {
        return false;
    }


    private void release() {
        MainHttpUtil.cancel(MainHttpConsts.GET_ACTIVE_REPORT_LIST);
        MainHttpUtil.cancel(MainHttpConsts.ACTIVE_REPORT);
        if (mKeyBoardHeightUtil != null) {
            mKeyBoardHeightUtil.release();
        }
        mKeyBoardHeightUtil = null;
        if (mAdapter != null) {
            mAdapter.setActionListener(null);
        }
        mAdapter = null;
    }

    @Override
    public void onBackPressed() {
        release();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }
}
