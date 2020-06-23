package com.yunbao.main.http;

        import com.alibaba.fastjson.JSON;
        import com.alibaba.fastjson.JSONObject;
        import com.yunbao.common.CommonAppConfig;
        import com.yunbao.common.bean.GoodsBean;
        import com.yunbao.common.bean.UserBean;
        import com.yunbao.common.http.CommonHttpUtil;
        import com.yunbao.common.http.HttpCallback;
        import com.yunbao.common.http.HttpClient;
        import com.yunbao.common.interfaces.CommonCallback;
        import com.yunbao.common.utils.MD5Util;
        import com.yunbao.common.utils.SpUtil;
        import com.yunbao.common.utils.StringUtil;

        import java.io.File;

/**
 * Created by cxf on 2018/9/17.
 */

public class MainHttpUtil {

    private static final String SALT = "76576076c1f5f657b634e966c8836a06";
    private static final String DEVICE = "android";

    /**
     * 取消网络请求
     */
    public static void cancel(String tag) {
        HttpClient.getInstance().cancel(tag);
    }

    /**
     * 手机号 密码登录
     */
    public static void login(String phoneNum, String pwd, HttpCallback callback) {
        HttpClient.getInstance().get("Login.userLogin", MainHttpConsts.LOGIN)
                .params("user_login", phoneNum)
                .params("user_pass", pwd)
                .execute(callback);

    }

    /**
     * 第三方登录
     */
    public static void loginByThird(String openid, String nicename, String avatar, String type, HttpCallback callback) {
        String sign = MD5Util.getMD5("openid=" + openid + "&" + SALT);
        HttpClient.getInstance().get("Login.userLoginByThird", MainHttpConsts.LOGIN_BY_THIRD)
                .params("openid", openid)
                .params("nicename", nicename)
                .params("avatar", avatar)
                .params("type", type)
                .params("source", DEVICE)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 请求签到奖励
     */
    public static void requestBonus(HttpCallback callback) {
        HttpClient.getInstance().get("User.Bonus", MainHttpConsts.REQUEST_BONUS)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 获取登录信息，三方登录类型，服务和隐私条款
     */
    public static void getLoginInfo(HttpCallback callback) {
        HttpClient.getInstance().get("Home.getLogin", MainHttpConsts.GET_LOGIN_INFO)
                .execute(callback);
    }


    /**
     * 更新极光推送id
     */
    public static void updatePushId(String pushId) {
        HttpClient.getInstance().get("Login.upUserPush", "updatePushId")
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("pushid", pushId)
                .execute(CommonHttpUtil.NO_CALLBACK);
    }


    /**
     * 获取签到奖励
     */
    public static void getBonus(HttpCallback callback) {
        HttpClient.getInstance().get("User.getBonus", MainHttpConsts.GET_BONUS)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 用于用户首次登录设置分销关系
     */
    public static void setDistribut(String code, HttpCallback callback) {
        HttpClient.getInstance().get("User.setDistribut", MainHttpConsts.SET_DISTRIBUT)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("code", code)
                .execute(callback);
    }

    /**
     * 首页直播
     */
    public static void getHot(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Home.getHot", MainHttpConsts.GET_HOT)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 首页
     */
    public static void getFollow(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Home.getFollow", MainHttpConsts.GET_FOLLOW)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("p", p)
                .execute(callback);
    }

    /**
     * 首页 附近
     */
    public static void getNear(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Home.getNearby", MainHttpConsts.GET_NEAR)
                .params("lng", CommonAppConfig.getInstance().getLng())
                .params("lat", CommonAppConfig.getInstance().getLat())
                .params("p", p)
                .execute(callback);
    }


    //排行榜  收益榜
    public static void profitList(String type, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Home.profitList", MainHttpConsts.PROFIT_LIST)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("type", type)
                .params("p", p)
                .execute(callback);
    }

    //排行榜  贡献榜
    public static void consumeList(String type, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Home.consumeList", MainHttpConsts.CONSUME_LIST)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("type", type)
                .params("p", p)
                .execute(callback);

    }


    /**
     * 获取用户信息
     */
    public static void getBaseInfo(String uid, String token, final CommonCallback<UserBean> commonCallback) {
        HttpClient.getInstance().get("User.getBaseInfo", MainHttpConsts.GET_BASE_INFO)
                .params("uid", uid)
                .params("token", token)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0 && info.length > 0) {
                            JSONObject obj = JSON.parseObject(info[0]);
                            UserBean bean = JSON.toJavaObject(obj, UserBean.class);
                            CommonAppConfig.getInstance().setUserBean(bean);



                            CommonAppConfig.getInstance().setUserItemList(obj.getString("list"),obj.getString("list2"));
                            SpUtil.getInstance().setStringValue(SpUtil.USER_INFO, info[0]);
                            if (commonCallback != null) {
                                commonCallback.callback(bean);
                            }
                        }
                    }

                    @Override
                    public void onError() {
                        if (commonCallback != null) {
                            commonCallback.callback(null);
                        }
                    }
                });
    }


