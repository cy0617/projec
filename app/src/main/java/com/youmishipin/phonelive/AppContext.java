package com.youmishipin.phonelive;

import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.meihu.beautylibrary.MHSDK;
import com.mob.MobSDK;
import com.qiniu.pili.droid.shortvideo.PLShortVideoEnv;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.live.TXLiveBase;
import com.yunbao.beauty.ui.views.BeautyDataModel;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.CommonAppContext;
import com.yunbao.common.bean.MeiyanConfig;
import com.yunbao.common.utils.L;
import com.yunbao.im.utils.ImMessageUtil;
import com.yunbao.im.utils.ImPushUtil;


/**
 * Created by cxf on 2017/8/3.
 */

public class AppContext extends CommonAppContext {

    public static AppContext sInstance;
    private boolean mBeautyInited;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        //腾讯云鉴权url
        String ugcLicenceUrl = "http://license.vod2.myqcloud.com/license/v1/e4b2bb15909b4b4fb6ee2aec824784dd/TXUgcSDK.licence";
        //腾讯云鉴权key
        String ugcKey = "175bf7f3b4bd7d2312c95e30a22fb7b5";
        TXLiveBase.getInstance().setLicence(this, ugcLicenceUrl, ugcKey);
        L.setDeBug(BuildConfig.DEBUG);
        //初始化腾讯bugly
        CrashReport.initCrashReport(this);
        CrashReport.setAppVersion(this, CommonAppConfig.getInstance().getVersion());
        //初始化ShareSdk
        MobSDK.init(this);
        //初始化极光推送
        ImPushUtil.getInstance().init(this);
        //初始化极光IM
        ImMessageUtil.getInstance().init();

        //初始化 ARouter
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
//        if (!LeakCanary.isInAnalyzerProcess(this)) {
//            LeakCanary.install(this);
//        }
        PLShortVideoEnv.init(this);
        L.e("=="+ com.tencent.rtmp.TXLiveBase.getSDKVersionStr());
    }

    /**
     * 初始化美狐
     */

    public void initBeautySdk(String beautyKey) {
        CommonAppConfig.getInstance().setBeautyKey(beautyKey);
        if (!TextUtils.isEmpty(beautyKey)) {
            if (!mBeautyInited) {
                mBeautyInited = true;
                MHSDK.getInstance().init(this, beautyKey);
                CommonAppConfig.getInstance().setTiBeautyEnable(true);

                //根据后台配置设置美颜参数
                MeiyanConfig meiyanConfig = CommonAppConfig.getInstance().getConfig().parseMeiyanConfig();
                int[] dataArray = meiyanConfig.getDataArray();
                BeautyDataModel.getInstance().setBeautyDataMap(dataArray);

                L.e("美狐初始化------->" + beautyKey);
            }
        } else {
            MHSDK.getInstance().init(this, "");
            CommonAppConfig.getInstance().setTiBeautyEnable(false);
        }
    }

}
