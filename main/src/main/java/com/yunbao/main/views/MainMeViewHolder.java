package com.yunbao.main.views;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.HtmlConfig;
import com.yunbao.common.activity.WebViewActivity;
import com.yunbao.common.bean.LevelBean;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.bean.UserItemBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.CommonIconUtil;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.AbsMainViewHolder;
import com.yunbao.im.activity.ChatActivity;
import com.yunbao.live.activity.LiveRecordActivity;
import com.yunbao.live.activity.RoomManageActivity;
import com.yunbao.main.R;
import com.yunbao.main.activity.AuthWebViewActivity;
import com.yunbao.main.activity.BlankActivity;
import com.yunbao.main.activity.EditProfileActivity;
import com.yunbao.main.activity.FansActivity;
import com.yunbao.main.activity.FollowActivity;
import com.yunbao.main.activity.MyActiveActivity;
import com.yunbao.main.activity.MyLikeVideoActivity;
import com.yunbao.main.activity.MyProfitActivity;
import com.yunbao.main.activity.MyVideoActivity;
import com.yunbao.main.activity.RealNameViewActivity;
import com.yunbao.main.activity.SettingActivity;
import com.yunbao.main.activity.ThreeDistributActivity;
import com.yunbao.main.adapter.MainMeAdapter;
import com.yunbao.main.adapter.MainMeAdapter1;
import com.yunbao.main.dialog.MeLikedDialog;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.mall.activity.PayContentActivity1;
import com.yunbao.mall.activity.PayContentActivity2;

import java.util.List;

/**
 * Created by cxf on 2018/9/22.
 * 我的
 */

public class MainMeViewHolder extends AbsMainViewHolder implements OnItemClickListener<UserItemBean>, View.OnClickListener {

    private AppBarLayout mAppBarLayout;
    private TextView mTtileView;
    private ImageView mAvatar;
    private TextView mName;
    //    private ImageView mSex;
//    private ImageView mLevelAnchor;
//    private ImageView mLevel;
    private TextView mID;
    private TextView mFollow;
    private TextView mFans;
    private boolean mPaused;
    private RecyclerView mRecyclerView;
    private MainMeAdapter1 mAdapter;
    private TextView mBtnLiked;
    private TextView mTvWorks;
    private TextView mTvDynamics;
    private TextView mTvLikes;
    private UserBean mUserBean;
    private GridLayoutManager mGridLayoutManager;



    public MainMeViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_me;
    }

    @Override
    public void init() {

        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float totalScrollRange = appBarLayout.getTotalScrollRange();
                float rate = -1 * verticalOffset / totalScrollRange * 2;
                if (rate >= 1) {
                    rate = 1;
                }
                if (mTtileView != null) {
                    mTtileView.setAlpha(rate);
                }
            }
        });


        mTtileView = (TextView) findViewById(R.id.titleView);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mName = (TextView) findViewById(R.id.name);
