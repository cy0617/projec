package com.yunbao.beauty.ui.bean;


import com.meihu.beautylibrary.bean.WaterAlignEnum;
import com.yunbao.beauty.ui.enums.BeautyTypeEnum;
import com.yunbao.beauty.ui.enums.FilterEnum;

/**
 * Created by cxf on 2018/8/4.
 */

public class WatermarkBean extends BeautyBean{

    private WaterAlignEnum pos;
    private String iconPath;
    private String resPath;

    public WatermarkBean(WaterAlignEnum pos) {
        this.pos = pos;
    }

    public WatermarkBean(int imgSrc, int imgSrcSel, String shapeName, BeautyTypeEnum typeEnum, boolean checked, WaterAlignEnum pos) {
        super(imgSrc, imgSrcSel, shapeName, typeEnum, checked);
        this.pos = pos;
    }

    public WatermarkBean(String iconPath, String resPath, String shapeName, BeautyTypeEnum typeEnum, boolean checked, WaterAlignEnum pos) {
        this.iconPath = iconPath;
        this.resPath = resPath;
        this.effectName = shapeName;
        this.type = typeEnum;
        this.checked = checked;
        this.pos = pos;
    }

    public WaterAlignEnum getPos() {
        return pos;
    }

    public void setPos(WaterAlignEnum pos) {
        this.pos = pos;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getResPath() {
        return resPath;
    }

    public void setResPath(String resPath) {
        this.resPath = resPath;
    }
}
