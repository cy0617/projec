package com.yunbao.beauty.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meihu.beautylibrary.MHSDK;
import com.meihu.kalle.FileBinary;
import com.yunbao.beauty.R;
import com.yunbao.beauty.ui.bean.FilterBean;
import com.meihu.beautylibrary.constant.Constants;
import com.yunbao.beauty.ui.enums.FilterEnum;
import com.yunbao.beauty.ui.interfaces.OnItemClickListener;
import com.yunbao.beauty.ui.views.BeautyDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kongxr on 2019/8/23.
 * filterAdapter
 */

public class FilterAdapter extends  BaseBeautyAdapter<FilterAdapter.ViewHolder, FilterBean> {

    public FilterAdapter(Context context) {
        super(context,1);
        mList = new ArrayList<>();
        mList.add(new FilterBean(R.mipmap.icon_filter_orginal_new, 0, FilterEnum.NO_FILTER, 0, true));
        mList.add(new FilterBean(R.mipmap.icon_filter_langman_new, R.drawable.filter_langman, FilterEnum.ROMANTIC_FILTER));
        mList.add(new FilterBean(R.mipmap.icon_filter_qingxin_new, R.drawable.filter_qingxin, FilterEnum.FRESH_FILTER));
        mList.add(new FilterBean(R.mipmap.icon_filter_weimei_new, R.drawable.filter_weimei, FilterEnum.BEAUTIFUL_FILTER));
        mList.add(new FilterBean(R.mipmap.icon_filter_fennen_new, R.drawable.filter_fennen, FilterEnum.PINK_FILTER));
        mList.add(new FilterBean(R.mipmap.icon_filter_huaijiu_new, R.drawable.filter_huaijiu, FilterEnum.NOSTALGIC_FILTER));
        mList.add(new FilterBean(R.mipmap.icon_filter_qingliang_new, R.drawable.filter_qingliang, FilterEnum.COOL_FILTER));
        mList.add(new FilterBean(R.mipmap.icon_filter_landiao_new, R.drawable.filter_landiao, FilterEnum.BLUES_FILTER));
        mList.add(new FilterBean(R.mipmap.icon_filter_rixi_new, R.drawable.filter_rixi, FilterEnum.JAPANESE_FILTER));
//        mInflater = LayoutInflater.from(context);
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
                    FilterBean filterBean = mList.get(position);
                    BeautyDataModel.getInstance().setFilterChanged(filterBean.getFilterEnum());
                    notifyItemChanged(position, Constants.PAYLOAD);
                    mCheckedPosition = position;
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mList.get(position), position);
                    }
                }
            }
        };
    }

    public void setFilterChanged(FilterEnum filterEnum) {
        int value = filterEnum.getValue();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_list_filter_new, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {}

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, List<Object> payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        viewHolder.setData(mList.get(position), position, payload);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends BaseBeautyAdapter.Vh{

//        ImageView mImg;
        ImageView mCheckImg;
//        TextView mFilterName;

        ViewHolder(View itemView) {
            super(itemView);
            mCheckImg = itemView.findViewById(R.id.check_img);
        }

        void setData(FilterBean bean, int position, Object payload) {
            itemView.setTag(position);
            if (payload == null) {
                mImg.setImageResource(bean.getImgSrc());
                mBeautyName.setText(bean.getFilterEnum().getStringId());
            }
            if (position == 0 ){
                if (BeautyDataModel.getInstance().getFilterEnum() == null || BeautyDataModel.getInstance().getFilterEnum()==FilterEnum.NO_FILTER){
                    bean.setChecked(true);
                    mCheckedPosition = 0;
                }else {
                    bean.setChecked(false);
                }
            }else {
                if (BeautyDataModel.getInstance().getFilterEnum() != null && bean.getFilterEnum()==BeautyDataModel.getInstance().getFilterEnum()){
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