//        mSex = (ImageView) findViewById(R.id.sex);
//        mLevelAnchor = (ImageView) findViewById(R.id.level_anchor);
//        mLevel = (ImageView) findViewById(R.id.level);
        mID = (TextView) findViewById(R.id.id_val);
        mFollow = (TextView) findViewById(R.id.btn_follow);
        mFans = (TextView) findViewById(R.id.btn_fans);
        mBtnLiked = (TextView) findViewById(R.id.btn_liked);
        mTvWorks = findViewById(R.id.tv_works);
        mTvDynamics = findViewById(R.id.tv_dynamics);
        mTvLikes = findViewById(R.id.tv_likes);
        mFollow.setOnClickListener(this);
        mFans.setOnClickListener(this);
        mBtnLiked.setOnClickListener(this);
        findViewById(R.id.btn_edit).setOnClickListener(this);
        findViewById(R.id.btn_msg).setOnClickListener(this);
        findViewById(R.id.btn_wallet).setOnClickListener(this);
        findViewById(R.id.btn_detail).setOnClickListener(this);
        findViewById(R.id.btn_shop).setOnClickListener(this);
        findViewById(R.id.btn_setting).setOnClickListener(this);
        findViewById(R.id.btn_works).setOnClickListener(this);
        findViewById(R.id.btn_dynamic).setOnClickListener(this);
        findViewById(R.id.btn_like).setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        mPaused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isShowed() && mPaused) {
            loadData();
        }
        mPaused = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainHttpUtil.cancel(MainHttpConsts.GET_BASE_INFO);
    }

    @Override
    public void loadData() {
        if (isFirstLoadData()) {
            CommonAppConfig appConfig = CommonAppConfig.getInstance();
            UserBean u = appConfig.getUserBean();
            List<UserItemBean> list = appConfig.getUserItemList();
            if (u != null && list != null) {
                mUserBean = u;
                showData(u, list);
            }
        }
        MainHttpUtil.getBaseInfo(mCallback);
    }

    private CommonCallback<UserBean> mCallback = new CommonCallback<UserBean>() {
        @Override
        public void callback(UserBean bean) {
            List<UserItemBean> list = CommonAppConfig.getInstance().getUserItemList();
            if (bean != null) {
                mUserBean = bean;
                showData(bean, list);
            }
        }
    };

    private void showData(UserBean u, final List<UserItemBean> list) {
        ImgLoader.displayAvatar(mContext, u.getAvatar(), mAvatar);
        mTtileView.setText(u.getUserNiceName());
        mName.setText(u.getUserNiceName());
//        mSex.setImageResource(CommonIconUtil.getSexIcon(u.getSex()));
//        CommonAppConfig appConfig = CommonAppConfig.getInstance();
//        LevelBean anchorLevelBean = appConfig.getAnchorLevel(u.getLevelAnchor());
//        if (anchorLevelBean != null) {
//            ImgLoader.display(mContext, anchorLevelBean.getThumb(), mLevelAnchor);
//        }
//        final LevelBean levelBean = appConfig.getLevel(u.getLevel());
//        if (levelBean != null) {
//            ImgLoader.display(mContext, levelBean.getThumb(), mLevel);
//        }
        mID.setText(u.getLiangNameTip());
        mFollow.setText(StringUtil.contact(WordUtil.getString(R.string.follow), " ", StringUtil.toWan(u.getFollows())));
        mFans.setText(StringUtil.contact(WordUtil.getString(R.string.fans), " ", StringUtil.toWan(u.getFans())));
        mBtnLiked.setText(StringUtil.contact(WordUtil.getString(R.string.me_liked), " ", StringUtil.toWan(u.getLikes())));
        mTvWorks.setText(StringUtil.toWan(u.getVideos()));
        mTvDynamics.setText(StringUtil.toWan(u.getDynamics()));
        mTvLikes.setText(StringUtil.toWan(u.getLoves()));
        if (list != null && list.size() > 0) {
            if (mGridLayoutManager == null) {
                mGridLayoutManager = new GridLayoutManager(mContext, 12, GridLayoutManager.VERTICAL, false);
                mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        L.e("position=" + position);
                        if (list.get(position).isGroupTop()) {
                            return 12;
                        } else {
                            if (list.get(position).isMore()) {
                                return 3;
                            }
                        }
                        return 4;
                    }
                });
                mRecyclerView.setLayoutManager(mGridLayoutManager);
            }
            if (mAdapter == null) {
                mAdapter = new MainMeAdapter1(mContext, list);
                mAdapter.setOnItemClickListener(this);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setList(list);
            }
        }
    }


    @Override
    public void onItemClick(UserItemBean bean, int position) {
        if (bean.getId() == 22) {//我的小店
            forwardMall();
            return;
        } else if (bean.getId() == 24) {//付费内容
            forwardPayContent();
            return;
        }
        String url = bean.getHref();
        if (TextUtils.isEmpty(url)) {
            switch (bean.getId()) {
                case 1:
                    forwardProfit();
                    break;
                case 2:
                    forwardCoin();
                    break;
                case 4:
                    RouteUtil.forwardMyCoin(mContext);
                    break;
                case 6:
                    //作品推广
                    break;
                case 13:
                    forwardSetting();
                    break;
                case 19:
                    forwardMyVideo();
                    break;
                case 20:
                    forwardRoomManage();
                    break;
                case 23://我的动态
                    mContext.startActivity(new Intent(mContext, MyActiveActivity.class));
                    break;
            }
        } else {
            if (!url.contains("?")) {
                url = StringUtil.contact(url, "?");
            }
            if (bean.getId() == 8) {//三级分销
                ThreeDistributActivity.forward(mContext, bean.getName(), url);
            } else if (bean.getId() == 11) {
                RealNameViewActivity.forward(mContext, url);
            } else {
                WebViewActivity.forward(mContext, url);
            }
        }
    }

    /**
     * 我的小店 商城
     */
    private void forwardMall() {
        UserBean u = CommonAppConfig.getInstance().getUserBean();
        if (u != null) {
            if (u.getIsOpenShop() == 0) {
                RouteUtil.forward(RouteUtil.PATH_MALL_BUYER);
            } else {
                RouteUtil.forward(RouteUtil.PATH_MALL_SELLER);
            }
        }
    }


    /**
     * 付费内容
     */
    private void forwardPayContent() {
        UserBean u = CommonAppConfig.getInstance().getUserBean();
        if (u != null) {
            if (u.getIsOpenPayContent() == 0) {
                PayContentActivity1.forward(mContext);
            } else {
                PayContentActivity2.forward(mContext);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_edit) {
            RouteUtil.forwardUserHome(mContext, CommonAppConfig.getInstance().getUid());
        } else if (i == R.id.btn_follow) {
            forwardFollow();

        } else if (i == R.id.btn_fans) {
            forwardFans();

        } else if (i == R.id.btn_msg) {
            ChatActivity.forward(mContext);
        } else if (i == R.id.btn_detail) {
            WebViewActivity.forward(mContext, HtmlConfig.DETAIL);
        } else if (i == R.id.btn_shop) {
            WebViewActivity.forward(mContext, HtmlConfig.SHOP);
        } else if (i == R.id.btn_setting ) {
            mContext.startActivity(new Intent(mContext, BlankActivity.class));
        } else if (i == R.id.btn_liked) {
            showLikedDialog();
        } else if (i == R.id.btn_works) {
            forwardMyVideo();
        } else if (i == R.id.btn_dynamic) {
            mContext.startActivity(new Intent(mContext, MyActiveActivity.class));//我的动态
        } else if (i == R.id.btn_like) {
            mContext.startActivity(new Intent(mContext, MyLikeVideoActivity.class));//我的点赞
        }
    }


    /**
     * 被赞弹窗
     */
    private void showLikedDialog() {
        if (mUserBean == null) {
            return;
        }
        MeLikedDialog.showLikedDialog(mContext, String.format(WordUtil.getString(R.string.me_liked_num_tip), mUserBean.getUserNiceName(), mUserBean.getLikes())).show();
    }


    /**
     * 我的关注
     */
    private void forwardFollow() {
        FollowActivity.forward(mContext, CommonAppConfig.getInstance().getUid());
    }

    /**
     * 我的粉丝
     */
    private void forwardFans() {
        FansActivity.forward(mContext, CommonAppConfig.getInstance().getUid());
    }

    /**
     * 直播记录
     */
    private void forwardLiveRecord() {
        LiveRecordActivity.forward(mContext, CommonAppConfig.getInstance().getUserBean());
    }

    /**
     * 我的收益
     */
    private void forwardProfit() {
        mContext.startActivity(new Intent(mContext, MyProfitActivity.class));
    }

    /**
     * 我的钻石
     */
    private void forwardCoin() {
        RouteUtil.forwardMyCoin(mContext);
    }

    /**
     * 设置
     */
    private void forwardSetting() {
        mContext.startActivity(new Intent(mContext, SettingActivity.class));
    }

    /**
     * 我的视频
     */
    private void forwardMyVideo() {
        mContext.startActivity(new Intent(mContext, MyVideoActivity.class));
    }

    /**
     * 房间管理
     */
    private void forwardRoomManage() {
        mContext.startActivity(new Intent(mContext, RoomManageActivity.class));
    }


}
