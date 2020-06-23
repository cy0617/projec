package com.yunbao.main.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.main.R;
import com.yunbao.main.views.ActiveHomeViewHolder;

/**
 * 我的动态
 */
public class MyActiveActivity extends AbsActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_active;
    }

    @Override
    protected void main() {
        findViewById(R.id.btn_add).setOnClickListener(this);
        ActiveHomeViewHolder vh = new ActiveHomeViewHolder(mContext, (ViewGroup) findViewById(R.id.container), CommonAppConfig.getInstance().getUid());
        vh.addToParent();
        vh.subscribeActivityLifeCycle();
        vh.loadData();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_add) {
            startActivity(new Intent(mContext, ActivePubActivity.class));
        }
    }

}
