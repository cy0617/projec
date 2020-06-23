package com.yunbao.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.VideoClassBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.L;
import com.yunbao.main.R;

import java.util.List;

public class MainHomeVideoClassAdapter extends RefreshAdapter<VideoClassBean> {

    private int mCheckedColor;
    private int mUnCheckedColor;
    private View.OnClickListener mOnClickListener;
    private int mCheckedPosition;

    public MainHomeVideoClassAdapter(Context context, List<VideoClassBean> list) {
        super(context, list);
        mCheckedColor = ContextCompat.getColor(mContext, R.color.global);
        mUnCheckedColor = ContextCompat.getColor(mContext, R.color.gray1);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                int position = (int) tag;
                if (mCheckedPosition == position) {
                    return;
                }
                mList.get(mCheckedPosition).setChecked(false);
                mList.get(position).setChecked(true);
                notifyItemChanged(mCheckedPosition);
                notifyItemChanged(position);
                mCheckedPosition = position;
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mList.get(position), position);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_main_home_video_class, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        ((Vh) vh).setData(mList.get(i), i);
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mText;
        ImageView mIvClass;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.tv_class);
            mIvClass = (ImageView) itemView.findViewById(R.id.iv_class);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(VideoClassBean bean, int position) {
            itemView.setTag(position);
            mText.setText(bean.getName());
            if (TextUtils.isEmpty(bean.getThumb())){
                if (bean.getId()==-1){
                    mIvClass.setImageResource(R.mipmap.icon_video_recommend);
                }else {
                    mIvClass.setImageDrawable(null);
                }
            }else {
                ImgLoader.display(mContext,bean.getThumb(),mIvClass);
            }
            mText.setTextColor(bean.isChecked() ? mCheckedColor : mUnCheckedColor);
        }
    }
}
