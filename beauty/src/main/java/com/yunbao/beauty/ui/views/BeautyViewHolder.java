package com.yunbao.beauty.ui.views;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meihu.beautylibrary.MHSDK;
import com.yunbao.beauty.R;
import com.yunbao.beauty.ui.adapter.StickerPagerAdapter;
import com.yunbao.beauty.ui.bean.QuickBeautyBean;
import com.yunbao.beauty.ui.bean.StickerCategaryBean;
import com.yunbao.beauty.ui.enums.QuickBeautyEnum;
import com.yunbao.beauty.ui.enums.QuickBeautyShapeEnum;
import com.yunbao.beauty.ui.interfaces.StickerCanClickListener;
import com.yunbao.beauty.ui.views.custom.LTabTextView;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Kongxr on 2019/8/23.
 * 美颜UI相关
 */

public class BeautyViewHolder extends BaseBeautyViewHolder implements View.OnClickListener {

    private StickerCanClickListener mCanClickListener;

    BeautyViewHolder(Context context, ViewGroup parentView, StickerCanClickListener canClickListener) {
        super(context, parentView, canClickListener);
    }

    @Override
    protected void processArguments(Object... args) {
        if (args != null && args.length > 0) {
            Object obj = args[0];
            if (obj != null && obj instanceof StickerCanClickListener) {
                mCanClickListener = (StickerCanClickListener) obj;
            }
        }

    }

    @Override
    protected int getLayoutId() {
        return super.getLayoutId();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void setStickerCategaryData(List<StickerCategaryBean> titles) {
        stickerViewpager = mStickerControlView.findViewById(R.id.vp_beauty_sticker);
        stickerPagerAdapter = new StickerPagerAdapter(titles, mCanClickListener);
        stickerPagerAdapter.setEffectListener(this);
        stickerViewpager.setAdapter(stickerPagerAdapter);
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

        stickerViewpager.setOnPageChangeListener(mOnPageChangeListener);
        View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                stickerViewpager.setCurrentItem(pos, true);
            }
        };
        for (int i = 0; i < titles.size(); i++) {
            TabLayout.Tab tab = tabs.newTab();
            TextView textView;
            if (MHSDK.getInstance().getVer().equals("1") && (i == 1 || i == 3)) {
                textView = new LTabTextView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                textView.setLayoutParams(layoutParams);
            } else {
                textView = new TextView(mContext);
            }
            textView.setText(titles.get(i).getName());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
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

    @Override
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

    @Override
    public void onEyeBrowChanged(int progress) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setEyeBrow(progress);
        BeautyDataModel.getInstance().setBeautyProgress(beautyNames[5], progress);
    }

    @Override
    public void onEyeLengthChanged(int progress) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setEyeLength(progress);
        BeautyDataModel.getInstance().setBeautyProgress(beautyNames[6], progress);
    }

    @Override
    public void onEyeCornerChanged(int progress) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setEyeCorner(progress);
        BeautyDataModel.getInstance().setBeautyProgress(beautyNames[7], progress);
    }

    @Override
    public void onEyeAlatChanged(int progress) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setEyeAlat(progress);
        BeautyDataModel.getInstance().setBeautyProgress(beautyNames[15], progress);
    }

    @Override
    public void onLengthenNoseChanged(int progress) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setLengthenNoseLift(progress);
        BeautyDataModel.getInstance().setBeautyProgress(beautyNames[13], progress);
    }

    @Override
    public void onQuickBeautyChanged(QuickBeautyBean beautyBean) {
        if (mhBeautyManager == null) return;
        BeautyDataModel.getInstance().setQuickBeautyBean(beautyBean);
        String effectName = beautyBean.getQuickBeautyEnum().getString(mContext);
        if (!effectName.equals(mContext.getResources().getString(R.string.beauty_origin))) {
            textSeekBar.setMaxProgress(100);
            textSeekBar.setProgress(50);
            textSeekBar.setVisibility(View.VISIBLE);
            setQuickBeautyData(beautyBean);
        } else {
            onShapeOrigin();
            onBeautyOrigin();
        }
        BeautyDataModel.getInstance().setQuickProgress(50);
        beautyPagerAdapter.notifyRvNotifyDataSetChanged();
    }

    private void setQuickBeautyData(QuickBeautyBean beautyBean) {
//        Log.d("meihu_beauty", "setQuickBeautyData");
        HashMap<QuickBeautyShapeEnum, QuickBeautyBean.ElementValue> beautyMap = beautyBean.getBeautyMap();
        BeautyDataModel.getInstance().setQuickBeautyDataMap(beautyMap);
        QuickBeautyBean.ElementValue whiteValue = beautyMap.get(QuickBeautyShapeEnum.BEAUTY_WHITE);
        QuickBeautyBean.ElementValue mopiValue = beautyMap.get(QuickBeautyShapeEnum.BEAUTY_GRIND);
        QuickBeautyBean.ElementValue hongrunValue = beautyMap.get(QuickBeautyShapeEnum.BEAUTY_TENDER);
        onMeiBaiChanged(whiteValue.getValue());
        onMoPiChanged(mopiValue.getValue());
        onFengNenChanged(hongrunValue.getValue());
        QuickBeautyBean.ElementValue bigEyeValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_BIGEYE);
        QuickBeautyBean.ElementValue eyeBrowValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_EYEBROW);
        QuickBeautyBean.ElementValue eyeLengthValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_EYELENGTH);
        QuickBeautyBean.ElementValue eyeCornerValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_EYECORNER);
        QuickBeautyBean.ElementValue faceValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_FACE);
        QuickBeautyBean.ElementValue mouseValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_MOUSE);
        QuickBeautyBean.ElementValue noseValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_NOSE);
        QuickBeautyBean.ElementValue chinValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_CHIN);
        QuickBeautyBean.ElementValue foreHeadValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_FOREHEAD);
        QuickBeautyBean.ElementValue lengthValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_LENGTHNOSE);
        QuickBeautyBean.ElementValue shaveFaceValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_SHAVE_FACE);
        QuickBeautyBean.ElementValue eyeAlatValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_EYEALAT);
        int[] shapeData = new int[]{bigEyeValue.getValue(), eyeBrowValue.getValue(), eyeLengthValue.getValue(), eyeCornerValue.getValue(), faceValue.getValue(), mouseValue.getValue(), noseValue.getValue(), chinValue.getValue(), foreHeadValue.getValue(), lengthValue.getValue(), shaveFaceValue.getValue(), eyeAlatValue.getValue()};
        onBigEyeChanged(shapeData[0]);
        onEyeBrowChanged(shapeData[1]);
        onEyeLengthChanged(shapeData[2]);
        onEyeCornerChanged(shapeData[3]);
        onFaceChanged(shapeData[4]);
        onMouseChanged(shapeData[5]);
        onNoseChanged(shapeData[6]);
        onChinChanged(shapeData[7]);
        onForeheadChanged(shapeData[8]);
        onLengthenNoseChanged(shapeData[9]);
        onFaceShaveChanged(shapeData[10]);
        onEyeAlatChanged(shapeData[11]);
    }

}
