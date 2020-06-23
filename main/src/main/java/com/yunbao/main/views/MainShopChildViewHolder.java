package com.yunbao.main.views;

        import android.content.Context;
        import android.view.ViewGroup;

        import com.yunbao.common.views.AbsMainViewHolder;
        import com.yunbao.main.R;

/**
 * 商圈
 */
public class MainShopChildViewHolder extends AbsMainViewHolder {
    public MainShopChildViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_shop_child;
    }

    @Override
    public void init() {

    }
}
