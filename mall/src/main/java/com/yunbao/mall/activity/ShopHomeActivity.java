package com.yunbao.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.ConfigBean;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.custom.ItemDecoration;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.im.activity.ChatRoomActivity;
import com.yunbao.im.bean.ImUserBean;
import com.yunbao.im.http.ImHttpConsts;
import com.yunbao.im.http.ImHttpUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.adapter.ShopHomeAdapter;
import com.yunbao.mall.bean.GoodsSimpleBean;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.List;

/**
 * 卖家店铺主页
 */
public class ShopHomeActivity extends AbsActivity implements OnItemClickListener<GoodsSimpleBean>, View.OnClickListener {

    public static void forward(Context context, String toUid) {
        Intent intent = new Intent(context, ShopHomeActivity.class);
        intent.putExtra(Constants.TO_UID, toUid);
        context.startActivity(intent);
    }

    private CommonRefreshView mRefreshView;
    private ShopHomeAdapter mAdapter;
    private ImageView mAvatar;
    private TextView mName;
    private TextView mGoodsNum;
    private TextView mSaleNumAll;
    private TextView mGoodsQuality;
    private TextView mTaiDuFuWu;//服务态度
    private TextView mTaiDuWuLiu;//物流态度
    private String mToUid;
    private JSONObject mShopInfo;
    private String mUnitString;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_home;
    }

    @Override
    protected void main() {
        ConfigBean configBean = CommonAppConfig.getInstance().getConfig();
        if (configBean != null) {
            setTitle(configBean.getShopSystemName());
        } else {
            setTitle(WordUtil.getString(R.string.mall_001));
        }
        mToUid = getIntent().getStringExtra(Constants.TO_UID);
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_goods_seller_2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                }
                return 1;
            }
        });
        mRefreshView.setLayoutManager(gridLayoutManager);
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 10, 0);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshView.setItemDecoration(decoration);
        mAdapter = new ShopHomeAdapter(mContext);
        mAdapter.setOnItemClickListener(ShopHomeActivity.this);
        mRefreshView.setRecyclerViewAdapter(mAdapter);
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<GoodsSimpleBean>() {
            @Override
            public RefreshAdapter<GoodsSimpleBean> getAdapter() {
                return null;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                MallHttpUtil.getShopHome(mToUid, p, callback);
            }

            @Override
            public List<GoodsSimpleBean> processData(String[] info) {
                JSONObject obj = JSON.parseObject(info[0]);
                mShopInfo = obj.getJSONObject("shop_info");
                return JSON.parseArray(obj.getString("list"), GoodsSimpleBean.class);
            }

            @Override
            public void onRefreshSuccess(List<GoodsSimpleBean> list, int listCount) {
                if (mShopInfo != null) {
                    if (mAvatar != null) {
                        ImgLoader.displayAvatar(mContext, mShopInfo.getString("avatar"), mAvatar);
                    }
                    if (mName != null) {
                        mName.setText(mShopInfo.getString("name"));
                    }
                    if (mGoodsNum != null) {
                        mGoodsNum.setText(String.format(mUnitString, mShopInfo.getString("goods_nums")));
                    }
                    if (mSaleNumAll != null) {
                        mSaleNumAll.setText(String.format(mUnitString, mShopInfo.getString("sale_nums")));
                    }
                    if (mGoodsQuality != null) {
                        mGoodsQuality.setText(mShopInfo.getString("quality_points"));
                    }
                    if (mTaiDuFuWu != null) {
                        mTaiDuFuWu.setText(mShopInfo.getString("service_points"));
                    }
                    if (mTaiDuWuLiu != null) {
                        mTaiDuWuLiu.setText(mShopInfo.getString("express_points"));
                    }
                }
            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<GoodsSimpleBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
        View headView = mAdapter.getHeadView();
        mAvatar = headView.findViewById(R.id.avatar);
        mName = headView.findViewById(R.id.name);
        mGoodsNum = headView.findViewById(R.id.goods_num);
        mSaleNumAll = headView.findViewById(R.id.sale_num_all);
        mGoodsQuality = headView.findViewById(R.id.goods_quality);
        mTaiDuFuWu = headView.findViewById(R.id.taidu_fuwu);
        mTaiDuWuLiu = headView.findViewById(R.id.taidu_wuliu);
        headView.findViewById(R.id.btn_cert).setOnClickListener(this);
        View btnKefu = headView.findViewById(R.id.btn_kefu);
        if (!TextUtils.isEmpty(mToUid) && mToUid.equals(CommonAppConfig.getInstance().getUid())) {
            btnKefu.setVisibility(View.INVISIBLE);
        } else {
            btnKefu.setOnClickListener(this);
        }
        mUnitString = WordUtil.getString(R.string.mall_168);
        mRefreshView.initData();
    }

    @Override
    public void onItemClick(GoodsSimpleBean bean, int position) {
        GoodsDetailActivity.forward(mContext, bean.getId(), true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_cert) {
            forwardCert();
        } else if (id == R.id.btn_kefu) {
            forwardChat();
        }
    }


    /**
     * 资质证明
     */
    private void forwardCert() {
        if (mShopInfo != null) {
            ShopDetailActivity.forward(mContext, mShopInfo.getString("certificate_desc"), mShopInfo.getString("certificate"));
        }
    }

    /**
     * 私信聊天
     */
    private void forwardChat() {
        if (mShopInfo == null) {
            return;
        }
        ImHttpUtil.getImUserInfo(mShopInfo.getString("uid"), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    ImUserBean bean = JSON.parseObject(info[0], ImUserBean.class);
                    if (bean != null) {
                        ChatRoomActivity.forward(mContext, bean, bean.getAttent() == 1, false);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_SHOP_HOME);
        ImHttpUtil.cancel(ImHttpConsts.GET_IM_USER_INFO);
        super.onDestroy();
    }
}
