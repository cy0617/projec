package com.yunbao.beauty.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.yunbao.beauty.R;
import com.yunbao.beauty.custom.TextSeekBar;
import com.yunbao.beauty.ui.adapter.FilterAdapter;
import com.yunbao.beauty.ui.bean.FilterBean;
import com.yunbao.beauty.ui.bean.StickerCategaryBean;
import com.yunbao.beauty.ui.interfaces.BeautyEffectListener;
import com.yunbao.beauty.ui.interfaces.DefaultBeautyEffectListener;
import com.yunbao.beauty.ui.interfaces.IBeautyViewHolder;
import com.yunbao.beauty.ui.interfaces.MHCameraClickListener;
import com.yunbao.beauty.ui.interfaces.OnItemClickListener;
import com.yunbao.common.utils.BitmapUtil;
import com.yunbao.common.views.AbsViewHolder;

import java.util.List;


/**
 * Created by cxf on 2018/6/22.
 * 默认美颜
 */
public class DefaultBeautyViewHolder extends AbsViewHolder implements View.OnClickListener, IBeautyViewHolder {

    private SparseArray<View> mSparseArray;
    private int mCurKey;
    private FilterAdapter mFilterAdapter;
    private MHProjectBeautyEffectListener mEffectListener;
    private IBeautyViewHolder.VisibleListener mVisibleListener;
    private boolean mShowed;

    public DefaultBeautyViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_beauty_default;
    }

    @Override
    public void init() {
        findViewById(R.id.btn_beauty).setOnClickListener(this);
        findViewById(R.id.btn_filter).setOnClickListener(this);
        findViewById(R.id.btn_hide).setOnClickListener(this);
        mSparseArray = new SparseArray<>();
        mSparseArray.put(R.id.btn_beauty, findViewById(R.id.group_beauty));
        mSparseArray.put(R.id.btn_filter, findViewById(R.id.group_filter));
        mCurKey = R.id.btn_beauty;
        TextSeekBar.OnSeekChangeListener onSeekChangeListener = new TextSeekBar.OnSeekChangeListener() {
            @Override
            public void onProgressChanged(View view, int progress) {
                if (mEffectListener != null) {
                    int i = view.getId();
                    if (i == R.id.seek_meibai) {
                        mEffectListener.onMeiBaiChanged(progress);
                    } else if (i == R.id.seek_mopi) {
                        mEffectListener.onMoPiChanged(progress);
                    } else if (i == R.id.seek_hongrun) {
                        mEffectListener.onFengNenChanged(progress);
                    }
                }
            }
        };

        TextSeekBar seekMeiBai = ((TextSeekBar) findViewById(R.id.seek_meibai));
        TextSeekBar seekMoPi = ((TextSeekBar) findViewById(R.id.seek_mopi));
        TextSeekBar seekHongRun = ((TextSeekBar) findViewById(R.id.seek_hongrun));
        seekMeiBai.setOnSeekChangeListener(onSeekChangeListener);
        seekMoPi.setOnSeekChangeListener(onSeekChangeListener);
        seekHongRun.setOnSeekChangeListener(onSeekChangeListener);

        //滤镜
        RecyclerView filterRecyclerView = (RecyclerView) findViewById(R.id.filter_recyclerView);
        filterRecyclerView.setHasFixedSize(true);
        filterRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mFilterAdapter = new FilterAdapter(mContext);
        mFilterAdapter.setOnItemClickListener(new OnItemClickListener<FilterBean>() {
            @Override
            public void onItemClick(FilterBean bean, int position) {
                if (mEffectListener != null) {
                    int filterSrc = bean.getFilterSrc();
                    if (filterSrc != 0) {
                        Bitmap bitmap = BitmapUtil.getInstance().decodeBitmap(filterSrc);
                        mEffectListener.onFilterChanged(bitmap);
                    }
                }
            }
        });
        filterRecyclerView.setAdapter(mFilterAdapter);
    }

    @Override
    public void setEffectListener(BeautyEffectListener beautyEffectListener) {
        if (beautyEffectListener != null && beautyEffectListener instanceof MHProjectBeautyEffectListener) {
            mEffectListener = (MHProjectBeautyEffectListener) beautyEffectListener;
        }
    }

    @Override
    public void show() {
        if (mVisibleListener != null) {
            mVisibleListener.onVisibleChanged(true);
        }
        if (mParentView != null && mContentView != null) {
            ViewParent parent = mContentView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mContentView);
            }
            mParentView.addView(mContentView);
        }
        mShowed = true;
    }

    @Override
    public void hide() {
        removeFromParent();
        if (mVisibleListener != null) {
            mVisibleListener.onVisibleChanged(false);
        }
        mShowed = false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_beauty || id == R.id.btn_filter) {
            toggle(id);
        } else if (id == R.id.btn_hide) {
            hide();
        }
    }

    private void toggle(int key) {
        if (mCurKey == key) {
            return;
        }
        mCurKey = key;
        for (int i = 0, size = mSparseArray.size(); i < size; i++) {
            View v = mSparseArray.valueAt(i);
            if (mSparseArray.keyAt(i) == key) {
                if (v.getVisibility() != View.VISIBLE) {
                    v.setVisibility(View.VISIBLE);
                }
            } else {
                if (v.getVisibility() == View.VISIBLE) {
                    v.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public boolean isShowed() {
        return mShowed;
    }

    @Override
    public void release() {
        mVisibleListener = null;
        mEffectListener = null;
    }

    @Override
    public void setVisibleListener(VisibleListener visibleListener) {
        mVisibleListener = visibleListener;
    }

    @Override
    public void setStickerCategaryData(List<StickerCategaryBean> titles) {

    }

    @Override
    public void setCameraClickListener(MHCameraClickListener cameraClickListener) {

    }


}
