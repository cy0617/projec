package com.yunbao.beauty.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meihu.beautylibrary.MHSDK;
import com.yunbao.beauty.R;
import com.yunbao.beauty.ui.bean.BeautyBean;
import com.yunbao.beauty.ui.bean.ShapeBean;
import com.yunbao.beauty.ui.enums.BeautyTypeEnum;
import com.yunbao.beauty.ui.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kongxr on 2019/8/23.
 * shapeAdapter
 */


public class ShapeAdapter extends BaseBeautyAdapter<BeautyAdapter.Vh, BeautyBean> {

    ShapeAdapter(Context context, int ver) {
        super(context, ver);
        mList = new ArrayList<>();
        String[] shapeNames;
        TypedArray shapeArray;
        TypedArray shapeArraySel;
        if (ver == 1) {
            shapeNames = context.getResources().getStringArray(R.array.name_beauty_shape);
            shapeArray = context.getResources().obtainTypedArray(R.array.icons_beauty_shape);
            shapeArraySel = context.getResources().obtainTypedArray(R.array.icons_beauty_shape_seledted);
        }else {
            shapeNames = context.getResources().getStringArray(R.array.name_default_beauty_shape);
            shapeArray = context.getResources().obtainTypedArray(R.array.icons_default_beauty_shape);
            shapeArraySel = context.getResources().obtainTypedArray(R.array.icons_default_beauty_shape_seledted);
        }
        for (int i = 0; i < shapeNames.length; i++) {
            String shapeName = shapeNames[i];
            int imgSrc = shapeArray.getResourceId(i, R.mipmap.beauty_origin);
            int ImgSrcSel = shapeArraySel.getResourceId(i, R.mipmap.beauty_origin);
            BeautyBean shapeBean = new BeautyBean(imgSrc, ImgSrcSel, shapeName, BeautyTypeEnum.SHAPE_TYPE_ENUM,false);
            mList.add(shapeBean);
        }
        shapeArray.recycle();
        shapeArraySel.recycle();
        mInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener<BeautyBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BeautyAdapter.Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BeautyAdapter.Vh(mInflater.inflate(R.layout.item_list_shape_new, parent, false));
    }

    @Override
    public void onBindViewHolder(BeautyAdapter.Vh vh, int position, List<Object> payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        vh.setData(mList.get(position), position, payload);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


//    class ViewHolder extends BaseBeautyAdapter.ViewHolder {
//
//        ViewHolder(View itemView) {
//            super(itemView);
//            mImg = itemView.findViewById(R.id.img);
//            mBeautyName = itemView.findViewById(R.id.tv_beauty_name);
//            itemView.setOnClickListener(mOnClickListener);
//        }
//
//        @Override
//        void setData(BeautyBean bean, int position, Object payload) {
//            itemView.setTag(position);
//            if (payload == null) {
//                mBeautyName.setText(bean.getEffectName());
//            }
//            if (bean.isChecked()) {
//                mImg.setImageResource(bean.getImgSrcSel());
//                mBeautyName.setTextColor(MHSDK.getInstance().getAppContext().getResources().getColor(R.color.shape_icon_select_color));
//            } else {
//                mImg.setImageResource(bean.getImgSrc());
//                mBeautyName.setTextColor(MHSDK.getInstance().getAppContext().getResources().getColor(R.color.bg_black));
//            }
//        }
//    }

}
