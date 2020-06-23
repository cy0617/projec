package com.yunbao.mall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.CommonIconUtil;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.SpUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

/**
 * 买家提现
 */
public class BuyerCashActivity extends AbsActivity implements View.OnClickListener {

    public static void forward(Context context, String balanceVal) {
        Intent intent = new Intent(context, BuyerCashActivity.class);
        intent.putExtra(Constants.MALL_CASH_BALANCE, balanceVal);
        ((Activity) context).startActivityForResult(intent, 0);
    }

    private String mAccountID;
    private TextView mBalance;
    private EditText mMoney;
    private View mChooseTip;
    private View mAccountGroup;
    private ImageView mAccountIcon;
    private TextView mAccountName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buyer_cash;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_295));
        mBalance = findViewById(R.id.balance);
        mMoney = findViewById(R.id.money);
        mChooseTip = findViewById(R.id.choose_tip);
        mAccountGroup = findViewById(R.id.account_group);
        mAccountIcon = findViewById(R.id.account_icon);
        mAccountName = findViewById(R.id.account_name);
        findViewById(R.id.btn_choose_account).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        String balanceVal = getIntent().getStringExtra(Constants.MALL_CASH_BALANCE);
        mBalance.setText(renderBalanceText(balanceVal));
    }

    private CharSequence renderBalanceText(String text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        if (!text.contains(".")) {
            text += ".00";
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new AbsoluteSizeSpan(16, true), text.indexOf("."), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_choose_account) {
            chooseAccount();
        } else if (id == R.id.btn_submit) {
            submit();
        }
    }

    private void submit() {
        if (TextUtils.isEmpty(mAccountID)) {
            ToastUtil.show(R.string.profit_choose_account);
            return;
        }
        String money = mMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            ToastUtil.show(R.string.mall_306);
            return;
        }
        MallHttpUtil.goodsCash(mAccountID, money, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    setResult(RESULT_OK);
                    finish();
                }
                ToastUtil.show(msg);
            }
        });
    }

    /**
     * 选择提现账户
     */
    private void chooseAccount() {
        RouteUtil.forwardCashAccount(mAccountID);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getAccount();
    }

    private void getAccount() {
        String[] values = SpUtil.getInstance().getMultiStringValue(Constants.CASH_ACCOUNT_ID, Constants.CASH_ACCOUNT, Constants.CASH_ACCOUNT_TYPE);
        if (values != null && values.length == 3) {
            String accountId = values[0];
            String account = values[1];
            String type = values[2];
            if (!TextUtils.isEmpty(accountId) && !TextUtils.isEmpty(account) && !TextUtils.isEmpty(type)) {
                if (mChooseTip.getVisibility() == View.VISIBLE) {
                    mChooseTip.setVisibility(View.INVISIBLE);
                }
                if (mAccountGroup.getVisibility() != View.VISIBLE) {
                    mAccountGroup.setVisibility(View.VISIBLE);
                }
                mAccountID = accountId;
                mAccountIcon.setImageResource(CommonIconUtil.getCashTypeIcon(Integer.parseInt(type)));
                mAccountName.setText(account);
            } else {
                if (mAccountGroup.getVisibility() == View.VISIBLE) {
                    mAccountGroup.setVisibility(View.INVISIBLE);
                }
                if (mChooseTip.getVisibility() != View.VISIBLE) {
                    mChooseTip.setVisibility(View.VISIBLE);
                }
                mAccountID = null;
            }
        } else {
            if (mAccountGroup.getVisibility() == View.VISIBLE) {
                mAccountGroup.setVisibility(View.INVISIBLE);
            }
            if (mChooseTip.getVisibility() != View.VISIBLE) {
                mChooseTip.setVisibility(View.VISIBLE);
            }
            mAccountID = null;
        }
    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GOODS_CASH);
        super.onDestroy();
    }
}
