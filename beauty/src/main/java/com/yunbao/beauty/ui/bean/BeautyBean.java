package com.yunbao.beauty.ui.bean;

import com.yunbao.beauty.ui.enums.BeautyTypeEnum;

public class BeautyBean {

    protected int imgSrc;
    protected int imgSrcSel;
    protected String effectName;
    protected boolean checked;
    protected BeautyTypeEnum type;

    public BeautyBean() {
    }

    public BeautyBean(int imgSrc, int imgSrcSel, String shapeName, BeautyTypeEnum typeEnum, boolean checked) {
        this.imgSrc = imgSrc;
        this.imgSrcSel = imgSrcSel;
        this.effectName = shapeName;
        this.type = typeEnum;
        this.checked = checked;
    }

    public int getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(int imgSrc) {
        this.imgSrc = imgSrc;
    }

    public int getImgSrcSel() {
        return imgSrcSel;
    }

    public void setImgSrcSel(int imgSrcSel) {
        this.imgSrcSel = imgSrcSel;
    }

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public BeautyTypeEnum getType() {
        return type;
    }

    public void setType(BeautyTypeEnum type) {
        this.type = type;
    }
}
