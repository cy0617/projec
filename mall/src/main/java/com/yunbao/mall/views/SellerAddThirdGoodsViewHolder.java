package com.yunbao.mall.views;
import android.content.Context;
import android.view.ViewGroup;
import com.yunbao.mall.R;
/**
 * 卖家添加商品
 */
public class SellerAddThirdGoodsViewHolder extends AbsSellerAddGoodsViewHolder {

    public SellerAddThirdGoodsViewHolder(Context context, ViewGroup parentView, String goodsId) {
        super(context, parentView, goodsId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_seller_add_third_goods;
    }

    @Override
    public void init() {
        super.init();
        mEtGoodLinks=findViewById(R.id.et_link);
    }



}
