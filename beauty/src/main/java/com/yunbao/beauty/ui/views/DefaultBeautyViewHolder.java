package com.yunbao.beauty.ui.views;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.beauty.R;
import com.yunbao.beauty.ui.adapter.StickerPagerAdapter;
import com.yunbao.beauty.ui.bean.StickerCategaryBean;

import java.util.List;
import java.util.Objects;


/**
 * Created by kxr on 2019/11/01.
 * 默认美颜
 */

public class DefaultBeautyViewHolder extends BaseBeautyViewHolder implements View.OnClickListener {

    public DefaultBeautyViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    public  DefaultBeautyViewHolder(Context context, ViewGroup parentView, Object... args) {
        super(context, parentView, args);
    }

    @Override
    protected int getLayoutId() {
        return super.getLayoutId();
    }

    @Override
    public void init() {
        super.init();
    }

//    @Override
//    protected void initShapeView() {
//        String[] shapeNames = mContext.getResources().getStringArray(R.array.name_default_beauty_shape);
//        TextSeekBarNew shapeSeekbar = mShapeControlView.findViewById(R.id.tsb_beauty_shape);
//        shapeSeekbar.setVisibility(View.GONE);
//        shownSeekBarsMap.put(R.string.beauty_shape, shapeSeekbar);
//        OnItemClickListener<ShapeBean> onItemClickListener = (bean, position) -> {
//            String shapeName = bean.getShapeName();
//            curShownProgressName = shapeName;
//            beautyShownName[1] = shapeName;
//            if (position == 0) {
////                processMap.clear();
//                DefaultBeautyViewHolder.this.onShapeOrigin();
//                shapeSeekbar.setVisibility(View.GONE);
//            } else {
////                Integer process = processMap.get(shapeName);
////                shapeSeekbar.setProgress(process == null ? 0 : process);
//                int beautyProgress = com.meihu.beautylibrary.ui.views.BeautyDataModel.getInstance().getBeautyProgress(shapeName);
//                shapeSeekbar.setProgress(beautyProgress);
//                shapeSeekbar.setVisibility(View.VISIBLE);
//            }
//        };
//        RecyclerView shapeRecyclerView = mShapeControlView.findViewById(R.id.rv_beauty_shape);
//
//        shapeSeekbar.setOnSeekChangeListener((view, progress) -> {
//            BeautyDataModel.getInstance().setBeautyProgress(curShownProgressName, progress);
//            if (curShownProgressName.equals(shapeNames[0])) {
////                mEffectListener.onShapeOrigin();
//                DefaultBeautyViewHolder.this.onShapeOrigin();
//            } else if (curShownProgressName.equals(shapeNames[1])) {
////                mEffectListener.onBigEyeChanged(progress);
//                DefaultBeautyViewHolder.this.onBigEyeChanged(progress);
//            } else if (curShownProgressName.equals(shapeNames[2])) {
////                mEffectListener.onFaceChanged(progress);
//                DefaultBeautyViewHolder.this.onFaceChanged(progress);
//            }
//        });
//        shapeRecyclerView.setHasFixedSize(true);
//        shapeRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
//        DefaultShapeAdapter DefaultShapeAdapterNew = new DefaultShapeAdapter(mContext);
//        DefaultShapeAdapterNew.setOnItemClickListener(onItemClickListener);
//        shapeRecyclerView.setAdapter(DefaultShapeAdapterNew);
//    }

    @Override
    public void setStickerCategaryData(List<StickerCategaryBean> titles) {
        ViewPager viewpager = mStickerControlView.findViewById(R.id.vp_beauty_sticker);
        stickerPagerAdapter = new StickerPagerAdapter(titles,null);
        stickerPagerAdapter.setEffectListener(this);
        viewpager.setAdapter(stickerPagerAdapter);
        TabLayout tabs = mStickerControlView.findViewById(R.id.tl_beauty_sticker);
        ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setTabSelected(Objects.requireNonNull(tabs.getTabAt(position)), position, true);
                int count = tabs.getTabCount();
                for (int i = 0; i < count; i++) {
                    if (i != position) {
                        setTabSelected(Objects.requireNonNull(tabs.getTabAt(i)), i, false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
        viewpager.setOnPageChangeListener(mOnPageChangeListener);
        View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                viewpager.setCurrentItem(pos, true);
            }
        };
        for (int i = 0; i < titles.size(); i++) {
            TabLayout.Tab tab = tabs.newTab();
            TextView textView = new TextView(mContext);
            textView.setText(titles.get(i).getName());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
            textView.setTextColor(mContext.getResources().getColor(R.color.tab_default_text_color));
            tab.setCustomView(textView);
            if (tab.getCustomView() != null) {
                View tabView = (View) tab.getCustomView().getParent();
                tabView.setTag(i);
                tabView.setOnClickListener(mTabOnClickListener);
            }
            tabs.addTab(tab, i);
        }
        setTabSelected(Objects.requireNonNull(tabs.getTabAt(0)), 0, true);
    }

    protected void setTabSelected(TabLayout.Tab tabAt, int position, boolean isSelected) {
        super.setTabSelected(tabAt, position, isSelected);
    }

    @Override
    public void release() {
        super.release();
    }

    @Override
    public void setVisibleListener(VisibleListener visibleListener) {
        super.setVisibleListener(visibleListener);
    }

//    @Override
//    public void onSpeciallyChanged(String speciallyName) {
//        if (mhBeautyManager == null) return;
//        mhBeautyManager.setSpeciallyEffect(speciallyName);
//    }
//
//    @Override
//    public void onDistortionChanged(String distortionName) {
//        if (mhBeautyManager == null) return;
//        mhBeautyManager.setDistortionEffect(distortionName);
//    }
}
