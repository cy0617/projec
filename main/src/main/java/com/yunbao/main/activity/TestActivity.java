package com.yunbao.main.activity;

import android.os.Handler;
import android.os.Message;

import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.utils.L;
import com.yunbao.main.R;
import com.yunbao.main.custom.TestView;

public class TestActivity extends AbsActivity {
    private static final String TAG = "TestActivity";
    private int progress;
    TestView testView;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             progress++;
            //testView.setProgress(progress / 10f);
            if (progress < 10) {
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void main() {
        L.e("life-main");
        testView = findViewById(R.id.test);
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        L.e("life-onStart");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        L.e("life-onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        L.e("life-onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
