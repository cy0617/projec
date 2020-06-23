package com.yunbao.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.bean.UserItemBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.L;
import com.yunbao.main.R;

import java.util.List;


/**
 * 个人主页列表
 */
public class MainMeAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NORMAL = 0;
    private static final int GROUP_TOP = 1;
    private Context mContext;
    private List<UserItemBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<UserItemBean> mOnItemClickListener;

    public MainMeAdapter1(Context context, List<UserItemBean> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    UserItemBean bean = (UserItemBean) tag;
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(bean, 0);
                    }
                }
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener<UserItemBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        UserItemBean bean = mList.get(position);
        if (bean.isGroupTop()) {
            return GROUP_TOP;
        } else {
            return NORMAL;
        }
    }


    public void setList(List<UserItemBean> list) {
        if (list == null) {
            return;
        }
        boolean changed = false;
        if (mList.size() != list.size()) {
            changed = true;
        } else {
            for (int i = 0, size = mList.size(); i < size; i++) {
                if (!mList.get(i).equals(list.get(i))) {
                    changed = true;
                    break;
                }
            }
        }
        if (changed) {
            mList = list;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == GROUP_TOP) {
            return new TopVh(mInflater.inflate(R.layout.item_me_0, parent, false));
        } else {
            return new Vh(mInflater.inflate(R.layout.item_me, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof TopVh) {
            ((TopVh) vh).setData(mList.get(position));
        } else if (vh instanceof Vh) {
            ((Vh) vh).setData(mList.get(position));
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        L.e("---onAttachedToRecyclerView--");
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case GROUP_TOP:
                            return 12;
                        case NORMAL:
                            if (mList.get(position).isMore()) {
                                return 3;
                            } else {
                                return 4;
                            }
                        default:
                            return 4;
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class TopVh extends RecyclerView.ViewHolder {
        TextView mName;

        public TopVh(View itemView) {
            super(itemView);
            mName = (TextView) itemView;
        }

        void setData(UserItemBean bean) {
            mName.setText(bean.getName());
        }
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        TextView mName;

        public Vh(View itemView) {
            super(itemView);
            mThumb = (ImageView) itemView.findViewById(R.id.thumb);
            mName = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(UserItemBean bean) {
            itemView.setTag(bean);
            ImgLoader.display(mContext, bean.getThumb(), mThumb);
            mName.setText(bean.getName());
        }
    }
}
