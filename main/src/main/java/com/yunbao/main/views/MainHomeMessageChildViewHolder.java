package com.yunbao.main.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.ConfigBean;
import com.yunbao.common.event.FollowEvent;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.SpUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.views.AbsMainViewHolder;
import com.yunbao.im.activity.ChatRoomActivity;
import com.yunbao.im.activity.SystemMessageActivity;
import com.yunbao.im.adapter.ImListAdapter;
import com.yunbao.im.bean.ImUserBean;
import com.yunbao.im.bean.SystemMessageBean;
import com.yunbao.im.event.ImUserMsgEvent;
import com.yunbao.im.event.SystemMsgEvent;
import com.yunbao.im.http.ImHttpConsts;
import com.yunbao.im.http.ImHttpUtil;
import com.yunbao.im.utils.ImMessageUtil;
import com.yunbao.main.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

/**
 * 米聊
 */
public class MainHomeMessageChildViewHolder extends AbsMainViewHolder implements View.OnClickListener, ImListAdapter.ActionListener{
    private View mBtnSystemMsg;
    private RecyclerView mRecyclerView;
    private ImListAdapter mAdapter;
    private View mSystemMsgRedPoint;//系统消息的红点
    private TextView mSystemMsgContent;
    private TextView mSystemTime;
    private HttpCallback mSystemMsgCallback;
    private boolean mPriMsgSwitchOpen;//私信开关是否开启

    public MainHomeMessageChildViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_chat_list;
    }

    @Override
    public void init() {
        mRecyclerView = (RecyclerView) findViewById(com.yunbao.im.R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ImListAdapter(mContext);
        mAdapter.setActionListener(this);
        mRecyclerView.setAdapter(mAdapter);
        View headView = mAdapter.getHeadView();
        mBtnSystemMsg = headView.findViewById(com.yunbao.im.R.id.btn_system_msg);
        mBtnSystemMsg.setOnClickListener(this);
        mSystemMsgRedPoint = headView.findViewById(com.yunbao.im.R.id.red_point);
        mSystemMsgContent = headView.findViewById(com.yunbao.im.R.id.msg);
        mSystemTime = headView.findViewById(com.yunbao.im.R.id.time);
        mSystemMsgCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    SystemMessageBean bean = JSON.parseObject(info[0], SystemMessageBean.class);
                    if (mSystemMsgContent != null) {
                        mSystemMsgContent.setText(bean.getContent());
                    }
                    if (mSystemTime != null) {
                        mSystemTime.setText(bean.getAddtime());
                    }
                    if (SpUtil.getInstance().getBooleanValue(SpUtil.HAS_SYSTEM_MSG)) {
                        if (mSystemMsgRedPoint != null && mSystemMsgRedPoint.getVisibility() != View.VISIBLE) {
                            mSystemMsgRedPoint.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        };
        ImageView avatar = headView.findViewById(com.yunbao.im.R.id.avatar);
        avatar.setImageResource(CommonAppConfig.getInstance().getAppIconRes());
        EventBus.getDefault().register(this);
        ConfigBean configBean = CommonAppConfig.getInstance().getConfig();
        mPriMsgSwitchOpen = configBean != null && configBean.getPriMsgSwitch() == 1;
    }

    public void release() {
        EventBus.getDefault().unregister(this);
        ImHttpUtil.cancel(ImHttpConsts.GET_SYSTEM_MESSAGE_LIST);
        ImHttpUtil.cancel(ImHttpConsts.GET_IM_USER_INFO);
    }


    public void loadData() {
        getSystemMessageList();
        if (!mPriMsgSwitchOpen) {
            return;
        }

        String uids = ImMessageUtil.getInstance().getConversationUids();
        ImHttpUtil.getImUserInfo(uids, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    List<ImUserBean> list = JSON.parseArray(Arrays.toString(info), ImUserBean.class);
                    list = ImMessageUtil.getInstance().getLastMsgInfoList(list);
                    if (mRecyclerView != null && mAdapter != null && list != null) {
                        mAdapter.setList(list);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
         if (i == R.id.btn_system_msg) {
            forwardSystemMessage();

        }
    }

    /**
     * 前往系统消息
     */
    private void forwardSystemMessage() {
        SpUtil.getInstance().setBooleanValue(SpUtil.HAS_SYSTEM_MSG, false);
        if (mSystemMsgRedPoint != null && mSystemMsgRedPoint.getVisibility() == View.VISIBLE) {
            mSystemMsgRedPoint.setVisibility(View.INVISIBLE);
        }
        SystemMessageActivity.forward(mContext);
    }

    @Override
    public void onItemClick(ImUserBean bean) {
        if (bean != null) {
            ImMessageUtil.getInstance().markAllMessagesAsRead(bean.getId(), true);
            if (Constants.MALL_IM_ADMIN.equals(bean.getId())) {
                RouteUtil.forward(RouteUtil.PATH_MALL_ORDER_MSG);
            } else {
                ChatRoomActivity.forward(mContext, bean, bean.getAttent() == 1, false);
            }
        }
    }

    @Override
    public void onItemDelete(ImUserBean bean, int size) {
        ImMessageUtil.getInstance().removeConversation(bean.getId());
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowEvent(FollowEvent e) {
        if (e != null) {
            if (mAdapter != null) {
                mAdapter.setFollow(e.getToUid(), e.getIsAttention());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSystemMsgEvent(SystemMsgEvent e) {
        getSystemMessageList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImUserMsgEvent(final ImUserMsgEvent e) {
        if (mPriMsgSwitchOpen && e != null && mRecyclerView != null && mAdapter != null) {
            int position = mAdapter.getPosition(e.getUid());
            if (position < 0) {
                ImHttpUtil.getImUserInfo(e.getUid(), new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0 && info.length > 0) {
                            ImUserBean bean = JSON.parseObject(info[0], ImUserBean.class);
                            bean.setLastMessage(e.getLastMessage());
                            bean.setUnReadCount(e.getUnReadCount());
                            bean.setLastTime(e.getLastTime());
                            mAdapter.insertItem(bean);
                        }
                    }
                });
            } else {
                mAdapter.updateItem(e.getLastMessage(), e.getLastTime(), e.getUnReadCount(), position);
            }
        }
    }

    /**
     * 忽略未读
     */
    public void ignoreUnReadCount() {
        SpUtil.getInstance().setBooleanValue(SpUtil.HAS_SYSTEM_MSG, false);
        if (mSystemMsgRedPoint != null && mSystemMsgRedPoint.getVisibility() == View.VISIBLE) {
            mSystemMsgRedPoint.setVisibility(View.INVISIBLE);
        }
        ImMessageUtil.getInstance().markAllConversationAsRead();
        if (mAdapter != null) {
            mAdapter.resetAllUnReadCount();
        }
        ToastUtil.show(com.yunbao.im.R.string.im_msg_ignore_unread_2);
    }

    /**
     * 获取系统消息
     */
    private void getSystemMessageList() {
        ImHttpUtil.getSystemMessageList(1, mSystemMsgCallback);
    }
}
