package com.yunbao.beauty.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.constant.Constants;
import com.yunbao.beauty.R;
import com.yunbao.beauty.ui.bean.BeautyBean;
import com.yunbao.beauty.ui.enums.BeautyTypeEnum;
import com.yunbao.beauty.ui.views.BeautyDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kongxr on 2019/10/31.
 * SpeciallyAdapter 特效
 */

public class SpeciallyAdapter extends BaseBeautyAdapter<SpeciallyAdapter.ViewHolder, BeautyBean> {

    private final String[] stringArray;

    SpeciallyAdapter(Context context) {
        super(context, 1);
        mList = new ArrayList<>();
        stringArray = MHSDK.getInstance().getAppContext().getResources().getStringArray(R.array.specially_name_list);
        TypedArray speciallyArray = context.getResources().obtainTypedArray(R.array.specially_icon_list);
        for (int i = 0; i < stringArray.length; i++) {
            String beautyName = stringArray[i];
            int imgSrc = speciallyArray.getResourceId(i, com.yunbao.beauty.R.mipmap.beauty_origin);
            int ImgSrcSel = speciallyArray.getResourceId(i, com.yunbao.beauty.R.mipmap.beauty_origin);
            BeautyBean beautyBean = new BeautyBean(imgSrc, ImgSrcSel, beautyName, BeautyTypeEnum.SPECIALLY_TYPE_ENUM,false);
            mList.add(beautyBean);
        }
        speciallyArray.recycle();
        mOnClickListener = v -> {
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
//                BeautyBean speciallyBean = mList.get(position);
//                BeautyDataModel.getInstance().setSpeciallyName(speciallyBean.getEffectName());
                notifyItemChanged(position, Constants.PAYLOAD);
                mCheckedPosition = position;
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mList.get(position), position);
                }
            }
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_list_filter_new, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {}

    @Override
    public void onBindViewHolder(ViewHolder vh, int position, List<Object> payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        vh.setData(mList.get(position), position, payload);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends BaseBeautyAdapter.Vh {

//        ImageView mImg;
        ImageView mCheckImg;
//        TextView mSpeciallyName;

        ViewHolder(View itemView) {
            super(itemView);
            mCheckImg = itemView.findViewById(R.id.check_img);
        }

        void setData(BeautyBean bean, int position, Object payload) {
            itemView.setTag(position);
            if (payload == null) {
                mImg.setImageResource(bean.getImgSrc());
                mBeautyName.setText(bean.getEffectName());
            }
            String speciallyName = BeautyDataModel.getInstance().getSpeciallyName();
            if (position == 0 ){
                if (TextUtils.isEmpty(speciallyName) || speciallyName.equals(stringArray[0])){
                    bean.setChecked(true);
                    mCheckedPosition = 0;
                }else {
                    bean.setChecked(false);
                }
            }else {
                if (bean.getEffectName().equals(speciallyName)){
                    bean.setChecked(true);
                    mCheckedPosition = position;
                }else {
                    bean.setChecked(false);
                }
            }
            if (bean.isChecked()) {
                if (mCheckImg.getVisibility() != View.VISIBLE) {
                    mCheckImg.setVisibility(View.VISIBLE);
                }
                mBeautyName.setTextColor(MHSDK.getInstance().getAppContext().getResources().getColor(R.color.shape_icon_select_color));
            } else {
                if (mCheckImg.getVisibility() == View.VISIBLE) {
                    mCheckImg.setVisibility(View.INVISIBLE);
                }
                mBeautyName.setTextColor(MHSDK.getInstance().getAppContext().getResources().getColor(R.color.bg_black));
            }
        }
    }
}
