package com.yunbao.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.yunbao.common.Constants;
import com.yunbao.common.R;
import com.yunbao.common.adapter.ChooseVideoAdapter;
import com.yunbao.common.bean.ChooseImageBean;
import com.yunbao.common.bean.ChooseVideoBean;
import com.yunbao.common.custom.ItemDecoration;
import com.yunbao.common.dialog.VideoPreviewDialog;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.interfaces.VideoResultCallback;
import com.yunbao.common.utils.ChooseVideoUtil;
import com.yunbao.common.utils.ProcessImageUtil;
import com.yunbao.common.utils.RouteUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择视频
 */
public class ChooseVideoActivity extends AbsActivity implements ChooseVideoAdapter.ActionListener {

    private RecyclerView mRecyclerView;
    private ChooseVideoAdapter mAdapter;
    private ChooseVideoUtil mChooseVideoUtil;
//    private ProcessImageUtil mImageUtil;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_video;
    }

    @Override
    protected void main() {
//        mImageUtil = new ProcessImageUtil(this);
//        mImageUtil.setVideoResultCallback(new VideoResultCallback() {
//            @Override
//            public void onSuccess(File file) {
//                Intent intent = new Intent();
//                intent.putExtra(Constants.CHOOSE_VIDEO, file.getAbsolutePath());
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        });
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 5, 5);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRecyclerView.addItemDecoration(decoration);
        mChooseVideoUtil = new ChooseVideoUtil();
        mChooseVideoUtil.getLocalVideoList(new CommonCallback<List<ChooseVideoBean>>() {
            @Override
            public void callback(List<ChooseVideoBean> videoList) {
                if (mContext != null && mRecyclerView != null) {
                    List<ChooseVideoBean> list = new ArrayList<>();
                    list.add(new ChooseVideoBean(ChooseImageBean.CAMERA));
                    if (videoList != null && videoList.size() > 0) {
                        list.addAll(videoList);
                    }
                    mAdapter = new ChooseVideoAdapter(mContext, list);
                    mAdapter.setActionListener(ChooseVideoActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    @Override
    public void onCameraClick() {
//        if (mImageUtil != null) {
//            mImageUtil.getVideoRecord();
//        }
        RouteUtil.videoRecord(this, 100);
    }

    @Override
    public void onVideoItemClick(ChooseVideoBean bean) {
        VideoPreviewDialog dialog = new VideoPreviewDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.VIDEO_PATH, bean.getVideoFile().getAbsolutePath());
        bundle.putLong(Constants.VIDEO_DURATION, bean.getDuration());
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "VideoPreviewDialog");
    }

    /**
     * 使用视频
     */
    public void useVideo(String videoPath, long duration) {
        Intent intent = new Intent();
        intent.putExtra(Constants.CHOOSE_VIDEO, videoPath);
        intent.putExtra(Constants.VIDEO_DURATION, duration);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        if (mChooseVideoUtil != null) {
            mChooseVideoUtil.release();
        }
        mChooseVideoUtil = null;
//        if (mImageUtil != null) {
//            mImageUtil.release();
//        }
//        mImageUtil = null;
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                String path = data.getStringExtra(Constants.VIDEO_PATH);
                long duration = data.getIntExtra(Constants.VIDEO_DURATION, 0);
                if (!TextUtils.isEmpty(path)) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.CHOOSE_VIDEO, path);
                    intent.putExtra(Constants.VIDEO_DURATION, duration);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }
    }
}
