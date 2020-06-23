package com.yunbao.main.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.model.Response;
import com.megvii.meglive_sdk.listener.DetectCallback;
import com.megvii.meglive_sdk.listener.PreCallback;
import com.megvii.meglive_sdk.manager.MegLiveManager;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.R;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.http.JsonBean;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.main.bean.GetuserauthBean;
import com.yunbao.main.facerecognition.GenerateSign;
import com.yunbao.main.facerecognition.HttpRequestCallBack;
import com.yunbao.main.facerecognition.HttpRequestManager;
import com.yunbao.main.http.MainHttpUtil;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;

import static android.os.Build.VERSION_CODES.M;

/**
 * 实名认证
 */

public class RealNameViewActivity extends AbsActivity implements DetectCallback, PreCallback {
    private static final int ACTION_YY = 1;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int EXTERNAL_STORAGE_REQ_WRITE_EXTERNAL_STORAGE_CODE = 101;
    private ProgressDialog mProgressDialog;
    private MegLiveManager megLiveManager;
    private static final String GET_BIZTOKEN_URL = "https://api.megvii.com/faceid/v3/sdk/get_biz_token";
    private static final String VERIFY_URL = "https://api.megvii.com/faceid/v3/sdk/verify";
    private static final String API_KEY = "XUKr15RvgbZPm9HOCo5EAKHIw1q_boHN";
    private static final String SECRET = "o3zU6YEjYrUT0eTYL_xMo3NBASs20-BJ";
    private String sign = "";
    private static final String SIGN_VERSION = "hmac_sha1";
    private byte[] imageRef;//底库图片
    private int buttonType;
    private static final String LANGUAGE = "zh";//en


    private ProgressBar mProgressBar;
    private WebView mWebView;
    private final int CHOOSE = 100;//Android 5.0以下的
    private final int CHOOSE_ANDROID_5 = 200;//Android 5.0以上的
    private ValueCallback<Uri> mValueCallback;
    private ValueCallback<Uri[]> mValueCallback2;
    private boolean mPaySuc;
    private EditText mEtName;
    private EditText mEtNumber;
    private TextView mBtnNext;
    private String name;
    private String number;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void main() {
        megLiveManager = MegLiveManager.getInstance();
        /**
         * 如果build.gradle中的applicationId 与 manifest中package不一致，必须将manifest中package通过
         * 下面方法传入，如果一致可以不调用此方法
         */
        megLiveManager.setManifestPack(this, "com.yunbao.main");

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

        mEtName = findViewById(R.id.realname_et_name);
        mEtNumber = findViewById(R.id.realname_et_number);
        mBtnNext = findViewById(R.id.realname_btn_next);
        //姓名
        name = mEtName.getText().toString();
        //身份证号
        number = mEtNumber.getText().toString();

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //第一次判断是否认证
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(number)) {

                } else {
                    buttonType = ACTION_YY;
                    requestCameraPerm();
                }
            }
        });


        MainHttpUtil.getGetuserauth(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 & info.length > 0) {

                    JSONObject obj = JSON.parseObject(info[0]);
                    GetuserauthBean bean = JSON.toJavaObject(obj, GetuserauthBean.class);

                    String msg1 = bean.getMsg();
                    Log.e("eeeeeee", "onSuccess: " + msg1);
                }
            }

            @Override
            public void onError(Response<JsonBean> response) {
                Log.e("eeeeeee", "onError: "+response );
            }
        });
        long currtTime = System.currentTimeMillis() / 1000;
        long expireTime = (System.currentTimeMillis() + 60 * 60 * 100) / 1000;
        sign = GenerateSign.appSign(API_KEY, SECRET, currtTime, expireTime);

    }


    private void requestCameraPerm() {
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                requestWriteExternalPerm();
            }
        } else {
            beginDetect(buttonType);
        }
    }

    private void requestWriteExternalPerm() {
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_REQ_WRITE_EXTERNAL_STORAGE_CODE);
            } else {
                beginDetect(buttonType);
            }
        } else {
            beginDetect(buttonType);
        }
    }

    /**
     * 姓名&身份证号
     * @param type
     */
    private void beginDetect(int type) {
        if (type == ACTION_YY) {
            getBizToken("meglive", 1, name, number, "111", null);
        }
    }

    private static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imgBytes = baos.toByteArray();
        return imgBytes;
    }

    private void getBizToken(String livenessType, int comparisonType, String idcardName, String idcardNum, String uuid, byte[] image) {
        mProgressDialog.show();
        HttpRequestManager.getInstance().getBizToken(this, GET_BIZTOKEN_URL, sign, SIGN_VERSION, livenessType, comparisonType, idcardName, idcardNum, uuid, image, new HttpRequestCallBack() {

            @Override
            public void onSuccess(String responseBody) {
                try {
                    org.json.JSONObject json = new org.json.JSONObject(responseBody);
                    String bizToken = json.optString("biz_token");
                    megLiveManager.preDetect(RealNameViewActivity.this, bizToken, "en", "https://api.megvii.com", RealNameViewActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {
                Log.w("onFailure", "statusCode=" + statusCode + ",responseBody" + new String(responseBody));
            }
        });
    }

    @Override
    public void onDetectFinish(String token, int errorCode, String errorMessage, String data) {
        if (errorCode == 1000) {
            verify(token, data.getBytes());
        }
    }

    @Override
    public void onPreStart() {
        showDialogDismiss();
    }

    @Override
    public void onPreFinish(String token, int errorCode, String errorMessage) {
        progressDialogDismiss();
        if (errorCode == 1000) {
            megLiveManager.setVerticalDetectionType(MegLiveManager.DETECT_VERITICAL_FRONT);
            megLiveManager.startDetect(this);
        }
    }

    private void verify(String token, byte[] data) {
        showDialogDismiss();
        HttpRequestManager.getInstance().verify(this, VERIFY_URL, sign, SIGN_VERSION, token, data, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String responseBody) {
                Log.w("result", responseBody);
                progressDialogDismiss();
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {
                Log.w("result", new String(responseBody));
                progressDialogDismiss();
            }
        });
    }


    private void progressDialogDismiss() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    private void showDialogDismiss() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) {
                    mProgressDialog.show();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length < 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //拒绝了权限申请
            } else {
                requestWriteExternalPerm();
            }
        } else if (requestCode == EXTERNAL_STORAGE_REQ_WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults.length < 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //拒绝了权限申请
            } else {
                beginDetect(buttonType);
            }
        }
    }

