package com.yunbao.video.activity;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.meihu.beautylibrary.manager.MHBeautyManager;
import com.tencent.live.TXUGCRecord;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.ugc.TXRecordCommon;
import com.tencent.ugc.TXUGCPartsManager;
import com.yunbao.beauty.simple.SimpleBeautyEffectListener;
import com.yunbao.beauty.simple.SimpleBeautyViewHolder;
import com.yunbao.beauty.simple.SimpleFilterBean;
import com.yunbao.beauty.ui.enums.FilterEnum;
import com.yunbao.beauty.ui.interfaces.DefaultBeautyEffectListener;
import com.yunbao.beauty.ui.interfaces.IBeautyViewHolder;
import com.yunbao.beauty.ui.views.BaseBeautyViewHolder;
import com.yunbao.beauty.ui.views.BeautyDataModel;
import com.yunbao.beauty.ui.views.BeautyViewHolderFactory;
import com.yunbao.beauty.views.MHProjectBeautyEffectListener;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.CommonAppContext;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.MeiyanConfig;
import com.yunbao.common.custom.DrawableRadioButton2;
import com.yunbao.common.utils.BitmapUtil;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.video.R;
import com.yunbao.video.bean.MusicBean;
import com.yunbao.video.custom.RecordProgressView;
import com.yunbao.video.custom.VideoRecordBtnView;
import com.yunbao.video.views.VideoMusicViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cxf on 2018/12/5.
 * 视频录制
 */

public class VideoRecordActivity extends AbsActivity implements
        TXRecordCommon.ITXVideoRecordListener, //视频录制进度回调
