package com.yunbao.main.activity;

import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.custom.ItemDecoration;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.JsonUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.VideoHomeAdapter;
import com.yunbao.main.views.VideoHomeViewHolder;
import com.yunbao.video.activity.VideoPlayActivity;
import com.yunbao.video.bean.VideoBean;
import com.yunbao.video.event.VideoDeleteEvent;
import com.yunbao.video.event.VideoScrollPageEvent;
import com.yunbao.video.http.VideoHttpConsts;
import com.yunbao.video.http.VideoHttpUtil;
import com.yunbao.video.interfaces.VideoScrollDataHelper;
import com.yunbao.video.utils.VideoStorge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

/**
 * 我的点赞
 */
public class MyLikeVideoActivity extends AbsActivity implements OnItemClickListener<VideoBean> {
    private CommonRefreshView mRefreshView;
    private VideoHomeAdapter mAdapter;
    private VideoScrollDataHelper mVideoScrollDataHelper;
    private VideoHomeViewHolder.ActionListener mActionListener;
    private String mKey= Constants.LIKE_VIDEO;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_like_video;
    }

    @Override
    protected void main() {
        super.main();
        setTitle(WordUtil.getString(R.string.me_my_likes));
        mRefreshView = (CommonRefreshView) findViewById(R.id.refreshView);
        mRefreshView.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 2, 0);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<VideoBean>() {
            @Override
            public RefreshAdapter<VideoBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new VideoHomeAdapter(mContext);
                    mAdapter.setOnItemClickListener(MyLikeVideoActivity.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                VideoHttpUtil.getLikeVideos( p, callback);
            }

            @Override
            public List<VideoBean> processData(String[] info) {
                return JsonUtil.getJsonToList(Arrays.toString(info),VideoBean.class);
            }
            @Override
            public void onRefreshSuccess(List<VideoBean> list, int listCount) {
                VideoStorge.getInstance().put(mKey, list);
            }
            @Override
            public void onRefreshFailure() {

            }
            @Override
            public void onLoadMoreSuccess(List<VideoBean> loadItemList, int loadItemCount) {

            }
            @Override
            public void onLoadMoreFailure() {

            }
        });

        mVideoScrollDataHelper = new VideoScrollDataHelper() {

            @Override
            public void loadData(int p, HttpCallback callback) {
                VideoHttpUtil.getLikeVideos( p, callback);
            }
        };
        mRefreshView.initData();
        EventBus.getDefault().register(this);
    }

    public void release() {
        mVideoScrollDataHelper = null;
        mActionListener = null;
        VideoHttpUtil.cancel(VideoHttpConsts.GET_LIKE_VIDEO);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoScrollPageEvent(VideoScrollPageEvent e) {
        if (!TextUtils.isEmpty(mKey) && mKey.equals(e.getKey()) && mRefreshView != null) {
            mRefreshView.setPageCount(e.getPage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoDeleteEvent(VideoDeleteEvent e) {
        if (mAdapter != null) {
            mAdapter.deleteVideo(e.getVideoId());
            if (mAdapter.getItemCount() == 0 && mRefreshView != null) {
                mRefreshView.showEmpty();
            }
        }
        if (mActionListener != null) {
            mActionListener.onVideoDelete(1);
        }
    }

    @Override
    public void onItemClick(VideoBean bean, int position) {
        int page = 1;
        if (mRefreshView != null) {
            page = mRefreshView.getPageCount();
        }
        VideoStorge.getInstance().putDataHelper(mKey, mVideoScrollDataHelper);
        VideoPlayActivity.forward(mContext, position, mKey, page);
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }
}
