package com.yunbao.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.GoodsBean;

/**
 * Created by cxf on 2019/2/25.
 */

public class RouteUtil {
    //Intent隐式启动 action
    public static final String PATH_LAUNCHER = "/app/LauncherActivity";
    public static final String PATH_LOGIN_INVALID = "/main/LoginInvalidActivity";
    public static final String PATH_USER_HOME = "/main/UserHomeActivity";
    public static final String PATH_COIN = "/main/MyCoinActivity";
    public static final String PATH_GOODS = "/main/ShopGoodsActivity";
    public static final String PATH_CASH_ACCOUNT = "/main/CashActivity";
    public static final String PATH_VIDEO_RECORD = "/main/ActiveVideoRecordActivity";
    public static final String PATH_ACTIVE_VIDEO_PLAY = "/main/ActiveVideoPlayActivity";
    public static final String PATH_MALL_BUYER = "/mall/BuyerActivity";
    public static final String PATH_MALL_SELLER = "/mall/SellerActivity";
    public static final String PATH_MALL_GOODS_SEARCH = "/mall/GoodsSearchActivity";
    public static final String PATH_MALL_GOODS_DETAIL = "/mall/GoodsDetailActivity";
    public static final String PATH_MALL_PAY_CONTENT_DETAIL = "/mall/PayContentDetailActivity";
    public static final String PATH_MALL_ORDER_MSG = "/mall/OrderMessageActivity";

    /**
     * 启动页
     */
    public static void forwardLauncher(Context context) {
        ARouter.getInstance().build(PATH_LAUNCHER)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .navigation();
    }

    /**
     * 登录过期
     */
    public static void forwardLoginInvalid(String tip) {
        ARouter.getInstance().build(PATH_LOGIN_INVALID)
                .withString(Constants.TIP, tip)
                .navigation();
    }

    /**
     * 跳转到个人主页
     */
    public static void forwardUserHome(Context context, String toUid) {
        forwardUserHome(context, toUid, false, null);
    }

    /**
     * 跳转到个人主页
     */
    public static void forwardUserHome(Context context, String toUid, boolean fromLiveRoom, String fromLiveUid) {
        ARouter.getInstance().build(PATH_USER_HOME)
                .withString(Constants.TO_UID, toUid)
                .withBoolean(Constants.FROM_LIVE_ROOM, fromLiveRoom)
                .withString(Constants.LIVE_UID, fromLiveUid)
                .navigation();
    }

    /**
     * 跳转到充值页面
     */
    public static void forwardMyCoin(Context context) {
        ARouter.getInstance().build(PATH_COIN).navigation();
    }


//    public static void forwardGoods(Context context, GoodsBean goodsBean, String storeId, boolean mustBuy) {
//        Postcard postcard = ARouter.getInstance().build(PATH_GOODS);
//        postcard.withParcelable(Constants.GOODS, goodsBean);
//        postcard.withBoolean(Constants.MUST_BUY, mustBuy);
//
//        if (!TextUtils.isEmpty(storeId)) {
//            postcard.withString(Constants.UID, storeId);
//        }
//        postcard.navigation(context);
//    }
//
//    public static void forwardGoods(Context context, GoodsBean goodsBean, String storeId) {
//        forwardGoods(context, goodsBean, storeId, false);
//    }


    public static void videoRecord(Activity activity, int requestCode) {
        ARouter.getInstance().build(PATH_VIDEO_RECORD)
                .navigation(activity, requestCode);
    }

    public static void searchMallGoods(Activity activity, int requestCode) {
        ARouter.getInstance().build(PATH_MALL_GOODS_SEARCH)
                .navigation(activity, requestCode);
    }

    /**
     * 提现 选择账户
     */
    public static void forwardCashAccount(String accountId) {
        ARouter.getInstance().build(PATH_CASH_ACCOUNT)
                .withString(Constants.CASH_ACCOUNT_ID, accountId)
                .navigation();
    }

    /**
     * 商品详情页面
     */
    public static void forwardGoodsDetail(String goodsId, boolean fromShop) {
        ARouter.getInstance().build(PATH_MALL_GOODS_DETAIL)
                .withString(Constants.MALL_GOODS_ID, goodsId)
                .withBoolean(Constants.MALL_GOODS_FROM_SHOP, fromShop)
                .navigation();
    }


    /**
     * 付费内容详情页面
     */
    public static void forwardPayContentDetail(String goodsId) {
        ARouter.getInstance().build(PATH_MALL_PAY_CONTENT_DETAIL)
                .withString(Constants.MALL_GOODS_ID, goodsId)
                .navigation();
    }


    /**
     * 跳转到视频播放
     */
    public static void forwardVideoPlay(String videoUrl, String coverImgUrl) {
        ARouter.getInstance().build(PATH_ACTIVE_VIDEO_PLAY)
                .withString(Constants.VIDEO_PATH, videoUrl)
                .withString(Constants.URL, coverImgUrl)
                .navigation();
    }


    public static void forward(String path) {
        ARouter.getInstance().build(path).navigation();
    }
}
