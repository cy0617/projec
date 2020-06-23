package com.yunbao.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.AddGoodsImageBean;

import java.io.File;

public class AddGoodsTitleAdapter extends RefreshAdapter<AddGoodsImageBean> {

    private String mTip0;
    private String mTip1;
    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mDeleteClickListener;
    private File mVideoFile;
    private String mVideoUrl;
    private String mVideoImgUrl;

    public AddGoodsTitleAdapter(Context context) {
        super(context);
        mTip0 = WordUtil.getString(R.string.mall_082);
        mTip1 = WordUtil.getString(R.string.mall_083);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                AddGoodsImageBean bean = mList.get(position);
                if (!bean.isEmpty()) {
                    return;
                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(bean, position);
                }
            }
        };
        mDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                mList.get(position).setEmpty();
                if (position == 0 || position == mList.size() - 1) {
                    if (position == 0) {
                        mVideoFile = null;
                        mVideoUrl = null;
                        mVideoImgUrl = null;
                    }
                    notifyItemChanged(position);
                } else {
                    mList.remove(position);
                    if (!mList.get(mList.size() - 1).isEmpty()) {
                        mList.add(new AddGoodsImageBean());
                    }
                    notifyDataSetChanged();
                }
            }
        };
    }

    public File getVideoFile() {
        return mVideoFile;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public String getVideoImgUrl() {
        return mVideoImgUrl;
    }

    public void setVideoImgUrl(String videoImgUrl) {
        mVideoImgUrl = videoImgUrl;
    }

    public void setImageFile(int position, File file) {
        if (position == 0) {
            mVideoFile = file;
        }
        mList.get(position).setFile(file);
        int size = mList.size();
        if (position == size - 1 && size < 10) {
            mList.add(new AddGoodsImageBean());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_add_goods_title, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position), position);
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mTip;
        ImageView mImg;
        View mBtnDel;


        public Vh(@NonNull View itemView) {
            super(itemView);
            mTip = itemView.findViewById(R.id.tip);
            mImg = itemView.findViewById(R.id.img);
            mBtnDel = itemView.findViewById(R.id.btn_del);
            itemView.setOnClickListener(mOnClickListener);
            mBtnDel.setOnClickListener(mDeleteClickListener);
        }

        void setData(AddGoodsImageBean bean, int position) {
            itemView.setTag(position);
            mBtnDel.setTag(position);
            if (position > 1 && bean.isEmpty()) {
                mTip.setText(StringUtil.contact(String.valueOf(mList.size() - 2), "/9"));
            } else {
                if (position == 0) {
                    mTip.setText(mTip0);
                } else {
                    mTip.setText(mTip1);
                }
            }
            if (!bean.isEmpty()) {
                if (bean.getFile() != null) {
                    if (position == 0) {
                        ImgLoader.displayVideoThumb(mContext, bean.getFile(), mImg);
                    } else {
                        ImgLoader.display(mContext, bean.getFile(), mImg);
                    }
                } else {
                    ImgLoader.display(mContext, bean.getImgUrl(), mImg);
                }
                if (mBtnDel.getVisibility() != View.VISIBLE) {
                    mBtnDel.setVisibility(View.VISIBLE);
                }
            } else {
                mImg.setImageDrawable(null);
                if (mBtnDel.getVisibility() == View.VISIBLE) {
                    mBtnDel.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

}
