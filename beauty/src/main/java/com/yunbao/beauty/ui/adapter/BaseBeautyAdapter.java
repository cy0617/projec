package com.yunbao.beauty.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.constant.Constants;
import com.yunbao.beauty.R;
import com.yunbao.beauty.ui.bean.BeautyBean;
import com.yunbao.beauty.ui.interfaces.OnItemClickListener;

import java.util.List;

public abstract class BaseBeautyAdapter<T extends BaseBeautyAdapter.Vh, K extends BeautyBean> extends RecyclerView.Adapter<T> {

    String[] stringArray;
    List<K> mList;
    LayoutInflater mInflater;
    View.OnClickListener mOnClickListener;
    protected OnItemClickListener<K> mOnItemClickListener;
    int mCheckedPosition = -1;
    int ver;

    public BaseBeautyAdapter(Context context, int ver) {
        this.ver = ver;
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    int position = (int) tag;
                    if (mCheckedPosition == position) {
                        return;
                    }
                    if (mCheckedPosition >= 0 && mCheckedPosition < mList.size()) {
                        mList.get(mCheckedPosition).setChecked(false);
                        notifyItemChanged(mCheckedPosition, Constants.PAYLOAD);
                    }
                    mList.get(position).setChecked(true);
                    notifyItemChanged(position, Constants.PAYLOAD);
                    mCheckedPosition = position;
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mList.get(position), position);
                    }
                }
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(BaseBeautyAdapter.Vh holder, int position) { }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mImg;
        TextView mBeautyName;

        Vh(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img);
            mBeautyName = itemView.findViewById(R.id.tv_beauty_name);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(BeautyBean bean, int position, Object payload) {
            itemView.setTag(position);
            if (payload == null) {
                mBeautyName.setText(bean.getEffectName());
            }
            if (bean.isChecked()) {
                mImg.setImageResource(bean.getImgSrcSel());
                mBeautyName.setTextColor(MHSDK.getInstance().getAppContext().getResources().getColor(R.color.shape_icon_select_color));
            } else {
                mImg.setImageResource(bean.getImgSrc());
                mBeautyName.setTextColor(MHSDK.getInstance().getAppContext().getResources().getColor(R.color.bg_black));
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener<K> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    protected void release(){
        stringArray = null;
        mInflater = null;
        mList = null;
        mOnClickListener = null;
        mOnItemClickListener = null;
    }
}