//        BeautyEffectListener, //设置美颜数值回调
        TXUGCRecord.VideoCustomProcessListener //视频录制中自定义预处理（添加美颜回调）
{

    private static final String TAG = "VideoRecordActivity";
    private static final int MIN_DURATION = 5000;//最小录制时间5s
    private static final int MAX_DURATION = 15000;//最大录制时间15s
    //按钮动画相关
    private VideoRecordBtnView mVideoRecordBtnView;
    private View mRecordView;
    private ValueAnimator mRecordBtnAnimator;//录制开始后，录制按钮动画
    private Drawable mRecordDrawable;
    private Drawable mUnRecordDrawable;

    /****************************/
    private boolean mRecordStarted;//是否开始了录制（true 已开始 false 未开始）
    private boolean mRecordStoped;//是否结束了录制
    private boolean mRecording;//是否在录制中（（true 录制中 false 暂停中）
    private ViewGroup mRoot;
    private TXCloudVideoView mVideoView;//预览控件
    private RecordProgressView mRecordProgressView;//录制进度条
    private TextView mTime;//录制时间
    private DrawableRadioButton2 mBtnFlash;//闪光灯按钮
    private TXUGCRecord mRecorder;//录制器
    private TXRecordCommon.TXUGCCustomConfig mCustomConfig;//录制相关配置
    private boolean mFrontCamera = true;//是否是前置摄像头
    private String mVideoPath;//视频的保存路径
    private int mRecordSpeed;//录制速度
    private View mGroup1;
    private View mGroup2;
    private View mGroup3;
    private View mGroup4;
    private View mBtnNext;//录制完成，下一步
    private Dialog mStopRecordDialog;//停止录制的时候的dialog
    private boolean mIsReachMaxRecordDuration;//是否达到最大录制时间
    private long mDuration;//录制视频的长度
    private IBeautyViewHolder mBeautyViewHolder;
    private MHBeautyManager mTiSDKManager;//萌颜的各种工具
    private VideoMusicViewHolder mVideoMusicViewHolder;
    private MusicBean mMusicBean;//背景音乐
    private boolean mHasBgm;
    private boolean mBgmPlayStarted;//是否已经开始播放背景音乐了
    private Bitmap mFilterBitmap;//基础美颜的滤镜
    private int mMeibai = 0;//美白
    private int mMoPi = 0;//磨皮
    private int mFengNen = 0;//粉嫩
    private long mRecordTime;
    protected String[] beautyNames;
    protected int[] txBeautyData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_record;
    }

    @Override
    protected boolean isStatusBarWhite() {
        return true;
    }

    @Override
    protected void main() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //按钮动画相关
        mVideoRecordBtnView = (VideoRecordBtnView) findViewById(R.id.record_btn_view);
        mRecordView = findViewById(R.id.record_view);
        mUnRecordDrawable = ContextCompat.getDrawable(mContext, R.drawable.bg_btn_record_1);
        mRecordDrawable = ContextCompat.getDrawable(mContext, R.drawable.bg_btn_record_2);
        mRecordBtnAnimator = ValueAnimator.ofFloat(100, 0);
        mRecordBtnAnimator.setDuration(500);
        mRecordBtnAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                if (mVideoRecordBtnView != null) {
                    mVideoRecordBtnView.setRate((int) v);
                }
            }
        });
        mRecordBtnAnimator.setRepeatCount(-1);
        mRecordBtnAnimator.setRepeatMode(ValueAnimator.REVERSE);

        /****************************/
        mRoot = (ViewGroup) findViewById(R.id.root);
        mGroup1 = findViewById(R.id.group_1);
        mGroup2 = findViewById(R.id.group_2);
        mGroup3 = findViewById(R.id.group_3);
        mGroup4 = findViewById(R.id.group_4);
        mVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        // mVideoView.enableHardwareDecode(true);
        mTime = findViewById(R.id.time);
        mRecordProgressView = (RecordProgressView) findViewById(R.id.record_progress_view);
        mRecordProgressView.setMaxDuration(MAX_DURATION);
        mRecordProgressView.setMinDuration(MIN_DURATION);
        mBtnFlash = (DrawableRadioButton2) findViewById(R.id.btn_flash);
        mBtnNext = findViewById(R.id.btn_next);


        initCameraRecord();


    }

    /*
     根据后台参数设置美颜
     */
    private void setBeautyByConfig() {
        if (CommonAppConfig.getInstance().isTiBeautyEnable()) {
            beautyNames = mContext.getResources().getStringArray(R.array.name_beauty_name_array);
            int[] beautyMap = BeautyDataModel.getInstance().getCurrentBeautyMap();
            int  skinWhiting= beautyMap[0];
            int  skinSmooth = beautyMap[1];
            int  skinTenderness = beautyMap[2];
            txBeautyData = new int[3];
            txBeautyData[0] = skinWhiting;
            txBeautyData[1] = skinSmooth;
            txBeautyData[2] = skinTenderness;
            setTxFilter();
        }
    }

    /**
     * 初始化录制器
     */
    private void initCameraRecord() {
        mRecorder = TXUGCRecord.getInstance(CommonAppContext.sInstance);
//        mRecorder.setBeautyDepth(TXLiveConstants.BEAUTY_STYLE_SMOOTH, txBeautyData[0], txBeautyData[1], txBeautyData[2]);
//        setTxFilter();
        mRecorder.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
        mRecorder.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mRecordSpeed = TXRecordCommon.RECORD_SPEED_NORMAL;
        mRecorder.setRecordSpeed(mRecordSpeed);
        mRecorder.setAspectRatio(TXRecordCommon.VIDEO_ASPECT_RATIO_9_16);
        mCustomConfig = new TXRecordCommon.TXUGCCustomConfig();
        mCustomConfig.videoResolution = TXRecordCommon.VIDEO_RESOLUTION_540_960;
        mCustomConfig.minDuration = MIN_DURATION;
        mCustomConfig.maxDuration = MAX_DURATION;
//        mCustomConfig.videoBitrate = 6500;
//        mCustomConfig.videoGop = 3;
//        mCustomConfig.videoFps = 20;
        mCustomConfig.isFront = mFrontCamera;
        mRecorder.setVideoRecordListener(this);
    }


    /**
     * 初始化萌颜
     */
    private void initBeauty() {

        try {
            mTiSDKManager = new MHBeautyManager(mContext);
        } catch (Exception e) {
            mTiSDKManager = null;
            ToastUtil.show(R.string.beauty_init_error);
        }
    }

    /**
     * 录制回调
     */
    @Override
    public void onRecordEvent(int event, Bundle bundle) {
        if (event == TXRecordCommon.EVT_ID_PAUSE) {
            if (mRecordProgressView != null) {
                mRecordProgressView.clipComplete();
            }
        } else if (event == TXRecordCommon.EVT_CAMERA_CANNOT_USE) {
            ToastUtil.show(R.string.video_record_camera_failed);
        } else if (event == TXRecordCommon.EVT_MIC_CANNOT_USE) {
            ToastUtil.show(R.string.video_record_audio_failed);
        }
    }

    /**
     * 录制回调 录制进度
     */
    @Override
    public void onRecordProgress(long milliSecond) {
        if (mRecordProgressView != null) {
            mRecordProgressView.setProgress((int) milliSecond);
        }
        if (mTime != null) {
            mTime.setText(String.format("%.2f", milliSecond / 1000f) + "s");
        }
        mRecordTime = milliSecond;
        if (milliSecond >= MIN_DURATION) {
            if (mBtnNext != null && mBtnNext.getVisibility() != View.VISIBLE) {
                mBtnNext.setVisibility(View.VISIBLE);
            }
        }
        if (milliSecond >= MAX_DURATION) {
            if (!mIsReachMaxRecordDuration) {
                mIsReachMaxRecordDuration = true;
                if (mRecordBtnAnimator != null) {
                    mRecordBtnAnimator.cancel();
                }
                showProccessDialog();
            }
        }
    }

    /**
     * 录制回调
     */
    @Override
    public void onRecordComplete(TXRecordCommon.TXRecordResult result) {
        hideProccessDialog();
        mRecordStarted = false;
        mRecordStoped = true;
        if (mRecorder != null) {
            mRecorder.toggleTorch(false);
            mRecorder.stopBGM();
            mDuration = mRecorder.getPartsManager().getDuration();
        }
        if (result.retCode < 0) {
            release();
            ToastUtil.show(R.string.video_record_failed);
        } else {
            VideoEditActivity.forward(this, mDuration, mVideoPath, true, mHasBgm);
        }
        exit();
    }


    /**
     * 录制中美颜处理回调
     */
    @Override
    public int onTextureCustomProcess(int i, int i1, int i2) {
        int texture = i;
        if (mTiSDKManager != null) {
            try {
                texture = mTiSDKManager.render(i, i1, i2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return texture;
    }

    /**
     * 录制中美颜处理回调
     */
    @Override
    public void onDetectFacePoints(float[] floats) {

    }

    /**
     * 录制中美颜处理回调
     */
    @Override
    public void onTextureDestroyed() {
        if (mTiSDKManager != null) {
            mTiSDKManager.destroy();
        }
        mTiSDKManager = null;
    }

    public void recordClick(View v) {
        if (mRecordStoped || !canClick()) {
            return;
        }
        int i = v.getId();
        if (i == R.id.btn_start_record) {
            clickRecord();

        } else if (i == R.id.btn_camera) {
            clickCamera();

        } else if (i == R.id.btn_flash) {
            clickFlash();

        } else if (i == R.id.btn_beauty) {
            clickBeauty();

        } else if (i == R.id.btn_music) {
            clickMusic();

        } else if (i == R.id.btn_speed_1) {
            changeRecordSpeed(TXRecordCommon.RECORD_SPEED_SLOWEST);

        } else if (i == R.id.btn_speed_2) {
            changeRecordSpeed(TXRecordCommon.RECORD_SPEED_SLOW);

        } else if (i == R.id.btn_speed_3) {
            changeRecordSpeed(TXRecordCommon.RECORD_SPEED_NORMAL);

        } else if (i == R.id.btn_speed_4) {
            changeRecordSpeed(TXRecordCommon.RECORD_SPEED_FAST);

        } else if (i == R.id.btn_speed_5) {
            changeRecordSpeed(TXRecordCommon.RECORD_SPEED_FASTEST);

        } else if (i == R.id.btn_upload) {
            clickUpload();

        } else if (i == R.id.btn_delete) {
            clickDelete();

        } else if (i == R.id.btn_next) {
            clickNext();

        }
    }

    /**
     * 点击摄像头
     */
    private void clickCamera() {
        if (mRecorder != null) {
            if (mBtnFlash != null && mBtnFlash.isChecked()) {
                mBtnFlash.doToggle();
                mRecorder.toggleTorch(mBtnFlash.isChecked());
            }
            mFrontCamera = !mFrontCamera;
            mRecorder.switchCamera(mFrontCamera);
        }
    }

    /**
     * 点击闪光灯
     */
    private void clickFlash() {
        if (mFrontCamera) {
            ToastUtil.show(R.string.live_open_flash);
            return;
        }
        if (mBtnFlash != null) {
            mBtnFlash.doToggle();
            if (mRecorder != null) {
                mRecorder.toggleTorch(mBtnFlash.isChecked());
            }
        }
    }

    /**
     * 点击美颜
     */
    private void clickBeauty() {
        if (mBeautyViewHolder == null) {
            if (CommonAppConfig.getInstance().isTiBeautyEnable()) {
                mBeautyViewHolder = BeautyViewHolderFactory.getBeautyViewHolder(mContext, mRoot,null);
                if (mBeautyViewHolder != null && mBeautyViewHolder instanceof BaseBeautyViewHolder) {
                    ((BaseBeautyViewHolder) mBeautyViewHolder).setMhBeautyManager(mTiSDKManager);
                    mBeautyViewHolder.setEffectListener(mBaseBeautyEffectListener);
                }
            } else {
                mBeautyViewHolder = new SimpleBeautyViewHolder(mContext, mRoot);
                mBeautyViewHolder.setEffectListener(new SimpleBeautyEffectListener() {
                    @Override
                    public void onFilterChanged(SimpleFilterBean filterBean) {
                        if (mFilterBitmap != null) {
                            mFilterBitmap.recycle();
                        }
                        if (mRecorder != null) {
                            int filterSrc = filterBean.getFilterSrc();
                            if (filterSrc != 0) {
                                Bitmap bitmap = BitmapUtil.getInstance().decodeBitmap(filterSrc);
                                if (bitmap != null) {
                                    mFilterBitmap = bitmap;
                                    mRecorder.setFilter(bitmap);
                                } else {
                                    mRecorder.setFilter(null);
                                }
                            } else {
                                mRecorder.setFilter(null);
                            }
                        }
                    }

                    @Override
                    public void onMeiBaiChanged(int progress) {
                        if (mRecorder != null) {
                            int v = progress / 10;
                            if (mMeibai != v) {
                                mMeibai = v;
                                mRecorder.setBeautyDepth(0, mMoPi, mMeibai, mFengNen);
                            }
                        }
                    }

                    @Override
                    public void onMoPiChanged(int progress) {
                        if (mRecorder != null) {
                            int v = progress / 10;
                            if (mMoPi != v) {
                                mMoPi = v;
                                mRecorder.setBeautyDepth(0, mMoPi, mMeibai, mFengNen);
                            }
                        }
                    }

                    @Override
                    public void onHongRunChanged(int progress) {
                        if (mRecorder != null) {
                            int v = progress / 10;
                            if (mFengNen != v) {
                                mFengNen = v;
                                mRecorder.setBeautyDepth(0, mMoPi, mMeibai, mFengNen);
                            }
                        }
                    }
                });
            }
            mBeautyViewHolder.setVisibleListener(new IBeautyViewHolder.VisibleListener() {
                @Override
                public void onVisibleChanged(boolean visible) {
                    if (mGroup1 != null) {
                        if (visible) {
                            if (mGroup1.getVisibility() == View.VISIBLE) {
                                mGroup1.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            if (mGroup1.getVisibility() != View.VISIBLE) {
                                mGroup1.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });


        }
        mBeautyViewHolder.show();
    }

    private void setTxFilter() {
        mRecorder.setBeautyDepth(TXLiveConstants.BEAUTY_STYLE_SMOOTH, txBeautyData[0], txBeautyData[1], txBeautyData[2]);
    }

    //meihu
    private DefaultBeautyEffectListener mBaseBeautyEffectListener = new MHProjectBeautyEffectListener() {

        public void onFilterChanged(Bitmap bitmap) {
            mRecorder.setFilter(bitmap);
        }

        @Override
        public void onFilterChanged(FilterEnum tiFilterEnum) {
            Bitmap lookupBitmap = null;
            switch (tiFilterEnum) {
                case NO_FILTER:
                    break;
                case ROMANTIC_FILTER:
                    lookupBitmap = BitmapFactory.decodeResource(mContext.getResources(), com.meihu.beautylibrary.R.drawable.filter_langman);
                    break;
                case FRESH_FILTER:
                    lookupBitmap = BitmapFactory.decodeResource(mContext.getResources(), com.meihu.beautylibrary.R.drawable.filter_qingxin);
                    break;
                case BEAUTIFUL_FILTER:
                    lookupBitmap = BitmapFactory.decodeResource(mContext.getResources(), com.meihu.beautylibrary.R.drawable.filter_weimei);
                    break;
                case PINK_FILTER:
                    lookupBitmap = BitmapFactory.decodeResource(mContext.getResources(), com.meihu.beautylibrary.R.drawable.filter_fennen);
                    break;
                case NOSTALGIC_FILTER:
                    lookupBitmap = BitmapFactory.decodeResource(mContext.getResources(), com.meihu.beautylibrary.R.drawable.filter_huaijiu);
                    break;
                case COOL_FILTER:
                    lookupBitmap = BitmapFactory.decodeResource(mContext.getResources(), com.meihu.beautylibrary.R.drawable.filter_qingliang);
                    break;
                case BLUES_FILTER:
                    lookupBitmap = BitmapFactory.decodeResource(mContext.getResources(), com.meihu.beautylibrary.R.drawable.filter_landiao);
                    break;
                case JAPANESE_FILTER:
                    lookupBitmap = BitmapFactory.decodeResource(mContext.getResources(), com.meihu.beautylibrary.R.drawable.filter_rixi);
                    break;
            }
//                MHRender.setStyle(lookupBitmap);
            mRecorder.setFilter(lookupBitmap);
        }

        @Override
        public void onBeautyOrigin() {
            BeautyDataModel.getInstance().resetOriginBasicBeautyData();
            txBeautyData = new int[]{0, 0, 0};
            setTxFilter();
        }


        @Override
        public void onMeiBaiChanged(int progress) {
//                MHRender.setSkinWhiting(progress);
            if (CommonAppConfig.getInstance().isTiBeautyEnable())
                BeautyDataModel.getInstance().setBeautyProgress(beautyNames[0], progress);
            txBeautyData[1] = progress;
            setTxFilter();
        }

        @Override
        public void onMoPiChanged(int progress) {
//                MHRender.setSkinSmooth(progress);
            if (CommonAppConfig.getInstance().isTiBeautyEnable())
                BeautyDataModel.getInstance().setBeautyProgress(beautyNames[1], progress);
            txBeautyData[0] = progress;
            setTxFilter();
        }


        @Override
        public void onFengNenChanged(int progress) {
//                MHRender.setSkinTenderness(progress);
            if (CommonAppConfig.getInstance().isTiBeautyEnable())
                BeautyDataModel.getInstance().setBeautyProgress(beautyNames[2], progress);
            txBeautyData[2] = progress;
            setTxFilter();
        }


    };


    private void setDefaultBeautyConfig() {
        final MeiyanConfig meiyanConfig = CommonAppConfig.getInstance().getConfig().parseMeiyanConfig();
        txBeautyData = new int[3];
        txBeautyData[0] = meiyanConfig.getSkin_whiting();
        txBeautyData[1] = meiyanConfig.getSkin_smooth();
        txBeautyData[2] = meiyanConfig.getSkin_tenderness();
        if (CommonAppConfig.getInstance().isTiBeautyEnable())
            BeautyDataModel.getInstance().setBeautyDataMap(meiyanConfig.getDataArray());


    }


    /**
     * 点击音乐
     */
    private void clickMusic() {
        if (mVideoMusicViewHolder == null) {
            mVideoMusicViewHolder = new VideoMusicViewHolder(mContext, mRoot);
            mVideoMusicViewHolder.setActionListener(new VideoMusicViewHolder.ActionListener() {
                @Override
                public void onChooseMusic(MusicBean musicBean) {
                    mMusicBean = musicBean;
                    mBgmPlayStarted = false;
                }
            });
            mVideoMusicViewHolder.addToParent();
            mVideoMusicViewHolder.subscribeActivityLifeCycle();
        }
        mVideoMusicViewHolder.show();
    }

    /**
     * 点击上传，选择本地视频
     */
    private void clickUpload() {
        Intent intent = new Intent(mContext, VideoChooseActivity.class);
        intent.putExtra(Constants.VIDEO_DURATION, MAX_DURATION);
        startActivityForResult(intent, 0);
    }

    /**
     * 选择本地视频的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            mVideoPath = data.getStringExtra(Constants.VIDEO_PATH);
            mDuration = data.getLongExtra(Constants.VIDEO_DURATION, 0);
            VideoEditActivity.forward(this, mDuration, mVideoPath, false, false);
            exit();
        }
    }


    /**
     * 开始预览
     */
    private void startCameraPreview() {
        if (mRecorder != null && mCustomConfig != null && mVideoView != null) {
            mRecorder.startCameraCustomPreview(mCustomConfig, mVideoView);
            setBeautyByConfig();
            if (!mFrontCamera) {
                mRecorder.switchCamera(false);
            }
            if (CommonAppConfig.getInstance().isTiBeautyEnable()) {
                initBeauty();
                mRecorder.setVideoProcessListener(this);
            }
        }
    }

    /**
     * 停止预览
     */
    private void stopCameraPreview() {
        if (mRecorder != null) {
            if (mRecording) {
                pauseRecord();
            }
            mRecorder.stopCameraPreview();
            mRecorder.setVideoProcessListener(null);
        }
    }

    /**
     * 点击录制
     */
    private void clickRecord() {
        if (mRecordStarted) {
            if (mRecording) {
                pauseRecord();
            } else {
                resumeRecord();
            }
        } else {
            startRecord();
        }
    }

    /**
     * 开始录制
     */
    private void startRecord() {
        if (mRecorder != null) {
            mVideoPath = StringUtil.generateVideoOutputPath();//视频保存的路径
            int result = mRecorder.startRecord(mVideoPath, CommonAppConfig.VIDEO_RECORD_TEMP_PATH, null);//为空表示不需要生成视频封面
            if (result != TXRecordCommon.START_RECORD_OK) {
                if (result == TXRecordCommon.START_RECORD_ERR_NOT_INIT) {
                    ToastUtil.show(R.string.video_record_tip_1);
                } else if (result == TXRecordCommon.START_RECORD_ERR_IS_IN_RECORDING) {
                    ToastUtil.show(R.string.video_record_tip_2);
                } else if (result == TXRecordCommon.START_RECORD_ERR_VIDEO_PATH_IS_EMPTY) {
                    ToastUtil.show(R.string.video_record_tip_3);
                } else if (result == TXRecordCommon.START_RECORD_ERR_API_IS_LOWER_THAN_18) {
                    ToastUtil.show(R.string.video_record_tip_4);
                } else if (result == TXRecordCommon.START_RECORD_ERR_LICENCE_VERIFICATION_FAILED) {
                    ToastUtil.show(R.string.video_record_tip_5);
                }
                return;
            }
        }
        mRecordStarted = true;
        mRecording = true;
        resumeBgm();
        startRecordBtnAnim();
        if (mGroup2 != null && mGroup2.getVisibility() == View.VISIBLE) {
            mGroup2.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 暂停录制
     */
    private void pauseRecord() {
        if (mRecorder == null) {
            return;
        }
        pauseBgm();
        mRecorder.pauseRecord();
        mRecording = false;
        stopRecordBtnAnim();
        if (mGroup2 != null && mGroup2.getVisibility() != View.VISIBLE) {
            mGroup2.setVisibility(View.VISIBLE);
        }
        TXUGCPartsManager partsManager = mRecorder.getPartsManager();
        if (partsManager != null) {
            List<String> partList = partsManager.getPartsPathList();
            if (partList != null && partList.size() > 0) {
                if (mGroup3 != null && mGroup3.getVisibility() == View.VISIBLE) {
                    mGroup3.setVisibility(View.INVISIBLE);
                }
                if (mGroup4 != null && mGroup4.getVisibility() != View.VISIBLE) {
                    mGroup4.setVisibility(View.VISIBLE);
                }
            } else {
                if (mGroup3 != null && mGroup3.getVisibility() != View.VISIBLE) {
                    mGroup3.setVisibility(View.VISIBLE);
                }
                if (mGroup4 != null && mGroup4.getVisibility() == View.VISIBLE) {
                    mGroup4.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    /**
     * 恢复录制
     */
    private void resumeRecord() {
        if (mRecorder != null) {
            int startResult = mRecorder.resumeRecord();
            if (startResult != TXRecordCommon.START_RECORD_OK) {
                ToastUtil.show(WordUtil.getString(R.string.video_record_failed));
                return;
            }
        }
        mRecording = true;
        resumeBgm();
        startRecordBtnAnim();
        if (mGroup2 != null && mGroup2.getVisibility() == View.VISIBLE) {
            mGroup2.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 暂停背景音乐
     */
    private void pauseBgm() {
        if (mRecorder != null) {
            mRecorder.pauseBGM();
        }
    }

    /**
     * 恢复背景音乐
     */
    private void resumeBgm() {
        if (mRecorder == null) {
            return;
        }
        if (!mBgmPlayStarted) {
            if (mMusicBean == null) {
                return;
            }
            int bgmDuration = mRecorder.setBGM(mMusicBean.getLocalPath());
            mRecorder.playBGMFromTime(0, bgmDuration);
            mRecorder.setBGMVolume(1);//背景音为1最大
            mRecorder.setMicVolume(0);//原声音为0
            mBgmPlayStarted = true;
            mHasBgm = true;
        } else {
            mRecorder.resumeBGM();
        }
    }

    /**
     * 按钮录制动画开始
     */
    private void startRecordBtnAnim() {
        if (mRecordView != null) {
            mRecordView.setBackground(mRecordDrawable);
        }
        if (mRecordBtnAnimator != null) {
            mRecordBtnAnimator.start();
        }
    }

    /**
     * 按钮录制动画停止
     */
    private void stopRecordBtnAnim() {
        if (mRecordView != null) {
            mRecordView.setBackground(mUnRecordDrawable);
        }
        if (mRecordBtnAnimator != null) {
            mRecordBtnAnimator.cancel();
        }
        if (mVideoRecordBtnView != null) {
            mVideoRecordBtnView.reset();
        }
    }

    /**
     * 调整录制速度
     */
    private void changeRecordSpeed(int speed) {
        if (mRecordSpeed == speed) {
            return;
        }
        mRecordSpeed = speed;
        if (mRecorder != null) {
            mRecorder.setRecordSpeed(speed);
        }
    }

    /**
     * 删除录制分段
     */
    private void clickDelete() {
        DialogUitl.showSimpleDialog(mContext, WordUtil.getString(R.string.video_record_delete_last), new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                doClickDelete();
            }
        });
    }

    /**
     * 删除录制分段
     */
    private void doClickDelete() {
        if (!mRecordStarted || mRecording || mRecorder == null) {
            return;
        }
        TXUGCPartsManager partsManager = mRecorder.getPartsManager();
        if (partsManager == null) {
            return;
        }
        List<String> partList = partsManager.getPartsPathList();
        if (partList == null || partList.size() == 0) {
            return;
        }
        partsManager.deleteLastPart();
        int time = partsManager.getDuration();
        if (mTime != null) {
            mTime.setText(String.format("%.2f", time / 1000f) + "s");
        }
        mRecordTime = time;
        if (time < MIN_DURATION && mBtnNext != null && mBtnNext.getVisibility() == View.VISIBLE) {
            mBtnNext.setVisibility(View.INVISIBLE);
        }
        if (mRecordProgressView != null) {
            mRecordProgressView.deleteLast();
        }
        partList = partsManager.getPartsPathList();
        if (partList != null && partList.size() == 0) {
            if (mGroup2 != null && mGroup2.getVisibility() != View.VISIBLE) {
                mGroup2.setVisibility(View.VISIBLE);
            }
            if (mGroup3 != null && mGroup3.getVisibility() != View.VISIBLE) {
                mGroup3.setVisibility(View.VISIBLE);
            }
            if (mGroup4 != null && mGroup4.getVisibility() == View.VISIBLE) {
                mGroup4.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 结束录制，会触发 onRecordComplete
     */
    public void clickNext() {
        stopRecordBtnAnim();
        if (mRecorder != null) {
            mRecorder.stopBGM();
            mRecorder.stopRecord();
            showProccessDialog();
        }
    }


    /**
     * 录制结束时候显示处理中的弹窗
     */
    private void showProccessDialog() {
        if (mStopRecordDialog == null) {
            mStopRecordDialog = DialogUitl.loadingDialog(mContext, WordUtil.getString(R.string.video_processing));
            mStopRecordDialog.show();
        }
    }

    /**
     * 隐藏处理中的弹窗
     */
    private void hideProccessDialog() {
        if (mStopRecordDialog != null) {
            mStopRecordDialog.dismiss();
        }
        mStopRecordDialog = null;
    }


    @Override
    public void onBackPressed() {
        if (!canClick()) {
            return;
        }
        if (mBeautyViewHolder != null && mBeautyViewHolder.isShowed()) {
            mBeautyViewHolder.hide();
            return;
        }
        if (mVideoMusicViewHolder != null && mVideoMusicViewHolder.isShowed()) {
            mVideoMusicViewHolder.hide();
            return;
        }
        List<Integer> list = new ArrayList<>();
        if (mRecordTime > 0) {
            list.add(R.string.video_re_record);
        }
        list.add(R.string.video_exit);
        DialogUitl.showStringArrayDialog(mContext, list.toArray(new Integer[list.size()]), new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.video_re_record) {
                    reRecord();
                } else if (tag == R.string.video_exit) {
                    exit();
                }
            }
        });
    }

    /**
     * 重新录制
     */
    private void reRecord() {
        if (mRecorder == null) {
            return;
        }
        mRecorder.pauseBGM();
        mMusicBean = null;
        mHasBgm = false;
        mBgmPlayStarted = false;
        mRecorder.pauseRecord();
        mRecording = false;
        stopRecordBtnAnim();
        if (mGroup2 != null && mGroup2.getVisibility() != View.VISIBLE) {
            mGroup2.setVisibility(View.VISIBLE);
        }
        TXUGCPartsManager partsManager = mRecorder.getPartsManager();
        if (partsManager != null) {
            partsManager.deleteAllParts();
        }
        mRecorder.onDeleteAllParts();
        if (mTime != null) {
            mTime.setText("0.00s");
        }
        mRecordTime = 0;
        if (mBtnNext != null && mBtnNext.getVisibility() == View.VISIBLE) {
            mBtnNext.setVisibility(View.INVISIBLE);
        }
        if (mRecordProgressView != null) {
            mRecordProgressView.deleteAll();
        }
        if (mGroup3 != null && mGroup3.getVisibility() != View.VISIBLE) {
            mGroup3.setVisibility(View.VISIBLE);
        }
        if (mGroup4 != null && mGroup4.getVisibility() == View.VISIBLE) {
            mGroup4.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 退出
     */
    private void exit() {
        release();
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        startCameraPreview();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopCameraPreview();
        if (mRecorder != null && mBtnFlash != null && mBtnFlash.isChecked()) {
            mBtnFlash.doToggle();
            mRecorder.toggleTorch(mBtnFlash.isChecked());
        }
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
        L.e(TAG, "-------->onDestroy");
    }

    private void release() {

        if (mStopRecordDialog != null && mStopRecordDialog.isShowing()) {
            mStopRecordDialog.dismiss();
        }
        if (mBeautyViewHolder != null) {
            mBeautyViewHolder.release();
        }
        if (mVideoMusicViewHolder != null) {
            mVideoMusicViewHolder.release();
        }
        if (mRecordProgressView != null) {
            mRecordProgressView.release();
        }
        if (mRecordBtnAnimator != null) {
            mRecordBtnAnimator.cancel();
        }
        if (mRecorder != null) {
            mRecorder.toggleTorch(false);
            mRecorder.stopBGM();
            if (mRecordStarted) {
                mRecorder.stopRecord();
            }
            mRecorder.stopCameraPreview();
            mRecorder.setVideoProcessListener(null);
            mRecorder.setVideoRecordListener(null);
            TXUGCPartsManager getPartsManager = mRecorder.getPartsManager();
            if (getPartsManager != null) {
                getPartsManager.deleteAllParts();
            }
            mRecorder.release();
        }
        if (CommonAppConfig.getInstance().isTiBeautyEnable()){
            BeautyDataModel.getInstance().destroyData();
            if (mTiSDKManager != null) {
                mTiSDKManager.destroy();
            }
            mTiSDKManager = null;
        }
        mStopRecordDialog = null;
        mBeautyViewHolder = null;
        mVideoMusicViewHolder = null;
        mRecordProgressView = null;
        mRecordBtnAnimator = null;
        mRecorder = null;


    }

    /****************美颜回调 end********************/


}
