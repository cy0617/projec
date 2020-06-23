package com.yunbao.beauty.ui.bean;


public class StickerServiceBean {

    /**
     * name : cat
     * category : default
     * thumb : thumb/ic_stick_cat.png
     * is_downloaded : false
     * type : 1
     */
    private String id;
    private String name;
    private String category;
    private String thumb;
    private String resource;
    private String uptime;
    private boolean is_downloaded;
    private boolean isDownLoading;
    private boolean isChecked;
    private int type;
    private StickerBeautyBean stickerBeautyData;
    public StickerServiceBean() {
    }

    public StickerServiceBean(String name, String category, String thumb, String resource, String uptime, int type) {
        this.name = name;
        this.category = category;
        this.thumb = thumb;
        this.resource = resource;
        this.uptime = uptime;
        this.type = type;
    }

//    public StickerServiceBean(String name, String category, String thumb, String resource, boolean is_downloaded, boolean isDownLoading, boolean isChecked, int type) {
//        this.name = name;
//        this.category = category;
//        this.thumb = thumb;
//        this.resource = resource;
//        this.is_downloaded = is_downloaded;
//        this.isDownLoading = isDownLoading;
//        this.isChecked = isChecked;
//        this.type = type;
//    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public boolean isIs_downloaded() {
        return is_downloaded;
    }

    public void setIs_downloaded(boolean is_downloaded) {
        this.is_downloaded = is_downloaded;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isDownLoading() {
        return isDownLoading;
    }

    public void setDownLoading(boolean downLoading) {
        isDownLoading = downLoading;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public StickerBeautyBean getStickerBeautyData() {
        return stickerBeautyData;
    }

    public void setStickerBeautyData(StickerBeautyBean stickerBeautyData) {
        this.stickerBeautyData = stickerBeautyData;
    }

}
