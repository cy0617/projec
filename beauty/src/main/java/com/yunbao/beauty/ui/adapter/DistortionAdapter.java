package com.yunbao.beauty.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meihu.beautylibrary.MHSDK;
import com.yunbao.beauty.R;
import com.yunbao.beauty.ui.bean.SpeciallyBean;
import com.meihu.beautylibrary.constant.Constants;
import com.yunbao.beauty.ui.enums.FilterEnum;
import com.yunbao.beauty.ui.interfaces.OnItemClickListener;
import com.yunbao.beauty.ui.views.BeautyDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kongxr on 2019/10/31.
 * DistortionAdapter  哈哈镜
 */


public class DistortionAdapter extends RecyclerView.Adapter<DistortionAdapter.Vh> {

    private ArrayList<SpeciallyBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<SpeciallyBean> mOnItemClickListener;
    private int mCheckedPosition = -1;
    private final String[] stringArray;

    public DistortionAdapter(Context context) {
        mList = new ArrayList<>();
        stringArray = MHSDK.getInstance().getAppContext().getResources().getStringArray(R.array.distortion_name_list);
        TypedArray shapeArray = context.getResources().obtainTypedArray(R.array.distortion_icon_list);
        for (int i = 0; i < stringArray.length; i++) {
            int resourceId = shapeArray.getResourceId(i, R.mipmap.beauty_origin);
            mList.add(new SpeciallyBean(stringArray[i],resourceId, i == 0));
        }
        shapeArray.recycle();
//        mList.add(new SpeciallyBean(stringArray[0],R.mipmap.icon_filter_orginal_new, true));
//        mList.add(new SpeciallyBean(stringArray[1],R.mipmap.icon_filter_langman_new));
//        mList.add(new SpeciallyBean(stringArray[2],R.mipmap.icon_filter_langman_new));
//        mList.add(new SpeciallyBean(stringArray[3],R.mipmap.icon_filter_langman_new));
//        mList.add(new SpeciallyBean(stringArray[4],R.mipmap.icon_filter_langman_new));
//        mList.add(new SpeciallyBean(stringArray[5],R.mipmap.icon_filter_langman_new));
        mInflater = LayoutInflater.from(context);
        mOnClickListener = v -> {
            Object tag = v.getTag();
            if (tag != null) {
                int position = (int) tag;
                if (mCheckedPosition == position) {
                    return;
                }
                if (mCheckedPosition >= 0 && mCheckedPosition < mList.size()) {
                    mList.get(mCheckedPosition).setmChecked(false);
                    notifyItemChanged(mCheckedPosition, Constants.PAYLOAD);
                }
                mList.get(position).setmChecked(true);
//                SpeciallyBean distortionBean = mList.get(position);
//                BeautyDataModel.getInstance().setDistortionName(distortionBean.getSpeciallyName());
                notifyItemChanged(position, Constants.PAYLOAD);
                mCheckedPosition = position;
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mList.get(position), position);
                }
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener<SpeciallyBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setFilterChanged(FilterEnum filterEnum) {
        int value = filterEnum.getValue();
    }

    @Override
    public Vh onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_list_distortion, parent, false));
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
        ImageView mCheckImg;
        TextView mDistortionName;

        public Vh(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img);
            mCheckImg = itemView.findViewById(R.id.check_img);
            mDistortionName = itemView.findViewById(R.id.tv_beauty_name);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(SpeciallyBean bean, int position, Object payload) {
            itemView.setTag(position);
            if (position == 0){
                mDistortionName.setVisibility(View.GONE);
            }else {
                mDistortionName.setVisibility(View.VISIBLE);
            }
            if (payload == null) {
                mImg.setImageResource(bean.getmImgSrc());
                mDistortionName.setText(bean.getSpeciallyName());
            }
            String distortionName = BeautyDataModel.getInstance().getDistortionName();
            if (position == 0 ){
                if (TextUtils.isEmpty(distortionName) || distortionName.equals(stringArray[0])){
                    bean.setmChecked(true);
                    mCheckedPosition = 0;
                }else {
                    bean.setmChecked(false);
                }
            }else {
                if (bean.getSpeciallyName().equals(distortionName)){
                    bean.setmChecked(true);
                    mCheckedPosition = position;
                }else {
                    bean.setmChecked(false);
                }
            }
            if (bean.ismChecked()) {
                if (mCheckImg.getVisibility() != View.VISIBLE) {
                    mCheckImg.setVisibility(View.VISIBLE);
                }
                mDistortionName.setTextColor(MHSDK.getInstance().getAppContext().getResources().getColor(R.color.shape_icon_select_color));
            } else {
                if (mCheckImg.getVisibility() == View.VISIBLE) {
                    mCheckImg.setVisibility(View.INVISIBLE);
                }
                mDistortionName.setTextColor(MHSDK.getInstance().getAppContext().getResources().getColor(R.color.bg_black));
            }
        }
    }
}
