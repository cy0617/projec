package com.yunbao.live.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yunbao.common.bean.GoodsBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.IntentHelper;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.live.R;
import com.yunbao.live.http.LiveHttpUtil;

import java.lang.ref.WeakReference;

public class ShopPopWindow {
    private Activity mContext;
    private PopupWindow mPopupWindow;
    private TextView mTvPrice;
    private ImageView mIvThumb;
    private GoodsBean mGoodsBean;

    public ShopPopWindow(Activity context) {
        mContext = new WeakReference<>(context).get();
    }
    public void showPopWindow(View view) {
        View root = mContext.getLayoutInflater().inflate(R.layout.view_shop_explaining, null);
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(root, DpUtil.dp2px(120), DpUtil.dp2px(140), true);
            mPopupWindow.setOutsideTouchable(true);
            mTvPrice = root.findViewById(R.id.price);
            mIvThumb = root.findViewById(R.id.thumb);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mGoodsBean != null) {
                        RouteUtil.forwardGoodsDetail(mGoodsBean.getId(), false);
                    }
                }
            });
            root.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                }
            });
        }
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAsDropDown(view, -DpUtil.dp2px(60) + DpUtil.dp2px(20), 0, Gravity.TOP);
        }
    }

    public void setData(GoodsBean goodsBean) {
        mGoodsBean = goodsBean;
        if (goodsBean == null) {
            return;
        }
        if (mTvPrice != null) {
            mTvPrice.setText("¥" + goodsBean.getPriceNow());
        }
        ImgLoader.display(mContext, goodsBean.getThumb(), mIvThumb);
    }

    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public void release(){
        dismiss();
        mPopupWindow=null;
        mContext=null;
    }


}
