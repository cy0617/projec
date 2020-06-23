package com.yunbao.beauty.ui.bean;


import com.meihu.beautylibrary.MHSDK;
import com.yunbao.beauty.ui.enums.BeautyTypeEnum;
import com.yunbao.beauty.ui.enums.QuickBeautyEnum;
import com.yunbao.beauty.ui.enums.QuickBeautyShapeEnum;

import java.util.HashMap;

/**
 * Created by cxf on 2018/8/4.
 */

public class QuickBeautyBean extends BeautyBean{

//    private ElementValue meibai;
//    private ElementValue  mopi ;
//    private ElementValue  hongrun ;
//    private ElementValue  dayan ;
//    private ElementValue  meimao ;
//    private ElementValue  yanju ;
//    private ElementValue  yanjiao ;
//    private ElementValue  shoulian ;
//    private ElementValue  zuixing ;
//    private ElementValue  shoubi ;
//    private ElementValue  xiaba ;
//    private ElementValue  etou ;
//    private ElementValue  changbi ;
//    private ElementValue  xuelian ;
//    private ElementValue  kaiyanjiao ;
    private QuickBeautyEnum quickBeautyEnum;
    private HashMap<QuickBeautyShapeEnum, ElementValue> beautyMap;

    public QuickBeautyBean(QuickBeautyEnum quickBeautyEnum) {
        this.quickBeautyEnum = quickBeautyEnum;
        this.effectName = MHSDK.getInstance().getAppContext().getString(quickBeautyEnum.getStringId());
        this.type = BeautyTypeEnum.ONKEY_TYPE_ENUM;
    }

    public QuickBeautyBean(QuickBeautyEnum quickBeautyEnum, String effectName) {
        this(quickBeautyEnum);
//        this.effectName = effectName;
    }


    public QuickBeautyBean(QuickBeautyEnum quickBeautyEnum, String effctName, int imgSrc) {
        this(quickBeautyEnum, effctName);
        this.imgSrc = imgSrc;
    }

    public QuickBeautyBean(QuickBeautyEnum quickBeautyEnum, String effctName, int imgSrc , HashMap<QuickBeautyShapeEnum, ElementValue>beautyMap) {
        this(quickBeautyEnum, effctName, imgSrc);
        this.beautyMap = beautyMap;
    }

    public QuickBeautyBean(QuickBeautyEnum quickBeautyEnum, String effctName, int imgSrc , HashMap<QuickBeautyShapeEnum, ElementValue>beautyMap, boolean checked) {
        this(quickBeautyEnum, effctName, imgSrc, beautyMap);
        this.checked = checked;
    }

    public QuickBeautyEnum getQuickBeautyEnum() {
        return quickBeautyEnum;
    }

    public void setQuickBeautyEnum(QuickBeautyEnum quickBeautyEnum) {
        this.quickBeautyEnum = quickBeautyEnum;
    }

    public HashMap<QuickBeautyShapeEnum, ElementValue> getBeautyMap() {
        return beautyMap;
    }

    public void setBeautyMap(HashMap<QuickBeautyShapeEnum, ElementValue> beautyMap) {
        this.beautyMap = beautyMap;
    }

    public static class ElementValue {
        private boolean isUse;
        private int value;
        private boolean isInterval;
        private int minValue;
        private int maxValue;

        public ElementValue() {}//未使用

        public ElementValue(int value) { //单值
            this.isUse = true;
            this.value = value;
            this.isInterval = false;
        }

        public ElementValue(int value, int minValue, int maxValue) { //带区间取值
            this.isUse = true;
            this.value = value;
            this.isInterval = true;
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        public boolean isUse() {
            return isUse;
        }

        public void setUse(boolean use) {
            isUse = use;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public boolean isInterval() {
            return isInterval;
        }

        public void setInterval(boolean interval) {
            isInterval = interval;
        }

        public int getMinValue() {
            return minValue;
        }

        public void setMinValue(int minValue) {
            this.minValue = minValue;
        }

        public int getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(int maxValue) {
            this.maxValue = maxValue;
        }
    }
}