    /**
     * 获取用户信息
     */
    public static void getBaseInfo(CommonCallback<UserBean> commonCallback) {
        getBaseInfo(CommonAppConfig.getInstance().getUid(),
                CommonAppConfig.getInstance().getToken(),
                commonCallback);
    }


    /**
     * 用户个人主页信息
     */
    public static void getUserHome(String touid, HttpCallback callback) {
        HttpClient.getInstance().get("User.getUserHome", MainHttpConsts.GET_USER_HOME)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("touid", touid)
                .execute(callback);
    }

    /**
     * 拉黑对方， 解除拉黑
     */
    public static void setBlack(String toUid, HttpCallback callback) {
        HttpClient.getInstance().get("User.setBlack", MainHttpConsts.SET_BLACK)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("touid", toUid)
                .execute(callback);
    }

    /**
     * 获取个性设置列表
     */
    public static void getSettingList(HttpCallback callback) {
        HttpClient.getInstance().get("User.getPerSetting", MainHttpConsts.GET_SETTING_LIST)
                .execute(callback);
    }

    /**
     * 搜索
     */
    public static void search(String key, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Home.search", MainHttpConsts.SEARCH)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("key", key)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 获取对方的关注列表
     *
     * @param touid 对方的uid
     */
    public static void getFollowList(String touid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.getFollowsList", MainHttpConsts.GET_FOLLOW_LIST)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("touid", touid)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 获取对方的粉丝列表
     *
     * @param touid 对方的uid
     */
    public static void getFansList(String touid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.getFansList", MainHttpConsts.GET_FANS_LIST)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("touid", touid)
                .params("p", p)
                .execute(callback);

    }

    /**
     * 上传头像，用post
     */
    public static void updateAvatar(File file, HttpCallback callback) {
        HttpClient.getInstance().post("User.updateAvatar", MainHttpConsts.UPDATE_AVATAR)
                .isMultipart(true)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("file", file)
                .execute(callback);
    }

    /**
     * 更新用户资料
     *
     * @param fields 用户资料 ,以json形式出现
     */
    public static void updateFields(String fields, HttpCallback callback) {
        HttpClient.getInstance().get("User.updateFields", MainHttpConsts.UPDATE_FIELDS)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("fields", fields)
                .execute(callback);
    }


    /**
     * 获取 我的收益 可提现金额数
     */
    public static void getProfit(HttpCallback callback) {
        HttpClient.getInstance().get("User.getProfit", MainHttpConsts.GET_PROFIT)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 获取 提现账户列表
     */
    public static void getCashAccountList(HttpCallback callback) {
        HttpClient.getInstance().get("User.GetUserAccountList", MainHttpConsts.GET_USER_ACCOUNT_LIST)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }


    /**
     * 添加 提现账户
     */
    public static void addCashAccount(String account, String name, String bank, int type, HttpCallback callback) {
        HttpClient.getInstance().get("User.SetUserAccount", MainHttpConsts.ADD_CASH_ACCOUNT)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("account", account)
                .params("name", name)
                .params("account_bank", bank)
                .params("type", type)
                .execute(callback);
    }

    /**
     * 删除 提现账户
     */
    public static void deleteCashAccount(String accountId, HttpCallback callback) {
        HttpClient.getInstance().get("User.delUserAccount", MainHttpConsts.DEL_CASH_ACCOUNT)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("id", accountId)
                .execute(callback);
    }

    /**
     * 提现
     */
    public static void doCash(String votes, String accountId, HttpCallback callback) {
        HttpClient.getInstance().get("User.setCash", MainHttpConsts.DO_CASH)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("cashvote", votes)//提现的票数
                .params("accountid", accountId)//账号ID
                .execute(callback);
    }


    /**
     * 分类直播
     */
    public static void getClassLive(int classId, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Home.getClassLive", MainHttpConsts.GET_CLASS_LIVE)
                .params("liveclassid", classId)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 获取自己收到的主播印象列表
     */
    public static void getMyImpress(HttpCallback callback) {
        HttpClient.getInstance().get("User.GetMyLabel", MainHttpConsts.GET_MY_IMPRESS)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 用于用户首次登录推荐
     */
    public static void getRecommend(HttpCallback callback) {
        HttpClient.getInstance().get("Home.getRecommend", MainHttpConsts.GET_RECOMMEND)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .execute(callback);
    }


    /**
     * 用于用户首次登录推荐,关注主播
     */
    public static void recommendFollow(String touid, HttpCallback callback) {
        HttpClient.getInstance().get("Home.attentRecommend", MainHttpConsts.RECOMMEND_FOLLOW)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("touid", touid)
                .execute(callback);
    }

    /**
     * 获取验证码接口 注册用
     */
    public static void getRegisterCode(String mobile, HttpCallback callback) {
        String sign = MD5Util.getMD5("mobile=" + mobile + "&" + SALT);
        HttpClient.getInstance().get("Login.getCode", MainHttpConsts.GET_REGISTER_CODE)
                .params("mobile", mobile)
                .params("sign", sign)
                .execute(callback);
    }

    /**
     * 手机注册接口
     */
    public static void register(String user_login, String pass, String pass2, String code,String inviteCode, HttpCallback callback) {
        HttpClient.getInstance().get("Login.userReg", MainHttpConsts.REGISTER)
                .params("user_login", user_login)
                .params("user_pass", pass)
                .params("user_pass2", pass2)
                .params("code", code)
                .params("agent_code", inviteCode)
                .params("source", DEVICE)
                .execute(callback);
    }

    /**
     * 找回密码
     */
    public static void findPwd(String user_login, String pass, String pass2, String code, HttpCallback callback) {
        HttpClient.getInstance().get("Login.userFindPass", MainHttpConsts.FIND_PWD)
                .params("user_login", user_login)
                .params("user_pass", pass)
                .params("user_pass2", pass2)
                .params("code", code)
                .execute(callback);
    }


    /**
     * 重置密码
     */
    public static void modifyPwd(String oldpass, String pass, String pass2, HttpCallback callback) {
        HttpClient.getInstance().get("User.updatePass", MainHttpConsts.MODIFY_PWD)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("oldpass", oldpass)
                .params("pass", pass)
                .params("pass2", pass2)
                .execute(callback);
    }


    /**
     * 获取验证码接口 找回密码用
     */
    public static void getFindPwdCode(String mobile, HttpCallback callback) {
        String sign = MD5Util.getMD5("mobile=" + mobile + "&" + SALT);
        HttpClient.getInstance().get("Login.getForgetCode", MainHttpConsts.GET_FIND_PWD_CODE)
                .params("mobile", mobile)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 三级分销页面 获取二维码
     */
    public static void getQrCode(HttpCallback callback) {
        HttpClient.getInstance().get("Agent.getCode", MainHttpConsts.GET_QR_CODE)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }

    /**
     * 获取店铺信息
     */

    public static void getShop(int p, String touid, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.GetShop", MainHttpConsts.GET_SHOP)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("p", p)
                .params("touid", touid)
                .execute(callback);
    }

    /**
     * 获取不含有推荐商品的店铺信息
     */

    public static void getShopInfo(String touid, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.getShopInfo", MainHttpConsts.GET_SHOP_INFO)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("touid", touid)
                .execute(callback);
    }


    /**
     * 发布商品
     */

    public static void setGoods(int type, GoodsBean goodsBean, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.SetGoods", MainHttpConsts.SET_GOODS)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("type", type)
                .params("name", goodsBean.getName())
                .params("href", goodsBean.getLink())
                .params("thumb", goodsBean.getThumb())
//                .params("old_price", goodsBean.getPriceOrigin())
                .params("price", goodsBean.getPriceNow())
                .params("des", goodsBean.getDes())
                .execute(callback);
    }

    /**
     * 获取推荐商品列表
     */

    public static void getRecommentGoods(String touid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.GetRecomment", MainHttpConsts.GET_RECOMMENT)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("touid", touid)
                .params("p", p)
                .execute(callback);
    }


    /*用于上架/下架商品 状态，-1下架1上架*/
    public static void upGoodsStatus(String goodsid, int status, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.UpStatus", MainHttpConsts.SHOP_UPSTATUS)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("goodsid", goodsid)
                .params("status", status)
                .execute(callback);
    }

    /*删除商品*/
    public static void delGoods(String goodsid, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.DelGoods", MainHttpConsts.SHOP_UPSTATUS)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("goodsid", goodsid)
                .execute(callback);
    }


    /*获取小程序商品*/
    public static void getApplets(String goodsid, HttpCallback callback) {
        HttpClient.getInstance().get("Shop.GetApplets", MainHttpConsts.GET_APPLETS)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("id", goodsid)
                .execute(callback);
    }


    /**
     * 发布动态
     *
     * @param activeType    动态类型：0：纯文字；1：文字+图片；2：文字+视频；3：文字+音频
     * @param text          文字内容
     * @param images        图片地址集合：每张图片地址之间用；号隔开
     * @param videoImage    视频封面
     * @param videoUrl      视频地址
     * @param address       详细地理位置
     * @param voiceUrl      语音地址
     * @param voiceDuration 语音时长
     * @param callback
     */
    public static void activePublish(
            int activeType, String text, String images, String videoImage,
            String videoUrl, String address, String voiceUrl, int voiceDuration,
            HttpCallback callback) {
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        String uid = appConfig.getUid();
        String sign = MD5Util.getMD5(StringUtil.contact("type=", String.valueOf(activeType), "&uid=", uid, "&", SALT));
        HttpClient.getInstance().get("Dynamic.setDynamic", MainHttpConsts.ACTIVE_PUBLISH)
                .params("uid", uid)
                .params("token", appConfig.getToken())
                .params("lat", appConfig.getLat())
                .params("lng", appConfig.getLng())
                .params("city", appConfig.getCity())
                .params("type", activeType)
                .params("title", text)
                .params("thumb", images)
                .params("video_thumb", videoImage)
                .params("href", videoUrl)
                .params("address", address)
                .params("voice", voiceUrl)
                .params("length", voiceDuration)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 获取个人中心的动态
     */
    public static void getActiveHome(String touid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Dynamic.getHomeDynamic", MainHttpConsts.GET_HOME_ACTIVE)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("touid", touid)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 获取首页推荐的动态
     */
    public static void getActiveRecommend(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Dynamic.getRecommendDynamics", MainHttpConsts.GET_ACTIVE_RECOMMEND)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }


    /**
     * 获取首页关注的动态
     */
    public static void getActiveFollow(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Dynamic.getAttentionDynamic", MainHttpConsts.GET_ACTIVE_FOLLOW)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }


    /**
     * 获取首页最新的动态
     */
    public static void getActiveNewest(int p, HttpCallback callback) {
        CommonAppConfig appConfig = CommonAppConfig.getInstance();
        HttpClient.getInstance().get("Dynamic.getNewDynamic", MainHttpConsts.GET_ACTIVE_NEWEST)
                .params("uid", appConfig.getUid())
                .params("token", appConfig.getToken())
                .params("lng", appConfig.getLng())
                .params("lat", appConfig.getLat())
                .params("p", p)
                .execute(callback);
    }


    /**
     * 点赞动态
     */
    public static void activeAddLike(String activeId, HttpCallback callback) {
        String uid = CommonAppConfig.getInstance().getUid();
        String sign = MD5Util.getMD5(StringUtil.contact("dynamicid=", activeId, "&uid=", uid, "&", SALT));
        HttpClient.getInstance().get("Dynamic.addLike", MainHttpConsts.ACTIVE_ADD_LIKE)
                .params("uid", uid)
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("dynamicid", activeId)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 获取举报动态内容列表
     */
    public static void getActiveReportList(HttpCallback callback) {
        HttpClient.getInstance().get("Dynamic.getReportlist", MainHttpConsts.GET_ACTIVE_REPORT_LIST)
                .execute(callback);
    }


    /**
     * 举报动态
     */
    public static void activeReport(String activeId, String content, HttpCallback callback) {
        HttpClient.getInstance().get("Dynamic.report", MainHttpConsts.ACTIVE_REPORT)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("dynamicid", activeId)
                .params("content", content)
                .execute(callback);
    }


    /**
     * 删除动态
     */
    public static void activeDelete(String activeId, HttpCallback callback) {
        HttpClient.getInstance().get("Dynamic.del", MainHttpConsts.ACTIVE_DELETE)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("dynamicid", activeId)
                .execute(callback);
    }


    /**
     * 评论动态
     *
     * @param activeId  动态ID
     * @param toUid     回复的评论UID
     * @param commentId 回复的评论commentid
     * @param parentId  回复的评论ID
     * @param content   内容
     * @param callback
     */
    public static void activeComment(String activeId, String toUid, String commentId, String parentId, String content, HttpCallback callback) {
        HttpClient.getInstance().get("Dynamic.setComment", MainHttpConsts.ACTIVE_COMMENT)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("dynamicid", activeId)
                .params("touid", toUid)
                .params("commentid", commentId)
                .params("parentid", parentId)
                .params("content", content)
                .params("type", 0)//0：文字；1：语音
                .params("voice", "")//语音Url
                .params("length", "")//语音时长
                .execute(callback);
    }


    /**
     * 获取动态评论列表
     */
    public static void getActiveComments(String activeId, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Dynamic.getComments", MainHttpConsts.GET_ACTIVE_COMMENTS)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("dynamicid", activeId)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 动态评论点赞
     */
    public static void setActiveCommentLike(String commentId, HttpCallback callback) {
        String uid = CommonAppConfig.getInstance().getUid();
        String sign = MD5Util.getMD5(StringUtil.contact("commentid=", commentId, "&uid=", uid, "&", SALT));
        HttpClient.getInstance().get("Dynamic.addCommentLike", MainHttpConsts.SET_ACTIVE_COMMENT_LIKE)
                .params("uid", uid)
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("commentid", commentId)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 获取动态评论回复
     */
    public static void getActiveCommentReply(String commentid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Dynamic.getReplys", MainHttpConsts.GET_ACTIVE_COMMENT_REPLY)
                .params("commentid", commentid)
                .params("uid", CommonAppConfig.getInstance().getUid())
                .params("token", CommonAppConfig.getInstance().getToken())
                .params("p", p)
                .execute(callback);
    }

    /**
     *
     */
//    public static void getGetuserauth(CommonCallback<GetuserauthBean> commonCallback) {
//        getGetuserauth(CommonAppConfig.getInstance().getUid(),
//                CommonAppConfig.getInstance().getToken(),
//                commonCallback);
//    }
    /**
     *
     * 判断用户是否认证成功
     */
    public static void getGetuserauth(HttpCallback callback) {
        HttpClient.getInstance().post("Youmio.Getuserauth",MainHttpConsts.GET_USER_AUTH)
                .isMultipart(true)
                .params("uid",CommonAppConfig.getInstance().getUid())
                .params("token",CommonAppConfig.getInstance().getToken())
                .execute(callback);
    }
//    /**
//     *
//     * 判断用户是否认证成功
//     */
//    public static void getGetuserauth(String uid,String token,final CommonCallback<GetuserauthBean> commonCallback) {
//        HttpClient.getInstance().post("Youmio.Getuserauth",MainHttpConsts.GET_USER_AUTH)
//                .isMultipart(true)
//                .params("uid",uid)
//                .params("token",token)
//                .execute(new HttpCallback() {
//                    @Override
//                    public void onSuccess(int code, String msg, String[] info) {
//                        Log.e("55555", "onSuccess: "+code );
//                        Log.e("55555", "onSuccess: "+msg);
//                        if (code==0&&info.length>0){
//                            JSONObject obj = JSON.parseObject(info[0]);
//                            GetuserauthBean bean = JSON.toJavaObject(obj, GetuserauthBean.class);
//
//                            String msg1 = bean.getMsg();
//                            int code1 = bean.getCode();
//
//                            if (commonCallback!=null){
//                                commonCallback.callback(bean);
//
//                            }
//                        }
//                    }
//                });
//    }
}






