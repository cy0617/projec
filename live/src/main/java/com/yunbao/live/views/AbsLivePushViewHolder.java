package com.yunbao.live.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.meihu.beautylibrary.manager.MHBeautyManager;
import com.yunbao.beauty.simple.SimpleBeautyEffectListener;
import com.yunbao.beauty.ui.interfaces.DefaultBeautyEffectListener;
import com.yunbao.beauty.ui.views.BeautyDataModel;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.bean.MeiyanConfig;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.live.R;
import com.yunbao.live.interfaces.ILivePushViewHolder;
import com.yunbao.live.interfaces.LivePushListener;


/**
 * Created by cxf on 2018/12/22.
 */

public abstract class AbsLivePushViewHolder extends AbsViewHolder implements ILivePushViewHolder {

    protected final String TAG = getClass().getSimpleName();
    protected LivePushListener mLivePushListener;
    protected boolean mCameraFront;//是否是前置摄像头
    protected boolean mFlashOpen;//闪光灯是否开启了
    protected boolean mPaused;
    protected boolean mStartPush;
    protected ViewGroup mBigContainer;
    protected ViewGroup mSmallContainer;
    protected ViewGroup mLeftContainer;
    protected ViewGroup mRightContainer;
    protected ViewGroup mPkContainer;
    protected View mPreView;
    protected boolean mOpenCamera;//是否选择了相机

    //倒计时
    protected TextView mCountDownText;
    protected int mCountDownCount = 3;


    protected String[] beautyNames;
    protected DefaultBeautyEffectListener mEffectListener;//萌颜的效果监听
    protected MHBeautyManager mMhSDKManager;//各种萌颜效果控制器


    public AbsLivePushViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    public AbsLivePushViewHolder(Context context, ViewGroup parentView, Object... args) {
        super(context, parentView, args);
    }

    @Override
    public void init() {
        mBigContainer = (ViewGroup) findViewById(R.id.big_container);
        mSmallContainer = (ViewGroup) findViewById(R.id.small_container);
        mLeftContainer = (ViewGroup) findViewById(R.id.left_container);
        mRightContainer = (ViewGroup) findViewById(R.id.right_container);
        mPkContainer = (ViewGroup) findViewById(R.id.pk_container);
        mCameraFront = true;
        if (CommonAppConfig.getInstance().isTiBeautyEnable()) {
            beautyNames = mContext.getResources().getStringArray(R.array.name_beauty_name_array);
            initBeauty();
            mEffectListener = getDefaultEffectListener();
        }
    }



    /**
     * 初始化萌颜
     */
    private void initBeauty() {
        try {
            mMhSDKManager = new MHBeautyManager(mContext);
            mMhSDKManager.setBeautyDataModel(BeautyDataModel.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
            mMhSDKManager = null;
            ToastUtil.show(R.string.beauty_init_error);
        }
    }


    /**
     * 开播的时候 3 2 1倒计时
     */
    protected void startCountDown() {
        ViewGroup parent = (ViewGroup) mContentView;
        mCountDownText = (TextView) LayoutInflater.from(mContext).inflate(R.layout.view_count_down, parent, false);
        parent.addView(mCountDownText);
        mCountDownText.setText(String.valueOf(mCountDownCount));
        ScaleAnimation animation = new ScaleAnimation(3, 1, 3, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setRepeatCount(2);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mCountDownText != null) {
                    ViewGroup parent = (ViewGroup) mCountDownText.getParent();
                    if (parent != null) {
                        parent.removeView(mCountDownText);
                        mCountDownText = null;
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (mCountDownText != null) {
                    mCountDownCount--;
                    mCountDownText.setText(String.valueOf(mCountDownCount));
                }
            }
        });
        mCountDownText.startAnimation(animation);
    }


    @Override
    public ViewGroup getSmallContainer() {
        return mSmallContainer;
    }


    @Override
    public ViewGroup getRightContainer() {
        return mRightContainer;
    }

    @Override
    public ViewGroup getPkContainer() {
        return mPkContainer;
    }


    @Override
    public void setOpenCamera(boolean openCamera) {
        mOpenCamera = openCamera;
    }

    @Override
    public void setLivePushListener(LivePushListener livePushListener) {
        mLivePushListener = livePushListener;
    }

    @Override
    public DefaultBeautyEffectListener getEffectListener() {
        return mEffectListener;
    }

    protected abstract void onCameraRestart();

    public abstract DefaultBeautyEffectListener getDefaultEffectListener();


    public abstract SimpleBeautyEffectListener getSimpleBeautyEffectListener();


    @Override
    public void release() {
        if (mCountDownText != null) {
            mCountDownText.clearAnimation();
        }
        if (mMhSDKManager != null) {
            L.e("销毁了");
            mMhSDKManager.destroy();
            BeautyDataModel.getInstance().destroyData();
        }
        mLivePushListener = null;
    }

    @Override
    public void onReStart() {
        if (mOpenCamera) {
            mOpenCamera = false;
            onCameraRestart();
        }
    }

    @Override
    public void onDestroy() {

        L.e(TAG, "LifeCycle------>onDestroy");
    }

    public MHBeautyManager getMhSDKManager() {
        return mMhSDKManager;
    }

    public boolean isFlashOpen() {
        return mFlashOpen;
    }

}
