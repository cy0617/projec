package com.yunbao.beauty.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meihu.beautylibrary.MHSDK;
import com.yunbao.beauty.R;
import com.yunbao.beauty.ui.bean.ShapeBean;
import com.meihu.beautylibrary.constant.Constants;
import com.yunbao.beauty.ui.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kongxr on 2019/8/23.
 * shapeAdapter
 */


public class DefaultShapeAdapter extends RecyclerView.Adapter<DefaultShapeAdapter.Vh> {

    private List<ShapeBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<ShapeBean> mOnItemClickListener;
    private int mCheckedPosition = -1;

    public DefaultShapeAdapter(Context context) {
        mList = new ArrayList<>();
        String[] shapeNames = context.getResources().getStringArray(R.array.name_default_beauty_shape);
        TypedArray shapeArray = context.getResources().obtainTypedArray(R.array.icons_default_beauty_shape);
        TypedArray shapeArraySel = context.getResources().obtainTypedArray(R.array.icons_default_beauty_shape_seledted);
//        int[] shapeIcons = context.getResources().getIntArray(R.array.icons_beauty_shape);
//        int[] shapeIconsSel = context.getResources().getIntArray(R.array.icons_beauty_shape_seledted);
        for (int i = 0; i < shapeNames.length; i++) {
            String shapeName = shapeNames[i];
            int imgSrc = shapeArray.getResourceId(i, R.mipmap.beauty_origin);
            int ImgSrcSel = shapeArraySel.getResourceId(i, R.mipmap.beauty_origin);
            ShapeBean shapeBean = new ShapeBean(imgSrc, ImgSrcSel, shapeName, false);
            mList.add(shapeBean);
        }
        shapeArray.recycle();
        shapeArraySel.recycle();
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

    public void setOnItemClickListener(OnItemClickListener<ShapeBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    @Override
    public Vh onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_list_shape_new, parent, false));
    }

    @Override
    public void onBindViewHolder(Vh holder, int position) {

    }

    @Override
    public void onBindViewHolder(Vh vh, int position, List<Object> payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        vh.setData(mList.get(position), position, payload);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class Vh extends RecyclerView.ViewHolder {

        ImageView mImg;
        TextView mShapeName;

        Vh(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img);
            mShapeName = itemView.findViewById(R.id.tv_beauty_name);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(ShapeBean bean, int position, Object payload) {
            itemView.setTag(position);
            if (payload == null) {
                mShapeName.setText(bean.getShapeName());
            }
            if (bean.isChecked()) {
                mImg.setImageResource(bean.getImgSrcSel());
                mShapeName.setTextColor(MHSDK.getInstance().getAppContext().getResources().getColor(R.color.shape_icon_select_color));
            } else {
                mImg.setImageResource(bean.getImgSrc());
                mShapeName.setTextColor(MHSDK.getInstance().getAppContext().getResources().getColor(R.color.bg_black));
            }
        }
    }

}
