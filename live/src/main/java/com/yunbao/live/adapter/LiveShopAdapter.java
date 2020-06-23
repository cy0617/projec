package com.yunbao.live.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.GoodsBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.live.R;
import com.yunbao.live.activity.LiveActivity;
import com.yunbao.live.http.LiveHttpUtil;
import com.yunbao.live.socket.SocketChatUtil;

/**
 * Created by cxf on 2019/8/29.
 */

public class LiveShopAdapter extends RefreshAdapter<GoodsBean> {

    private View.OnClickListener mOnClickListener;
    private ActionListener mActionListener;
    private String mMoneySymbol;
    private int mCurPos=-1;


    public LiveShopAdapter(Context context) {
        super(context);
        mMoneySymbol = WordUtil.getString(R.string.money_symbol);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canClick()) {
                    return;
                }
                final int position = (int) v.getTag();
                final GoodsBean bean = mList.get(position);
                if (v.getId() == R.id.btn_delete) {
                    LiveHttpUtil.shopSetSale(bean.getId(), 0, new HttpCallback() {
                                @Override
                                public void onSuccess(int code, String msg, String[] info) {
                                    if (code == 0) {
                                        mList.remove(position);
                                        notifyDataSetChanged();
                                        if (mActionListener != null) {
                                            mActionListener.onDeleteSuccess();
                                        }
                                    } else {
                                        ToastUtil.show(msg);
                                    }
                                }
                            }
                    );
                } else if (v.getId() == R.id.btn_explain) {
                    if (mCurPos!=-1&&mCurPos!=position){
                        mList.get(mCurPos).setIstalking(0);
                    }
                    final int type = bean.getIstalking() == 1 ? 0 : 1;
                    LiveHttpUtil.setShopTalking(bean.getId(), type, new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            if (code == 0) {
                                bean.setIstalking(type);
                                notifyDataSetChanged();
                                mCurPos = position;
                                ((LiveActivity) mContext).sendShopExplainMsg(bean);
                            } else {
                                ToastUtil.show(msg);
                            }
                        }
                    });
                }
            }
        };

    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position), position);
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        TextView mDes;
        TextView mPrice;
        TextView mPriceOrigin;
        View mBtnDel;
        TextView mTvExplain;
        View mExplain;

        public Vh(View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.thumb);
            mDes = itemView.findViewById(R.id.des);
            mPrice = itemView.findViewById(R.id.price);
            mPriceOrigin = itemView.findViewById(R.id.price_origin);
            mBtnDel = itemView.findViewById(R.id.btn_delete);
            mExplain = itemView.findViewById(R.id.explaining);
            mTvExplain = itemView.findViewById(R.id.btn_explain);
            mBtnDel.setOnClickListener(mOnClickListener);
            mTvExplain.setOnClickListener(mOnClickListener);
        }

        void setData(GoodsBean bean, int position) {
            mBtnDel.setTag(position);
            mTvExplain.setTag(position);
            ImgLoader.display(mContext, bean.getThumb(), mThumb);
            mPrice.setText(StringUtil.contact(mMoneySymbol, bean.getPriceNow()));
//            mPriceOrigin.setText(StringUtil.contact(mMoneySymbol, bean.getPriceOrigin()));
//            mPriceOrigin.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mDes.setText(bean.getName());
            boolean isTalking = bean.getIstalking() == 1;
            if (mCurPos==-1&&isTalking){
                mCurPos=position;
            }
            mExplain.setVisibility(bean.getIstalking() == 1 ? View.VISIBLE : View.GONE);
            mTvExplain.setText(isTalking ? WordUtil.getString(R.string.mall_380) : WordUtil.getString(R.string.mall_379));
            mTvExplain.setBackgroundResource(isTalking ? R.drawable.bg_btn_explain : R.drawable.bg_btn_explain2);
        }
    }


    public interface ActionListener {
        void onDeleteSuccess();
        // void onExplain(boolean istalking);
    }


}
