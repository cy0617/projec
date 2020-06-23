/*
package com.yunbao.live.views;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.manager.LogManager;
import com.meihu.beautylibrary.manager.VerifyManager;
import com.meihu.phonebeautyui.ui.interfaces.DefaultBeautyEffectListener;
import com.meihu.phonebeautyui.ui.views.BaseBeautyViewHolder;
import com.meihu.phonebeautyui.ui.views.BeautyViewHolderFactory;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.meihu.beautylibrary.manager.MHBeautyManager;
import com.meihu.phonebeautyui.ui.enums.FilterEnum;
import com.meihu.phonebeautyui.ui.interfaces.MHCameraClickListener;
import com.meihu.beautylibrary.utils.ToastUtil;
import com.meihu.phonebeautyui.ui.views.BeautyDataModel;
import com.yunbao.beauty.ui.views.BaseBeautyViewHolder;
import com.yunbao.beauty.ui.views.BeautyDataModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements DefaultBeautyEffectListener, ITXLivePushListener, TXLivePusher.VideoCustomProcessListener, MHCameraClickListener {

    private TXLivePushConfig mLivePushConfig;
    private TXLivePusher mLivePusher;
    private TXCloudVideoView mCaptureView;
    private PhoneStateListener mPhoneListener = null;
    private RotationObserver mRotationObserver;
    private boolean mFrontCamera = true;
    private MHBeautyManager mhBeautyManager;
    private AlertDialog dialog;
    private ContentResolver contentResolver;
    private boolean mIsPreView;
    private RelativeLayout beautyContainer;
    private Handler mHandler;
    private BaseBeautyViewHolder beautyViewHolder;
    //    private int[] txBeautyData= new int[3];
    private boolean isResume;
    private int retryCount;
    private Thread beautyThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        BeautyDataModel.getInstance().setFilterChanged(FilterEnum.PINK_FILTER);
        if (Build.VERSION.SDK_INT >= 21) {//5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main_tx);
        mHandler = new Handler(getMainLooper());
        Log.d("meihu_beauty", "MainActivity--onCreate");
        showAppInfo();
        initView();
        initPusher();
        initphoneStateListener();
        findViewById(R.id.iv_change_camera).setOnClickListener(v -> changeCamera());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("meihu_beauty", "MainActivity--onNewIntent");
    }


    @Override
    protected void onStart() {
        super.onStart();
        LogManager.getInstance().writeData("MainActivity_onStart()");
        push();
        isResume = true;
        Log.d("meihu_beauty", "MainActivity--onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCaptureView != null) {
            mCaptureView.onResume();
        }
        Log.d("meihu_beauty", "MainActivity--onResume");
//        if (!mLivePusher.isPushing()) mLivePusher.resumePusher();
    }

    private void showAppInfo() {
        TextView TvsdkShow = findViewById(R.id.tv_show_sdkdata);
        String packageName = getApplicationContext().getPackageName();
        String appVersion = BuildConfig.VERSION_NAME;
        String sdkVersion = BuildConfig.VERSION_NAME;
        String appBuildType = BuildConfig.BUILD_TYPE;
        TvsdkShow.setText(String.format(" packageName: %s \n app version: %s \n sdk version: %s \n buildTypes: %s", packageName, appVersion, sdkVersion, appBuildType));
    }

    private void initView() {
//        int[] beautyData = new int[]{-1,-1,-1,50,30,40,50,70,10,50,40,20,80,40,60,50};
//        BeautyDataModel.getInstance().setBeautyDataMap(beautyData); //设置美颜初始值
//        BeautyDataModel.getInstance().setWatermark(1); //设置水印初始值
//        BeautyDataModel.getInstance().setSpeciallyName(getResources().getString(R.string.hmosaic)); //设置特效初始值
//        BeautyDataModel.getInstance().setDistortionName(getResources().getString(R.string.haha_12)); //设置哈哈镜初始值
        beautyContainer = findViewById(R.id.beauty_view_container);
//        mRtmp = (EditText) findViewById(R.id.et_rmtp);
//        mTextView = findViewById(R.id.tv_track_text);
        mCaptureView = findViewById(R.id.video_view);
        initBeautyView();
    }

    private void initPusher() {
        mLivePusher = new TXLivePusher(this);
        mLivePushConfig = new TXLivePushConfig();
        mLivePushConfig.setVideoFPS(12);
        mLivePushConfig.setVideoEncodeGop(5);
        mLivePusher.setConfig(mLivePushConfig);
        mLivePusher.setVideoProcessListener(this);
    }

    private void initBeautyView() {
        if (MHSDK.getInstance().isVerEnd()){
            setBeautyView();
        }else {
            if (beautyThread != null) {
                beautyThread.interrupt();
                beautyThread = null;
            }
            beautyThread = new BeautyThread(this);
            beautyThread.start();
        }
    }

    private void showRetryDialog() {
        if (dialog != null) {
            dialog.cancel();
        }
        SDKDialogListener diaLogListener = new SDKDialogListener(this);
        dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("资源获取失败，请检查网络")
                .setCancelable(false)
                .setPositiveButton("确定", diaLogListener)
                .create();
        dialog.show();
    }

    private void showExitDialog() {
        if (dialog != null) {
            dialog.cancel();
        }
        DiaLogListener diaLogListener = new DiaLogListener(this);
        dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("资源获取失败，请检查网络并退出重试")
                .setCancelable(false)
                .setPositiveButton("确定", diaLogListener)
                .create();
        dialog.show();
    }


    private void setBeautyView(){
        LogManager.getInstance().writeData("MainActivity_setBeautyView_SDKVersion=" + MHSDK.getInstance().getVer());
        if (!VerifyManager.getInstance().isAccess()){
            if (retryCount < 10) {
                showRetryDialog();
            }else {
                showExitDialog();
                return;
            }
        }
        if (beautyViewHolder != null) {
            beautyViewHolder.release();
            beautyViewHolder = null;
        }
        beautyViewHolder = BeautyViewHolderFactory.getBeautyViewHolder(getApplicationContext(), beautyContainer);
        beautyViewHolder.show();
        beautyViewHolder.setEffectListener(this);
        beautyViewHolder.setCameraClickListener(this);
        beautyViewHolder.setMhBeautyManager(mhBeautyManager);
    }


    private void initphoneStateListener() {
        mPhoneListener = new TXPhoneStateListener(mLivePusher);
        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        mRotationObserver = new RotationObserver(this);
        contentResolver = getContentResolver();
        startObserver(contentResolver);
    }


    private void changeCamera() {
        mFrontCamera = !mFrontCamera;
        mLivePusher.switchCamera();
        mLivePusher.setMirror(mFrontCamera);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("meihu_beauty", "MainActivity--onPause");
    }

    @Override
    protected void onStop() {
        Log.d("meihu_beauty", "MainActivity--onStop");
        LogManager.getInstance().writeData("MainActivity_onStop()");
        super.onStop();
        isResume = false;
        stopPush();
        if (mCaptureView != null) {
            mCaptureView.onPause();
        }
        if (isFinishing()){
            if (beautyThread != null){
                beautyThread.interrupt();
                beautyThread = null;
            }
            Log.d("meihu_beauty", "isFinishing");
            if (beautyViewHolder != null) {
                beautyViewHolder.release();
            }
            MHSDK.getInstance().clearVerNote();
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("meihu_beauty", "MainActivity--onDestroy");
        LogManager.getInstance().writeData("MainActivity_onDestroy()");
        if (beautyViewHolder != null) {
            beautyViewHolder.release();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        stopPush();
        if (mCaptureView != null) mCaptureView.onDestroy();
//        mPanelLayout.release();
//        if (mhBeautyManager != null) mhBeautyManager.destroy();
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
        stopObserver(contentResolver);
        mRotationObserver = null;
        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
        mPhoneListener = null;
        MHSDK.getInstance().clearVerNote();
        LogManager.getInstance().closeFile();
        super.onDestroy();
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onBackPressed() {
        DiaLogListener diaLogListener = new DiaLogListener(this);
        dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定要退出吗？")
                .setPositiveButton("确定", diaLogListener)
                .setNegativeButton("取消", diaLogListener)
                .create();
        dialog.show();
    }

    private void setTxFilter() {
        int[] currentBeautyMap = BeautyDataModel.getInstance().getCurrentBeautyMap();
        mLivePusher.setBeautyFilter(TXLiveConstants.BEAUTY_STYLE_SMOOTH, currentBeautyMap[1], currentBeautyMap[0], currentBeautyMap[2]);
    }

    private void push() {
        if (mIsPreView) return;
        int[] beautyData = new int[]{-1,-1,-1,30,40,50,70,10,50,40,20,80,40,60,50};
        BeautyDataModel.getInstance().setBeautyDataMap(beautyData);
        //String rtmpUrl = "https://room.qcloud.com/weapp/live_room/add_pusher?userID=user_4debca9c_515e&token=5bacc8b47ebca9651466c36a195f4c4f";
        String rtmpUrl = "rtmp://3891.livepush.myqcloud.com/live/3891_user_b04a6109_1af7?bizid=3891&txSecret=611e618e88dcfd79711961cb66442e54&txTime=5CE28D09";//mRtmp.getText().toString();
//        int i = mLivePusher.startPusher(rtmpUrl);
//        Log.d("mLivePusherResult==" , i+"");
//        mLivePushConfig.setVideoBitrate(1200);
//        mLivePushConfig.setAutoAdjustBitrate(false);
//        mLivePushConfig.setVideoFPS(20);
        mCaptureView.setVisibility(View.VISIBLE);
//        mLivePusher.setPushListener(this);
        mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960);
//        mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640);
        mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
//        int[] beautyDefaultDatas = BeautyDataModel.getInstance().getDefaultAllBeautyDatas();
//        mLivePusher.setBeautyFilter(TXLiveConstants.BEAUTY_STYLE_SMOOTH, beautyDefaultDatas[0], beautyDefaultDatas[1], beautyDefaultDatas[2]);
        mLivePusher.setMirror(true);
        // 设置自定义视频处理回调，在主播预览及编码前回调出来，用户可以用来做自定义美颜或者增加视频特效
        mLivePusher.setConfig(mLivePushConfig);
        setPushScene(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION, false);
        mLivePusher.startCameraPreview(mCaptureView);
        setTxFilter();
//        mLivePusher.startPusher()
//        mIsPushing = true;
        mIsPreView =true;
    }


    private void stopPush() {
//        mLivePusher.stopBGM();
        mLivePusher.stopCameraPreview(true);
        mLivePusher.setPushListener(null);
//        mLivePusher.stopPusher();
        mCaptureView.setVisibility(View.GONE);
//        mPusherView.setVisibility(View.GONE);
//        mLivePushConfig.setPauseImg(null);
        mIsPreView = false;
    }

    @Override
    public int onTextureCustomProcess(int texture, int width, int height) {
        int textureId = texture;
        try {
            if (isResume) {
                if (mhBeautyManager == null) {
                    mhBeautyManager = new MHBeautyManager(getApplicationContext());
                    mhBeautyManager.setBeautyDataModel(BeautyDataModel.getInstance());
                    if (beautyViewHolder != null) {
                        beautyViewHolder.setMhBeautyManager(mhBeautyManager);
                    }
                    return textureId;
                }
                textureId = mhBeautyManager.render(texture, width, height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textureId;
    }

    @Override
    public void onDetectFacePoints(float[] floats) {

    }

    @Override
    public void onTextureDestoryed() {
        Log.d("meihu_library", "onTextureDestoryed");
        if (mhBeautyManager != null) {
            mhBeautyManager.destroy();
            mhBeautyManager = null;
        }
    }

    @Override
    public void onPushEvent(int i, Bundle bundle) {

    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    @Override
    public void onCameraClick() {
        mLivePusher.snapshot(bitmap -> {
            String s = saveBitmap(MainActivity.this, bitmap);
            if (!TextUtils.isEmpty(s)){
                ToastUtil.show(MainActivity.this, "拍照成功，请到相册查看");
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(s))));
            }else {
                ToastUtil.show(MainActivity.this, "拍照失败");
            }
        });
    }

    private String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory() + File.separator + "meihu" + File.separator;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + "meihu" + File.separator;
        }
        try {
            filePic = new File(savePath + System.currentTimeMillis() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return filePic.getAbsolutePath();
    }

    public void setPushScene(int type, boolean enableAdjustBitrate) {
        Log.i("meihu_library", "setPushScene: type = " + type + " enableAdjustBitrate = " + enableAdjustBitrate);
        // 码率、分辨率自适应都关闭
        boolean autoBitrate = enableAdjustBitrate;
        boolean autoResolution = false;
        switch (type) {
            case TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION: */
