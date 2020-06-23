package com.yunbao.beauty.ui.bean;

public class StickerCategaryBean {

    /**
     * name : cat
     * category : default
     * thumb : thumb/ic_stick_cat.png
     * is_downloaded : false
     * type : 1
     */

    private String id;
    private String name;

    public StickerCategaryBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
