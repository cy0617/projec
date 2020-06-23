package com.yunbao.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.ViewPagerAdapter;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.AbsMainViewHolder;
import com.yunbao.mall.R;
import com.yunbao.mall.views.SellerAddGoodsViewHolder;
import com.yunbao.mall.views.SellerAddThirdGoodsViewHolder;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class SellerAddGoodsActivity extends AbsActivity {
    public static void forward(Context context, String goodsId) {
        Intent intent = new Intent(context, SellerAddGoodsActivity.class);
        if (!TextUtils.isEmpty(goodsId)) {
            intent.putExtra(Constants.MALL_GOODS_ID, goodsId);
        }
        context.startActivity(intent);
    }

    private MagicIndicator indicator;
    private ViewPager viewPager;
    private List<FrameLayout> mViewList;
    private AbsMainViewHolder[] mViewHolders;
    private SellerAddGoodsViewHolder mSellerAddGoodsViewHolder;
    private SellerAddThirdGoodsViewHolder mSellerAddThirdGoodsViewHolder;
    private String mGoodsId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seller_add_goods_1;
    }

    @Override
    protected void main() {
        super.main();
        mGoodsId = getIntent().getStringExtra(Constants.MALL_GOODS_ID);
        indicator = (MagicIndicator) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewList = new ArrayList<>();
        mViewHolders=new AbsMainViewHolder[2];
        for (int i = 0; i < 2; i++) {
            FrameLayout frameLayout = new FrameLayout(mContext);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mViewList.add(frameLayout);
        }
        viewPager.setAdapter(new ViewPagerAdapter(mViewList));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                loadPageData(position, true);
                if (mViewHolders != null) {
                    for (int i = 0, length = mViewHolders.length; i < length; i++) {
                        AbsMainViewHolder vh = mViewHolders[i];
                        if (vh != null) {
                            vh.setShowed(position == i);
                        }
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        final String[] titles = new String[]{WordUtil.getString(R.string.mall_376),WordUtil.getString(R.string.mall_377)};
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.getPaint().setFakeBoldText(true);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.gray4));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.global));
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewPager != null) {
                            viewPager.setCurrentItem(index);
                        }
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setXOffset(DpUtil.dp2px(5));
                linePagerIndicator.setRoundRadius(DpUtil.dp2px(2));
                linePagerIndicator.setColors(ContextCompat.getColor(mContext, R.color.global));

                return linePagerIndicator;
            }
        });
        indicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer();
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return DpUtil.dp2px(30);
            }
        });

        ViewPagerHelper.bind(indicator, viewPager);
        loadPageData(0, false);
    }

    private void loadPageData(int position, boolean needlLoadData) {
        if (mViewHolders == null) {
            return;
        }
        AbsMainViewHolder vh = mViewHolders[position];
        if (vh == null) {
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }
                if (position == 0) {
                    mSellerAddThirdGoodsViewHolder = new SellerAddThirdGoodsViewHolder(mContext, parent, mGoodsId);
                    vh = mSellerAddThirdGoodsViewHolder;
                } else if (position == 1) {
                    mSellerAddGoodsViewHolder = new SellerAddGoodsViewHolder(mContext, parent, mGoodsId);
                    vh = mSellerAddGoodsViewHolder;
                }
                if (vh == null) {
                    return;
                }
                mViewHolders[position] = vh;
                vh.addToParent();
                vh.subscribeActivityLifeCycle();
            }
        }
        if (needlLoadData && vh != null) {
            vh.loadData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