/*360p*//*

                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION, autoBitrate, autoResolution);
//                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION: */
/*540p*//*

                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION, autoBitrate, autoResolution);
//                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION: */
/*720p*//*

                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION, autoBitrate, autoResolution);
//                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_720_1280;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_LINKMIC_MAIN_PUBLISHER: */
/*连麦大主播*//*

                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_LINKMIC_MAIN_PUBLISHER, autoBitrate, autoResolution);
//                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_LINKMIC_SUB_PUBLISHER: */
/*连麦小主播*//*

                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_LINKMIC_SUB_PUBLISHER, autoBitrate, autoResolution);
//                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_320_480;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_REALTIEM_VIDEOCHAT: */
/*实时*//*

                if (mLivePusher != null) {
                    mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_REALTIEM_VIDEOCHAT, autoBitrate, autoResolution);
//                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;
                }
                break;
            default:
                break;
        }
        // 设置场景化配置后，SDK 内部会根据场景自动选择相关的配置参数，所以我们这里把内部的config获取出来，赋值到外部。
        mLivePushConfig = mLivePusher.getConfig();

        // 是否开启硬件加速
        if (true) {
            mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_HARDWARE);
            mLivePusher.setConfig(mLivePushConfig);
        }
    }


    //观察屏幕旋转设置变化，类似于注册动态广播监听变化机制
    private static class RotationObserver extends ContentObserver {
        WeakReference<MainActivity> mainActivityWeakReference;
//        ContentResolver mResolver;

        RotationObserver(MainActivity mainActivity) {
            super(null);
            mainActivityWeakReference = new WeakReference<>(mainActivity);
//            mResolver = mainActivity.getContentResolver();
        }

        //屏幕旋转设置改变时调用
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity == null) return;
            //更新按钮状态
            if (isActivityCanRotation(mainActivity)) {
                onActivityRotation(mainActivity);
            } else {
                mainActivity.mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
                mainActivity.mLivePusher.setRenderRotation(0);
                mainActivity.mLivePusher.setConfig(mainActivity.mLivePushConfig);
            }
        }
    }

    private void startObserver(ContentResolver mResolver) {
        mResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION), false, mRotationObserver);
    }

    private void stopObserver(ContentResolver mResolver) {
        mResolver.unregisterContentObserver(mRotationObserver);
        mRotationObserver = null;
        mResolver = null;
    }

    */
