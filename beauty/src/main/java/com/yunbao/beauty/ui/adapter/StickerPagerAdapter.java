package com.yunbao.beauty.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.meihu.beautylibrary.MHSDK;
import com.yunbao.beauty.ui.interfaces.StickerCanClickListener;
import com.yunbao.beauty.ui.manager.StickerManager;
import com.yunbao.beauty.ui.bean.StickerBeautyBean;
import com.yunbao.beauty.ui.bean.StickerCategaryBean;
import com.yunbao.beauty.ui.interfaces.MHBeautyEffectListener;
import com.yunbao.beauty.ui.views.BeautyDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kongxr on 2019/8/23.
 * StickerPagerAdapter
 */

public class StickerPagerAdapter extends PagerAdapter {

    private List<StickerCategaryBean> titles = new ArrayList<>();
    private SparseArray<RecyclerView> viewsList = new SparseArray<>();
    private MHBeautyEffectListener effectUpdateListener;
    private StickerCanClickListener mCanClickListener;

    public StickerPagerAdapter(List<StickerCategaryBean> titles, StickerCanClickListener canClickListener) {
        this.titles.clear();
        this.titles.addAll(titles);
        mCanClickListener = canClickListener;
    }

    @Override
    public int getCount() {//必须实现
        return titles.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {//必须实现
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {//必须实现，实例化
//        int i = viewsList.indexOfKey(position);
        View existRecyclerView = viewsList.get(position);
        if (existRecyclerView != null) {
            container.addView(existRecyclerView);
            return existRecyclerView;
        }
        RecyclerView recyclerView = new RecyclerView(MHSDK.getInstance().getAppContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MHSDK.getInstance().getAppContext(), 5, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        StickerAdapter stickerAdapter = new StickerAdapter(container.getContext(), titles.get(position).getId(),mCanClickListener);
        stickerAdapter.setOnItemClickListener((bean, position1) -> {
            if (effectUpdateListener != null) {
                effectUpdateListener.onStickerChanged(bean.getName());
                StickerManager.getInstance().setmCurStickerName(bean.getName());
                StickerBeautyBean stickerBeautyData = bean.getStickerBeautyData();
                notifyRvNotifyDataSetChanged();
                if (stickerBeautyData != null && stickerBeautyData.hasDataBeauty()) {
                    BeautyDataModel.getInstance().storageLastNormalBeautyData();
                    effectUpdateListener.onMeiBaiChanged(Integer.valueOf(stickerBeautyData.getBig_eye()));
                    effectUpdateListener.onMoPiChanged(Integer.valueOf(stickerBeautyData.getSkin_smooth()));
                    effectUpdateListener.onFengNenChanged(Integer.valueOf(stickerBeautyData.getSkin_tenderness()));
//                    effectUpdateListener.onBrightChanged(Integer.valueOf(stickerBeautyData.getSkin_brightness()));
                    effectUpdateListener.onBigEyeChanged(Integer.valueOf(stickerBeautyData.getBig_eye()));
                    effectUpdateListener.onEyeBrowChanged(Integer.valueOf(stickerBeautyData.getEye_brow()));
                    effectUpdateListener.onEyeLengthChanged(Integer.valueOf(stickerBeautyData.getEye_length()));
                    effectUpdateListener.onEyeCornerChanged(Integer.valueOf(stickerBeautyData.getEye_corner()));
                    effectUpdateListener.onFaceChanged(Integer.valueOf(stickerBeautyData.getFace_lift()));
                    effectUpdateListener.onMouseChanged(Integer.valueOf(stickerBeautyData.getMouse_lift()));
                    effectUpdateListener.onNoseChanged(Integer.valueOf(stickerBeautyData.getNose_lift()));
                    effectUpdateListener.onChinChanged(Integer.valueOf(stickerBeautyData.getChin_lift()));
                    effectUpdateListener.onForeheadChanged(Integer.valueOf(stickerBeautyData.getForehead_lift()));
                    effectUpdateListener.onLengthenNoseChanged(Integer.valueOf(stickerBeautyData.getLengthen_noseLift()));
                    effectUpdateListener.onFaceShaveChanged(Integer.valueOf(stickerBeautyData.getFace_shave()));
                    effectUpdateListener.onEyeAlatChanged(Integer.valueOf(stickerBeautyData.getEye_alat()));
                } else {
                    HashMap<String, Integer> lastNormalBeautyDataMap = BeautyDataModel.getInstance().getLastNormalBeautyDataMap();
                    if (lastNormalBeautyDataMap == null || lastNormalBeautyDataMap.size() <= 0)
                        return;
                    String[] beautyAllNames = BeautyDataModel.getInstance().getBeautyAllNames();
                    int[] progress = new int[beautyAllNames.length];
                    for (int i = 0; i < beautyAllNames.length; i++) {
                        Integer integer = lastNormalBeautyDataMap.get(beautyAllNames[i]);
                        progress[i] = integer == null ? 0 : integer;
                    }
                    effectUpdateListener.onMeiBaiChanged(progress[0]);
                    effectUpdateListener.onMoPiChanged(progress[1]);
                    effectUpdateListener.onFengNenChanged(progress[2]);
//                    effectUpdateListener.onBrightChanged(progress[3]);
                    effectUpdateListener.onBigEyeChanged(progress[4]);
                    effectUpdateListener.onEyeBrowChanged(progress[5]);
                    effectUpdateListener.onEyeLengthChanged(progress[6]);
                    effectUpdateListener.onEyeCornerChanged(progress[7]);
                    effectUpdateListener.onFaceChanged(progress[8]);
                    effectUpdateListener.onMouseChanged(progress[9]);
                    effectUpdateListener.onNoseChanged(progress[10]);
                    effectUpdateListener.onChinChanged(progress[11]);
                    effectUpdateListener.onForeheadChanged(progress[12]);
                    effectUpdateListener.onLengthenNoseChanged(progress[13]);
                    effectUpdateListener.onFaceShaveChanged(progress[14]);
                    effectUpdateListener.onEyeAlatChanged(progress[15]);
                    BeautyDataModel.getInstance().clearLastNormalBeautyData();
                }
            }
        });
        recyclerView.setAdapter(stickerAdapter);
        viewsList.put(position, recyclerView);
        container.addView(recyclerView);
        return recyclerView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
//        container.removeView(mViewList.get(position));
        container.removeView(viewsList.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
//            return super.getPageTitle(position);
        return titles.get(position).getName();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    public void release() {
        if (viewsList == null || viewsList.size() <= 0) return;
        for (int i = 0; i < viewsList.size(); i++) {
            RecyclerView recyclerView = viewsList.valueAt(i);
            if (recyclerView != null) {
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter instanceof StickerAdapter) {
                    ((StickerAdapter) adapter).clear();
                }
            }
        }
        viewsList.clear();
        viewsList = null;
        effectUpdateListener = null;
    }

    public void setEffectListener(MHBeautyEffectListener mEffectListener) {
        this.effectUpdateListener = mEffectListener;
    }

    public void notifyRvNotifyDataSetChanged() {
        for (int i = 0; i < viewsList.size(); i++) {
            RecyclerView recyclerView = viewsList.valueAt(i);
            if (recyclerView != null && recyclerView.getAdapter() != null) {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }
}
