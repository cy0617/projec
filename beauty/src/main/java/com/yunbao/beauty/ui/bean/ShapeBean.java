package com.yunbao.beauty.ui.bean;


/**
 * Created by cxf on 2018/8/4.
 */

public class ShapeBean {

    private int imgSrc;
    private int imgSrcSel;
    private String shapeName;
    private boolean checked;

    public ShapeBean() {
    }

    public ShapeBean(int imgSrc, int imgSrcSel, String shapeName, boolean checked) {
        this.imgSrc = imgSrc;
        this.imgSrcSel = imgSrcSel;
        this.shapeName = shapeName;
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

    public String getShapeName() {
        return shapeName;
    }

    public void setShapeName(String shapeName) {
        this.shapeName = shapeName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