/**
     * 判断Activity是否可旋转。只有在满足以下条件的时候，Activity才是可根据重力感应自动旋转的。
     * 系统“自动旋转”设置项打开；
     *
     * @return false---Activity可根据重力感应自动旋转
     *//*

    protected static boolean isActivityCanRotation(MainActivity mainActivity) {
        // 判断自动旋转是否打开
        int flag = Settings.System.getInt(mainActivity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        if (flag == 0) {
            return false;
        }
        return true;
    }

    protected static void onActivityRotation(MainActivity mainActivity) {
        // 自动旋转打开，Activity随手机方向旋转之后，需要改变推流方向
        int mobileRotation = mainActivity.getWindowManager().getDefaultDisplay().getRotation();
        int pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
        boolean screenCaptureLandscape = false;
        switch (mobileRotation) {
            case Surface.ROTATION_0:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
                break;
            case Surface.ROTATION_180:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_UP;
                break;
            case Surface.ROTATION_90:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT;
                screenCaptureLandscape = true;
                break;
            case Surface.ROTATION_270:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_LEFT;
                screenCaptureLandscape = true;
                break;
            default:
                break;
        }
        mainActivity.mLivePusher.setRenderRotation(0); //因为activity也旋转了，本地渲染相对正方向的角度为0。
        mainActivity.mLivePushConfig.setHomeOrientation(pushRotation);
        if (mainActivity.mLivePusher.isPushing()) {
            int VIDEO_SRC_CAMERA = 0;
            int VIDEO_SRC_SCREEN = 1;
            int videoSrc = VIDEO_SRC_CAMERA;
            if (VIDEO_SRC_CAMERA == videoSrc) {
                mainActivity.mLivePusher.setConfig(mainActivity.mLivePushConfig);
                mainActivity.mLivePusher.stopCameraPreview(true);
                mainActivity.mLivePusher.startCameraPreview(mainActivity.mCaptureView);
            } else if (VIDEO_SRC_SCREEN == videoSrc) {
                //录屏横竖屏推流的判断条件是，视频分辨率取360*640还是640*360
                int currentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;
                switch (currentVideoResolution) {
                    case TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640:
                        if (screenCaptureLandscape)
                            mainActivity.mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_640_360);
                        else
                            mainActivity.mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640);
                        break;
                    case TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960:
                        if (screenCaptureLandscape)
                            mainActivity.mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_960_540);
                        else
                            mainActivity.mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960);
                        break;
                    case TXLiveConstants.VIDEO_RESOLUTION_TYPE_720_1280:
                        if (screenCaptureLandscape)
                            mainActivity.mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_1280_720);
                        else
                            mainActivity.mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_720_1280);
                        break;
                    default:
                }
                mainActivity.mLivePusher.setConfig(mainActivity.mLivePushConfig);
                mainActivity.mLivePusher.stopScreenCapture();
                mainActivity.mLivePusher.startScreenCapture();
            }
        }
    }


    static class TXPhoneStateListener extends PhoneStateListener {
        WeakReference<TXLivePusher> mPusher;

        TXPhoneStateListener(TXLivePusher pusher) {
            mPusher = new WeakReference<>(pusher);
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            TXLivePusher pusher = mPusher.get();
            if (pusher == null) return;
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    pusher.pausePusher();
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    pusher.pausePusher();
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    pusher.resumePusher();
                    break;
                default:
            }
        }
    }

    @Override
    public void onFilterChanged(FilterEnum filterEnum) {
        Bitmap lookupBitmap = null;
        switch (filterEnum) {
            case NO_FILTER:
                break;
            case ROMANTIC_FILTER:
                lookupBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.filter_langman);
                break;
            case FRESH_FILTER:
                lookupBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.filter_qingxin);
                break;
            case BEAUTIFUL_FILTER:
                lookupBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.filter_weimei);
                break;
            case PINK_FILTER:
                lookupBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.filter_fennen);
                break;
            case NOSTALGIC_FILTER:
                lookupBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.filter_huaijiu);
                break;
            case COOL_FILTER:
                lookupBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.filter_qingliang);
                break;
            case BLUES_FILTER:
                lookupBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.filter_landiao);
                break;
            case JAPANESE_FILTER:
                lookupBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.filter_rixi);
                break;
        }
        mLivePusher.setFilter(lookupBitmap);
    }

    @Override
    public void onBeautyOrigin() {
        setTxFilter();
    }

    @Override
    public void onMeiBaiChanged(int progress) {
        setTxFilter();
    }

    @Override
    public void onMoPiChanged(int progress) {
        setTxFilter();
    }

    @Override
    public void onFengNenChanged(int progress) {
        setTxFilter();
    }


    static class DiaLogListener implements DialogInterface.OnClickListener {
        WeakReference<MainActivity> activityWeakReference;
        DiaLogListener(MainActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            MainActivity activity = activityWeakReference.get();
            if (activity == null)return;
            switch (which){
                case -1:
                    dialog.cancel();
                    MHSDK.getInstance().clearVerNote();
                    activity.finish();
//                    System.exit(0);
                    break;
                case -2:
                    dialog.cancel();
                    break;
            }
        }
    }

    static class SDKDialogListener implements DialogInterface.OnClickListener {
        WeakReference<MainActivity> activityWeakReference;
        SDKDialogListener(MainActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            MainActivity activity = activityWeakReference.get();
            if (activity == null)return;
            switch (which){
                case -1:
                    activity.retryCount ++;
//                    MHSDK.getInstance().init(activity.getApplicationContext(), "558bb8022b7043756d62b497fea77183");
                    MHSDK.getInstance().clearVerNote();
                    VerifyManager.getInstance().verifyLicence(activity, "558bb8022b7043756d62b497fea77183");
                    activity.initBeautyView();
                    dialog.cancel();
//                    System.exit(0);
                    break;
                case -2:
                    dialog.cancel();
                    break;
            }
        }
    }

    //由于demo进入应用直接进入美颜界面，为了防止授权校验未完成而一直显示标准版界面，需要等授权校验完成后初始化对应的美颜界面（延迟1秒或者按下面处理）
    static class BeautyThread extends  Thread {
        WeakReference<MainActivity> mainActivityWeakReference;

        BeautyThread(MainActivity mainActivity) {
            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void run() {
            while (mainActivityWeakReference.get()!= null && !MHSDK.getInstance().isVerEnd()) {
                if (isInterrupted()) return;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            MainActivity activity = mainActivityWeakReference.get();
            if (activity != null){
                activity.mHandler.post(activity::setBeautyView);
            }
        }
    }

}*/