//    private CommonCallback<GetuserauthBean> mCallback = new CommonCallback<GetuserauthBean>() {
//
//        @Override
//        public void callback(GetuserauthBean bean) {
//            String msg = bean.getMsg();
//            Log.e("123123", "callback: "+msg );
//        }
//    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case CHOOSE://5.0以下选择图片后的回调
                processResult(resultCode, intent);
                break;
            case CHOOSE_ANDROID_5://5.0以上选择图片后的回调
                processResultAndroid5(resultCode, intent);
                break;
        }
    }

    private void processResult(int resultCode, Intent intent) {
        if (mValueCallback == null) {
            return;
        }
        if (resultCode == RESULT_OK && intent != null) {
            Uri result = intent.getData();
            mValueCallback.onReceiveValue(result);
        } else {
            mValueCallback.onReceiveValue(null);
        }
        mValueCallback = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void processResultAndroid5(int resultCode, Intent intent) {
        if (mValueCallback2 == null) {
            return;
        }
        if (resultCode == RESULT_OK && intent != null) {
            mValueCallback2.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
        } else {
            mValueCallback2.onReceiveValue(null);
        }
        mValueCallback2 = null;
    }

    protected boolean canGoBack() {
        return mWebView != null && mWebView.canGoBack();
    }

    @Override
    public void onBackPressed() {
        if (isNeedExitActivity()) {
            finish();
        } else {
            if (canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
        }
    }


    private boolean isNeedExitActivity() {
        if (mWebView != null) {
            String url = mWebView.getUrl();
            if (!TextUtils.isEmpty(url)) {
                return url.contains("g=Appapi&m=Auth&a=success")//身份认证成功页面
                        || url.contains("g=Appapi&m=Family&a=home")//家族申请提交成功页面
                        || url.contains("Appapi/Auth/succ")//身份认证成功页面
                        || url.contains("/Auth/succ") || mPaySuc;//支付成功

            }
        }
        return false;
    }

    public static void forward(Context context, String url, boolean addArgs) {
        if (addArgs) {
            if (!url.contains("?")) {
                url = StringUtil.contact(url, "?");
            }
            /*实名认证的url*/
            url = StringUtil.contact(url, "&uid=", CommonAppConfig.getInstance().getUid(), "&token=", CommonAppConfig.getInstance().getToken());
        }
        Intent intent = new Intent(context, RealNameViewActivity.class);
        intent.putExtra(Constants.URL, url);
        context.startActivity(intent);
    }

    public static void forward(Context context, String url) {
        forward(context, url, true);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.destroy();
        }
        super.onDestroy();
    }

}
