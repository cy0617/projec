package com.yunbao.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yunbao.main.R;

public class MeLikedDialog {
    public static Dialog showLikedDialog(Context context, String content) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_me_liked);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (!TextUtils.isEmpty(content)) {
            TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
            if (tv_content != null) {
                tv_content.setText(content);
            }
        }
        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

}
