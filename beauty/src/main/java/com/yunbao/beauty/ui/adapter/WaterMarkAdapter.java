package com.yunbao.beauty.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.bean.WaterAlignEnum;
import com.yunbao.beauty.R;
import com.yunbao.beauty.ui.bean.WatermarkBean;
import com.yunbao.beauty.ui.enums.BeautyTypeEnum;
import com.yunbao.beauty.ui.interfaces.OnItemClickListener;
import com.yunbao.beauty.ui.views.BeautyDataModel;
import com.yunbao.common.Constants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kongxr on 2019/10/31.
 * SpeciallyAdapter 特效
 */

public class WaterMarkAdapter extends BaseBeautyAdapter<WaterMarkAdapter.ViewHolder, WatermarkBean> {

    WaterMarkAdapter(Context context) {
        super(context, 1);
        String[] iconList = new String[0],resList = new String[0];
        try {
            iconList = context.getAssets().list(Constants.WATERMARK_ASSETS_FORDERNAME + File.separator + Constants.WATERMARK_ICON_FORDERNAME);
            resList = context.getAssets().list(Constants.WATERMARK_ASSETS_FORDERNAME + File.separator + Constants.WATERMARK_RES_FORDERNAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (iconList == null || iconList.length <= 0 || resList == null || resList.length <= 0) return;
        mList = new ArrayList<>();
        stringArray = context.getResources().getStringArray(R.array.specially__watermark_name_list);
        for (int i = 0; i < stringArray.length; i++) {
            String beautyName = stringArray[i];
            String iconPath = Constants.WATERMARK_ASSETS_FORDERNAME + File.separator + Constants.WATERMARK_ICON_FORDERNAME + File.separator + iconList[i];
            String resPath = Constants.WATERMARK_ASSETS_FORDERNAME + File.separator + Constants.WATERMARK_RES_FORDERNAME + File.separator + resList[i];
            WatermarkBean beautyBean = new WatermarkBean(iconPath, resPath, beautyName, BeautyTypeEnum.WATER_TYPE_ENUM,false, WaterAlignEnum.getValue(i));
            mList.add(beautyBean);
        }
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
                notifyItemChanged(position, Constants.PAYLOAD);
                mCheckedPosition = position;
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mList.get(position), position);
                }
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener<WatermarkBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_list_watermark, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {}

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

        void setData(WatermarkBean bean, int position, Object payload) {
            itemView.setTag(position);
            InputStream iconIs=null;
            if (payload == null) {
                try {
                    String iconPath = bean.getIconPath();
                    iconIs = MHSDK.getInstance().getAppContext().getAssets().open(iconPath);
                    Bitmap waterBitmap = BitmapFactory.decodeStream(iconIs);
                    mImg.setImageBitmap(waterBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (iconIs != null)  {
                        try {
                            iconIs.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mBeautyName.setText(bean.getEffectName());
            }
            WatermarkBean watermarkBean = BeautyDataModel.getInstance().getWatermarkBean();
            String speciallyName= "";
            if (watermarkBean != null) {
                speciallyName = watermarkBean.getEffectName();
            }
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
            mBeautyName.setVisibility(View.GONE);
            if (bean.isChecked()) {
                if (mCheckImg.getVisibility() != View.VISIBLE) {
                    mCheckImg.setVisibility(View.VISIBLE);
                }
//                mBeautyName.setTextColor(MHSDK.getInstance().getAppContext().getResources().getColor(R.color.shape_icon_select_color));
            } else {
                if (mCheckImg.getVisibility() == View.VISIBLE) {
                    mCheckImg.setVisibility(View.INVISIBLE);
                }
//                mBeautyName.setTextColor(MHSDK.getInstance().getAppContext().getResources().getColor(R.color.bg_black));
            }
        }
    }
}
