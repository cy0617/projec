package com.yunbao.beauty.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.meihu.beautylibrary.MHSDK;
import com.yunbao.beauty.ui.bean.BeautyBean;
import com.yunbao.beauty.ui.bean.StickerCategaryBean;
import com.yunbao.beauty.ui.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kongxr on 2019/11/12.
 * StickerPagerAdapter
 */

public class SpeciallyPagerAdapter extends PagerAdapter {

    private List<StickerCategaryBean> titles = new ArrayList<>();
    private SparseArray<RecyclerView> viewsList = new SparseArray<>();
    private OnItemClickListener<BeautyBean> mOnItemClickListener;
    private int ver;

    public SpeciallyPagerAdapter(List<StickerCategaryBean> titles, int ver) {
        this.titles.clear();
        this.titles.addAll(titles);
        this.ver = ver;
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
        View exitRecyclerView = viewsList.get(position);
        if (exitRecyclerView != null) {
            return exitRecyclerView;
        }
        BaseBeautyAdapter beautyAdapter = null;
        switch (position){
            case 0:
                beautyAdapter = new SpeciallyAdapter(container.getContext());
                break;
            case 1:
                beautyAdapter = new WaterMarkAdapter(container.getContext());
                break;
            default:
                beautyAdapter = new SpeciallyAdapter(container.getContext());
                break;
        }
        RecyclerView recyclerView = new RecyclerView(MHSDK.getInstance().getAppContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MHSDK.getInstance().getAppContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (beautyAdapter != null) {
            beautyAdapter.setOnItemClickListener(new OnItemClickListener<BeautyBean>() {
                @Override
                public void onItemClick(BeautyBean bean, int position) {
                    if (mOnItemClickListener != null) {
                        SpeciallyPagerAdapter.this.notifyRvNotifyDataSetChanged();
                        mOnItemClickListener.onItemClick(bean, position);
                    }
                }
            });
        }
        recyclerView.setAdapter(beautyAdapter);
        viewsList.put(position, recyclerView);
        container.addView(recyclerView);
//        ((ContentViewPager)container).setViewForPosition(recyclerView, position);
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
            if (recyclerView != null){
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter instanceof StickerAdapter) {
                    ((StickerAdapter) adapter).clear();
                }
            }
        }
        viewsList.clear();
        viewsList = null;
        mOnItemClickListener = null;
    }

//    public void setEffectListener(OnItemClickListener<BeautyBean> mEffectListener) {
//        this.mOnItemClickListener = mEffectListener;
//    }

    public void setOnItemClickListener(OnItemClickListener<BeautyBean> mEffectListener) {
        this.mOnItemClickListener = mEffectListener;
    }

    public void notifyRvNotifyDataSetChanged(){
        for (int i = 0; i < viewsList.size(); i++) {
            RecyclerView recyclerView = viewsList.valueAt(i);
            if (recyclerView != null && recyclerView.getAdapter() != null) {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }
}
