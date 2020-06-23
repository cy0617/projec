package com.yunbao.beauty.ui.interfaces;

/**
 * Created by kxr on 2019/10/9.
 * RecyclerView的Adapter点击事件
 */

public interface OnItemClickListener<T> {
    void onItemClick(T bean, int position);
}
