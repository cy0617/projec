package com.yunbao.beauty.ui.bean;

import android.text.TextUtils;

public class StickerBeautyBean {


    /**
     * skin_whiting : 1
     * skin_smooth : 2
     * skin_tenderness : 3
     * skin_saturation : 4
     * eye_brow : 10
     * big_eye : 20
     * eye_length : 30
     * eye_corner : 40
     * eye_alat : 50
     * face_lift : 60
     * face_shave : 70
     * mouse_lift : 80
     * nose_lift : 90
     * chin_lift : 10
     * forehead_lift : 20
     * lengthen_noseLift : 30
     */

    private String skin_whiting;
    private String skin_smooth;
    private String skin_tenderness;
    private String skin_saturation;
    private String skin_brightness;
    private String eye_brow;
    private String big_eye;
    private String eye_length;
    private String eye_corner;
    private String eye_alat;
    private String face_lift;
    private String face_shave;
    private String mouse_lift;
    private String nose_lift;
    private String chin_lift;
    private String forehead_lift;
    private String lengthen_noseLift;

    public String getSkin_whiting() {
        return skin_whiting;
    }

    public void setSkin_whiting(String skin_whiting) {
        this.skin_whiting = skin_whiting;
    }

    public String getSkin_smooth() {
        return skin_smooth;
    }

    public void setSkin_smooth(String skin_smooth) {
        this.skin_smooth = skin_smooth;
    }

    public String getSkin_tenderness() {
        return skin_tenderness;
    }

    public void setSkin_tenderness(String skin_tenderness) {
        this.skin_tenderness = skin_tenderness;
    }

    public String getSkin_saturation() {
        return skin_saturation;
    }

    public void setSkin_saturation(String skin_saturation) {
        this.skin_saturation = skin_saturation;
    }

    public String getSkin_brightness() {
        return skin_brightness;
    }

    public void setSkin_brightness(String skin_brightness) {
        this.skin_brightness = skin_brightness;
    }

    public String getEye_brow() {
        return eye_brow;
    }

    public void setEye_brow(String eye_brow) {
        this.eye_brow = eye_brow;
    }

    public String getBig_eye() {
        return big_eye;
    }

    public void setBig_eye(String big_eye) {
        this.big_eye = big_eye;
    }

    public String getEye_length() {
        return eye_length;
    }

    public void setEye_length(String eye_length) {
        this.eye_length = eye_length;
    }

    public String getEye_corner() {
        return eye_corner;
    }

    public void setEye_corner(String eye_corner) {
        this.eye_corner = eye_corner;
    }

    public String getEye_alat() {
        return eye_alat;
    }

    public void setEye_alat(String eye_alat) {
        this.eye_alat = eye_alat;
    }

    public String getFace_lift() {
        return face_lift;
    }

    public void setFace_lift(String face_lift) {
        this.face_lift = face_lift;
    }

    public String getFace_shave() {
        return face_shave;
    }

    public void setFace_shave(String face_shave) {
        this.face_shave = face_shave;
    }

    public String getMouse_lift() {
        return mouse_lift;
    }

    public void setMouse_lift(String mouse_lift) {
        this.mouse_lift = mouse_lift;
    }

    public String getNose_lift() {
        return nose_lift;
    }

    public void setNose_lift(String nose_lift) {
        this.nose_lift = nose_lift;
    }

    public String getChin_lift() {
        return chin_lift;
    }

    public void setChin_lift(String chin_lift) {
        this.chin_lift = chin_lift;
    }

    public String getForehead_lift() {
        return forehead_lift;
    }

    public void setForehead_lift(String forehead_lift) {
        this.forehead_lift = forehead_lift;
    }

    public String getLengthen_noseLift() {
        return lengthen_noseLift;
    }

    public void setLengthen_noseLift(String lengthen_noseLift) {
        this.lengthen_noseLift = lengthen_noseLift;
    }

    public boolean hasDataBeauty() {
        return !TextUtils.isEmpty(big_eye) && !"-1".equals(big_eye);
    }
}
