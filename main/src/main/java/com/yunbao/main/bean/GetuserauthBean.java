package com.yunbao.main.bean;

import android.support.annotation.NonNull;

/**
 * 实名认证实体类
 */
public class GetuserauthBean {
    /**
     * code : 0
     * msg : 未实名认证成功
     * info : 1.3
     */

    private int code;
    private String msg;
    private String info;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", info='" + info + '\'' +
                '}';
    }

}
